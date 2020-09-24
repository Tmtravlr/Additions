package com.tmtravlr.additions.gui.view.edit.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.api.gui.IGuiItemAddedFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.edit.GuiEditSelectType;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of items you can create.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017 
 */
public class GuiEditSelectTypeItem extends GuiEditSelectType<IGuiItemAddedFactory<?>> {
	
	public GuiEditSelectTypeItem(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
	}

	@Override
	public Collection<IGuiItemAddedFactory<?>> getTypes() {
		return ItemAddedManager.getAllGuiFactories().values();
	}

	@Override
	public String getDescription(IGuiItemAddedFactory<?> editFactory) {
		return editFactory.getDescription();
	}
	
	@Override
	public String getTitle(IGuiItemAddedFactory<?> editFactory) {
		return editFactory.getTitle();
	}

	@Override
	public void createObject() {
		this.mc.displayGuiScreen(this.getSelectedType().getEditScreen(this.parentScreen, this.addon, null));
	}

}
