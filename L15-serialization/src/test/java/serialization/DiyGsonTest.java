package serialization;

import com.google.gson.Gson;
import org.junit.Test;

import javax.lang.model.type.NullType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class DiyGsonTest {
    private static final Gson gson = new Gson();
    private static final Serialization serialization = new DiyGson();
    private final DemoObject realObject = new DemoObject(
            13,
            true,
            "hello, json!",
            IntStream.range(0, 10).toArray(),
            List.of("tt", "rr"),
            new DemoObject.InnerDemoObject(10, "inner hello!"));

    @Test
    public void serializeObject() {
        final String json = serialization.toJson(realObject);
        final DemoObject fromJson = gson.fromJson(json, DemoObject.class);
        assertEquals(realObject, fromJson);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void serializeCollection() {
        Collection<String> list = List.of("asd", "xcv");
        final String json = serialization.toJson(list);
        final Collection<String> fromJson = gson.fromJson(json, Collection.class);
        assertEquals(list, fromJson);
    }

    @Test
    public void serializeIntArray() {
        final int[] array = IntStream.range(0, 10).toArray();
        final String json = serialization.toJson(array);
        final Integer[] fromJson = gson.fromJson(json, Integer[].class);
        assertArrayEquals(array, Arrays.stream(fromJson).mapToInt(Integer::intValue).toArray());
    }

    @Test
    public void serializeBooleanArray() {
        final boolean[] array = {true, false, true};
        final String json = serialization.toJson(array);
        final Boolean[] fromJson = gson.fromJson(json, Boolean[].class);
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], fromJson[i]);
        }
    }

    @Test
    public void serializeStringArray() {
        final String[] array = {"true", "false", "true", null};
        final String json = serialization.toJson(array);
        final String[] fromJson = gson.fromJson(json, String[].class);
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], fromJson[i]);
        }
    }

    @Test
    public void serializeObjectsArray() {
        DemoObject[] array = {
                new DemoObject(1, false, "www",
                        IntStream.range(0, 5).toArray(), List.of("a", "b"),
                        new DemoObject.InnerDemoObject(5, "bb")),
                new DemoObject(2, true, "zzz",
                        IntStream.range(0, 10).toArray(), List.of("a", "b"),
                        new DemoObject.InnerDemoObject(10, "nn")),
                null
        };
        final String json = serialization.toJson(array);
        final DemoObject[] fromJson = gson.fromJson(json, DemoObject[].class);
        assertArrayEquals(array, fromJson);
    }

    @Test
    public void serializeNull() {
        final String json = serialization.toJson(null);
        final NullType fromJson = gson.fromJson(json, NullType.class);
        assertNull(fromJson);
    }
}