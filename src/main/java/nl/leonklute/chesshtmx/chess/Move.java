package nl.leonklute.chesshtmx.chess;

import java.util.List;
import java.util.Optional;

public record Move(
        Location from,
        Location to,
        Optional<Piece> promotion) {
    public static Move from(String algebraic) {
        Optional<Piece> promotion = Optional.empty();
        if(algebraic.length() == 5 ){
            Color color = algebraic.charAt(3) == '8' ? Color.WHITE : Color.BLACK;
            promotion = Optional.of(Piece.of(algebraic.charAt(4), color));
        }
        return new Move(Location.from(algebraic.substring(0,2)), Location.from(algebraic.substring(2,4)), promotion);
    }

    public List<String> asAlgebraic() {
        return List.of(from.asAlgebraic(), to.asAlgebraic());
    }
}
