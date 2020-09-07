package com.tmtravlr.additions.gui.view.edit.loottable;

import java.util.Collection;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;
import com.tmtravlr.additions.api.gui.IGuiLootTablePresetFactory;
import com.tmtravlr.additions.gui.view.edit.GuiEditSelectType;

import net.minecraft.client.gui.GuiScreen;

/**
 * Page for selecting a loot table preset type to add.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2018
 */
public class GuiEditSelectTypeLootTablePreset extends GuiEditSelectType<IGuiLootTablePresetFactory> {
	
	public GuiEditSelectTypeLootTablePreset(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
	}

	@Override
	public Collection<IGuiLootTablePresetFactory> getTypes() {
		return LootTablePresetManager.getAllGuiFactories().values();
	}
	
	@Override
	public String getDescription(IGuiLootTablePresetFactory editFactory) {
		return editFactory.getDescription();
	}

	@Override
	public String getTitle(IGuiLootTablePresetFactory editFactory) {
		return editFactory.getTitle();
	}

	@Override
	public void createObject() {
		this.mc.displayGuiScreen(this.getSelectedType().getEditScreen(this.parentScreen, this.addon, null));
	}

}
