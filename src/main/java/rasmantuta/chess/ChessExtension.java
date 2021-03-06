package rasmantuta.chess;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import rasmantuta.js.ChessException;
import rasmantuta.js.ChessJS;
import rasmantuta.js.Color;
import rasmantuta.js.Move;
import rasmantuta.js.Piece;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChessExtension extends ChessJS {

    public static final List<String> LETTERS = List.of("a", "b", "c", "d", "e", "f", "g", "h");
    public static final List<String> SQUARES;
    int numberOfMoves = 0;

    static{
        List<String> squares = new ArrayList<>();
        IntStream.iterate(1, i -> i < 10, i -> i++).forEach(i -> LETTERS.forEach(l -> squares.add(l + i)));
        SQUARES = Collections.unmodifiableList(squares);
    }

    public ChessExtension(ScriptObjectMirror chess) {
        super(chess);
    }
    public static ChessExtension chess() {
        ScriptObjectMirror chess;
        try {
            chess = (ScriptObjectMirror) invocable.invokeFunction("Chess");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ChessException("Failed to initialize javaScript chess engine.", e);
        }
        return new ChessExtension(chess);
    }

    public static ChessExtension chess(String arg) {
        ScriptObjectMirror chess;
        try {
            chess = (ScriptObjectMirror) invocable.invokeFunction("Chess", arg);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ChessException("Failed to initialize javaScript chess engine.", e);
        }
        return new ChessExtension(chess);
    }

    public List<Piece> pieces(Color color){
        return SQUARES.stream()
                .map(this::get)
                .filter(piece -> null != piece && piece.color == color)
                .collect(Collectors.toList());
    }

    public int numberOfPieces(Color color){
        return pieces(color).size();
    }

    public List<Move> movesInformation(){
        return verboseMoves();
    }

    @Override
    public Move move(String move) {
        Move move1 = super.move(move);
        if(null != move1) numberOfMoves ++;

        return move1;
    }

    @Override
    public Move move(Move move) {
        Move move1 = super.move(move);
        if(null != move1) numberOfMoves ++;

        return move1;
    }

    @Override
    public boolean gameOver() {
        return numberOfMoves >= 100 || super.gameOver();
    }

    @Override
    public boolean loadPgn(String pgn) {
        boolean success = super.loadPgn(pgn);
        if(success) numberOfMoves = history().size();
        return success;
    }

    @Override
    public boolean loadSloppyPgn(String pgn, String newlineChar) {
        boolean success = super.loadSloppyPgn(pgn, newlineChar);
        if(success) numberOfMoves = history().size();
        return success;
    }
}
