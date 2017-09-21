package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.EnumSet;

import static org.junit.Assert.assertNotNull;

public class MoveTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void serializeSimpleMove() throws Exception {
        Move move = new Move("e2", "e4");

        String moveString = mapper.writeValueAsString(move);
        assertNotNull(moveString);
        JSONAssert.assertEquals("{\"from\": \"e2\", \"to\": \"e4\"}", moveString, false);
    }

    @Test
    public void serializeSimpleMovePromotion() throws Exception {
        Move move = new Move("e7", "e8", Type.QUEEN);

        String moveString = mapper.writeValueAsString(move);
        assertNotNull(moveString);
        JSONAssert.assertEquals("{\"from\": \"e7\", \"to\": \"e8\", \"promotion\": \"q\" }", moveString, false);
    }

    @Test
    public void serializeMove() throws Exception {
        Move move = new Move("e7", "e8", Color.WHITE, EnumSet.of(Flag.NON_CAPTURE, Flag.PAWN_TWO_SQUARES), Type.PAWN, "e8Q", Type.QUEEN);

        String moveString = mapper.writeValueAsString(move);
        assertNotNull(moveString);
        JSONAssert.assertEquals("{\"from\": \"e7\", \"to\": \"e8\", \"promotion\": \"q\", \"color\": \"w\" , \"flags\": \"nb\" , \"piece\": \"p\" , \"san\": \"e8Q\" }", moveString, false);
    }
}
