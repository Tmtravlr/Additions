package com.tmtravlr.additions.gui.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeList;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * Shows info about an addon, like what is loaded.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiViewAddon extends GuiView {
	
	protected final int EDIT_BUTTON = this.buttonCount++;
	
	private GuiComponentAdditionTypeList additionTypes;
	private final Addon addon;

	public GuiViewAddon(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title);
		this.addon = addon;
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		this.title = this.addon.name + TextFormatting.RESET +  " " + I18n.format("gui.additions.main.byAuthor", this.addon.author, this.addon.id);
	}

	@Override
	public void initComponents() {
		this.additionTypes = new GuiComponentAdditionTypeList(this, this.addon);
		
		this.components.add(this.additionTypes);
	}

	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButton(EDIT_BUTTON, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.edit")));
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == EDIT_BUTTON) {
    		this.mc.displayGuiScreen(new GuiEditAddon(this, I18n.format("gui.edit.editing", this.addon.name), this.addon));
    	} else {
    		super.actionPerformed(button);
    	}
	}
}
