package rasmantuta.js;

import org.junit.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static rasmantuta.js.ChessJS.chess;

/*
    test content taken from the chess.js readme.md
 */
public class ChessJSTest {
    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static final String INITIAL_FEN_EXPECTED = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    public void testChess() throws Exception {
        // board defaults to the starting position when called with no parameters
        ChessJS chess = chess();
        assertThat(chess.fen(), is(INITIAL_FEN_EXPECTED));

        // pass in a FEN string to load a particular position
        String fen = "r1k4r/p2nb1p1/2b4p/1p1n1p2/2PP4/3Q1NB1/1P3PPP/R5K1 b - c3 0 19";
        ChessJS chess2 = chess(fen);
        assertThat(chess2.fen(), is(fen));
    }

    @Test
    public void ascii() throws Exception {
        ChessJS chess = chess();

// make some moves
        chess.move("e4");
        chess.move("e5");
        chess.move("f4");

        String ascii = chess.ascii();
        String asciiExpected =
                "   +------------------------+\n" +
                        " 8 | r  n  b  q  k  b  n  r |\n" +
                        " 7 | p  p  p  p  .  p  p  p |\n" +
                        " 6 | .  .  .  .  .  .  .  . |\n" +
                        " 5 | .  .  .  .  p  .  .  . |\n" +
                        " 4 | .  .  .  .  P  P  .  . |\n" +
                        " 3 | .  .  .  .  .  .  .  . |\n" +
                        " 2 | P  P  P  P  .  .  P  P |\n" +
                        " 1 | R  N  B  Q  K  B  N  R |\n" +
                        "   +------------------------+\n" +
                        "     a  b  c  d  e  f  g  h\n";
        assertThat(ascii, is(asciiExpected));
    }

    @Test
    public void clear() throws Exception {
        ChessJS chess = chess();
        chess.clear();
        String fen = chess.fen();

        assertThat(fen, is("8/8/8/8/8/8/8/8 w - - 0 1")); //<- empty board

    }

    @Test
    public void fen() throws Exception {
        ChessJS chess = chess();

// make some moves
        chess.move("e4");
        chess.move("e5");
        chess.move("f4");

        String fen = chess.fen();
        assertThat(fen, is("rnbqkbnr/pppp1ppp/8/4p3/4PP2/8/PPPP2PP/RNBQKBNR b KQkq f3 0 2"));
    }

    @Test
    public void game_over() throws Exception {
        ChessJS chess = chess();
        boolean gameOver = chess.gameOver();
        assertThat(gameOver, is(false));

        ChessJS chess2 = chess("4k3/4P3/4K3/8/8/8/8/8 b - - 0 78");
        boolean gameOver2 = chess2.gameOver();
        assertThat(gameOver2, is(true));

        ChessJS chess3 = chess("rnb1kbnr/pppp1ppp/8/4p3/5PPq/8/PPPPP2P/RNBQKBNR w KQkq - 1 3");
        boolean gameOver3 = chess3.gameOver();
        assertThat(gameOver3, is(true));
    }

    @Test
    public void get() throws Exception {
        ChessJS chess = chess();
        chess.clear();
        boolean put = chess.put(new Piece(Type.PAWN, Color.BLACK), "a5");// put a black pawn on a5

        assertThat(put, is(true));

        Piece a5 = chess.get("a5");
        assertThat(a5.color, is(Color.BLACK));
        assertThat(a5.type, is(Type.PAWN));
        Piece a6 = chess.get("a6");
        assertThat(a6, is(nullValue()));
    }


    @Test
    public void header() throws Exception {
        ChessJS chess = chess();

        chess.header("White", "Robert James Fischer");
        Map<String, Object> header = chess.header("Black", "Mikhail Tal");

        assertThat(header.get("White"), is("Robert James Fischer"));
        assertThat(header.get("Black"), is("Mikhail Tal"));

        chess.header("White", "Morphy", "Black", "Anderssen", "Date", "1858-??-??");

        Map<String, Object> header1 = chess.header();

        assertThat(header1.get("White"), is("Morphy"));
        assertThat(header1.get("Black"), is("Anderssen"));
        assertThat(header1.get("Date"), is("1858-??-??"));
    }

    @Test
    public void history() throws Exception {
        ChessJS chess = chess();
        chess.move("e4");
        chess.move("e5");
        chess.move("f4");
        chess.move("exf4");

        List<String> history = chess.history();
        assertThat(history, is(Arrays.asList("e4", "e5", "f4", "exf4")));

        List<Move> moves = chess.verboseHistory();
        assertThat(moves, is(Arrays.asList(
                new Move("e2", "e4", Color.WHITE, EnumSet.of(Flag.PAWN_TWO_SQUARES), Type.PAWN, "e4"),
                new Move("e7", "e5", Color.BLACK, EnumSet.of(Flag.PAWN_TWO_SQUARES), Type.PAWN, "e5"),
                new Move("f2", "f4", Color.WHITE, EnumSet.of(Flag.PAWN_TWO_SQUARES), Type.PAWN, "f4"),
                new Move("e5", "f4", Color.BLACK, EnumSet.of(Flag.CAPTURE), Type.PAWN, "exf4")
        )));
    }

