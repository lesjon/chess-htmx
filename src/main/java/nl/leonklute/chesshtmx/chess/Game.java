package nl.leonklute.chesshtmx.chess;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Game {

    private final Map<Location, Piece> position = new HashMap<>();
    private Color active = Color.WHITE;
    private boolean whiteCastleKingSide = false;
    private boolean whiteCastleQueenSide = false;
    private boolean blackCastleKingSide = false;
    private boolean blackCastleQueenSide = false;

    private String enPassant;

    private int halfmoveClock = 0;
    private int fullmoveNumber = 1;

    public void move(Location from, Location to) {
        position.put(to, position.get(from));
        if (from.equals(to)) {
            return;
        }
        position.remove(from);
    }

    public void put(Location location, Piece piece) {
        position.put(location, piece);
    }
}
