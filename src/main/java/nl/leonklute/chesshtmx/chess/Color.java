package nl.leonklute.chesshtmx.chess;

public enum Color {
    WHITE, BLACK;
    Color next() {
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }
}
