package nl.leonklute.chesshtmx.controller;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.*;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import nl.leonklute.chesshtmx.service.PuzzleService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static nl.leonklute.chesshtmx.chess.Piece.toImgCode;
import static nl.leonklute.chesshtmx.service.PuzzleService.mapState;

@Slf4j
@RequestMapping("/puzzle")
@org.springframework.stereotype.Controller
public class PuzzleController {

    private final PuzzleService puzzleService;
    private Location from;

    public PuzzleController(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @GetMapping("")
    public String index(Principal principal, Model model) {
        log.info("index principal: {}", principal);
        var puzzle = puzzleService.getCurrentPuzzle(principal);
        if(null == puzzle) {
            return "redirect:/puzzle/list";
        }
        model.addAllAttributes(mapState(puzzle.game(), puzzle.orientation()));
        model.addAttribute("isUserMove", false);
        return "puzzle";
    }


    @PostMapping("/next-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void nextMove(@RequestParam(defaultValue = "false") String failed, Principal principal) throws IOException {
        log.debug("nextMove");
        puzzleService.nextMove(principal, false);
    }

    @PostMapping("/show-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void nextMove(Principal principal) throws IOException {
        log.debug("showMove");
        puzzleService.nextMove(principal, false);
    }

    @PostMapping("/start-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startMove(@RequestParam String location) {
        log.debug("startMove: {}", location);
        from = Location.from(location);
    }

    @PostMapping("/end-move")
    public String endMove(@RequestParam String location, Principal principal, Model model) {
        log.debug("endMove: {}", location);
        Puzzle puzzle = puzzleService.getCurrentPuzzle(principal);
        Location to = Location.from(location);
        if (to.equals(from)) {
            from = null;
            return "empty";
        }
        Move userMove = new Move(from, to);
        Map<String, String> valueMap = new HashMap<>();
        if(puzzle.isNextMove(userMove)){
            puzzle.game().move(userMove);
            valueMap.put(from.asAlgebraic(), null);
            valueMap.put(to.asAlgebraic(), toImgCode(puzzle.game().getPosition().get(to)));
        }else {
            model.addAttribute("wrongMove", userMove.asAlgebraic());
        }
        model.addAttribute("state", valueMap);
        model.addAttribute("rank", Character.toString(location.charAt(0)));
        model.addAttribute("rankIndex", location.codePointAt(0) - 'a');
        model.addAttribute("file", location.codePointAt(1) - '0');
        model.addAttribute("isUserMove", true);
        from = null;
        model.addAllAttributes(mapState(puzzle.game(), puzzle.orientation()));
        return "board";
    }

    @GetMapping("/{puzzleId}")
    public String getPuzzle(@PathVariable String puzzleId,Principal principal, Model model) {
        Optional<PuzzleEntity> puzzleOption = puzzleService.getPuzzle(puzzleId);
        log.debug("starting puzzle: {}", puzzleOption);
        if (puzzleOption.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var puzzleEntity = puzzleOption.get();
        var parser = new FenParser();
        var game = parser.parse(puzzleEntity.FEN);
        game.move(Move.from(puzzleEntity.moves.split(" ")[0]));
        List<Move> moves = Arrays.stream(puzzleEntity.moves.split(" ")).map(Move::from).toList();
        var puzzle = new Puzzle(game, game.getActive(), moves);
        puzzleService.setCurrentPuzzle(principal, puzzle);
        return "redirect:/puzzle";
    }

    @GetMapping("/list")
    public String getPuzzles(@RequestParam(defaultValue = "0") String page, Model model) {
        var puzzles = puzzleService.getPaginatedPuzzles(Integer.parseInt(page));
        log.info("puzzles: {}", puzzles);
        model.addAttribute("puzzles", puzzles);
        model.addAttribute("page", page);
        log.debug("model: {}", model.asMap());
        return "puzzles";
    }

}
