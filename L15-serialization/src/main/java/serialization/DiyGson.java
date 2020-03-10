package serialization;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class DiyGson implements Serialization {

    @Override
    public String toJson(Object src) {
        if (src == null) {
            return JsonValue.NULL.toString();
        } else {
            return serialize(src).toString();
        }
    }

    private JsonValue serialize(Object src) {
        if (Type.isNull(src)) {
            return JsonValue.NULL;
        } else if (Type.isBoolean(src)) {
            return (boolean) src ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (Type.isByte(src)) {
            return Json.createValue((byte) src);
        } else if (Type.isShort(src)) {
            return Json.createValue((short) src);
        } else if (Type.isInt(src)) {
            return Json.createValue((int) src);
        } else if (Type.isLong(src)) {
            return Json.createValue((long) src);
        } else if (Type.isFloat(src)) {
            return Json.createValue((float) src);
        } else if (Type.isDouble(src)) {
            return Json.createValue((double) src);
        } else if (Type.isCharacter(src)) {
            return Json.createValue(src.toString());
        } else if (Type.isString(src)) {
            return Json.createValue((String) src);
        } else if (Type.isCollection(src)) {
            return Json.createArrayBuilder((Collection<?>) src).build();
        } else if (Type.isArray(src)) {
            var builder = serializeArray(src);
            return builder.build();
        } else {
            var builder = serializeObject(src);
            return builder.build();
        }
    }

    private JsonArrayBuilder serializeArray(Object src) {
        var builder = Json.createArrayBuilder();
        for (int i = 0; i < Array.getLength(src); i++) {
            final Object o = Array.get(src, i);
            if (o == null) {
                builder.addNull();
            } else {
                builder.add(serialize(o));
            }
        }
        return builder;
    }

    private JsonObjectBuilder serializeObject(Object src) {
        var builder = Json.createObjectBuilder();
        var reflection = new Reflection(src);
        var fields = reflection.getFields();
        for (Field field : fields) {
            var val = reflection.getFieldValue(field);
            if (val == null) {
                builder.add(field.getName(), JsonValue.NULL);
            } else {
                builder.add(field.getName(), serialize(val));
            }
        }
        return builder;
    }
}
