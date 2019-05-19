package com.tmtravlr.additions.gui.view;

import java.io.IOException;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.api.gui.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSetAddonVersionAndLock;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.message.GuiMessageBoxUnlockingAddon;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeButton;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;
import com.tmtravlr.additions.type.AdditionTypeManager;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

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
	protected final int DELETE_BUTTON = this.buttonCount++;
	
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
			this.buttonList.add(new GuiButton(DELETE_BUTTON, 10, this.height - 30, 60, 20, TextFormatting.RED + I18n.format("gui.buttons.delete")));
		}
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == EDIT_BUTTON) {
    		this.mc.displayGuiScreen(new GuiEditAddon(this, I18n.format("gui.edit.editing", this.addon.name), this.addon));
    	} else if (button.id == SHARE_BUTTON) {
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, new GuiMessageBoxSetAddonVersionAndLock(this, this.fontRenderer, this.addon), I18n.format("gui.popup.shareAddon.title"), new TextComponentString("").appendSibling(new TextComponentTranslation("gui.popup.shareAddon.message1")).appendText(" ").appendSibling(new TextComponentTranslation("gui.popup.shareAddon.message2").setStyle((new Style()).setColor(TextFormatting.AQUA))), I18n.format("gui.buttons.back"), TextFormatting.AQUA + I18n.format("gui.buttons.share")));
    	} else if (button.id == DELETE_BUTTON) {
    		GuiMessageBox addonDeletedScreen = new GuiMessageBox(this, I18n.format("gui.popup.addonDeleted.title", this.addon.name), new TextComponentTranslation("gui.popup.addonDeleted.message", this.addon.name), I18n.format("gui.popup.addonDeleted.button.restart")) {
    			
    			@Override
    			public void onFirstButtonClicked() {
    				this.mc.shutdown();
    			}
    			
    		};
    		
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, addonDeletedScreen, I18n.format("gui.popup.deleteAddon.title", this.addon.name), new TextComponentTranslation("gui.popup.deleteAddon.message", this.addon.name), I18n.format("gui.buttons.back"), TextFormatting.RED + I18n.format("gui.buttons.delete")) {
    			
    			@Override
    			public void onSecondButtonClicked() {
    				try {
    					AddonLoader.deleteAddon(GuiViewAddon.this.addon);
    				} catch (IOException e) {
    					AdditionsMod.logger.warn("Unable to delete addon " + GuiViewAddon.this.addon.id, e);
    					this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.deleteAddon.problem.noDelete.title", GuiViewAddon.this.addon.name), new TextComponentTranslation("gui.popup.deleteAddon.problem.noDelete.message", e.getClass() + ": " + e.getMessage(), GuiViewAddon.this.addon.addonFolder.getName()), I18n.format("gui.buttons.goToFolder")) {
    						
    						@Override
    		    			public void onFirstButtonClicked() {
    		    				CommonGuiUtils.openFolder(GuiViewAddon.this.addon.addonFolder.getParentFile());
    		    			}
    						
    					});
    					return;
    				}
    				super.onSecondButtonClicked();
    			}
    			
    		});
    	} else if (button.id == UNLOCK_BUTTON) {
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, new GuiMessageBoxUnlockingAddon(this, this.addon), I18n.format("gui.popup.unlockAddon.warning.title"), new TextComponentTranslation("gui.popup.unlockAddon.warning.message"), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.unlock")));
    	} else {
    		super.actionPerformed(button);
    	}
	}
}
