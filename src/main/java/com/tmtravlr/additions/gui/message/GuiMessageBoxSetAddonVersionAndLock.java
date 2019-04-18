package com.tmtravlr.additions.gui.message;

import java.io.File;
import java.io.IOException;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewAddon;
import com.tmtravlr.additions.gui.view.GuiViewLootTables;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiMessageBoxSetAddonVersionAndLock extends GuiMessageBoxTwoButton {
	
	protected final GuiTextFieldVersion versionInput;
	protected final String versionLabel = I18n.format("gui.popup.addonVersion.label");
	
	protected Addon addon;
	
	public GuiMessageBoxSetAddonVersionAndLock(GuiScreen parentScreen, FontRenderer fontRenderer, Addon addon) {
		super(parentScreen, parentScreen, I18n.format("gui.popup.addonVersion.title"), StringUtils.isNullOrEmpty(addon.version) ? new TextComponentTranslation("gui.popup.addonVersion.message") : new TextComponentTranslation("gui.popup.addonVersion.message.lastVersion", addon.version), I18n.format("gui.buttons.cancel"), I18n.format("gui.buttons.share"));
		this.addon = addon;
		
		String suggestedVersion;
		
		if (StringUtils.isNullOrEmpty(addon.version)) {
			suggestedVersion = "1.0";
		} else {
			suggestedVersion = addon.version;
			
			int lastDotIndex = addon.version.lastIndexOf('.');
			if (lastDotIndex >= 0 && lastDotIndex + 1 < addon.version.length()) {
				String lastNumberString = addon.version.substring(lastDotIndex + 1);
				String versionStart = addon.version.substring(0, lastDotIndex + 1);
				if (!lastNumberString.isEmpty()) {
					try {
						int lastNumber = Integer.parseInt(lastNumberString);
						lastNumber++;
						suggestedVersion = versionStart + lastNumber;
					} catch (NumberFormatException e) {
						AdditionsMod.logger.warn("Couldn't parse number '" + lastNumberString + "' in version string for addon " + addon.id, e);
					}
				}
			}
		}
		
		this.versionInput = new GuiTextFieldVersion(fontRenderer);
		this.versionInput.setValidator(input -> input.matches("[0-9\\.]*"));
		this.versionInput.setText(suggestedVersion);
		this.versionInput.setCursorPosition(0);
	}
    
	@Override
    protected int getPopupWidth() {
    	return 350;
    }
	
    @Override
    protected int getPopupHeight() {
    	return this.versionInput.height + 20 + super.getPopupHeight();
    }

    @Override
    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
		super.drawScreenOverlay(mouseX, mouseY, partialTicks);
		
		int popupWidth = this.getPopupWidth();
    	int popupHeight = this.getPopupHeight();
    	int popupX = this.width / 2 - popupWidth / 2;
    	int popupY = this.height / 2 - popupHeight / 2;
    	int popupRight = popupX + popupWidth;
		
		int componentY = popupY + 65 + this.textHeight;
		int labelOffset = 80;

		this.drawString(this.fontRenderer, this.versionLabel, popupX + 10, componentY + this.versionInput.height/2 - 5, 0xFFFFFF);
		this.versionInput.drawInMessageBox(popupX + labelOffset + 10, componentY, popupRight - 20);
		
		this.drawPostRender();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	this.versionInput.mouseClicked(mouseX, mouseY, mouseButton);
    	
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	this.versionInput.textboxKeyTyped(keyTyped, keyCode);

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
    }
    
    @Override
    protected void onSecondButtonClicked() {
    	if (this.versionInput.getText().isEmpty()) {
    		this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.addonVersion.problem.noVersion.title"), new TextComponentTranslation("gui.popup.addonVersion.problem.noVersion.message"), I18n.format("gui.buttons.back")));
    	} else {
    		this.addon.version = this.versionInput.getText();
    		AddonLoader.saveAddon(this.addon);
    		
    		this.mc.displayGuiScreen(new GuiMessageBoxLockingAddon(this.parentScreen, this.addon));
    	}
    }
    
    private class GuiTextFieldVersion extends GuiTextField {

		public GuiTextFieldVersion(FontRenderer fontRenderer) {
			super(0, fontRenderer, 0, 0, 0, 20);
		}
		
		public void drawInMessageBox(int x, int y, int right) {
			this.x = x;
			this.y = y;
			this.width = right - x;
			
			this.drawTextBox();
		}
    	
    }
}

