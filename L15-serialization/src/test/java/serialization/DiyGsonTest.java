package serialization;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class DiyGsonTest {
    private static final DemoObject realObject = new DemoObject();
    private static final Gson gson = new Gson();

    @Test
    public void serializeObjectAndDeserializeBack() {
        Serialization serialization = new DiyGson();
        final String json = serialization.toJson(realObject);
        final DemoObject fromJsonObject = gson.fromJson(json, realObject.getClass());
        Assert.assertEquals(realObject, fromJsonObject);
    }
}