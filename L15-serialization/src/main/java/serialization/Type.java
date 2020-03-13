package serialization;

import java.util.Collection;

class Type {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    public static boolean isByte(Object obj) {
        return obj instanceof Byte;
    }

    public static boolean isShort(Object obj) {
        return obj instanceof Short;
    }

    public static boolean isInt(Object obj) {
        return obj instanceof Integer;
    }

    public static boolean isLong(Object obj) {
        return obj instanceof Long;
    }

    public static boolean isFloat(Object obj) {
        return obj instanceof Float;
    }

    public static boolean isDouble(Object obj) {
        return obj instanceof Double;
    }

    public static boolean isCharacter(Object obj) {
        return obj instanceof Character;
    }

    public static boolean isString(Object obj) {
        return obj instanceof String;
    }

    public static boolean isCollection(Object obj) {
        return obj instanceof Collection;
    }

    public static boolean isArray(Object obj) {
        return obj.getClass().isArray();
    }
}
