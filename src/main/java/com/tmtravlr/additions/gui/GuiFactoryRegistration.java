package com.tmtravlr.additions.gui;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButton;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonCreativeTab;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonItem;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSimple;
import com.tmtravlr.additions.gui.view.edit.item.IGuiEditItemFactory;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFactoryRegistration {

	public static void registerGuiFactories() {
		AdditionTypeManager.registerAdditionTypeGuiFactory(new IAdditionTypeGuiFactory() {
			@Override
			public GuiAdditionTypeButton createButton(GuiView parent, Addon addon) {
				return new GuiAdditionTypeButtonItem(parent, addon);
			}
		});
		
		AdditionTypeManager.registerAdditionTypeGuiFactory(new IAdditionTypeGuiFactory() {
			@Override
			public GuiAdditionTypeButton createButton(GuiView parent, Addon addon) {
				return new GuiAdditionTypeButtonCreativeTab(parent, addon);
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedSimple.TYPE, new IGuiEditItemFactory<ItemAddedSimple>() {
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedSimple item) {
				return new GuiEditItemSimple(parent, item == null ? I18n.format("gui.edit.item.simple.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			@Override
			public GuiEdit getDuplicateScreen(GuiView parent, Addon addon, ItemAddedSimple item) {
				GuiEditItemSimple editScreen = new GuiEditItemSimple(parent, I18n.format("gui.edit.item.simple.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
	}
	
}
