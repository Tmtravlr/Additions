package com.tmtravlr.additions.gui.view.edit.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.*;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.view.edit.GuiEditSelectType;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiEditSelectTypeItem extends GuiEditSelectType<ResourceLocation> {

	private Map<ResourceLocation, String> descriptions = new HashMap<>();
	
	public GuiEditSelectTypeItem(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
		
		this.descriptions.put(ItemAddedManager.getTypeFor(ItemAddedSimple.class), I18n.format(ItemAddedSimple.DESCRIPTION));
		this.descriptions.put(ItemAddedManager.getTypeFor(ItemAddedFood.class), I18n.format(ItemAddedFood.DESCRIPTION));
	}

	@Override
	public Collection<ResourceLocation> getTypes() {
		return ItemAddedManager.getAllTypes();
	}

	@Override
	public String getDescription(ResourceLocation type) {
		return this.descriptions.get(type);
	}

	@Override
	public void createObject() {
		ResourceLocation selected = this.getSelectedType();
		
		IGuiEditItemFactory editFactory = ItemAddedManager.getGuiFactoryFor(selected);
		
		if (editFactory != null) {
			this.mc.displayGuiScreen(editFactory.getEditScreen(this.parentScreen, this.addon, null));
		} else {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", selected), I18n.format("gui.buttons.close")));
		}
	}

}
