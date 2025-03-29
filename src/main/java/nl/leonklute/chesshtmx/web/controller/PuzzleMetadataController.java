package nl.leonklute.chesshtmx.web.controller;

import nl.leonklute.chesshtmx.service.PuzzleService;
import nl.leonklute.chesshtmx.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@RequestMapping("puzzle-metadata")
public class PuzzleMetadataController {

    private final PuzzleService puzzleService;
    private final UserService userService;

    public PuzzleMetadataController(PuzzleService puzzleService, UserService userService) {
        this.puzzleService = puzzleService;
        this.userService = userService;
    }

    @PostMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disablePuzzle(@RequestParam("puzzle-id") String puzzleId){
        puzzleService.disable(puzzleId);
    }

    @PostMapping("/additional-moves")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void additionalMoves(@RequestParam("puzzle-id") String puzzleId, @RequestParam("additional-moves") String additionalMoves){
        puzzleService.addAdditionalMoves(puzzleId, additionalMoves);
    }

    @PostMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void like(Principal principal, @RequestParam("puzzle-id") String puzzleId){
        long id = userService.getUserIdByPrincipal(principal);
        puzzleService.like(puzzleId, id);
    }

    @PostMapping("/dislike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dislike(Principal principal, @RequestParam("puzzle-id") String puzzleId){
        long id = userService.getUserIdByPrincipal(principal);
        puzzleService.dislike(puzzleId, id);
    }

}
