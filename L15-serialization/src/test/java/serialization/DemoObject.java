package serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DemoObject {
    private final int anInt;
    private final boolean aBoolean;
    private final String string;
    private final int[] intArray;
    private final List<String> list = new ArrayList<>();
    private final InnerDemoObject innerObject;

    public DemoObject(int anInt, boolean aBoolean, String string,
                      int[] intArray, List<String> list,
                      InnerDemoObject innerObject) {
        this.anInt = anInt;
        this.aBoolean = aBoolean;
        this.string = string;
        this.intArray = intArray;
        this.list.addAll(list);
        this.innerObject = innerObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DemoObject that = (DemoObject) o;

        if (anInt != that.anInt) return false;
        if (aBoolean != that.aBoolean) return false;
        if (!string.equals(that.string)) return false;
        if (!Arrays.equals(intArray, that.intArray)) return false;
        if (!list.equals(that.list)) return false;
        return innerObject.equals(that.innerObject);
    }

    @Override
    public int hashCode() {
        int result = anInt;
        result = 31 * result + (aBoolean ? 1 : 0);
        result = 31 * result + string.hashCode();
        result = 31 * result + Arrays.hashCode(intArray);
        result = 31 * result + list.hashCode();
        result = 31 * result + innerObject.hashCode();
        return result;
    }

    public static class InnerDemoObject {
        private final int innerInt;
        private final String innerString;

        public InnerDemoObject(int innerInt, String innerString) {
            this.innerInt = innerInt;
            this.innerString = innerString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InnerDemoObject that = (InnerDemoObject) o;

            if (innerInt != that.innerInt) return false;
            return innerString.equals(that.innerString);
        }

        @Override
        public int hashCode() {
            int result = innerInt;
            result = 31 * result + innerString.hashCode();
            return result;
        }
    }
}
