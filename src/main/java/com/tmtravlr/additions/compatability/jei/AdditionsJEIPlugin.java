package com.tmtravlr.additions.compatability.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewingComplete;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.additions.util.BrewingCompleteDisplayRecipes;
import com.tmtravlr.additions.util.BrewingCompleteDisplayRecipes.DisplayRecipe;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

/**
 * JEI Plugin to add the brewing recipes, since they don't work by default
 * @author Rebeca
 * @date May 2021
 */
@JEIPlugin
public class AdditionsJEIPlugin implements IModPlugin {
	
	@Override
	public void register(IModRegistry registry) {
		List<IRecipeWrapper> wrappers = new ArrayList<>();	
		
		for (RecipeAddedBrewingComplete recipe : AdditionTypeRecipe.INSTANCE.getAllAdditions().stream()
				.filter(recipe -> recipe instanceof RecipeAddedBrewingComplete)
				.map(recipe -> (RecipeAddedBrewingComplete) recipe)
				.collect(Collectors.toList())) {
			for (DisplayRecipe displayRecipe : BrewingCompleteDisplayRecipes.createDisplayRecipes(recipe)) {
				IRecipeWrapper wrapper = registry.getJeiHelpers().getVanillaRecipeFactory()
						.createBrewingRecipe(displayRecipe.ingredient.getAllStacks(), displayRecipe.input, displayRecipe.output);
				
				wrappers.add(wrapper);
			}
		}
		
		registry.addRecipes(wrappers, VanillaRecipeCategoryUid.BREWING);
	}

}
