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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validation that = (Validation) o;

        if (valid != that.valid) return false;
        if (error_number != that.error_number) return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        int result = (valid ? 1 : 0);
        result = 31 * result + error_number;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Validation{" +
                "valid=" + valid +
                ", error_number=" + error_number +
                ", error='" + error + '\'' +
                '}';
    }
}
