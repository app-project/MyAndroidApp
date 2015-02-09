package com.app.myapp.util;


import java.lang.reflect.Type;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;

public class JsonHelper_BAK {
	// 反序列化json
    public static <T> T parseObject(String jsonStr, Type type) {
        GsonBuilder builder = new GsonBuilder();
        // 不转换没有 @Expose 注解的字段
       // builder.excludeFieldsWithoutExposeAnnotation();
        //由于gson没有反序列化Date的功能，此处自己构造一个DateTime的反序列化类，将其注册到GsonBuilder中
        DateDeserializer ds = new DateDeserializer();
        builder.registerTypeAdapter(Date.class, ds);
        Gson gson = builder.create();
        return gson.fromJson(jsonStr, type);
    }
    
    
    // 序列化Json
    public static String toJson(Object object) {
        GsonBuilder builder = new GsonBuilder();
        // 不转换没有 @Expose 注解的字段
       // builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        return gson.toJson(object);
    }
}
 class DateDeserializer implements JsonDeserializer<Date> {
	 @Override
    public Date deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
        Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
        Matcher matcher = pattern.matcher(json.getAsJsonPrimitive()
                .getAsString());
        String result = matcher.replaceAll("$2");
        return new Date(new Long(result));
    }

}
