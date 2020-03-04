package serialization;

class DemoObject {
    private final int anInt = 13;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DemoObject that = (DemoObject) o;

        return anInt == that.anInt;
    }

    @Override
    public int hashCode() {
        return anInt;
    }
}
