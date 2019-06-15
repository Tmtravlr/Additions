package com.tmtravlr.additions.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Some handy tools for generating json
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since January 2019
 */
public class JsonGenerator {

	public static JsonObject createJsonObject(JsonElementPair... elements) {
		JsonObject json = new JsonObject();
		
		for (JsonElementPair elementPair : elements) {
			json.add(elementPair.name, elementPair.element);
		}
		
		return json;
	}

	public static JsonArray createJsonNumberArray(Number... numbers) {
		JsonArray numberArray = new JsonArray();
		
		for (Number number : numbers) {
			numberArray.add(number);
		}
		
		return numberArray;
	}

	public static JsonArray createJsonStringArray(String... strings) {
		JsonArray stringArray = new JsonArray();
		
		for (String string : strings) {
			stringArray.add(string);
		}
		
		return stringArray;
	}

	public static JsonArray createJsonObjectArray(JsonObject... jsonObjects) {
		JsonArray jsonObjectArray = new JsonArray();
		
		for (JsonObject json : jsonObjects) {
			jsonObjectArray.add(json);
		}
		
		return jsonObjectArray;
	}

	public static class JsonElementPair {
		public String name;
		public JsonElement element;
		
		public JsonElementPair(String name, JsonElement element) {
			this.name = name;
			this.element = element;
		}
		
		public JsonElementPair(String name, Number element) {
			this.name = name;
			this.element = new JsonPrimitive(element);
		}
		
		public JsonElementPair(String name, String element) {
			this.name = name;
			this.element = new JsonPrimitive(element);
		}
		
		public JsonElementPair(String name, Character element) {
			this.name = name;
			this.element = new JsonPrimitive(element);
		}
		
		public JsonElementPair(String name, Boolean element) {
			this.name = name;
			this.element = new JsonPrimitive(element);
		}
	}
}
