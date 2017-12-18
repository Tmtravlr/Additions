package com.tmtravlr.additions.addon.items;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

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
import com.tmtravlr.additions.gui.view.edit.item.IGuiEditItemFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Types of effects. Set up so more can be added easily.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017
 */
public class ItemAddedManager {
	private static final Map<ResourceLocation, IItemAdded.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.<ResourceLocation, IItemAdded.Serializer<?>> newHashMap();
	private static final Map<Class<? extends IItemAdded>, IItemAdded.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.<Class <? extends IItemAdded> , IItemAdded.Serializer<?>>newHashMap();
	private static final Map<Class<? extends IItemAdded>, ResourceLocation> CLASS_TO_NAME_MAP = Maps.<Class <? extends IItemAdded> , ResourceLocation>newHashMap();
	private static final Map<ResourceLocation, IGuiEditItemFactory> NAME_TO_GUI_MAP = Maps.<ResourceLocation, IGuiEditItemFactory> newHashMap();
	
	public static void registerItemType(IItemAdded.Serializer <? extends IItemAdded > itemSerializer) {
	    ResourceLocation resourcelocation = itemSerializer.getItemAddedType();
	    Class<? extends IItemAdded> oclass = (Class<? extends IItemAdded>)itemSerializer.getItemAddedClass();
	
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
	
	public static void registerGuiFactory(ResourceLocation type, IGuiEditItemFactory factory) {
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
		return NAME_TO_SERIALIZER_MAP.keySet();
	}
	
	public static IGuiEditItemFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	static {
	    registerItemType(new ItemAddedSimple.Serializer());
	    registerItemType(new ItemAddedFood.Serializer());
	}
	
	public static class Serializer implements JsonDeserializer<IItemAdded>, JsonSerializer<IItemAdded> {
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

        public JsonElement serialize(IItemAdded itemAdded, Type type, JsonSerializationContext context) {
            IItemAdded.Serializer<IItemAdded> serializer = ItemAddedManager.<IItemAdded>getSerializerFor(itemAdded);
            JsonObject json = serializer.serialize(itemAdded, context);
            json.addProperty("type", serializer.getItemAddedType().toString());
            return json;
        }
    }
}
