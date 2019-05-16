package com.tmtravlr.additions.gui.view;

import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.functions.FunctionAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardFunction;
import com.tmtravlr.additions.gui.view.edit.GuiEditFunction;
import com.tmtravlr.additions.type.AdditionTypeFunction;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the functions added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class GuiViewFunctions extends GuiViewAdditionType {

	public GuiViewFunctions(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.function.title"), new TextComponentTranslation("gui.view.addon.functions.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<FunctionAdded> additions = AdditionTypeFunction.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.id == null || second == null || second.id == null) {
				return 0;
			}
			return first.id.compareTo(second.id);
		});
		
		for (FunctionAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardFunction(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditFunction(this, I18n.format("gui.edit.function.title"), this.addon, null);
	}

}
