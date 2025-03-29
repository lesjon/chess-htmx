package nl.leonklute.chesshtmx.service;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.Color;
import nl.leonklute.chesshtmx.chess.Game;
import nl.leonklute.chesshtmx.chess.Move;
import nl.leonklute.chesshtmx.chess.Puzzle;
import nl.leonklute.chesshtmx.db.PuzzleMetadataRepository;
import nl.leonklute.chesshtmx.db.PuzzleRepository;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import nl.leonklute.chesshtmx.db.model.PuzzleMetadataEntity;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.puzzle.PuzzleResult;
import nl.leonklute.chesshtmx.service.model.PuzzleListing;
import nl.leonklute.chesshtmx.web.GameWebSocketHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.leonklute.chesshtmx.chess.Piece.toImgCode;
import static nl.leonklute.chesshtmx.web.PuzzlesConfig.PAGE_SIZE;

@Slf4j
@Service
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final PuzzleMetadataRepository puzzleMetadataRepository;

    private final Map<Long, Puzzle> puzzleCache;

    private final GameWebSocketHandler gameWebSocketHandler;

    private final TemplateEngine templateEngine;
    private final UserService userService;

    public PuzzleService(PuzzleRepository puzzleRepository,
                         PuzzleMetadataRepository puzzleMetadataRepository, GameWebSocketHandler gameWebSocketHandler,
                         TemplateEngine templateEngine, UserService userService) {
        this.puzzleRepository = puzzleRepository;
        this.puzzleMetadataRepository = puzzleMetadataRepository;
        this.gameWebSocketHandler = gameWebSocketHandler;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.puzzleCache = new HashMap<>();
    }

    public static Map<String, Object> mapState(Game game, Color orientation) {
        Map<String, Object> model = new HashMap<>();
        Map<String, String> valueMap = game.getPosition().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(e -> e.getKey().asAlgebraic(), e -> toImgCode(e.getValue())));
        model.put("state", valueMap);
        switch (orientation) {
            case WHITE -> {
                model.put("files", new int[]{8, 7, 6, 5, 4, 3, 2, 1});
                model.put("rankIndices", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
            }
            case BLACK -> {
                model.put("files", new int[]{1, 2, 3, 4, 5, 6, 7, 8});
                model.put("rankIndices", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
            }
        }
        model.put("ranks", new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
        if (!game.getMoves().isEmpty()) {
            model.put("lastMove", game.getMoves().getLast().asAlgebraic());
        }
        return model;
    }

    public Optional<PuzzleEntity> getPuzzle(String puzzleId) {
        return puzzleRepository.findById(puzzleId);
    }

    public List<PuzzleListing> getPaginatedPuzzles(int page, String theme) {
        PageRequest request;
        if (page < 0) {
            request = PageRequest.of(Math.abs(page) - 1, PAGE_SIZE, Sort.Direction.DESC, "rating");
        } else {
            request = PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "rating");
        }
        if (theme.equals("*")) {
            return puzzleRepository.findAllBy(request).toList();
        }
        return puzzleRepository.findAllByThemesContaining(request, theme).toList();
    }

    public Puzzle getCurrentPuzzle(long user) {
        return puzzleCache.get(user);
    }

    public void setCurrentPuzzle(long user, Puzzle puzzle) {
        puzzleCache.put(user, puzzle);
    }

    public void nextMove(long userId) throws IOException {
        Puzzle puzzle = puzzleCache.get(userId);
        if (puzzle.isFinished()) {
            return;
        }
        Move move = puzzle.doNextMove();
        Context context = new Context();

        Map<String, String> pieceMap = new HashMap<>();
        pieceMap.put(move.from().asAlgebraic(), null);
        pieceMap.put(move.to().asAlgebraic(), toImgCode(puzzle.game().getPosition().get(move.to())));
        context.setVariable("state", pieceMap);
        var userToMove = puzzle.game().getActive().equals(puzzle.orientation());
        context.setVariable("userToMove", userToMove);
        if (!userToMove)
            context.setVariable("wrongMove", move.asAlgebraic());
        context.setVariables(mapState(puzzle.game(), puzzle.orientation()));
        String html = templateEngine.process("board", context);

        Optional<Principal> principalOption = userService.getPrincipal();

        if (principalOption.isPresent()) {
            gameWebSocketHandler.send(principalOption.get(), new TextMessage(html));
        }
    }

    public void disable(String puzzleId) {
        log.warn("disabling: {}", puzzleId);
        Optional<PuzzleEntity> puzzleEntityOption = puzzleRepository.findById(puzzleId);
        log.info("puzzleEntityOption {}", puzzleEntityOption);
        if (puzzleEntityOption.isEmpty())
            return;
        PuzzleMetadataEntity puzzleMetadata = puzzleEntityOption.get().getPuzzleMetadataEntity();
        puzzleMetadata.setActive(false);
        var saved = puzzleMetadataRepository.save(puzzleMetadata);
        log.info("saved: {}", saved);
    }

    public void addAdditionalMoves(String puzzleId, String additionalMoves) {
        log.warn("adding moves '{}' to: {}", additionalMoves, puzzleId);
        Optional<PuzzleEntity> puzzleEntityOption = puzzleRepository.findById(puzzleId);
        log.info("puzzleEntityOption {}", puzzleEntityOption);
        if (puzzleEntityOption.isEmpty())
            return;
        PuzzleMetadataEntity puzzleMetadata = puzzleEntityOption.get().getPuzzleMetadataEntity();
        puzzleMetadata.setAdditionalMoves(additionalMoves);
        var saved = puzzleMetadataRepository.save(puzzleMetadata);
        log.info("saved: {}", saved);
    }

    public PuzzleListing getNextPuzzle(long userId) {
        Optional<UserEntity> userOption = userService.getUserById(userId);
        if (userOption.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unkown user requested next puzzle");
        List<PuzzleListing> puzzles = puzzleRepository.findFirst10ByRating(userOption.get().getRating(), Sort.by("rating"));
        return puzzles.getFirst();
    }

    public PuzzleResult puzzleFinished(long userId) {
        var puzzle = puzzleCache.get(userId);
        if (puzzle.attemptedMoves().isEmpty()) {
            return PuzzleResult.SUCCES;
        } else {
            return PuzzleResult.FAILURE;
        }
    }

    public void like(String puzzleId, long userId) {
        log.warn("adding like from '{}' to: {}", userService.getUserById(userId), puzzleId);
        Optional<PuzzleEntity> puzzleEntityOption = puzzleRepository.findById(puzzleId);
        log.info("puzzleEntityOption {}", puzzleEntityOption);
        if (puzzleEntityOption.isEmpty())
            return;
        PuzzleMetadataEntity puzzleMetadata = puzzleEntityOption.get().getPuzzleMetadataEntity();
        boolean success = puzzleMetadata.addLike(userId);
        log.info("success is {}", success);
        var saved = puzzleMetadataRepository.save(puzzleMetadata);
        log.info("saved: {}", saved);
    }

    public void dislike(String puzzleId, long userId) {
        log.warn("adding dislike from '{}' to: {}", userService.getUserById(userId), puzzleId);
        Optional<PuzzleEntity> puzzleEntityOption = puzzleRepository.findById(puzzleId);
        log.info("puzzleEntityOption {}", puzzleEntityOption);
        if (puzzleEntityOption.isEmpty())
            return;
        PuzzleMetadataEntity puzzleMetadata = puzzleEntityOption.get().getPuzzleMetadataEntity();
        boolean success = puzzleMetadata.addDislike(userId);
        log.info("success is {}", success);
        var saved = puzzleMetadataRepository.save(puzzleMetadata);
        log.info("saved: {}", saved);
    }
}
