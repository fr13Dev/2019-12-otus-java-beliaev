package serialization;

import java.lang.reflect.Field;

public class Reflection {
    private final Object obj;

    public Reflection(Object obj) {
        this.obj = obj;
    }

    public Field[] getFields() {
        var fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    public Object getFieldValue(Field fld) {
        try {
            return fld.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("failed to get value of field %h", fld.getName()));
        }
    }
}
