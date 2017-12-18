package com.tmtravlr.additions.gui.view;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardItem;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionList;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditSelectTypeItem;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Shows a list of cards with info about the items added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiViewItems extends GuiViewAdditionType {

	public GuiViewItems(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.item.title"));
	}
	
	@Override
	public void initComponents() {
		super.initComponents();
		
		List<IItemAdded> addedItems = AdditionTypeManager.getAdditionType(AdditionTypeItem.NAME).getAllAdditions(this.addon);
		for (IItemAdded addedItem : addedItems) {
			this.additions.addAdditionCard(new GuiAdditionCardItem(this, this.addon, addedItem));
		}
		
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditSelectTypeItem(this, I18n.format("gui.edit.item.title"), this.addon);
	}

}
