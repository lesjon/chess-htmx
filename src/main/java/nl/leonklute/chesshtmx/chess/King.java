package nl.leonklute.chesshtmx.chess;

public record King(Color color) implements Piece {

    @Override
    public String toString() {
        return switch (color){
            case BLACK -> String.valueOf(Character.toChars(0x265A));
            case WHITE -> String.valueOf(Character.toChars(0x2654));
        };
    }

    public String toImgCode() {
        return switch (color){
            case BLACK -> "kd";
            case WHITE -> "kl";
        };
    }

}
