package nl.leonklute.chesshtmx.chess;

public record Location(int file, int rank) {
    public String asAlgebraic() {
        return "" + ((char) ('a' + file - 1)) + rank;
    }

    public static Location from(String algebraicLocation){
        return new Location(algebraicLocation.charAt(0) - 'a' + 1, algebraicLocation.charAt(1) - '0');
    }

    public String lightOrDark() {
        if (file % 2 == rank % 2) {
            return "dark";
        }
        return "light";
    }
}
