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

import static rasmantuta.js.Converter.*;

public class ChessJS {
    protected final static Invocable invocable;
    protected final static ScriptEngine engine;
    protected final ScriptObjectMirror chess;
    final private static ScriptObjectMirror VERBOSE;

    static {
        engine = new NashornScriptEngineFactory().getScriptEngine("–optimistic-types=true");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStreamReader reader = new InputStreamReader(classloader.getResourceAsStream("rasmantuta/js/chess/chess.js"));

        try {
            Object eval = engine.eval("load(\"src/main/resources/rasmantuta/js/chess/chess.js\");");
//            Object eval = engine.eval(reader);
        } catch (ScriptException e) {
            throw new ChessException("Failed to load javaScript chess engine into Nashorn.", e);
        }
        invocable = (Invocable) engine;
        VERBOSE = convertJSONString(engine, "{ verbose: true }");
    }

    protected ChessJS(ScriptObjectMirror chess) {
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

    public String ascii() {
        return (String) chess.callMember("ascii");
    }

    public void clear() {
        chess.callMember("clear");
    }

    public String fen() {
        return (String) chess.callMember("fen");
    }

    public boolean gameOver() {
        return (boolean) chess.callMember("game_over");
    }

    public Piece get(String square) {
        ScriptObjectMirror piece = (ScriptObjectMirror) chess.callMember("get", square);

        return piece(piece);
    }

    public Map<String, Object> header(String... headerItems) {
        ScriptObjectMirror header = (ScriptObjectMirror) chess.callMember("header", headerItems);
        return Collections.unmodifiableMap(header);
    }

    public List<String> history() {
        ScriptObjectMirror history = (ScriptObjectMirror) chess.callMember("history");

        return history.values().stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<Move> verboseHistory() {
        ScriptObjectMirror history = (ScriptObjectMirror) chess.callMember("history", VERBOSE);

        return movesList(history);
    }

    public boolean insufficientMaterial() {
        return (boolean) chess.callMember("insufficient_material");
    }

    public boolean inCheck() {
        return (boolean) chess.callMember("in_check");
    }

    public boolean inCheckmate() {
        return (boolean) chess.callMember("in_checkmate");
    }

    public boolean inDraw() {
        return (boolean) chess.callMember("in_draw");
    }

    public boolean inStalemate() {
        return (boolean) chess.callMember("in_stalemate");
    }

    public boolean inThreefoldRepetition() {
        return (boolean) chess.callMember("in_threefold_repetition");
    }

    public boolean load(String fen) {
        return (boolean)chess.callMember("load", fen);
    }

    public boolean loadPgn(String pgn) {
        return (boolean)chess.callMember("load_pgn", pgn);
    }

    public boolean loadSloppyPgn(String pgn, String newlineChar) {
        ScriptObjectMirror options = Converter.empty(engine);
        options.put("sloppy", true);
        if(null != newlineChar) options.put("newline_char", newlineChar);
        return (boolean) chess.callMember("load_pgn", pgn, options);
    }

    public Move move(String move) {
        ScriptObjectMirror retMove = (ScriptObjectMirror) chess.callMember("move", move);
        return Converter.move(retMove);
    }

    public Move move(Move move) {
        ScriptObjectMirror retMove = (ScriptObjectMirror) chess.callMember("move", convert(engine, move));
        return Converter.move(retMove);
    }

    public Move sloppyMove(String move) {
        ScriptObjectMirror sloppy = convertJSONString(engine, "{sloppy: true}");
        ScriptObjectMirror retMove = (ScriptObjectMirror) chess.callMember("move", move, sloppy);
        return Converter.move(retMove);
    }


    public List<String> moves() {
        ScriptObjectMirror moves = (ScriptObjectMirror) chess.callMember("moves");
        return moves.values().stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> moves(String square) {
        ScriptObjectMirror jsonSquare = convertJSONString(engine, "{square: \"" + square + "\"}");
        ScriptObjectMirror moves = (ScriptObjectMirror) chess.callMember("moves", jsonSquare);
        return moves.values().stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<Move> verboseMoves() {
        ScriptObjectMirror moves = (ScriptObjectMirror) chess.callMember("moves", VERBOSE);
        return movesList(moves);
    }

    public String pgn(Integer maxWidth, String newlineChar) {
        ScriptObjectMirror options = empty(engine);

        if(null != maxWidth) options.put("max_width", maxWidth);
        if(null != newlineChar) options. put("newline_char", newlineChar);

        return (String)chess.callMember("pgn", options);
    }

    public String pgn(){
        return pgn(null, null);
    }

    public boolean put(Piece piece, String square) {
        boolean put;
        ScriptObjectMirror p = convert(engine, piece);
        put = (boolean) chess.callMember("put", p, square);
        return put;
    }

    public Piece remove(String square) {
        ScriptObjectMirror removed = (ScriptObjectMirror) chess.callMember("remove", square);
        return piece(removed);
    }

    public void reset() {
        chess.callMember("reset");
    }

    public SquareColor squareColor(String square) {
        return SquareColor.fromColor((String)chess.callMember("square_color", square));
    }

    public Color turn() {
        return Color.fromColor((String)chess.callMember("turn"));
    }

    public Move undo() {
        return Converter.move((ScriptObjectMirror)chess.callMember("undo"));
    }

    public Validation validateFen(String fen) {
        return Converter.validation((ScriptObjectMirror)chess.callMember("validate_fen", fen));
    }
}