package com.tmtravlr.additions.addon.effects.cause;

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
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Manages added effect causes.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class EffectCauseManager {
	private static final Map<ResourceLocation, EffectCause.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends EffectCause>, EffectCause.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends EffectCause>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiEffectCauseFactory> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static void registerDefaultEffectCauses() {
		registerEffectCause(new EffectCauseItemInHand.Serializer());
		registerEffectCause(new EffectCauseItemInInventory.Serializer());
		registerEffectCause(new EffectCauseItemEquipped.Serializer());
		registerEffectCause(new EffectCauseItemRightClick.Serializer());
		registerEffectCause(new EffectCauseItemRightClickBlock.Serializer());
		registerEffectCause(new EffectCauseItemRightClickEntity.Serializer());
	    registerEffectCause(new EffectCauseItemUsing.Serializer());
	    registerEffectCause(new EffectCauseItemLeftClick.Serializer());
	    registerEffectCause(new EffectCauseItemDiggingBlock.Serializer());
	    registerEffectCause(new EffectCauseItemBreakBlock.Serializer());
	    registerEffectCause(new EffectCauseItemAttack.Serializer());
		registerEffectCause(new EffectCauseItemKill.Serializer());
	}
	
	public static void registerEffectCause(EffectCause.Serializer <? extends EffectCause> effectCauseSerializer) {
	    ResourceLocation resourcelocation = effectCauseSerializer.getEffectCauseType();
	    Class<? extends EffectCause> oclass = effectCauseSerializer.getEffectCauseClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register effect cause name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register effect cause class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, effectCauseSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, effectCauseSerializer);
	        CLASS_TO_NAME_MAP.put(oclass, resourcelocation);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiEffectCauseFactory factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static EffectCause.Serializer<?> getSerializerFor(ResourceLocation location) {
		EffectCause.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown effect cause '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends EffectCause> EffectCause.Serializer getSerializerFor(T effectCause) {
		EffectCause.Serializer<T> serializer = (EffectCause.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(effectCause.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown effect cause " + effectCause);
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends EffectCause> ResourceLocation getTypeFor(T effectCause) {
		return getTypeFor(effectCause.getClass());
	}
	
	public static <T extends EffectCause> ResourceLocation getTypeFor(Class effectCauseClass) {
		return CLASS_TO_NAME_MAP.get(effectCauseClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> effectCauseTypes = new ArrayList(NAME_TO_SERIALIZER_MAP.keySet());
		effectCauseTypes.sort(null);
		return effectCauseTypes;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiEffectCauseFactory> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiEffectCauseFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<EffectCause>, JsonSerializer<EffectCause> {
	    @Override
		public EffectCause deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
	        JsonObject json = JsonUtils.getJsonObject(jsonElement, "effect");
	        ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
	        EffectCause.Serializer<?> serializer;
	
	        try {
	            serializer = EffectCauseManager.getSerializerFor(resourcelocation);
	        } catch (IllegalArgumentException e) {
	            throw new JsonSyntaxException("Unknown effect cause '" + resourcelocation + "'");
	        }
	
	        return serializer.deserialize(json, context);
	    }
	
	    @Override
		public JsonElement serialize(EffectCause effectCause, Type type, JsonSerializationContext context) {
	    	EffectCause.Serializer<EffectCause> serializer = EffectCauseManager.<EffectCause>getSerializerFor(effectCause);
	        JsonObject json = serializer.serialize(effectCause, context);
	        json.addProperty("type", serializer.getEffectCauseType().toString());
	        return json;
	    }
	}
}
