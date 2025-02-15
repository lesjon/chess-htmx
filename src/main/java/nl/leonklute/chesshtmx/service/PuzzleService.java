package nl.leonklute.chesshtmx.service;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.Color;
import nl.leonklute.chesshtmx.chess.Game;
import nl.leonklute.chesshtmx.chess.Puzzle;
import nl.leonklute.chesshtmx.db.PuzzleRepository;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public PuzzleService(PuzzleRepository puzzleRepository) {
        this.puzzleRepository = puzzleRepository;
        this.puzzleCache = new HashMap<>();
    }

    public static void mapState(Model model, Game game, Color orientation) {
        Map<String, String> valueMap = game.getPosition().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(e -> e.getKey().asAlgebraic(), e -> toImgCode(e.getValue())));
        model.addAttribute("state", valueMap);
        switch (orientation) {
            case WHITE -> {
                model.addAttribute("files", new int[]{8, 7, 6, 5, 4, 3, 2, 1});
                model.addAttribute("rankIndices", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
            }
            case BLACK -> {
                model.addAttribute("files", new int[]{1, 2, 3, 4, 5, 6, 7, 8});
                model.addAttribute("rankIndices", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
            }
        }
        model.addAttribute("ranks", new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
        if (!game.getMoves().isEmpty()) {
            model.addAttribute("lastMove", game.getMoves().getLast().asAlgebraic());
        }
    }

    public Optional<PuzzleEntity> getPuzzle(String puzzleId) {
        return puzzleRepository.findById(puzzleId);
    }

    public List<PuzzleEntity> getPaginatedPuzzles(int page) {
        PageRequest request;
        if (page < 0) {
            request = PageRequest.of(Math.abs(page) - 1, PAGE_SIZE, Sort.Direction.DESC, "puzzleId");
        } else {
            request = PageRequest.of(page, PAGE_SIZE);
        }
        return puzzleRepository.findAll(request).getContent();
    }

    public Puzzle getCurrentPuzzle(Principal user) {
        return puzzleCache.get(user);
    }

    public void setCurrentPuzzle(Principal user, Puzzle puzzle) {
        puzzleCache.put(user, puzzle);
    }

}
