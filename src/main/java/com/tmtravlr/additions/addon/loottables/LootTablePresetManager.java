package com.tmtravlr.additions.addon.loottables;

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
import com.tmtravlr.additions.api.gui.IGuiLootTablePresetFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Manages added loot table presets.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date January 2019
 */
public class LootTablePresetManager {
	private static final Map<ResourceLocation, LootTablePreset.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends LootTablePreset>, LootTablePreset.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends LootTablePreset>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiLootTablePresetFactory> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static void registerDefaultLootTablePresets() {
	    registerLootTablePresetType(new LootTablePresetEmpty.Serializer());
	    registerLootTablePresetType(new LootTablePresetOtherLootTable.Serializer());
	    registerLootTablePresetType(new LootTablePresetBlockItself.Serializer());
	    registerLootTablePresetType(new LootTablePresetBlockItemDrop.Serializer());
	}
	
	public static void registerLootTablePresetType(LootTablePreset.Serializer <? extends LootTablePreset > lootTableSerializer) {
	    ResourceLocation resourcelocation = lootTableSerializer.getLootTablePresetType();
	    Class<? extends LootTablePreset> oclass = (Class<? extends LootTablePreset>)lootTableSerializer.getLootTablePresetClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register item type name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register item type class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, lootTableSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, lootTableSerializer);
	        CLASS_TO_NAME_MAP.put(oclass, resourcelocation);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiLootTablePresetFactory factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static LootTablePreset.Serializer<?> getSerializerFor(ResourceLocation location) {
		LootTablePreset.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown loot table preset type '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends LootTablePreset> LootTablePreset.Serializer getSerializerFor(T lootTablePreset) {
		LootTablePreset.Serializer<T> serializer = (LootTablePreset.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(lootTablePreset.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown loot table preset type " + lootTablePreset.getClass());
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends LootTablePreset> ResourceLocation getTypeFor(T lootTablePreset) {
		return getTypeFor(lootTablePreset.getClass());
	}
	
	public static <T extends LootTablePreset> ResourceLocation getTypeFor(Class lootTablePresetClass) {
		return CLASS_TO_NAME_MAP.get(lootTablePresetClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> types = new ArrayList(NAME_TO_SERIALIZER_MAP.keySet());
		types.sort(null);
		return types;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiLootTablePresetFactory> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiLootTablePresetFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<LootTablePreset>, JsonSerializer<LootTablePreset> {
        public LootTablePreset deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "loot_table");
            
            if (json.has("preset_type")) {
	            ResourceLocation presetType = new ResourceLocation(JsonUtils.getString(json, "preset_type"));
	            LootTablePreset.Serializer<?> serializer;
	
	            try {
	                serializer = getSerializerFor(presetType);
	            } catch (IllegalArgumentException e) {
	                throw new JsonSyntaxException("Unknown loot table preset type '" + presetType + "'");
	            }
	
	            return serializer.deserialize(json, context);
            }
            
            return null;
        }

        public JsonElement serialize(LootTablePreset lootTablePreset, Type type, JsonSerializationContext context) {
        	LootTablePreset.Serializer serializer = getSerializerFor(lootTablePreset);
            JsonObject json = serializer.serialize(lootTablePreset, context);
            json.addProperty("preset_type", serializer.getLootTablePresetType().toString());
            return json;
        }
    }
}
