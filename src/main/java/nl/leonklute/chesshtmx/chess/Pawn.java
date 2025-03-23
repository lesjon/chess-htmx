package nl.leonklute.chesshtmx.chess;


public record Pawn(Color color) implements Piece {

    @Override
    public String toString() {
        return switch (color){
            case BLACK -> String.valueOf(Character.toChars(0x265F));
            case WHITE -> String.valueOf(Character.toChars(0x2659));
        };
    }

    public String toImgCode() {
        return switch (color){
            case BLACK -> "pd";
            case WHITE -> "pl";
        };
    }

}
