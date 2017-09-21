package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Pieces {PAWN("p"), KNIGHT("n"), BISHOP("b"), ROOK("r"), QUEEN("q"), KING("k");
    private final String piece;
    Pieces(String piece) {
        this.piece = piece;
    }

    @JsonValue
    public String piece(){return piece;}

    @Override
    public String toString() {
        return piece;
    }

    @JsonCreator
    public static Pieces fromPiece(String text) {
        for (Pieces p : Pieces.values()) {
            if (p.piece.equalsIgnoreCase(text)) {
                return p;
            }
        }
        return null;
    }

}
