package rasmantuta.js;

public class Move {
    public final String from;
    public final String to;
    public final String color;
    public final String flags;
    public final String piece;
    public final String san;
    public final String promotion;

    public Move(String from, String to, String color, String flags, String piece, String san, String promotion) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.flags = flags;
        this.piece = piece;
        this.san = san;
        this.promotion = promotion;

    }
}
