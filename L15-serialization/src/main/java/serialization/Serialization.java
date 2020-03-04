package serialization;

public interface Serialization {

    <T> String toJson(T object);
}
