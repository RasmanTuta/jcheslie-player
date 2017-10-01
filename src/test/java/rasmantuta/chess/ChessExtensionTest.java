package rasmantuta.chess;

import org.hamcrest.core.Is;
import org.junit.Test;
import rasmantuta.js.ChessJS;
import rasmantuta.js.Color;
import rasmantuta.js.Flag;
import rasmantuta.js.Move;
import rasmantuta.js.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static rasmantuta.chess.ChessExtension.chess;

public class ChessExtensionTest {

    private static final String INITIAL_FEN_EXPECTED = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Pattern BLACK_PATTERN = Pattern.compile("[pbnqkr]");
    public static final Pattern WHITE_PATTERN = Pattern.compile("[PBNQKR]");

    @Test
    public void testChess() throws Exception {
        // board defaults to the starting position when called with no parameters
        ChessExtension chess = chess();
        assertThat(chess.fen(), is(INITIAL_FEN_EXPECTED));

        // pass in a FEN string to load a particular position
        String fen = "r1k4r/p2nb1p1/2b4p/1p1n1p2/2PP4/3Q1NB1/1P3PPP/R5K1 b - c3 0 19";
        ChessExtension chess2 = chess(fen);
        assertThat(chess2.fen(), is(fen));

    }

    @Test
    public void pieces() throws Exception {
        fail();
    }

    @Test
    public void numberOfPieces() throws Exception {
        ChessExtension chess = chess();

        chess.move("e4");
        assertThat(chess.numberOfPieces(Color.BLACK), is(16));
        assertThat(chess.numberOfPieces(Color.WHITE), is(16));
        chess.move("f5");
        assertThat(chess.numberOfPieces(Color.BLACK), is(16));
        assertThat(chess.numberOfPieces(Color.WHITE), is(16));
        chess.move("Nc3");
        assertThat(chess.numberOfPieces(Color.BLACK), is(16));
        assertThat(chess.numberOfPieces(Color.WHITE), is(16));
        chess.move("fxe4");
        assertThat(chess.numberOfPieces(Color.BLACK), is(16));
        assertThat(chess.numberOfPieces(Color.WHITE), is(15));

        IntStream.range(0, 100_000).forEach(i -> chess.numberOfPieces(Color.WHITE));
    }

    @Test
    public void numberOfPiecesAlt() throws Exception {
        ChessExtension chess = chess();

        chess.move("e4");
        assertThat(alternativeNumberOfPieces(chess, Color.BLACK), is(16));
        assertThat(alternativeNumberOfPieces(chess, Color.WHITE), is(16));
        chess.move("f5");
        assertThat(alternativeNumberOfPieces(chess, Color.BLACK), is(16));
        assertThat(alternativeNumberOfPieces(chess, Color.WHITE), is(16));
        chess.move("Nc3");
        assertThat(alternativeNumberOfPieces(chess, Color.BLACK), is(16));
        assertThat(alternativeNumberOfPieces(chess, Color.WHITE), is(16));
        chess.move("fxe4");
        assertThat(alternativeNumberOfPieces(chess, Color.BLACK), is(16));
        assertThat(alternativeNumberOfPieces(chess, Color.WHITE), is(15));

        IntStream.range(0, 100_000).forEach(i -> alternativeNumberOfPieces(chess, Color.WHITE));
    }

    int alternativeNumberOfPieces(ChessExtension chess, Color color){
        String fen = chess.fen();
        String firstPart = fen.substring(0, fen.indexOf(' '));
        Pattern p = color==Color.WHITE ? WHITE_PATTERN: BLACK_PATTERN;

        Matcher matcher = p.matcher(firstPart);


        int i = 0;
        while(matcher.find()){
            i++;
        }
        return i;
    }

    @Test
    public void movesInformation() throws Exception {
        fail();
    }

    @Test
    public void move() throws Exception {
        ChessExtension chess = chess();

        Move e4 = chess.move("e4");
        assertThat(e4, is(new Move("e2", "e4", Color.WHITE, Flag.flags("b"), Type.PAWN, "e4")));

        assertNull(chess.move("nf6")); // SAN is case sensitive!!

        Move nf6 = chess.move("Nf6");
        assertThat(nf6, is(new Move("g8", "f6", Color.BLACK, Flag.flags("n"), Type.KNIGHT, "Nf6")));

        assertThat(chess.numberOfMoves, is(2));
    }

