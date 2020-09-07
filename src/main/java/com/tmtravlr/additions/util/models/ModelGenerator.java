package com.tmtravlr.additions.util.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.util.JsonGenerator;

/**
 * Generates json for models
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018
 */
public class ModelGenerator {	
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	public static String getAnimation() {
		JsonObject json = new JsonObject();
		json.add("animation", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("frametime", 1)));
		
		return GSON.toJson(json);
	}

	public static String getBlur() {
		JsonObject json = new JsonObject();
		json.add("texture", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("blur", true)));
		
		return GSON.toJson(json);
	}
	
}
