package nl.leonklute.chesshtmx.controller;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.chess.*;
import nl.leonklute.chesshtmx.service.PuzzleService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

import static nl.leonklute.chesshtmx.chess.Piece.toImgCode;

@Slf4j
@RequestMapping("/analysis")
@org.springframework.stereotype.Controller
public class AnalysisController {

    @GetMapping("/")
    public String index(Model model) {
        PuzzleService.mapState(model, Game.standardGame(), Color.WHITE);
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
        FenParser fenParser = new FenParser();
        var game = fenParser.parse(String.join("/", row1, row2, row3, row4, row5, row6, row7, row8etc));
        PuzzleService.mapState(model, game, game.getActive());
        return "index";
    }

}
