package com.tmtravlr.additions.gui.message;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Shows a message saying the resources are reloading, since otherwise the game just freezes and people might get confused.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiMessageBoxUnlockingAddon extends GuiMessageBox {
	
	private final Addon addon;
	private final String zipFileName;
	private int delay = 20;

	public GuiMessageBoxUnlockingAddon(GuiScreen parentScreen, Addon addon) {
		super(parentScreen, I18n.format("gui.popup.unlockAddon.title"), new TextComponentTranslation("gui.popup.unlockAddon.message"), "");
		this.addon = addon;
		this.zipFileName = addon.addonFolder.getName();
	}

	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.clear();
	}
	
	@Override
	public void updateScreen() {
		if (delay-- <= 0) {
			final File zipFileFolder = this.addon.addonFolder.getParentFile();
			try {
				AddonLoader.unlockAddon(GuiMessageBoxUnlockingAddon.this.addon);
				
				this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.unlockAddon.success.title"), new TextComponentTranslation("gui.popup.unlockAddon.success.message"), I18n.format("gui.buttons.continue")) {
					@Override
				    protected void onFirstButtonClicked() {
						if (this.parentScreen instanceof GuiView) {
							((GuiView) parentScreen).refreshView();
						}
						
						super.onFirstButtonClicked();
					}
				});
			} catch (IOException e) {
				if (e instanceof FileSystemException && e.getMessage().contains("The process cannot access the file because it is being used by another process.")) {
					AdditionsMod.logger.warn("Error while unlocking addon " + GuiMessageBoxUnlockingAddon.this.addon.id, e);
					this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.unlockAddon.problem.noDelete.title", this.zipFileName), new TextComponentString("").appendSibling(new TextComponentTranslation("gui.popup.unlockAddon.problem.noDelete.message1", this.zipFileName)).appendText(" ").appendSibling(new TextComponentTranslation("gui.popup.unlockAddon.problem.noDelete.message2").setStyle(new Style().setColor(TextFormatting.RED))), I18n.format("gui.buttons.goToFolder")) {
						@Override
					    protected void onFirstButtonClicked() {
							CommonGuiUtils.openFolder(zipFileFolder);
							
							if (this.parentScreen instanceof GuiView) {
								((GuiView) parentScreen).refreshView();
							}
							
							super.onFirstButtonClicked();
						}
						
						@Override
					    protected int getPopupWidth() {
					    	return 400;
					    }
					});
				} else {
					AdditionsMod.logger.warn("Error while unlocking addon " + GuiMessageBoxUnlockingAddon.this.addon.id, e);
					this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.unlockAddon.problem.title"), new TextComponentTranslation("gui.popup.unlockAddon.problem.message", e.getClass() + ": " + e.getMessage()), I18n.format("gui.buttons.back")));
				}
			}
		}
	}

}
