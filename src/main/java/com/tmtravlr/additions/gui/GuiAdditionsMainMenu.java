package com.tmtravlr.additions.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.GuiSlotModList;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.view.GuiViewAddon;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;

/**
 * Additions Main Menu
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class GuiAdditionsMainMenu extends GuiScreen {
	
    private static final ResourceLocation TITLE_TEXTURE = new ResourceLocation("additions:textures/gui/additions_title_big.png");

	private static final int BUTTON_DONE = 0;
	
	private GuiScreen parentScreen;
	
	private ArrayList<Addon> addonsToDisplay;
	public Addon createNew = new Addon();
	
	private GuiScrollingAddonList addonList;
	
	public GuiAdditionsMainMenu(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	/**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
	@Override
    public void initGui() {
		this.addonsToDisplay = new ArrayList<>(AddonLoader.addonsLoaded);
		this.addonsToDisplay.add(0, createNew);
		
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

    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        this.addonList.handleMouseInput(mouseX, mouseY);
    }
    
    public void addonSelected(Addon addon) {
    	if (addon == createNew) {
    		this.mc.displayGuiScreen(new GuiEditAddon(this, I18n.format("gui.edit.addon.title")));
    	} else {
    		this.mc.displayGuiScreen(new GuiViewAddon(this, addon.name + TextFormatting.RESET +  " " + I18n.format("gui.additions.main.byAuthor", addon.author, addon.id), addon));
    	}
    }
    
    public FontRenderer getFontRenderer() {
    	return this.fontRenderer;
    }
}