    @Test
    public void insufficient_material() throws Exception {
        ChessJS chess = chess("k7/8/n7/8/8/8/8/7K b - - 0 1");
        assertThat(chess.insufficientMaterial(), is(true));

        ChessJS chess2 = chess();
        assertThat(chess2.insufficientMaterial(), is(false));
    }

    @Test
    public void in_check() throws Exception {
        ChessJS chess = chess("rnb1kbnr/pppp1ppp/8/4p3/5PPq/8/PPPPP2P/RNBQKBNR w KQkq - 1 3");
        assertThat(chess.inCheck(), is(true));

        ChessJS chess2 = chess();
        assertThat(chess2.inCheck(), is(false));

    }

    @Test
    public void in_checkmate() throws Exception {
        ChessJS chess = chess("rnb1kbnr/pppp1ppp/8/4p3/5PPq/8/PPPPP2P/RNBQKBNR w KQkq - 1 3");
        assertThat(chess.inCheckmate(), is(true));

        ChessJS chess2 = chess();
        assertThat(chess2.inCheckmate(), is(false));

    }

    @Test
    public void in_draw() throws Exception {
        ChessJS chess = chess("4k3/4P3/4K3/8/8/8/8/8 b - - 0 78");
        assertThat(chess.inDraw(), is(true));

        ChessJS chess2 = chess();
        assertThat(chess2.inDraw(), is(false));
    }

    @Test
    public void in_stalemate() throws Exception {
        ChessJS chess = chess("4k3/4P3/4K3/8/8/8/8/8 b - - 0 78");
        assertThat(chess.inStalemate(), is(true));

        ChessJS chess2 = chess();
        assertThat(chess2.inStalemate(), is(false));
    }

