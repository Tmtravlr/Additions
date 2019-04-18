package com.tmtravlr.additions.addon.effects;

import java.lang.reflect.Type;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.RandomValueRange;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

/**
 * Simple class to hold info for an effect, for instance when food is eaten.
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public abstract class EffectInfo {
	
	public abstract void applyEffect(Entity entity);
	
	public abstract void applyEffect(BlockPos pos);
	
	public static class Serializer implements JsonDeserializer<EffectInfo>, JsonSerializer<EffectInfo> {
		public JsonElement serialize(EffectInfo effect, Type typeOfSrc, JsonSerializationContext context) {
			throw new JsonSyntaxException("Should never serialize EffectInfo directly!");
        }
		
		public EffectInfo deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
			JsonObject json = jsonElement.getAsJsonObject();
        	String typeName = JsonUtils.getString(json, "type");
        	EffectType type = EffectType.REGISTRY.getValue(new ResourceLocation(typeName));
        	
        	if(type == null) {
        		throw new JsonSyntaxException("Unknown effect type " + typeName);
        	}
        	
        	return context.deserialize(json, type.effectClass);
        }
    }
	
	public static enum Target {
		SELF,
		OTHER,
		BLOCK
	}
	
}
