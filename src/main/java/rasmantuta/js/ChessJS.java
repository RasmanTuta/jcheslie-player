package rasmantuta.js;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessJS {
    private final static Invocable invocable;
    protected final static ScriptEngine engine;
    private final ScriptObjectMirror chess;

    static {
        engine = new NashornScriptEngineFactory().getScriptEngine("–optimistic-types=true");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStreamReader reader = new InputStreamReader(classloader.getResourceAsStream("rasmantuta/js/chess/chess.js"));

        try {
            Object eval = engine.eval("load(\"src/main/resources/rasmantuta/js/chess/chess.js\");");
//            Object eval = engine.eval(reader);
        } catch (ScriptException e) {
            throw new ChessException("Failed to load javaScript chess engine into Nashorn.", e);
        }
        invocable = (Invocable) engine;
    }

    public ChessJS(ScriptObjectMirror chess) {
        this.chess = chess;
    }

    public static ChessJS chess() {
        ScriptObjectMirror chess;
        try {
            chess = (ScriptObjectMirror) invocable.invokeFunction("Chess");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ChessException("Failed to initialize javaScript chess engine.", e);
        }
        return new ChessJS(chess);
    }

    public static ChessJS chess(String arg) {
        ScriptObjectMirror chess;
        try {
            chess = (ScriptObjectMirror) invocable.invokeFunction("Chess", arg);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ChessException("Failed to initialize javaScript chess engine.", e);
        }
        return new ChessJS(chess);
    }

    String ascii() {
        String ascii = (String) chess.callMember("ascii");
        return ascii;
    }

    void clear() {
        chess.callMember("clear");
    }

    String fen() {
        String fen = (String) chess.callMember("fen");
        return fen;
    }

    boolean gameOver() {
        boolean go = (boolean) chess.callMember("game_over");
        return go;
    }

    Piece get(String square) {
        ScriptObjectMirror piece = (ScriptObjectMirror) chess.callMember("get", square);

        return Converter.piece(piece);
    }

    Map<String, Object> header(String ... headerItems) {
        ScriptObjectMirror header = (ScriptObjectMirror) chess.callMember("header", headerItems);
        return Collections.unmodifiableMap(header);
    }

    List<String> history() {
        ScriptObjectMirror history = (ScriptObjectMirror) chess.callMember("history");

        return history.values().stream().map(Object::toString).collect(Collectors.toList());
    }

    List<Move> verboseHistory() {
        ScriptObjectMirror verbose = Converter.convertJSONString(engine, "{ verbose: true }");
        ScriptObjectMirror history = (ScriptObjectMirror) chess.callMember("history", verbose);

        return history.values().stream().map(h ->Converter.move((ScriptObjectMirror) h)).collect(Collectors.toList());
    }

    boolean insufficientMaterial() {
        return (boolean) chess.callMember("insufficient_material");
    }

    boolean inCheck() {
        return (boolean) chess.callMember("in_check");
    }

    boolean inCheckmate() {
        return (boolean) chess.callMember("in_checkmate");
    }

    boolean inDraw() {
        return (boolean) chess.callMember("in_draw");
    }

    boolean inStalemate() {
        return (boolean) chess.callMember("in_stalemate");
    }

    boolean inThreefoldRepetition() {
        return (boolean) chess.callMember("in_threefold_repetition");
    }

    boolean load(String fen) {
        return false;
    }

    boolean loadPgn(String pgn) {
        return false;
    }
    boolean loadSloppyPgn(String pgn, Character newlineChar) {
        return false;
    }

    Move move(String move) {
        ScriptObjectMirror retMove = (ScriptObjectMirror)chess.callMember("move", move);
        return Converter.move(retMove);
    }

    Object move(Move move) {
        ScriptObjectMirror retMove = (ScriptObjectMirror)chess.callMember("move", Converter.convert(engine, move));
        return Converter.move(retMove);
    }

    Object sloppyMove(String move) {
        ScriptObjectMirror sloppy = Converter.convertJSONString(engine, "{sloppy: true}");
        ScriptObjectMirror retMove = (ScriptObjectMirror)chess.callMember("move", move, sloppy);
        return Converter.move(retMove);
    }


    String[] moves(Map<Object, Object> options) {
        return null;
    }

    Move[] movesInformation() {
        return null;
    }

    int numberOfPieces(String color) {
        return 0;
    }

    int perft(int depth) {
        return 0;
    }

    String pgn(Map<Object, Object> options) {
        return "";
    }

    boolean put(Piece piece, String square) {
        boolean put;
        ScriptObjectMirror p = Converter.convert(engine, piece);
        put = (boolean) chess.callMember("put", p, square);
        return put;
    }

    Piece remove(String square) {
        return null;
    }

    boolean reset() {
        return false;
    }

    String squareColor(String square) {
        return "";
    }

    String turn() {
        return "";
    }

    Move undo() {
        return null;
    }

    Validation validateFen(String fen) {
        return null;
    }
}