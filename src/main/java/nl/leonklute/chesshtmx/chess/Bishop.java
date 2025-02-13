package nl.leonklute.chesshtmx.chess;

public record Bishop(Color color) implements Piece {

    @Override
    public String toString() {
        return switch (color){
            case BLACK -> String.valueOf(Character.toChars(0x265D));
            case WHITE -> String.valueOf(Character.toChars(0x2657));
        };
    }

    public String toImgCode() {
        return switch (color){
            case BLACK -> "bd";
            case WHITE -> "bl";
        };
    }

}
