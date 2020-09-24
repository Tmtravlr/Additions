package com.tmtravlr.additions.gui.view;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardEffect;
import com.tmtravlr.additions.gui.type.card.recipe.GuiAdditionCardRecipe;
import com.tmtravlr.additions.gui.view.edit.GuiEditEffectList;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditSelectTypeRecipe;
import com.tmtravlr.additions.type.AdditionTypeEffect;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the recipes added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiViewEffects extends GuiViewAdditionType {

	public GuiViewEffects(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.effectList.title"), new TextComponentTranslation("gui.view.addon.effects.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<EffectList> additions = AdditionTypeEffect.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.id == null || second == null || second.id == null) {
				return 0;
			}
			return first.id.compareTo(second.id);
		});
		
		for (EffectList addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardEffect(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditEffectList(this, I18n.format("gui.edit.effectList.title"), this.addon, null);
	}

}
