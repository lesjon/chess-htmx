package nl.leonklute.chesshtmx.chess;

import org.springframework.lang.Nullable;

public sealed interface Piece permits Pawn, King, Knight, Bishop, Rook, Queen {

    static String toImgCode(@Nullable Piece piece) {
        if (piece == null) {
            return "";
        }
        return piece.toImgCode();
    }

    static Piece of(Character pieceLetterLowerCase, Color color) {
        return switch (pieceLetterLowerCase) {
            case 'n'-> new Knight(color);
            case 'b'-> new Bishop(color);
            case 'r'-> new Rook(color);
            case 'q'-> new Queen(color);
            default ->
                    throw new IllegalStateException("Unexpected value: " + pieceLetterLowerCase);
        };
    }

    Color color();

    String toImgCode();
}
