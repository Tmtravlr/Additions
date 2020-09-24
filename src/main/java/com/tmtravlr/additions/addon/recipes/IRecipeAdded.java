package com.tmtravlr.additions.addon.recipes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.ResourceLocation;

/**
 * Represents an added recipe
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date February 2019 
 */
public interface IRecipeAdded {
	
	public void setId(ResourceLocation id);
	
	public ResourceLocation getId();
	
	public void registerRecipe();

	public abstract static class Serializer<T extends IRecipeAdded> {
		
		private final ResourceLocation recipeType;
        private final Class<T> recipeClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.recipeType = location;
            this.recipeClass = clazz;
        }

		public ResourceLocation getRecipeAddedType() {
			return this.recipeType;
		}
		
		public Class<T> getRecipeAddedClass() {
			return this.recipeClass;
		}

		public abstract JsonObject serialize(T recipe, JsonSerializationContext context);

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }
	
}
