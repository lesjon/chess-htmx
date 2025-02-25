package nl.leonklute.chesshtmx.chess;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public record Puzzle(
        Game game,
        Color orientation,
        List<Move> moves,
        String id) {

    public boolean isNextMove(Move move) {
        log.debug("isNextMove({})", move);
        return !isFinished() && move.equals(moves.get(game.getMoves().size()));
    }

    public Move doNextMove() {
        var move = moves.get(game.getMoves().size());
        game.move(move);
        return move;
    }

    public boolean isFinished() {
        log.info("gameMoves: {} >= puzzleMove: {}", game.getMoves().size(), moves.size());
        return game.getMoves().size() >= moves.size();
    }
}
