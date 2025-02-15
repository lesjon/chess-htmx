package nl.leonklute.chesshtmx.chess;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public record Puzzle(Game game, Color orientation, List<Move> moves) {

    public boolean isNextMove(Move move){
        log.debug("isNextMove({})", move);
        return move.equals(moves.get(game.getMoves().size()));
    }

}
