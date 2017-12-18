package com.tmtravlr.additions.gui.texture;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.GuiMessagePopup;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a message saying the resources are reloading, since otherwise the game just freezes and people might get confused.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiRefreshingResources extends GuiMessagePopup {
	
	private int delay = 20;

	public GuiRefreshingResources(GuiScreen parentScreen) {
		super(parentScreen, I18n.format("gui.popup.reloadResources.title"), new TextComponentTranslation("gui.popup.reloadResources.message"), "");
	}

	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.clear();
	}
	
	@Override
	public void updateScreen() {
		if (delay-- <= 0) {
			AdditionsMod.proxy.refreshResources();
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

}
