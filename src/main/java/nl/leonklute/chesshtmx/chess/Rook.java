package nl.leonklute.chesshtmx.chess;

public record Rook(Color color) implements Piece {

    @Override
    public String toString() {
        return switch (color){
            case BLACK -> String.valueOf(Character.toChars(0x265C));
            case WHITE -> String.valueOf(Character.toChars(0x2656));
        };
    }

    public String toImgCode() {
        return switch (color){
            case BLACK -> "rd";
            case WHITE -> "rl";
        };
    }


}
