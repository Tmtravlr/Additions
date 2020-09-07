package com.tmtravlr.additions.gui.view;

import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardItem;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditSelectTypeItem;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the items added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiViewItems extends GuiViewAdditionType {

	public GuiViewItems(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.item.title"), new TextComponentTranslation("gui.view.addon.items.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {		
		List<IItemAdded> additions = AdditionTypeManager.getAdditionType(AdditionTypeItem.NAME).getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.getId() == null || second == null || second.getId() == null) {
				return 0;
			}
			return first.getId().compareTo(second.getId());
		});
		
		for (IItemAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardItem(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditSelectTypeItem(this, I18n.format("gui.edit.item.title"), this.addon);
	}

}
