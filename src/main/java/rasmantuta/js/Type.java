package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {PAWN("p"), KNIGHT("n"), BISHOP("b"), ROOK("r"), QUEEN("q"), KING("k");
    private final String piece;
    Type(String piece) {
        this.piece = piece;
    }

    @JsonValue
    public String piece(){return piece;}

    @Override
    public String toString() {
        return piece;
    }

    @JsonCreator
    public static Type fromPiece(String text) {
        for (Type p : Type.values()) {
            if (p.piece.equalsIgnoreCase(text)) {
                return p;
            }
        }
        return null;
    }

}
