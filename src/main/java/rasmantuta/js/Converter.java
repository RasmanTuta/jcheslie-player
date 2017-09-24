package rasmantuta.js;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class Converter {

    private final static ObjectMapper mapper = new ObjectMapper();

    static{
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

//    public static Bindings piece(ScriptEngine engine, Piece piece) {
//        Bindings bindings = engine.createBindings();
//        bindings.put("type", piece.type.piece());
//        bindings.put("color", piece.color.color());
//
//        return bindings;
//    }

    public static Piece piece(ScriptObjectMirror mirror) {
        if (null == mirror) return null;
        Color color = Color.fromColor((String) mirror.get("color"));
        Type piece = Type.fromPiece((String) mirror.get("type"));
        return new Piece(piece, color);
    }

    public static ScriptObjectMirror convert(ScriptEngine engine, Object obj) {
        String inputModelAsString;
        try {
            inputModelAsString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new ChessException("Failed to serialize object to JSON", e);
        }
        try {
            return (ScriptObjectMirror) engine.eval("Java.asJSONCompatible(" + inputModelAsString + ")");
        } catch (ScriptException e) {
            throw new ChessException("Failed to eval JSON object", e);
        }
    }

    public static ScriptObjectMirror convertJSONString(ScriptEngine engine, String json) {
        try {
            return (ScriptObjectMirror) engine.eval("Java.asJSONCompatible(" + json + ")");
        } catch (ScriptException e) {
            throw new ChessException("Failed to eval JSON object", e);
        }
    }

    public static Move move(Map<String, Object> move) {

        return null == move ? null : new Move((String)move.get("from"), (String)move.get("to"), Color.fromColor((String)move.get("color")), Flag.flags((String)move.get("flags"))
                , Type.fromPiece((String)move.get("piece")), (String)move.get("san"), Type.fromPiece((String)move.get("promotion")));
    }
}