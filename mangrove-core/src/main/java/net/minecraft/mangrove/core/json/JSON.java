package net.minecraft.mangrove.core.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSON {
	private static final Gson gson=new GsonBuilder().setPrettyPrinting().create();
	
	
	public static JsonObject newObject(){
		return new JsonObject();
	}
	
	public static String toJson(JsonElement obj){
		return gson.toJson(obj);
	}

	public static JsonObject fromJson(String json) {
		return gson.fromJson(json, JsonObject.class);
	}
}
