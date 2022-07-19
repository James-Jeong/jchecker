package core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PrettyPrinter {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String printJson(Object object) {
        return gson.toJson(object);
    }

}
