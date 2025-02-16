package nl.leonklute.chesshtmx.service;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.GameWebSocketHandler;
import nl.leonklute.chesshtmx.chess.Color;
import nl.leonklute.chesshtmx.chess.Game;
import nl.leonklute.chesshtmx.chess.Move;
import nl.leonklute.chesshtmx.chess.Puzzle;
import nl.leonklute.chesshtmx.db.PuzzleRepository;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

import static nl.leonklute.chesshtmx.PuzzlesConfig.PAGE_SIZE;
import static nl.leonklute.chesshtmx.chess.Piece.toImgCode;

@Slf4j
@Service
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;

    private final Map<Principal, Puzzle> puzzleCache;

    private final GameWebSocketHandler gameWebSocketHandler;

    private final TemplateEngine templateEngine;

    public PuzzleService(PuzzleRepository puzzleRepository,
                         GameWebSocketHandler gameWebSocketHandler,
                         TemplateEngine templateEngine) {
        this.puzzleRepository = puzzleRepository;
        this.gameWebSocketHandler = gameWebSocketHandler;
        this.templateEngine = templateEngine;
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

    public List<PuzzleEntity> getPaginatedPuzzles(int page) {
        PageRequest request;
        if (page < 0) {
            request = PageRequest.of(Math.abs(page) - 1, PAGE_SIZE, Sort.Direction.DESC, "rating");
        } else {
            request = PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "rating");
        }
        return puzzleRepository.findAll(request).getContent();
    }

    public Puzzle getCurrentPuzzle(Principal user) {
        return puzzleCache.get(user);
    }

    public void setCurrentPuzzle(Principal user, Puzzle puzzle) {
        puzzleCache.put(user, puzzle);
    }

    public void nextMove(Principal principal, boolean isUserMove) throws IOException {
        Puzzle puzzle = puzzleCache.get(principal);
        if (puzzle.isFinished()) {
            return;
        }
        Move move = puzzle.doNextMove();
        Context context = new Context();

        Map<String, String> pieceMap = new HashMap<>();
        pieceMap.put(move.from().asAlgebraic(), null);
        pieceMap.put(move.to().asAlgebraic(), toImgCode(puzzle.game().getPosition().get(move.to())));
        context.setVariable("state", pieceMap);
        context.setVariable("isUserMove", isUserMove);
        if(isUserMove)
            context.setVariable("wrongMove", move.asAlgebraic());
        context.setVariables(mapState(puzzle.game(), puzzle.orientation()));
        String html = templateEngine.process("board", context);
        gameWebSocketHandler.send(principal, new TextMessage(html));
    }
}
