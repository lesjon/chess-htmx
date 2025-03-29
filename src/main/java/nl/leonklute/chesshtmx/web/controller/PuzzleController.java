package nl.leonklute.chesshtmx.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.*;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.PuzzleService;
import nl.leonklute.chesshtmx.service.UserService;
import nl.leonklute.chesshtmx.service.model.PuzzleListing;
import nl.leonklute.chesshtmx.puzzle.Themes;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
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
    private final UserService userService;
    private Location from;

    public PuzzleController(PuzzleService puzzleService, UserService userService) {
        this.puzzleService = puzzleService;
        this.userService = userService;
    }

    @GetMapping("")
    public String index(Principal principal, Model model) {
        log.info("index principal: {}", principal);
        var puzzle = puzzleService.getCurrentPuzzle(userService.getUserIdByPrincipal(principal));
        if (null == puzzle) {
            return "redirect:/puzzle/next";
        }
        model.addAllAttributes(mapState(puzzle.game(), puzzle.orientation()));
        model.addAttribute("userToMove", true);
        model.addAttribute("puzzle", puzzle);
        return "puzzle";
    }

    @GetMapping("/next")
    public String nextPuzzle(Principal principal) {
        var puzzle = puzzleService.getNextPuzzle(userService.getUserIdByPrincipal(principal));
        return "redirect:/puzzle/" + puzzle.getPuzzleId();
    }

    @PostMapping("/next-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void nextMove(Principal principal) throws IOException {
        log.debug("nextMove");
        puzzleService.nextMove(userService.getUserIdByPrincipal(principal));
    }

    @PostMapping("/show-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void showMove(Principal principal) throws IOException {
        log.debug("showMove");
        puzzleService.nextMove(userService.getUserIdByPrincipal(principal));
    }

    @PostMapping("/start-move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startMove(@RequestParam String location) {
        log.debug("startMove: {}", location);
        from = Location.from(location);
    }

    @PostMapping("/end-move")
    public String endMove(@RequestParam String location, @Nullable @RequestParam String promotion, Principal principal, Model model, HttpServletResponse response) {
        log.debug("endMove: {}", location);
        Puzzle puzzle = puzzleService.getCurrentPuzzle(userService.getUserIdByPrincipal(principal));
        Location to = Location.from(location);
        if (to.equals(from)) {
            from = null;
            return "empty";
        }
        var optionalPiece = Optional.ofNullable(promotion)
                .map(s -> Piece.of(s.charAt(0), puzzle.orientation()));
        Move userMove = new Move(from, to, optionalPiece);
        Map<String, String> valueMap = new HashMap<>();
        if (puzzle.isNextMove(userMove)) {
            puzzle.game().move(userMove);
            if(puzzle.isFinished()){
                var result = puzzleService.puzzleFinished(userService.getUserIdByPrincipal(principal));

                UserEntity user = userService.getUserByPrincipal(principal);
                user.setRating(user.getRating() + 1);
                user.setRating(user.getRating() - 1);
                userService.update(user);
                response.addHeader("HX-Refresh", "true");
            }
            valueMap.put(from.asAlgebraic(), null);
            valueMap.put(to.asAlgebraic(), toImgCode(puzzle.game().getPosition().get(to)));
        } else {
            model.addAttribute("wrongMove", userMove.asAlgebraic());
        }
        model.addAttribute("state", valueMap);
        model.addAttribute("rank", Character.toString(location.charAt(0)));
        model.addAttribute("rankIndex", location.codePointAt(0) - 'a');
        model.addAttribute("file", location.codePointAt(1) - '0');
        model.addAttribute("userToMove", false);
        from = null;
        model.addAllAttributes(mapState(puzzle.game(), puzzle.orientation()));
        return "board";
    }

    @GetMapping("/{puzzleId}")
    public String getPuzzle(@PathVariable String puzzleId, Principal principal, Model model) {
        Optional<PuzzleEntity> puzzleOption = puzzleService.getPuzzle(puzzleId);
        log.debug("starting puzzle: {}", puzzleOption);
        if (puzzleOption.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var puzzleEntity = puzzleOption.get();
        var parser = new FenParser();
        var game = parser.parse(puzzleEntity.getFEN());
        game.move(Move.from(puzzleEntity.getMoves().split(" ")[0]));
        List<Move> moves = Arrays.stream(puzzleEntity.getMoves().split(" ")).map(Move::from).toList();
        var puzzle = new Puzzle(game, game.getActive(), moves, new ArrayList<>(), puzzleEntity.getPuzzleId());
        puzzleService.setCurrentPuzzle(userService.getUserIdByPrincipal(principal), puzzle);
        return "redirect:/puzzle";
    }

    @GetMapping("/list")
    public String getPuzzles(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "*") String theme, Model model) {
        List<PuzzleListing> puzzles = puzzleService.getPaginatedPuzzles(Integer.parseInt(page), theme);
        log.debug("puzzles: {}", puzzles);
        model.addAttribute("puzzles", puzzles);
        model.addAttribute("page", page);
        model.addAttribute("themes", Arrays.stream(Themes.values()).map(Themes::getName).toArray());
        model.addAttribute("filteredTheme", theme);
        log.debug("model: {}", model.asMap());
        return "puzzles";
    }

}
