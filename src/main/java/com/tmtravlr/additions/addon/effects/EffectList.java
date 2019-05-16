package com.tmtravlr.additions.addon.effects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

/**
 * List of effects with a cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectList {
	
	public ResourceLocation id;
	public EffectCause cause;
	public List<Effect> effects = new ArrayList<>();
	
	public static class Serializer implements JsonDeserializer<EffectList>, JsonSerializer<EffectList> {
	    public EffectList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
	        JsonObject json = JsonUtils.getJsonObject(jsonElement, "effectList");
	        EffectList effectList = new EffectList();
	        
        	effectList.cause = context.deserialize(JsonUtils.getJsonObject(json, "cause"), EffectCause.class);
	        effectList.effects = Arrays.asList((Effect[])context.deserialize(JsonUtils.getJsonArray(json, "effects"), Effect[].class));
	
	        return effectList;
	    }
	
	    public JsonElement serialize(EffectList effectList, Type type, JsonSerializationContext context) {
	    	JsonObject json = new JsonObject();
			
			if (effectList.cause == null) {
				throw new IllegalArgumentException("Tried to save effect list without a cause");
			}
			
			json.add("cause", context.serialize(effectList.cause, EffectCause.class));
			
			if (effectList.effects.isEmpty()) {
				throw new IllegalArgumentException("Tried to save effect list without effects");
			}
			
			JsonArray effects = new JsonArray();
			for (Effect effect : effectList.effects) {
				effects.add(context.serialize(effect, Effect.class));
			}
			json.add("effects", effects);
			
			return json;
	    }
	}
}
