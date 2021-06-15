package com.tmtravlr.additions.addon.recipes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * An added smelting recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedSmelting implements IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "smelting");
	
	public ResourceLocation id;
	public ItemStack input = ItemStack.EMPTY;
	public ItemStack output = ItemStack.EMPTY;
	public float xp = 0;

	@Override
	public void setId(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public void registerRecipe(IForgeRegistry<IRecipe> registry) {
		GameRegistry.addSmelting(this.input, this.output, this.xp);
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedSmelting> {
		
		public Serializer() {
			super(TYPE, RecipeAddedSmelting.class);
		}

		public JsonObject serialize(RecipeAddedSmelting recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (recipeAdded.input.isEmpty()) {
				throw new IllegalArgumentException("Expected an input for smelting recipe " + recipeAdded.id);
			}
			
			json.add("input", OtherSerializers.ItemStackSerializer.serialize(recipeAdded.input));
			
			if (recipeAdded.output.isEmpty()) {
				throw new IllegalArgumentException("Expected an output for smelting recipe " + recipeAdded.id);
			}
			
			json.add("output", OtherSerializers.ItemStackSerializer.serialize(recipeAdded.output));
			
			if (recipeAdded.xp != 0) {
				json.addProperty("xp", recipeAdded.xp);
			}
			
			return json;
        }
		
		@Override
		public RecipeAddedSmelting deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedSmelting recipeAdded = new RecipeAddedSmelting();
			
			recipeAdded.input = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "input"));
			
			if (recipeAdded.input.isEmpty()) {
				throw new JsonParseException("Expected an input for smelting recipe");
			}
			
			recipeAdded.output = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "output"));
			
			if (recipeAdded.output.isEmpty()) {
				throw new JsonParseException("Expected an output for smelting recipe");
			}
			
			recipeAdded.xp = JsonUtils.getFloat(json, "xp", 0);
			
			return recipeAdded;
		}
    }

}
