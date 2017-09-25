package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Color {BLACK("b"), WHITE("w");
    private final String color;
    Color(String color) {
        this.color = color;
    }

    @JsonValue
    public String color(){return color;}

    public static Color fromColor(String text) {
        for (Color c : Color.values()) {
            if (c.color.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return null;
    }
}
