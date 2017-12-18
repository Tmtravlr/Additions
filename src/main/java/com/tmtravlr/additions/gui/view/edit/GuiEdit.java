package com.tmtravlr.additions.gui.view.edit;

import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Version of the View screen meant for editing objects. Has a save button by default.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public abstract class GuiEdit extends GuiView {
	
	protected final int SAVE_BUTTON = buttonCount++;
	
	protected boolean unsavedChanges = false;

	public GuiEdit(GuiScreen parentScreen, String title) {
		super(parentScreen, title);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButton(SAVE_BUTTON, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.save")));
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == SAVE_BUTTON) {
    		saveObject();
    	} else if (button.id == BACK_BUTTON) {
    		cancelEdit();
    	}
    }
	
	public void notifyHasChanges() {
		this.unsavedChanges = true;
	}
	
	public void saveObject() {}
	
	public void cancelEdit() {
		if (this.unsavedChanges) {
			final GuiView editObject = this;
			this.mc.displayGuiScreen(new GuiMessagePopupTwoButton(this, parentScreen, I18n.format("gui.warnDialogue.unsaved.title"), new TextComponentTranslation("gui.warnDialogue.unsaved.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.warnDialogue.unsaved.continue")));
		} else {
			this.mc.displayGuiScreen(parentScreen);
		}
	}
    
}
