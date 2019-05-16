package com.tmtravlr.additions.gui.view;

import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.gui.type.card.recipe.GuiAdditionCardRecipe;
import com.tmtravlr.additions.gui.view.edit.GuiEditCreativeTab;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditSelectTypeItem;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditSelectTypeRecipe;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the recipes added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiViewRecipes extends GuiViewAdditionType {

	public GuiViewRecipes(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.recipe.title"), new TextComponentTranslation("gui.view.addon.recipes.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<IRecipeAdded> additions = AdditionTypeRecipe.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.getId() == null || second == null || second.getId() == null) {
				return 0;
			}
			return first.getId().compareTo(second.getId());
		});
		
		for (IRecipeAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardRecipe(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditSelectTypeRecipe(this, I18n.format("gui.edit.recipe.title"), this.addon);
	}

}
