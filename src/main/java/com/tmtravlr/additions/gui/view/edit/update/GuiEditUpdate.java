package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Page that is more geared towards updating something simple,
 * and doesn't check if there are unsaved changes when going back.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date October 2018
 */
public abstract class GuiEditUpdate extends GuiEdit {
	
	public GuiEditUpdate(GuiScreen parentScreen, String title) {
		super(parentScreen, title);
	}

	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.update")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
	}
	
	@Override
	public void cancelEdit() {
		this.mc.displayGuiScreen(this.parentScreen);
	}

}
