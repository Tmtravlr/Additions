package com.tmtravlr.additions.gui.view;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardCreativeTab;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionList;
import com.tmtravlr.additions.gui.view.edit.GuiEditCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Shows a list of cards with info about the items added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiViewCreativeTabs extends GuiViewAdditionType {

	public GuiViewCreativeTabs(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.creativeTab.title"));
	}
	
	@Override
	public void initComponents() {
		super.initComponents();
		
		List<CreativeTabAdded> additions = AdditionTypeManager.getAdditionType(AdditionTypeCreativeTab.NAME).getAllAdditions(this.addon);
		for (CreativeTabAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardCreativeTab(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditCreativeTab(this, I18n.format("gui.edit.creativeTab.title"), this.addon, null);
	}

}
