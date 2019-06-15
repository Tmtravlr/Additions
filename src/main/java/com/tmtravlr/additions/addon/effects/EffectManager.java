package com.tmtravlr.additions.addon.effects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.api.gui.IGuiEffectFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Manages added effects.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class EffectManager {
	private static final Map<ResourceLocation, Effect.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends Effect>, Effect.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends Effect>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiEffectFactory> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static void registerDefaultEffects() {
	    registerEffectType(new EffectPotion.Serializer());
	    registerEffectType(new EffectCommand.Serializer());
	    registerEffectType(new EffectLootTableInside.Serializer());
	    registerEffectType(new EffectLootTableAt.Serializer());
	    registerEffectType(new EffectCancelNormal.Serializer());
	    registerEffectType(new EffectConsumeItem.Serializer());
	    registerEffectType(new EffectDamageItem.Serializer());
	}
	
	public static void registerEffectType(Effect.Serializer <? extends Effect > effectSerializer) {
	    ResourceLocation resourcelocation = effectSerializer.getEffectType();
	    Class<? extends Effect> oclass = effectSerializer.getEffectClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register effect type name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register effect type class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, effectSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, effectSerializer);
	        CLASS_TO_NAME_MAP.put(oclass, resourcelocation);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiEffectFactory factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static Effect.Serializer<?> getSerializerFor(ResourceLocation location) {
		Effect.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown effect type '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends Effect> Effect.Serializer getSerializerFor(T effect) {
		Effect.Serializer<T> serializer = (Effect.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(effect.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown effect type " + effect);
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends Effect> ResourceLocation getTypeFor(T effect) {
		return getTypeFor(effect.getClass());
	}
	
	public static <T extends Effect> ResourceLocation getTypeFor(Class effectClass) {
		return CLASS_TO_NAME_MAP.get(effectClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> effectTypes = new ArrayList(NAME_TO_SERIALIZER_MAP.keySet());
		effectTypes.sort(null);
		return effectTypes;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiEffectFactory> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiEffectFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<Effect>, JsonSerializer<Effect> {
	    @Override
		public Effect deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
	        JsonObject json = JsonUtils.getJsonObject(jsonElement, "effect");
	        ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
	        Effect.Serializer<?> serializer;
	
	        try {
	            serializer = EffectManager.getSerializerFor(resourcelocation);
	        } catch (IllegalArgumentException e) {
	            throw new JsonSyntaxException("Unknown effect type '" + resourcelocation + "'");
	        }
	
	        return serializer.deserialize(json, context);
	    }
	
	    @Override
		public JsonElement serialize(Effect effect, Type type, JsonSerializationContext context) {
	    	Effect.Serializer<Effect> serializer = EffectManager.<Effect>getSerializerFor(effect);
	        JsonObject json = serializer.serialize(effect, context);
	        json.addProperty("type", serializer.getEffectType().toString());
	        return json;
	    }
	}
}
