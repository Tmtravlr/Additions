package com.tmtravlr.additions.gui.view.edit;

import com.tmtravlr.additions.gui.message.GuiMessageBoxThreeButton;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Version of the View screen meant for editing objects. Has a save button by default.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public abstract class GuiEdit extends GuiView {
	
	protected final int BUTTON_SAVE = this.buttonCount++;
	
	protected boolean unsavedChanges = false;

	public GuiEdit(GuiScreen parentScreen, String title) {
		super(parentScreen, title);
	}
	
	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.save")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_SAVE) {
    		saveObject();
    	} else if (button.id == BUTTON_BACK) {
    		cancelEdit();
    	}
    }
    
    @Override
	public void refreshView() {}
	
	public void notifyHasChanges() {
		this.unsavedChanges = true;
	}
	
	public void saveObject() {}
	
	public void cancelEdit() {
		if (this.unsavedChanges) {
			final GuiView editObject = this;
			this.mc.displayGuiScreen(new GuiMessageBoxThreeButton(this, this.parentScreen, I18n.format("gui.warnDialogue.unsaved.title"), new TextComponentTranslation("gui.warnDialogue.unsaved.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.warnDialogue.unsaved.continue"), I18n.format("gui.buttons.save")) {
				
				@Override
				public void onThirdButtonClicked() {
					saveObject();
				}
			});
		} else {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}
    
}
