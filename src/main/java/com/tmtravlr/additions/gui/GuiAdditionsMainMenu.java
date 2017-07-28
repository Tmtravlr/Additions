package com.tmtravlr.additions.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiSlotModList;

import com.tmtravlr.additions.addon.AddonInfo;
import com.tmtravlr.additions.addon.AddonLoader;

public class GuiAdditionsMainMenu extends GuiScreen {
	
    private static final ResourceLocation TITLE_TEXTURE = new ResourceLocation("additions:textures/gui/additions_title_big.png");

	private static final int BUTTON_DONE = 0;
	
	private GuiScreen parentScreen;
	
	private ArrayList<AddonInfo> addonsToDisplay;
	public AddonInfo createNew = new AddonInfo();
	
	private GuiScrollingAddonList addonList;
	
	public GuiAdditionsMainMenu(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
		this.addonsToDisplay = new ArrayList<>(AddonLoader.addonsLoaded);
		this.addonsToDisplay.add(0, createNew);
	}
	
	/**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
	@Override
    public void initGui() {
		int maxHeight = this.height - 140;
		int listHeight = this.addonsToDisplay.size()*32 + 4;
		listHeight = Math.min(listHeight, maxHeight);
		
		int minY = 100 + (maxHeight - listHeight) / 2;
		
		this.addonList = new GuiScrollingAddonList(this, addonsToDisplay, 260, listHeight, minY, 32);
		
		this.buttonList.add(new GuiButton(BUTTON_DONE, this.width / 2 - 100, this.height - 30, 200, 20, I18n.format("gui.done")));
    }
	
	/**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        
        int midX = this.width / 2;
        GlStateManager.enableAlpha();
        
        this.addonList.drawScreen(mouseX, mouseY, partialTicks);
        
        this.mc.getTextureManager().bindTexture(TITLE_TEXTURE);
        this.drawModalRectWithCustomSizedTexture(midX - 160, 10, 0, 0, 320, 80, 320, 80);
        
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
	
	/**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) {
    	if(button.id == BUTTON_DONE) {
    		this.mc.displayGuiScreen(this.parentScreen);
    	}
    }
    
    public void addonSelected(AddonInfo addon) {
    	if (addon == createNew) {
    		//Create new addon
    	} else {
    		//Edit the addon
    	}
    }
    
    public FontRenderer getFontRenderer() {
    	return this.fontRenderer;
    }
}
