package rasmantuta.js;

public class Validation {
    public final boolean valid;
    public final int error_number;
    public final String error;

    public Validation(boolean valid, int error_number, String error) {
        this.valid = valid;
        this.error_number = error_number;
        this.error = error;
    }
}
