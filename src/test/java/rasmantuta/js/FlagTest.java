package rasmantuta.js;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.EnumSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FlagTest {
    @Test
    public void flagString() throws Exception {
        assertThat(Flag.CAPTURE.flagString(), is("c"));
        assertThat(Flag.EN_PASSANT.flagString(), is("e"));
        assertThat(Flag.KINGSIDE_CASTLING.flagString(), is("k"));
        assertThat(Flag.NON_CAPTURE.flagString(), is("n"));
        assertThat(Flag.PAWN_TWO_SQUARES.flagString(), is("b"));
        assertThat(Flag.PROMOTION.flagString(), is("p"));
        assertThat(Flag.QUEENSIDE_CASTLING.flagString(), is("q"));
    }

    @Test
    public void flag() throws Exception {
        assertThat(Flag.CAPTURE.flag(), is('c'));
        assertThat(Flag.EN_PASSANT.flag(), is('e'));
        assertThat(Flag.KINGSIDE_CASTLING.flag(), is('k'));
        assertThat(Flag.NON_CAPTURE.flag(), is('n'));
        assertThat(Flag.PAWN_TWO_SQUARES.flag(), is('b'));
        assertThat(Flag.PROMOTION.flag(), is('p'));
        assertThat(Flag.QUEENSIDE_CASTLING.flag(), is('q'));
    }

    @Test
    public void fromFlag() throws Exception {
        assertThat(Flag.fromFlag('c'), is(Flag.CAPTURE));
        assertThat(Flag.fromFlag('e'), is(Flag.EN_PASSANT));
        assertThat(Flag.fromFlag('k'), is(Flag.KINGSIDE_CASTLING));
        assertThat(Flag.fromFlag('n'), is(Flag.NON_CAPTURE));
        assertThat(Flag.fromFlag('b'), is(Flag.PAWN_TWO_SQUARES));
        assertThat(Flag.fromFlag('p'), is(Flag.PROMOTION));
        assertThat(Flag.fromFlag('q'), is(Flag.QUEENSIDE_CASTLING));
    }

    @Test
    public void flagsString() throws Exception {
        String flags = Flag.flags(EnumSet.allOf(Flag.class));

        assertThat(flags.length(), is(7));

        "ceknbpq".chars().mapToObj(c -> (char) c).map(c -> "" + c).forEach(c -> assertTrue(flags.contains(c)));
    }

    @Test
    public void flagsEnumSet() throws Exception {
        EnumSet<Flag> flags = Flag.flags("ceknbpq");
        assertThat(flags.size(), is(7));
        flags.containsAll(EnumSet.allOf(Flag.class));
    }

    @Test
    public void set() throws Exception {
        EnumSet<Flag> set1 = EnumSet.of(Flag.CAPTURE, Flag.EN_PASSANT);
        EnumSet<Flag> set2 = EnumSet.of(Flag.EN_PASSANT, Flag.CAPTURE);

        String flags1 = Flag.flags(set1);
        String flags2 = Flag.flags(set2);
    }
}