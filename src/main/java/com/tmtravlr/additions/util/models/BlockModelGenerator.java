package com.tmtravlr.additions.util.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

/**
 * Generates item/block/etc. models
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018
 */
public class BlockModelGenerator {
	
	public static String getSimpleBlockModel(String blockId) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "block/cube_all");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("all", AdditionsMod.MOD_ID + ":blocks/" + blockId)));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getSimpleBlockState(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("normal", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)))
				));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
}
