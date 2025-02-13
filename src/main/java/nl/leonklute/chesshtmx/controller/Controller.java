package nl.leonklute.chesshtmx.controller;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.*;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import nl.leonklute.chesshtmx.service.PuzzleService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.leonklute.chesshtmx.chess.Piece.toImgCode;

@Slf4j
@org.springframework.stereotype.Controller
public class Controller {

    private final PuzzleService puzzleService;
    private Game game;
    private Location from;

    public Controller(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
        game = new Game();
        game.put(new Location(1, 1), new Rook(Color.WHITE));
        game.put(new Location(2, 1), new Knight(Color.WHITE));
        game.put(new Location(3, 1), new Bishop(Color.WHITE));
        game.put(new Location(4, 1), new Queen(Color.WHITE));
        game.put(new Location(5, 1), new King(Color.WHITE));
        game.put(new Location(6, 1), new Bishop(Color.WHITE));
        game.put(new Location(7, 1), new Knight(Color.WHITE));
        game.put(new Location(8, 1), new Rook(Color.WHITE));
        for (int i = 1; i <= 8; i++) {
            game.put(new Location(i, 2), new Pawn(Color.WHITE));
        }

        for (int i = 1; i <= 8; i++) {
            game.put(new Location(i, 7), new Pawn(Color.BLACK));
        }
        game.put(new Location(1, 8), new Rook(Color.BLACK));
        game.put(new Location(2, 8), new Knight(Color.BLACK));
        game.put(new Location(3, 8), new Bishop(Color.BLACK));
        game.put(new Location(4, 8), new Queen(Color.BLACK));
        game.put(new Location(5, 8), new King(Color.BLACK));
        game.put(new Location(6, 8), new Bishop(Color.BLACK));
        game.put(new Location(7, 8), new Knight(Color.BLACK));
        game.put(new Location(8, 8), new Rook(Color.BLACK));
    }

    @GetMapping("/")
    public String index(Model model) {
        mapState(model);
        return "index";
    }

    @GetMapping("/fen/{row1}/{row2}/{row3}/{row4}/{row5}/{row6}/{row7}/{row8etc}")
    public String fen(@PathVariable String row1,
                      @PathVariable String row2,
                      @PathVariable String row3,
                      @PathVariable String row4,
                      @PathVariable String row5,
                      @PathVariable String row6,
                      @PathVariable String row7,
                      @PathVariable String row8etc, Model model) {
        log.info("fen: {} {} {} {} {} {} {} {}", row1, row2, row3, row4, row5, row6, row7, row8etc);
        FenParser fenParser = new FenParser();
        game = fenParser.parse(String.join("/", row1, row2, row3, row4, row5, row6, row7, row8etc));
        mapState(model);
        return "index";
    }

    private void mapState(Model model) {
        Map<String, String> valueMap = game.getPosition().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(e -> e.getKey().asAlgebraic(), e -> toImgCode(e.getValue())));
        model.addAttribute("state", valueMap);
        model.addAttribute("files", new int[]{8, 7, 6, 5, 4, 3, 2, 1});
        model.addAttribute("ranks", new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
        model.addAttribute("rankIndices", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        log.info("{}", model.asMap());
    }

    @PostMapping("/start-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startMove(@RequestParam String location) {
        log.info("startMove: {}", location);
        from = Location.from(location);
        log.info("from = {}", from);
    }

    @PostMapping("/end-move")
    public String endMove(@RequestParam String location, Model model) {
        log.info("endMove: {}", location);
        model.addAttribute("rank", Character.toString(location.charAt(0)));
        model.addAttribute("rankIndex", location.codePointAt(0) - 'a');
        model.addAttribute("file", location.codePointAt(1) - '0');
        Map<String, String> valueMap = new HashMap<>();
        Location to = Location.from(location);
        if (to.equals(from)) {
            from = null;
            return "move";
        }
        game.move(from, to);
        valueMap.put(from.asAlgebraic(), null);
        valueMap.put(to.asAlgebraic(), toImgCode(game.getPosition().get(to)));
        model.addAttribute("state", valueMap);
        log.info("model: {}", model);
        from = null;
        return "move";
    }

    @GetMapping("/puzzle/{puzzleId}")
    public String getPuzzle(@PathVariable String puzzleId, Model model) {
        Optional<PuzzleEntity> puzzleOption = puzzleService.getPuzzle(puzzleId);
        log.info("puzzle: {}", puzzleOption);
        if (puzzleOption.isEmpty())
            throw new RuntimeException();
        var puzzle = puzzleOption.get();
        var parser = new FenParser();
        game = parser.parse(puzzle.FEN);
        mapState(model);
        return "index";
    }

    @GetMapping("/puzzles")
    public String getPuzzles(@RequestParam String page, Model model){
        var puzzles = puzzleService.getPaginatedPuzzles(Integer.parseInt(page));
        log.info("puzzles: {}", puzzles);
        model.addAttribute("puzzles", puzzles);
        model.addAttribute("page", page);
        log.info("model: {}", model.asMap());
        return "puzzles";
    }

}
