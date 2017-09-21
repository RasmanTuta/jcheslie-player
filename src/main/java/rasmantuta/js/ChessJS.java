package rasmantuta.js;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ChessJS {
    private final static Invocable invocable;
    private final static ScriptEngine engine;
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

    List<String> verboseHistory() {
        return null;
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

    Object move(String move) {
        Object ret = chess.callMember("move", move);
        return ret;
    }

    Object move(Move move) {
        return null;
    }

    Object sloppyMove(String move) {
        return null;
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