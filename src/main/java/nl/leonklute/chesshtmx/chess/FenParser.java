package nl.leonklute.chesshtmx.chess;

public class FenParser {

    Game game = new Game();
    State state = State.PIECES;
    int x = 1;
    int y = 8;

    public Game parse(CharSequence fen) {
        for (int i = 0; i < fen.length(); i++) {
            parseChar(fen.charAt(i));
        }
        return game;
    }

    private void parseChar(char c) {
        switch (state) {
            case PIECES -> {
                switch (c) {
                    case '/' -> {
                        y--;
                        x = 1;
                    }
                    case ' ' ->
                            state = State.ACTIVE;

                    case '1' ->
                            x += 1;
                    case '2' ->
                            x += 2;
                    case '3' ->
                            x += 3;
                    case '4' ->
                            x += 4;
                    case '5' ->
                            x += 5;
                    case '6' ->
                            x += 6;
                    case '7' ->
                            x += 7;
                    case '8' ->
                            x += 8;

                    case 'k' ->
                            game.put(new Location(x++, y), new King(Color.BLACK));
                    case 'q' ->
                            game.put(new Location(x++, y), new Queen(Color.BLACK));
                    case 'r' ->
                            game.put(new Location(x++, y), new Rook(Color.BLACK));
                    case 'b' ->
                            game.put(new Location(x++, y), new Bishop(Color.BLACK));
                    case 'n' ->
                            game.put(new Location(x++, y), new Knight(Color.BLACK));
                    case 'p' ->
                            game.put(new Location(x++, y), new Pawn(Color.BLACK));

                    case 'K' ->
                            game.put(new Location(x++, y), new King(Color.WHITE));
                    case 'Q' ->
                            game.put(new Location(x++, y), new Queen(Color.WHITE));
                    case 'R' ->
                            game.put(new Location(x++, y), new Rook(Color.WHITE));
                    case 'B' ->
                            game.put(new Location(x++, y), new Bishop(Color.WHITE));
                    case 'N' ->
                            game.put(new Location(x++, y), new Knight(Color.WHITE));
                    case 'P' ->
                            game.put(new Location(x++, y), new Pawn(Color.WHITE));

                }
            }
            case ACTIVE -> {
                if (c == ' ') {
                    state = State.CASTLING;
                    break;
                }
                if (c == 'b')
                    game.setActive(Color.BLACK);
            }
            case CASTLING -> {
                switch (c) {
                    case ' ' ->
                            state = State.EN_PASSANT;
                    case 'Q' ->
                            game.setWhiteCastleQueenSide(false);
                    case 'K' ->
                            game.setWhiteCastleKingSide(false);
                    case 'q' ->
                            game.setBlackCastleQueenSide(false);
                    case 'k' ->
                            game.setBlackCastleKingSide(false);
                }

            }
            case EN_PASSANT -> {
                if (c == ' ') {
                    state = State.HALFMOVE_CLOCK;
                    break;
                }
                game.setEnPassant(game.getEnPassant() + Character.toString(c));
            }
            case HALFMOVE_CLOCK -> {
                if (c == ' ') {
                    state = State.FULLMOVE_NUMBER;
                    break;
                }
                game.setHalfmoveClock(game.getHalfmoveClock() * 10 + (c - '0'));
            }
            case FULLMOVE_NUMBER -> {
                if (c == ' ') {
                    state = State.FINISHED;
                    break;
                }
                game.setFullmoveNumber(game.getFullmoveNumber() * 10 + (c - '0'));
            }
            case FINISHED ->
                    throw new RuntimeException("More characters after parse finished: " + Character.toString(c));
        }
    }

    enum State {
        //        Piece placement data: Each rank is described, starting with rank 8 and ending with rank 1, with a "/" between each one; within each rank, the contents of the squares are described in order from the a-file to the h-file. Each piece is identified by a single letter taken from the standard English names in algebraic notation (pawn = "P", knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K"). White pieces are designated using uppercase letters ("PNBRQK"), while black pieces use lowercase letters ("pnbrqk"). A set of one or more consecutive empty squares within a rank is denoted by a digit from "1" to "8", corresponding to the number of squares.
        PIECES,
        //        Active color: "w" means that White is to move; "b" means that Black is to move.
        ACTIVE,
        //        Castling availability: If neither side has the ability to castle, this field uses the character "-". Otherwise, this field contains one or more letters: "K" if White can castle kingside, "Q" if White can castle queenside, "k" if Black can castle kingside, and "q" if Black can castle queenside. A situation that temporarily prevents castling does not prevent the use of this notation.
        CASTLING,
        //        En passant target square: This is a square over which a pawn has just passed while moving two squares; it is given in algebraic notation. If there is no en passant target square, this field uses the character "-". This is recorded regardless of whether there is a pawn in position to capture en passant.[6] An updated version of the spec has since made it so the target square is recorded only if a legal en passant capture is possible, but the old version of the standard is the one most commonly used.[7][8]
        EN_PASSANT,
        //        Halfmove clock: The number of halfmoves since the last capture or pawn advance, used for the fifty-move rule.[9]
        HALFMOVE_CLOCK,
        //        Fullmove number: The number of the full moves. It starts at 1 and is incremented after Black's move.
        FULLMOVE_NUMBER,
        FINISHED;
    }
}
