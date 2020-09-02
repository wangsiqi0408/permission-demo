package com.cmmr.permission.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {

    private boolean status;
    private String message;
    private Object data;

    public JsonData(boolean status) {
        this.status = status;
    }

    public static JsonData success(){
        JsonData data = new JsonData(true);
        return data;
    }

    public static JsonData success(String message){
        JsonData data = new JsonData(true);
        data.message = message;
        return data;
    }

    public static JsonData success(Object object){
        JsonData data = new JsonData(true);
        data.data = object;
        return data;
    }

    public static JsonData success(String message, Object object){
        JsonData data = new JsonData(true);
        data.message = message;
        data.data = object;
        return data;
    }

    public static JsonData fail(){
        return new JsonData(false);
    }

    public static JsonData fail(String message){
        JsonData data = new JsonData(false);
        data.message = message;
        return data;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("message", message);
        result.put("data", data);
        return result;
    }
}