    @Test
    public void in_threefold_repetition() throws Exception {
        ChessJS chess = chess("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq occurs 1st time
        assertThat(chess.inThreefoldRepetition(), is(false));

        chess.move("Nf3");
        chess.move("Nf6");
        chess.move("Ng1");
        chess.move("Ng8");
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq occurs 2nd time
        assertThat(chess.inThreefoldRepetition(), is(false));

        chess.move("Nf3");
        chess.move("Nf6");
        chess.move("Ng1");
        chess.move("Ng8");
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq occurs 3rd time
        assertThat(chess.inThreefoldRepetition(), is(true));
    }

    @Test
    public void load() throws Exception {
        ChessJS chess = chess();
        assertTrue(chess.load("4r3/8/2p2PPk/1p6/pP2p1R1/P1B5/2P2K2/3r4 w - - 1 45"));

        assertThat(chess.fen(), is("4r3/8/2p2PPk/1p6/pP2p1R1/P1B5/2P2K2/3r4 w - - 1 45"));

        assertFalse(chess.load("4r3/8/X12XPk/1p6/pP2p1R1/P1B5/2P2K2/3r4 w - - 1 45"));
    }

    @Test
    public void load_pgn() throws Exception {
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

        ChessJS chess = chess();

        assertTrue(chess.loadPgn(pgn));

        assertThat(chess.fen(), is("1r3kr1/pbpBBp1p/1b3P2/8/8/2P2q2/P4PPP/3R2K1 b - - 0 24"));

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
        assertThat(ascii, is(asciiExpected));
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

        ChessJS chess = chess();

        assertFalse(chess.loadPgn(pgn));
        assertTrue(chess.loadSloppyPgn(pgn, '|'));

        assertThat(chess.fen(), is("r1bqk2r/pppp1ppp/2P5/8/1b6/1Q3pP1/PP1PPP1P/R1B1KB1R b KQkq - 1 8"));
    }

    @Test
    public void move() throws Exception {
        ChessJS chess = chess();

        Move e4 = chess.move("e4");
        assertThat(e4, is(new Move("e2", "e4", Color.WHITE, Flag.flags("b"), Type.PAWN, "e4")));

        assertNull(chess.move("nf6")); // SAN is case sensitive!!

        Move nf6 = chess.move("Nf6");
        assertThat(nf6, is(new Move("g8", "f6", Color.BLACK, Flag.flags("n"), Type.KNIGHT, "Nf6")));
    }

    @Test
    public void extendedMove() throws Exception {
        ChessJS chess = chess();

        Object move = chess.move(new Move("g2", "g3"));
        assertThat(move, is(new Move("g2", "g3", Color.WHITE, Flag.flags("n"), Type.PAWN, "g3")));
    }

    @Test
    public void sloppyMove() throws Exception {
        ChessJS chess = chess();
// various forms of Long Algebraic Notation
        assertThat(chess.sloppyMove("e2e4"), is(new Move("e2", "e4", Color.WHITE, Flag.flags("b"), Type.PAWN, "e4")));
        assertThat(chess.sloppyMove("e7-e5"), is(new Move("e7", "e5", Color.BLACK, Flag.flags("b"), Type.PAWN, "e5")));
        assertThat(chess.sloppyMove("Pf2f4"), is(new Move("f2", "f4", Color.WHITE, Flag.flags("b"), Type.PAWN, "f4")));
        assertThat(chess.sloppyMove("Pe5xf4"), is(new Move("e5", "f4", Color.BLACK, Flag.flags("c"), Type.PAWN, "exf4")));

// correctly parses incorrectly disambiguated moves
        chess = chess("r2qkbnr/ppp2ppp/2n5/1B2pQ2/4P3/8/PPP2PPP/RNB1K2R b KQkq - 3 7");

        assertNull(chess.move("Nge7"));  // Ne7 is unambiguous because the knight on c6 is pinned

        assertThat(chess.sloppyMove("Nge7"), is(new Move("g8", "e7", Color.BLACK, Flag.flags("n"), Type.KNIGHT, "Ne7")));
    }

    @Test
    public void moves() throws Exception {
        ChessJS chess = chess();
        List<String> moves = chess.moves();

        assertThat(moves.size(), is(20));

        assertTrue(moves.containsAll(Arrays.asList("a3", "a4", "b3", "b4", "c3", "c4", "d3", "d4", "e3", "e4", "f3", "f4", "g3", "g4", "h3", "h4", "Na3", "Nc3", "Nf3", "Nh3")));

    }

    @Test
    public void movesSquare() throws Exception {
        ChessJS chess = chess();
        List<String> moves = chess.moves("e2");
        assertThat(moves.size(), is(2));
        assertTrue(moves.containsAll(Arrays.asList("e3", "e4")));

        moves = chess.moves("e9"); // invalid square
        assertTrue(moves.isEmpty());
    }

    @Test
    public void verboseMoves() throws Exception {
        ChessJS chess = chess();
        List<Move> moves = chess.verboseMoves();
        assertThat(moves.size(), is(20));
        assertTrue(moves.contains(new Move("a2", "a3", Color.WHITE, Flag.flags("n"), Type.PAWN, "a3")));
    }

    @Test
    public void numberOfPieces() throws Exception {
        fail();
    }

    @Test
    public void pgn() throws Exception {
        ChessJS chess = chess();

        chess.header("White", "Plunky", "Black", "Plinkie");
        chess.move("e4");
        chess.move("e5");
        chess.move("Nc3");
        chess.move("Nc6");

        String pgn = chess.pgn(5, "<br />");

        assertTrue(pgn.startsWith("[White \"Plunky\"]<br />[Black \"Plinkie\"]<br /><br />1. e4 e5<br />2. Nc3 Nc6"));

        pgn = chess.pgn(null, "<br />");
        assertTrue(pgn.startsWith("[White \"Plunky\"]<br />[Black \"Plinkie\"]<br /><br />1. e4 e5 2. Nc3 Nc6"));

        pgn = chess.pgn(null, null);
        assertTrue(pgn.startsWith("[White \"Plunky\"]\n[Black \"Plinkie\"]\n\n1. e4 e5 2. Nc3 Nc6"));

        pgn = chess.pgn();
        assertTrue(pgn.startsWith("[White \"Plunky\"]\n[Black \"Plinkie\"]\n\n1. e4 e5 2. Nc3 Nc6"));
    }



    @Test
    public void put() throws Exception {
        ChessJS chess = chess();

        chess.clear();

        assertTrue(chess.put(new Piece(Type.PAWN, Color.BLACK), "a5")); // put a black pawn on a5
        assertTrue(chess.put(new Piece(Type.KING, Color.WHITE), "h1")); // shorthand
        assertThat(chess.fen(), is("8/8/8/p7/8/8/8/7K w - - 0 1"));

        chess.clear();
        assertTrue(chess.put(new Piece(Type.KING, Color.WHITE), "a1"));
        assertFalse(chess.put(new Piece(Type.KING, Color.WHITE), "h1")); // fail - two kings
    }

    @Test
    public void remove() throws Exception {
        ChessJS chess = chess();
        chess.clear();
        chess.put(new Piece(Type.PAWN, Color.BLACK), "a5"); // put a black pawn on a5
        chess.put(new Piece(Type.KING, Color.WHITE), "h1"); // put a white king on h1

        assertThat(chess.remove("a5"), is(new Piece(Type.PAWN, Color.BLACK)));
        assertThat(chess.remove("h1"), is(new Piece(Type.KING, Color.WHITE)));
        assertThat(chess.remove("e1"), is(nullValue()));
    }

    @Test
    public void reset() throws Exception {
        ChessJS chess = chess();

        chess.clear();

        assertTrue(chess.put(new Piece(Type.PAWN, Color.BLACK), "a5")); // put a black pawn on a5
        assertTrue(chess.put(new Piece(Type.KING, Color.WHITE), "h1")); // shorthand
        assertThat(chess.fen(), is("8/8/8/p7/8/8/8/7K w - - 0 1"));

        chess.reset();
        assertThat(chess.fen(), is(INITIAL_FEN_EXPECTED));
    }

    @Test
    public void square_color() throws Exception {
        fail();
    }

    @Test
    public void turn() throws Exception {
        fail();
    }

    @Test
    public void undo() throws Exception {
        fail();
    }

    @Test
    public void validate_fen() throws Exception {
        fail();
    }

}