package nl.leonklute.chesshtmx.chess;

import org.springframework.lang.Nullable;

public sealed interface Piece permits Pawn, King, Knight, Bishop, Rook, Queen {

    static String toImgCode(@Nullable Piece piece) {
        if (piece == null) {
            return "";
        }
        return piece.toImgCode();
    }

    Color color();

    String toImgCode();
}
