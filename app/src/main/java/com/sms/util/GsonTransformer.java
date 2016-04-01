package com.sms.util;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonTransformer {

    final public static JsonSerializer<Date> serDate = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    final public static JsonDeserializer<Date> deserDate = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

//    final public static JsonDeserializer<Enum> deserEnum = new JsonDeserializer<Enum>() {
//        @Override
//        public Enum deserialize(JsonElement json, Type typeOfT,
//                                JsonDeserializationContext context) throws JsonParseException {
//            if (json== null){
//                return null;
//            }
//            int index = json.getAsInt();
//            if (typeOfT == AssignmentState.class){
//                return AssignmentState.fromInteger(index);
//            } else if (typeOfT == AssignmentStatus.class){
//                return AssignmentStatus.fromInteger(index);
//            }
//            return null;
//        }
//    };
//
//    final public static JsonSerializer<Enum> serEnum = new JsonSerializer<Enum>() {
//        @Override
//        public JsonElement serialize(Enum src, Type typeOfSrc, JsonSerializationContext
//                context) {
//            if (src == null) return null;
//            if (typeOfSrc == AssignmentState.class){
//                return new JsonPrimitive(((AssignmentState)src).index);
//            } else if (typeOfSrc == AssignmentStatus.class){
//                return new JsonPrimitive(((AssignmentStatus)src).index);
//            }
//            return null;
//        }
//    };

    public static Gson getGson() {
        return new GsonBuilder()
                    .registerTypeAdapter(Date.class, serDate)
                    .registerTypeAdapter(Date.class, deserDate)
//                    .registerTypeAdapter(Enum.class, serEnum)
//                    .registerTypeAdapter(Enum.class, deserEnum)
                    .create();
    }


}
