package com.tmtravlr.additions.addon.items;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.EntityHasScore;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;

/**
 * Types of effects. Set up so more can be added easily.
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class ItemAddedManager {
	private static final Map<ResourceLocation, IItemAdded.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.<ResourceLocation, IItemAdded.Serializer<?>> newHashMap();
	private static final Map<Class<? extends IItemAdded>, IItemAdded.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.<Class <? extends IItemAdded> , IItemAdded.Serializer<?>>newHashMap();
	
	public static void registerCondition(IItemAdded.Serializer <? extends IItemAdded > condition) {
	    ResourceLocation resourcelocation = condition.getItemAddedType();
	    Class<? extends IItemAdded> oclass = (Class<? extends IItemAdded>)condition.getItemAddedClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register item condition name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register item condition class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, condition);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, condition);
	    }
	}
	
	public static IItemAdded.Serializer<?> getSerializerForName(ResourceLocation location) {
	    IItemAdded.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown loot item condition '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IItemAdded> IItemAdded.Serializer getSerializerFor(T conditionClass) {
	    IItemAdded.Serializer<T> serializer = (IItemAdded.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(conditionClass.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown loot item condition " + conditionClass);
	    } else {
	        return serializer;
	    }
	}
	
	static {
	    registerCondition(new ItemAddedSimple.Serializer());
	    registerCondition(new ItemAddedFood.Serializer());
	}
	
	public static class Serializer implements JsonDeserializer<IItemAdded>, JsonSerializer<IItemAdded> {
        public IItemAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "item");
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IItemAdded.Serializer<?> serializer;

            try {
                serializer = ItemAddedManager.getSerializerForName(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown item added type '" + resourcelocation + "'");
            }

            return serializer.deserialize(json, context);
        }

        public JsonElement serialize(IItemAdded itemAdded, Type type, JsonSerializationContext context) {
            IItemAdded.Serializer<IItemAdded> serializer = ItemAddedManager.<IItemAdded>getSerializerFor(itemAdded);
            JsonObject json = serializer.serialize(itemAdded, context);
            json.addProperty("type", serializer.getItemAddedType().toString());
            return json;
        }
    }
}
