package com.tmtravlr.additions.addon.items;

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
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockLadder;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSlab;
import com.tmtravlr.additions.api.gui.IGuiItemAddedFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Manages added items.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017
 */
public class ItemAddedManager {
	private static final Map<ResourceLocation, IItemAdded.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IItemAdded>, IItemAdded.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IItemAdded>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiItemAddedFactory> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static void registerDefaultItems() {
	    registerItemType(new ItemAddedSimple.Serializer());
	    registerItemType(new ItemAddedFood.Serializer());
	    registerItemType(new ItemAddedRecord.Serializer());
	    registerItemType(new ItemAddedArmor.Serializer());
	    registerItemType(new ItemAddedHat.Serializer());
	    registerItemType(new ItemAddedSword.Serializer());
	    registerItemType(new ItemAddedClub.Serializer());
	    registerItemType(new ItemAddedPickaxe.Serializer());
	    registerItemType(new ItemAddedAxe.Serializer());
	    registerItemType(new ItemAddedShovel.Serializer());
	    registerItemType(new ItemAddedHoe.Serializer());
	    registerItemType(new ItemAddedShears.Serializer());
	    registerItemType(new ItemAddedFirestarter.Serializer());
	    registerItemType(new ItemAddedMultiTool.Serializer());
	    registerItemType(new ItemAddedShield.Serializer());
	    registerItemType(new ItemAddedBow.Serializer());
	    registerItemType(new ItemAddedGun.Serializer());
	    registerItemType(new ItemAddedThrowable.Serializer());
	    registerItemType(new ItemAddedArrow.Serializer());
	    registerItemType(new ItemAddedProjectile.Serializer());
	    registerItemType(new ItemAddedBlockSimple.Serializer());
	    registerItemType(new ItemAddedBlockSlab.Serializer());
	    registerItemType(new ItemAddedBlockLadder.Serializer());
	}
	
	public static void registerItemType(IItemAdded.Serializer <? extends IItemAdded > itemSerializer) {
	    ResourceLocation resourcelocation = itemSerializer.getItemAddedType();
	    Class<? extends IItemAdded> oclass = itemSerializer.getItemAddedClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register item type name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register item type class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, itemSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, itemSerializer);
	        CLASS_TO_NAME_MAP.put(oclass, resourcelocation);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiItemAddedFactory factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static IItemAdded.Serializer<?> getSerializerFor(ResourceLocation location) {
	    IItemAdded.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown item type '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IItemAdded> IItemAdded.Serializer getSerializerFor(T itemAdded) {
	    IItemAdded.Serializer<T> serializer = (IItemAdded.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(itemAdded.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown item type " + itemAdded);
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IItemAdded> ResourceLocation getTypeFor(T itemAdded) {
		return getTypeFor(itemAdded.getClass());
	}
	
	public static <T extends IItemAdded> ResourceLocation getTypeFor(Class itemClass) {
		return CLASS_TO_NAME_MAP.get(itemClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> itemTypes = new ArrayList(NAME_TO_SERIALIZER_MAP.keySet());
		itemTypes.sort(null);
		return itemTypes;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiItemAddedFactory> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiItemAddedFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<IItemAdded>, JsonSerializer<IItemAdded> {
        @Override
		public IItemAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "item");
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IItemAdded.Serializer<?> serializer;

            try {
                serializer = ItemAddedManager.getSerializerFor(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown item added type '" + resourcelocation + "'");
            }

            return serializer.deserialize(json, context);
        }

        @Override
		public JsonElement serialize(IItemAdded itemAdded, Type type, JsonSerializationContext context) {
            IItemAdded.Serializer<IItemAdded> serializer = ItemAddedManager.<IItemAdded>getSerializerFor(itemAdded);
            JsonObject json = serializer.serialize(itemAdded, context);
            json.addProperty("type", serializer.getItemAddedType().toString());
            return json;
        }

        public static void postDeserialize(JsonObject json, IItemAdded itemAdded) throws JsonParseException {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IItemAdded.Serializer<?> serializer;

            try {
                serializer = ItemAddedManager.getSerializerFor(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown item added type '" + resourcelocation + "'");
            }

            serializer.postDeserializeItemAdded(json, itemAdded);
        }
    }
}
