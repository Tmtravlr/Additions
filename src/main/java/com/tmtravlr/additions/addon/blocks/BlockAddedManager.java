package com.tmtravlr.additions.addon.blocks;

import com.google.gson.*;
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Manages added blocks.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2018
 */
public class BlockAddedManager {
	private static final Map<ResourceLocation, IBlockAdded.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IBlockAdded>, IBlockAdded.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IBlockAdded>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiBlockAddedFactory<?>> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static void registerDefaultBlocks() {
		registerBlockType(new BlockAddedSimple.Serializer());
		registerBlockType(new BlockAddedFalling.Serializer());
		registerBlockType(new BlockAddedStairs.Serializer());
	    registerBlockType(new BlockAddedSlab.Serializer());
	    registerBlockType(new BlockAddedCarpet.Serializer());
	    registerBlockType(new BlockAddedFacing.Serializer());
	    registerBlockType(new BlockAddedPillar.Serializer());
	    registerBlockType(new BlockAddedLadder.Serializer());
	    registerBlockType(new BlockAddedPane.Serializer());
	    registerBlockType(new BlockAddedFence.Serializer());
	    registerBlockType(new BlockAddedWall.Serializer());
	}
	
	public static void registerBlockType(IBlockAdded.Serializer <? extends IBlockAdded > blockSerializer) {
	    ResourceLocation resourcelocation = blockSerializer.getBlockAddedType();
	    Class<? extends IBlockAdded> oclass = blockSerializer.getBlockAddedClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
	        throw new IllegalArgumentException("Can't re-register block type name " + resourcelocation);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
	        throw new IllegalArgumentException("Can't re-register block type class " + oclass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(resourcelocation, blockSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(oclass, blockSerializer);
	        CLASS_TO_NAME_MAP.put(oclass, resourcelocation);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiBlockAddedFactory<?> factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static IBlockAdded.Serializer<?> getSerializerFor(ResourceLocation location) {
		IBlockAdded.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown block type '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IBlockAdded> IBlockAdded.Serializer<T> getSerializerFor(T blockAdded) {
		//noinspection unchecked
		IBlockAdded.Serializer<T> serializer = (IBlockAdded.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(blockAdded.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown block type " + blockAdded);
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IBlockAdded> ResourceLocation getTypeFor(T blockAdded) {
		return getTypeFor(blockAdded.getClass());
	}
	
	public static <T extends IBlockAdded> ResourceLocation getTypeFor(Class<T> blockClass) {
		return CLASS_TO_NAME_MAP.get(blockClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> blockTypes = new ArrayList<>(NAME_TO_SERIALIZER_MAP.keySet());
		blockTypes.sort(null);
		return blockTypes;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiBlockAddedFactory<?>> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiBlockAddedFactory<?> getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<IBlockAdded>, JsonSerializer<IBlockAdded> {
        @Override
		public IBlockAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "block");
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IBlockAdded.Serializer<?> serializer;

            try {
                serializer = BlockAddedManager.getSerializerFor(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown block added type '" + resourcelocation + "'");
            }

            return serializer.deserialize(json, context);
        }

        @Override
		public JsonElement serialize(IBlockAdded blockAdded, Type type, JsonSerializationContext context) {
        	IBlockAdded.Serializer<IBlockAdded> serializer = BlockAddedManager.getSerializerFor(blockAdded);
            JsonObject json = serializer.serialize(blockAdded, context);
            json.addProperty("type", serializer.getBlockAddedType().toString());
            return json;
        }

        public static void postDeserialize(JsonObject json, IBlockAdded blockAdded) throws JsonParseException {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IBlockAdded.Serializer<?> serializer;

            try {
                serializer = BlockAddedManager.getSerializerFor(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown block added type '" + resourcelocation + "'");
            }

            serializer.postDeserializeBlockAdded(json, blockAdded);
        }
    }
}
