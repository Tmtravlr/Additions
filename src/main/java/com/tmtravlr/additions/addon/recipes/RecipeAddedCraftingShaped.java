package com.tmtravlr.additions.addon.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
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
 * An added shaped crafting recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedCraftingShaped implements IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "crafting_shaped");
	
	public ResourceLocation id;
	public ShapedOreRecipe recipe;

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
	
	public boolean isMirrored() {
		boolean mirrored = true;
		if (this.recipe != null) {
			mirrored = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, this.recipe, "mirrored");
		}
		return mirrored;
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
			ReflectionHelper.setPrivateValue(ShapedOreRecipe.class, this.recipe, groupName, "group");
		}
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedCraftingShaped> {
		
		public Serializer() {
			super(TYPE, RecipeAddedCraftingShaped.class);
		}

		public JsonObject serialize(RecipeAddedCraftingShaped recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			JsonObject recipeJson = new JsonObject();
			
			recipeJson.addProperty("type", "forge:ore_shaped");
			
			if (!recipeAdded.recipe.getGroup().isEmpty()) {
				recipeJson.addProperty("group", recipeAdded.recipe.getGroup());
			}
			
			if (!recipeAdded.isMirrored()) {
				recipeJson.addProperty("mirrored", false);
			}
			
			if (!recipeAdded.recipe.getRecipeOutput().isEmpty()) {
				recipeJson.add("result", OtherSerializers.CraftingHelperItemStackSerializer.serialize(recipeAdded.recipe.getRecipeOutput()));
			} else {
				throw new IllegalArgumentException("Recipe must have an output");
			}
			
			if (recipeAdded.recipe != null && !recipeAdded.recipe.getIngredients().isEmpty()) {
				int width = recipeAdded.recipe.getRecipeWidth();
				int height = recipeAdded.recipe.getRecipeHeight();
				String[] pattern = new String[height];
				Arrays.fill(pattern, "");
				Map<IngredientOreNBT, Character> patternMap = new HashMap<>();
				JsonObject keyJson = new JsonObject();
				
				for (int i = 0; i < recipeAdded.recipe.getIngredients().size(); i++) {
					int x = i % width;
					int y = MathHelper.floor((double)i / (double)width);
					
					Ingredient ingredient = recipeAdded.recipe.getIngredients().get(i);
					char ingredientChar = ' ';
					
					if (ingredient != Ingredient.EMPTY) {
						if (ingredient instanceof IngredientOreNBT) {
							IngredientOreNBT ingredientOreNBT = (IngredientOreNBT) ingredient; 
							
							if (!patternMap.containsKey(ingredientOreNBT)) {
								ingredientChar = (char)(48 + patternMap.size());
								patternMap.put(ingredientOreNBT, ingredientChar);
								
								JsonObject ingredientJson = IngredientOreNBT.Serializer.serialize(ingredientOreNBT);
								ingredientJson.addProperty("type", IngredientOreNBT.TYPE.toString());
								keyJson.add(String.valueOf(ingredientChar), ingredientJson);
							} else {
								ingredientChar = patternMap.get(ingredientOreNBT);
							}
						} else {
							throw new IllegalArgumentException("Serializer only knows how to serialize IngredientOreNBT");
						}
					}
							
					pattern[y] += ingredientChar;
				}
				recipeJson.add("key", keyJson);
				
				JsonArray patternArray = new JsonArray();
				for (int y = 0; y < height; y++) {
					patternArray.add(String.valueOf(pattern[y]));
				}
				recipeJson.add("pattern", patternArray);
			} else {
				throw new IllegalArgumentException("Recipe must have an input");
			}
			
			json.add("recipe", recipeJson);
			return json;
        }
		
		@Override
		public RecipeAddedCraftingShaped deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedCraftingShaped recipeAdded = new RecipeAddedCraftingShaped();
			recipeAdded.recipe = ShapedOreRecipe.factory(new JsonContext(AdditionsMod.MOD_ID), JsonUtils.getJsonObject(json, "recipe"));
			return recipeAdded;
		}
    }

}
