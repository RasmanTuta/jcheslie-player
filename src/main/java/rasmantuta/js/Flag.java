package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Flag {
    NON_CAPTURE('n'), PAWN_TWO_SQUARES('b'), EN_PASSANT('e'), CAPTURE('c'), PROMOTION('p'), KINGSIDE_CASTLING('k'), QUEENSIDE_CASTLING('q');
    private Character flag;

    private Flag(Character f) {
        this.flag = f;
    }

    @JsonValue
    public String flagString(){
        return "" + flag;
    }

    public Character flag(){
        return flag;
    }

    public static Flag fromFlag(Character character) {
        for (Flag f : Flag.values()) {
            if (f.flag.equals(character)) {
                return f;
            }
        }
        return null;
    }

    public static EnumSet<Flag> flags(String flags){
        return EnumSet.copyOf(flags.chars().mapToObj(c -> (char) c).map(Flag::fromFlag).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public static String flags(EnumSet<Flag> flags){
        return flags.stream().map(f -> f.flag()).map(c -> "" + c).collect(Collectors.joining( "" ));
    }
}
