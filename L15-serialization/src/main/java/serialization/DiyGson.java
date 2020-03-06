package serialization;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;
import java.util.Collection;

public class DiyGson implements Serialization {

    @Override
    public String toJson(Object src) {
        if (src == null) {
            return "null";
        } else {
            return toJson(src, src.getClass());
        }
    }

    private <T> String toJson(Object src, Class<T> clazz) {
        if (clazz.equals(String.class)) {
            return (String) src;
        } else if (src instanceof Collection) {
            return Json.createArrayBuilder((Collection<?>) src).build().toString();
        } else if (clazz.isArray()) {
            var builder = getJsonArrayBuilder(src, clazz);
            return builder.build().toString();
        } else {
            var builder = getJsonObjectBuilder(src, src.getClass());
            return builder.build().toString();
        }
    }

    private <T> JsonArrayBuilder getJsonArrayBuilder(Object src, Class<T> clazz) {
        var builder = Json.createArrayBuilder();
        var type = clazz.getComponentType();
        if (type.isPrimitive()) {
            if (type.equals(int.class)) {
                var arr = (int[]) src;
                for (int i : arr) {
                    builder.add(i);
                }
            } else if (type.equals(boolean.class)) {
                var arr = (boolean[]) src;
                for (boolean i : arr) {
                    builder.add(i);
                }
            } else if (type.equals(char.class)) {
                var arr = (char[]) src;
                for (char i : arr) {
                    builder.add(i);
                }
            }
        } else if (type.equals(String.class)) {
            var arr = (String[]) src;
            for (String i : arr) {
                if (i == null) {
                    builder.addNull();
                } else {
                    builder.add(i);
                }
            }
        } else {
            final Object[] arr = (Object[]) src;
            for (Object o : arr) {
                if (o == null) {
                    builder.addNull();
                } else {
                    builder.add(getJsonObjectBuilder(o, o.getClass()));
                }
            }
        }
        return builder;
    }

    private <T> JsonObjectBuilder getJsonObjectBuilder(Object src, Class<T> clazz) {
        var builder = Json.createObjectBuilder();
        var fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            var type = field.getType();
            var val = getFieldValue(field, src);
            final String name = field.getName();
            if (type.isPrimitive()) {
                if (type.equals(int.class)) {
                    builder.add(name, (int) val);
                } else if (type.equals(boolean.class)) {
                    builder.add(name, (boolean) val);
                } else if (type.equals(char.class)) {
                    builder.add(name, (char) val);
                }
            } else if (type.equals(String.class)) {
                if (val == null) {
                    builder.addNull(name);
                } else {
                    builder.add(name, (String) val);
                }
            } else if (val instanceof Collection) {
                builder.add(name, Json.createArrayBuilder((Collection<?>) val));
            } else if (type.isArray()) {
                var arrayBuilder = getJsonArrayBuilder(val, val.getClass());
                builder.add(name, arrayBuilder);
            } else {
                if (val == null) {
                    builder.addNull(name);
                } else {
                    builder.add(name, getJsonObjectBuilder(val, val.getClass()));
                }
            }
        }
        return builder;
    }

    private Object getFieldValue(Field fld, Object src) {
        try {
            return fld.get(src);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("failed to get value of field %h", fld.getName()));
        }
    }
}
