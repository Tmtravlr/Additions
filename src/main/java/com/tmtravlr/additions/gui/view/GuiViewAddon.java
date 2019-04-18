package com.tmtravlr.additions.gui.view;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSetAddonVersionAndLock;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.message.GuiMessageBoxUnlockingAddon;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeButton;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Shows info about an addon, like what is loaded.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiViewAddon extends GuiView {
	
	protected final int EDIT_BUTTON = this.buttonCount++;
	protected final int SHARE_BUTTON = this.buttonCount++;
	protected final int UNLOCK_BUTTON = this.buttonCount++;
	
	private GuiComponentDisplayText addonLockedWarning;
	
	private final Addon addon;

	public GuiViewAddon(GuiScreen parentScreen, Addon addon) {
		super(parentScreen, addon.name + TextFormatting.RESET +  " " + I18n.format("gui.additions.main.byAuthor", addon.author, addon.id));
		this.addon = addon;
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		this.title = this.addon.name + TextFormatting.RESET +  " " + I18n.format("gui.additions.main.byAuthor", this.addon.author, this.addon.id);
		this.addonLockedWarning.setHidden(!this.addon.locked);
	}

	@Override
	public void initComponents() {
		
		this.addonLockedWarning = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.view.addon.locked.message"));
		this.addonLockedWarning.setHidden(!this.addon.locked);
		this.addonLockedWarning.setIgnoreLabel(true);
		this.addonLockedWarning.setCentered(true);
		
		this.components.add(this.addonLockedWarning);
		
		for (IAdditionTypeGuiFactory guiFactory : AdditionTypeManager.getAdditionTypeGuiFactories()) {
			GuiComponentAdditionTypeButton button = new GuiComponentAdditionTypeButton(guiFactory.createButton(this, this.addon));
			
			if (button.isAdvanced()) {
				this.advancedComponents.add(button);
			} else {
				this.components.add(button);
			}
		}
	}

	@Override
	protected void addButtons() {
		if (this.addon.locked) {
			this.buttonList.add(new GuiButton(UNLOCK_BUTTON, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.unlock")));
			this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
		} else {
			this.buttonList.add(new GuiButton(SHARE_BUTTON, this.width - 130, this.height - 30, 120, 20, TextFormatting.AQUA + I18n.format("gui.popup.shareAddon.button")));
			this.buttonList.add(new GuiButton(EDIT_BUTTON, this.width - 200, this.height - 30, 60, 20, I18n.format("gui.buttons.edit")));
			this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 270, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
		}
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == EDIT_BUTTON) {
    		this.mc.displayGuiScreen(new GuiEditAddon(this, I18n.format("gui.edit.editing", this.addon.name), this.addon));
    	} else if (button.id == SHARE_BUTTON) {
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, new GuiMessageBoxSetAddonVersionAndLock(this, this.fontRenderer, addon), I18n.format("gui.popup.shareAddon.title"), new TextComponentString("").appendSibling(new TextComponentTranslation("gui.popup.shareAddon.message1")).appendText(" ").appendSibling(new TextComponentTranslation("gui.popup.shareAddon.message2").setStyle((new Style()).setColor(TextFormatting.AQUA))), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.share")));
    	} else if (button.id == UNLOCK_BUTTON) {
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, new GuiMessageBoxUnlockingAddon(this, this.addon), I18n.format("gui.popup.unlockAddon.warning.title"), new TextComponentTranslation("gui.popup.unlockAddon.warning.message"), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.unlock")));
    	} else {
    		super.actionPerformed(button);
    	}
	}
}
