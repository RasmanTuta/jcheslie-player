package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.EnumSet;

public class Move {
    public final String from;
    public final String to;
    @JsonIgnore
    public final Color color;
    @JsonIgnore
    public final EnumSet<Flag> flags;
    @JsonIgnore
    public final Type piece;
    public final String san;
    @JsonIgnore
    public final Type promotion;

    public Move(String from, String to, Color color, EnumSet<Flag> flags, Type piece, String san, Type promotion) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.flags = flags;
        this.piece = piece;
        this.san = san;
        this.promotion = promotion;
    }

    public Move(String from, String to, Color color, EnumSet<Flag> flags, Type piece, String san) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.flags = flags;
        this.piece = piece;
        this.san = san;
        this.promotion = null;
    }

    public Move(String from, String to) {
        this.from = from;
        this.to = to;
        this.color = null;
        this.flags = null;
        this.piece = null;
        this.san = null;
        this.promotion = null;
    }

    public Move(String from, String to, Type promotion) {
        this.from = from;
        this.to = to;
        this.color = null;
        this.flags = null;
        this.piece = null;
        this.san = null;
        this.promotion = promotion;
    }

    @JsonProperty
    public String getColor(){
        return null == color ? null: color.color();
    }

    @JsonProperty
    public String getFlags(){
        return null == flags ? null: Flag.flags(flags);
    }

    @JsonProperty
    public String getPiece(){
        return null == piece ? null: piece.piece();
    }

    @JsonProperty
    public String getPromotion(){
        return null == promotion ? null: promotion.piece();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (!from.equals(move.from)) return false;
        if (!to.equals(move.to)) return false;
        if (color != move.color) return false;
        if (flags != null ? !flags.equals(move.flags) : move.flags != null) return false;
        if (piece != move.piece) return false;
        if (san != null ? !san.equals(move.san) : move.san != null) return false;
        return promotion == move.promotion;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (flags != null ? flags.hashCode() : 0);
        result = 31 * result + (piece != null ? piece.hashCode() : 0);
        result = 31 * result + (san != null ? san.hashCode() : 0);
        result = 31 * result + (promotion != null ? promotion.hashCode() : 0);
        return result;
    }
}
