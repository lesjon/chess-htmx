package nl.leonklute.chesshtmx.chess;

public record Knight(Color color) implements Piece {

    @Override
    public String toString() {
        return switch (color){
            case BLACK -> String.valueOf(Character.toChars(0x265E));
            case WHITE -> String.valueOf(Character.toChars(0x2658));
        };
    }

    public String toImgCode() {
        return switch (color){
            case BLACK -> "nd";
            case WHITE -> "nl";
        };
    }

}
