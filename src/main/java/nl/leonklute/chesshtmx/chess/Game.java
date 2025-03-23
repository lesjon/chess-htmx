package nl.leonklute.chesshtmx.chess;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<Move> moves = new ArrayList<>();

    public void move(Move move) {
        if (move.from().equals(move.to())) {
            return;
        }
        position.put(move.to(), position.get(move.from()));
        active = active.next();
        moves.add(move);
        position.remove(move.from());
    }

    public void put(Location location, Piece piece) {
        position.put(location, piece);
    }

    public static Game standardGame(){
        var game = new Game();
        game.put(new Location(1, 1), new Rook(Color.WHITE));
        game.put(new Location(2, 1), new Knight(Color.WHITE));
        game.put(new Location(3, 1), new Bishop(Color.WHITE));
        game.put(new Location(4, 1), new Queen(Color.WHITE));
        game.put(new Location(5, 1), new King(Color.WHITE));
        game.put(new Location(6, 1), new Bishop(Color.WHITE));
        game.put(new Location(7, 1), new Knight(Color.WHITE));
        game.put(new Location(8, 1), new Rook(Color.WHITE));
        for (int i = 1; i <= 8; i++) {
            game.put(new Location(i, 2), new Pawn(Color.WHITE));
        }

        for (int i = 1; i <= 8; i++) {
            game.put(new Location(i, 7), new Pawn(Color.BLACK));
        }
        game.put(new Location(1, 8), new Rook(Color.BLACK));
        game.put(new Location(2, 8), new Knight(Color.BLACK));
        game.put(new Location(3, 8), new Bishop(Color.BLACK));
        game.put(new Location(4, 8), new Queen(Color.BLACK));
        game.put(new Location(5, 8), new King(Color.BLACK));
        game.put(new Location(6, 8), new Bishop(Color.BLACK));
        game.put(new Location(7, 8), new Knight(Color.BLACK));
        game.put(new Location(8, 8), new Rook(Color.BLACK));
        return game;
    }
}
