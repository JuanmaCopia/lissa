package symsolve.bounds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Helper {

    public static String boundsToJson(Bounds bounds) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(bounds);
    }

    public static Bounds boundsFromJson(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.fromJson(jsonString, Bounds.class);
    }
}
