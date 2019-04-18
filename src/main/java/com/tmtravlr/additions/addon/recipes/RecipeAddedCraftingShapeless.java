package com.tmtravlr.additions.addon.recipes;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * An added shapeless crafting recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedCraftingShapeless implements IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "crafting_shapeless");
	
	public ResourceLocation id;
	public ShapelessOreRecipe recipe;

	@Override
	public void setId(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
	
	@Override
	public void registerRecipe() {
		ForgeRegistries.RECIPES.register(this.recipe.setRegistryName(this.id));
	}
	
	public String getRecipeGroup() {
		return this.recipe == null ? "" : this.recipe.getGroup();
	}
	
	/**
	 * The recipe must be set before calling this, or it will not work!
	 */
	public void updateRecipeGroup(String group) {
		if (this.recipe != null) {
			ResourceLocation groupName = group.contains(":") ? new ResourceLocation(group) : new ResourceLocation(AdditionsMod.MOD_ID, group);
			ReflectionHelper.setPrivateValue(ShapelessOreRecipe.class, this.recipe, groupName, "group");
		}
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedCraftingShapeless> {
		
		public Serializer() {
			super(TYPE, RecipeAddedCraftingShapeless.class);
		}

		public JsonObject serialize(RecipeAddedCraftingShapeless recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			JsonObject recipeJson = new JsonObject();
			JsonArray ingredientArray = new JsonArray();
			
			recipeJson.addProperty("type", "forge:ore_shapeless");
			
			if (!recipeAdded.recipe.getGroup().isEmpty()) {
				recipeJson.addProperty("group", recipeAdded.recipe.getGroup());
			}
			
			if (!recipeAdded.recipe.getRecipeOutput().isEmpty()) {
				recipeJson.add("result", OtherSerializers.CraftingHelperItemStackSerializer.serialize(recipeAdded.recipe.getRecipeOutput()));
			} else {
				throw new IllegalArgumentException("Recipe must have an output");
			}
			
			if (recipeAdded.recipe != null && !recipeAdded.recipe.getIngredients().isEmpty()) {
				JsonObject keyJson = new JsonObject();
				
				for (Ingredient ingredient: recipeAdded.recipe.getIngredients()) {
					
					if (ingredient instanceof IngredientOreNBT) {
						JsonObject ingredientJson = IngredientOreNBT.Serializer.serialize((IngredientOreNBT) ingredient);
						ingredientJson.addProperty("type", IngredientOreNBT.TYPE.toString());
						ingredientArray.add(ingredientJson);
					} else {
						throw new IllegalArgumentException("Serializer only knows how to serialize IngredientOreNBT");
					}
				}
			
				recipeJson.add("ingredients", ingredientArray);
			} else {
				throw new IllegalArgumentException("Recipe must have an input");
			}
			
			json.add("recipe", recipeJson);
			return json;
        }
		
		@Override
		public RecipeAddedCraftingShapeless deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedCraftingShapeless recipeAdded = new RecipeAddedCraftingShapeless();
			recipeAdded.recipe = ShapelessOreRecipe.factory(new JsonContext(AdditionsMod.MOD_ID), JsonUtils.getJsonObject(json, "recipe"));
			return recipeAdded;
		}
    }

}