    @Test
    public void extendedMove() throws Exception {
        ChessExtension chess = chess();

        Object move = chess.move(new Move("g2", "g3"));
        assertThat(move, Is.is(new Move("g2", "g3", Color.WHITE, Flag.flags("n"), Type.PAWN, "g3")));
        assertThat(chess.numberOfMoves, is(1));
    }

    @Test
    public void gameOver() throws Exception {
        fail();
    }

    @Test
    public void loadPgn() throws Exception {
        String pgn = "[Event \"Casual Game\"]\n" +
                "[Site \"Berlin GER\"]\n" +
                "[Date \"1852.??.??\"]\n" +
                "[EventDate \"?\"]\n" +
                "[Round \"?\"]\n" +
                "[Result \"1-0\"]\n" +
                "[White \"Adolf Anderssen\"]\n" +
                "[Black \"Jean Dufresne\"]\n" +
                "[ECO \"C52\"]\n" +
                "[WhiteElo \"?\"]\n" +
                "[BlackElo \"?\"]\n" +
                "[PlyCount \"47\"]\n" +
                "\n" +
                "1.e4 e5 2.Nf3 Nc6 3.Bc4 Bc5 4.b4 Bxb4 5.c3 Ba5 6.d4 exd4 7.O-O\n" +
                "d3 8.Qb3 Qf6 9.e5 Qg6 10.Re1 Nge7 11.Ba3 b5 12.Qxb5 Rb8 13.Qa4\n" +
                "Bb6 14.Nbd2 Bb7 15.Ne4 Qf5 16.Bxd3 Qh5 17.Nf6+ gxf6 18.exf6\n" +
                "Rg8 19.Rad1 Qxf3 20.Rxe7+ Nxe7 21.Qxd7+ Kxd7 22.Bf5+ Ke8\n" +
                "23.Bd7+ Kf8 24.Bxe7# 1-0";

        ChessExtension chess = chess();

        assertTrue(chess.loadPgn(pgn));

        assertThat(chess.fen(), Is.is("1r3kr1/pbpBBp1p/1b3P2/8/8/2P2q2/P4PPP/3R2K1 b - - 0 24"));

        String ascii = chess.ascii();
        String asciiExpected =
                "   +------------------------+\n" +
                        " 8 | .  r  .  .  .  k  r  . |\n" +
                        " 7 | p  b  p  B  B  p  .  p |\n" +
                        " 6 | .  b  .  .  .  P  .  . |\n" +
                        " 5 | .  .  .  .  .  .  .  . |\n" +
                        " 4 | .  .  .  .  .  .  .  . |\n" +
                        " 3 | .  .  P  .  .  q  .  . |\n" +
                        " 2 | P  .  .  .  .  P  P  P |\n" +
                        " 1 | .  .  .  R  .  .  K  . |\n" +
                        "   +------------------------+\n" +
                        "     a  b  c  d  e  f  g  h\n";
        assertThat(ascii, Is.is(asciiExpected));

        assertThat(chess.numberOfMoves, is(47));
    }

    @Test
    public void loadSloppyPgn() throws Exception {
        String pgn = String.join("|", new String[]{"[Event \"Wijk aan Zee (Netherlands)\"]",
                "[Date \"1971.01.26\"]",
                "[Result \"1-0\"]",
                "[White \"Tigran Vartanovich Petrosian\"]",
                "[Black \"Hans Ree\"]",
                "[ECO \"A29\"]",
                "",
                "1. Pc2c4 Pe7e5", // non-standard
                "2. Nc3 Nf6",
                "3. Nf3 Nc6",
                "4. g2g3 Bb4",    // non-standard
                "5. Nd5 Nxd5",
                "6. c4xd5 e5-e4", // non-standard
                "7. dxc6 exf3",
                "8. Qb3 1-0"});

        ChessExtension chess = chess();

        assertFalse(chess.loadPgn(pgn));
        assertTrue(chess.loadSloppyPgn(pgn, "\\|"));

        assertThat(chess.fen(), Is.is("r1bqk2r/pppp1ppp/2P5/8/1b6/1Q3pP1/PP1PPP1P/R1B1KB1R b KQkq - 1 8"));
        assertThat(chess.numberOfMoves, is(15));
    }
}