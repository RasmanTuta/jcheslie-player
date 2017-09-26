package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SquareColor { LIGHT("light"), DARK("dark");

    private final String color;

    SquareColor(String color) {
        this.color = color;
    }

    @JsonValue
    public String color(){return color;}

    public static SquareColor fromColor(String text) {
        for (SquareColor c : SquareColor.values()) {
            if (c.color.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return null;
    }

}
