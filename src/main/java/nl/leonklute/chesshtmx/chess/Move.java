package nl.leonklute.chesshtmx.chess;

import java.util.List;

public record Move(
        Location from,
        Location to) {
    public static Move from(String algebraic) {
        return new Move(Location.from(algebraic.substring(0,2)), Location.from(algebraic.substring(2,4)));
    }

    public List<String> asAlgebraic() {
        return List.of(from.asAlgebraic(), to.asAlgebraic());
    }
}
