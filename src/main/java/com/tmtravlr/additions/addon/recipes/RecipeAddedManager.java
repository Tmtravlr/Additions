package com.tmtravlr.additions.addon.recipes;

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
import com.tmtravlr.additions.gui.view.edit.recipe.IGuiRecipeAddedFactory;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Manages added recipes.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since February 2019
 */
public class RecipeAddedManager {
	private static final Map<ResourceLocation, IRecipeAdded.Serializer<?>> NAME_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IRecipeAdded>, IRecipeAdded.Serializer<?>> CLASS_TO_SERIALIZER_MAP = new HashMap<>();
	private static final Map<Class<? extends IRecipeAdded>, ResourceLocation> CLASS_TO_NAME_MAP = new HashMap<>();
	private static final LinkedHashMap<ResourceLocation, IGuiRecipeAddedFactory> NAME_TO_GUI_MAP = new LinkedHashMap<>();
	
	public static final List<IBrewingRecipe> BREWING_RECIPES = ReflectionHelper.getPrivateValue(BrewingRecipeRegistry.class, null, "recipes");
	
	public static void registerDefaultRecipes() {
	    registerRecipeType(new RecipeAddedCraftingShaped.Serializer());
	    registerRecipeType(new RecipeAddedCraftingShapeless.Serializer());
	    registerRecipeType(new RecipeAddedCraftingPotionTipping.Serializer());
	    registerRecipeType(new RecipeAddedCraftingModifyDamage.Serializer());
	    registerRecipeType(new RecipeAddedCraftingModifyNBT.Serializer());
	    registerRecipeType(new RecipeAddedCraftingDyeItem.Serializer());
	    registerRecipeType(new RecipeAddedSmelting.Serializer());
	    registerRecipeType(new RecipeAddedBrewing.Serializer());
	    registerRecipeType(new RecipeAddedBrewingComplete.Serializer());
	}
	
	public static void registerRecipeType(IRecipeAdded.Serializer <? extends IRecipeAdded > recipeSerializer) {
	    ResourceLocation addedType = recipeSerializer.getRecipeAddedType();
	    Class<? extends IRecipeAdded> addedClass = (Class<? extends IRecipeAdded>)recipeSerializer.getRecipeAddedClass();
	
	    if (NAME_TO_SERIALIZER_MAP.containsKey(addedType)) {
	        throw new IllegalArgumentException("Can't re-register recipe type name " + addedType);
	    } else if (CLASS_TO_SERIALIZER_MAP.containsKey(addedClass)) {
	        throw new IllegalArgumentException("Can't re-register recipe type class " + addedClass.getName());
	    } else {
	        NAME_TO_SERIALIZER_MAP.put(addedType, recipeSerializer);
	        CLASS_TO_SERIALIZER_MAP.put(addedClass, recipeSerializer);
	        CLASS_TO_NAME_MAP.put(addedClass, addedType);
	    }
	}
	
	public static void registerGuiFactory(ResourceLocation type, IGuiRecipeAddedFactory factory) {
		NAME_TO_GUI_MAP.put(type, factory);
	}
	
	public static IRecipeAdded.Serializer<?> getSerializerFor(ResourceLocation location) {
	    IRecipeAdded.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown recipe type '" + location + "'");
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IRecipeAdded> IRecipeAdded.Serializer getSerializerFor(T recipeAdded) {
	    IRecipeAdded.Serializer<T> serializer = (IRecipeAdded.Serializer<T>) CLASS_TO_SERIALIZER_MAP.get(recipeAdded.getClass());
	
	    if (serializer == null) {
	        throw new IllegalArgumentException("Unknown recipe type " + recipeAdded);
	    } else {
	        return serializer;
	    }
	}
	
	public static <T extends IRecipeAdded> ResourceLocation getTypeFor(T recipeAdded) {
		return getTypeFor(recipeAdded.getClass());
	}
	
	public static <T extends IRecipeAdded> ResourceLocation getTypeFor(Class recipeClass) {
		return CLASS_TO_NAME_MAP.get(recipeClass);
	}
	
	public static Collection<ResourceLocation> getAllTypes() {
		List<ResourceLocation> types = new ArrayList(NAME_TO_SERIALIZER_MAP.keySet());
		types.sort(null);
		return types;
	}
	
	public static LinkedHashMap<ResourceLocation, IGuiRecipeAddedFactory> getAllGuiFactories() {
		return NAME_TO_GUI_MAP;
	}
	
	public static IGuiRecipeAddedFactory getGuiFactoryFor(ResourceLocation type) {
		return NAME_TO_GUI_MAP.get(type);
	}
	
	public static class Serializer implements JsonDeserializer<IRecipeAdded>, JsonSerializer<IRecipeAdded> {
        public IRecipeAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "recipe");
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "type"));
            IRecipeAdded.Serializer<?> serializer;

            try {
                serializer = RecipeAddedManager.getSerializerFor(resourcelocation);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown recipe added type '" + resourcelocation + "'");
            }

            return serializer.deserialize(json, context);
        }

        public JsonElement serialize(IRecipeAdded addition, Type type, JsonSerializationContext context) {
            IRecipeAdded.Serializer<IRecipeAdded> serializer = RecipeAddedManager.<IRecipeAdded>getSerializerFor(addition);
            JsonObject json = serializer.serialize(addition, context);
            json.addProperty("type", serializer.getRecipeAddedType().toString());
            return json;
        }
    }
}
