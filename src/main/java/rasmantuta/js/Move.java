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
}
