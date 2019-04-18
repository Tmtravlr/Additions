package com.tmtravlr.additions.addon.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.oredict.OreDictionary;

/**
 * An added brewing recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedBrewing extends AbstractBrewingRecipe<IngredientOreNBT> implements IRecipeAdded {

	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "brewing");
	
	public ResourceLocation id;
	public ItemStack input;
	public IngredientOreNBT ingredient;
	public ItemStack output;
	
	public RecipeAddedBrewing() {
		super(ItemStack.EMPTY, IngredientOreNBT.EMPTY, ItemStack.EMPTY);
	}

	@Override
	public void setId(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public void registerRecipe() {
		//BrewingRecipeRegistry.addRecipe(this);
		
		// Registering in a very cheeky way here instead of BrewingRecipeRegistry.addRecipe(this); because otherwise the vanilla recipe breaks any recipes that use glowstone or redstone dust
		RecipeAddedManager.BREWING_RECIPES.add(0, this);
	}
	
	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return this.ingredient.apply(ingredient);
	}
	
	@Override
	public boolean isInput(ItemStack input) {
		return OreDictionary.itemMatches(this.input, input, false) && NBTUtil.areNBTEquals(input.getTagCompound(), this.input.getTagCompound(), true);
	}

    @Override
    public ItemStack getOutput()
    {
        return this.output;
    }
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedBrewing> {
		
		public Serializer() {
			super(TYPE, RecipeAddedBrewing.class);
		}

		public JsonObject serialize(RecipeAddedBrewing recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (recipeAdded.input.isEmpty()) {
				throw new IllegalArgumentException("Expected an input for brewing recipe " + recipeAdded.id);
			}
			
			json.add("input", OtherSerializers.ItemStackSerializer.serialize(recipeAdded.input));
			
			if (recipeAdded.ingredient.isEmpty()) {
				throw new IllegalArgumentException("Expected an ingredient for brewing recipe " + recipeAdded.id);
			}
			
			json.add("ingredient", IngredientOreNBT.Serializer.serialize(recipeAdded.ingredient));
			
			if (recipeAdded.output.isEmpty()) {
				throw new IllegalArgumentException("Expected an output for brewing recipe " + recipeAdded.id);
			}
			
			json.add("output", OtherSerializers.ItemStackSerializer.serialize(recipeAdded.output));
			
			return json;
        }
		
		@Override
		public RecipeAddedBrewing deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedBrewing recipeAdded = new RecipeAddedBrewing();
			
			recipeAdded.input = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "input"));
			
			if (recipeAdded.input.isEmpty()) {
				throw new JsonParseException("Expected an input for brewing recipe");
			}
			
			recipeAdded.ingredient = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "ingredient"));
			
			if (recipeAdded.ingredient.isEmpty()) {
				throw new JsonParseException("Expected an ingredient for brewing recipe");
			}
			
			recipeAdded.output = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "output"));
			
			if (recipeAdded.output.isEmpty()) {
				throw new JsonParseException("Expected an output for brewing recipe");
			}
			
			return recipeAdded;
		}
    }

}
