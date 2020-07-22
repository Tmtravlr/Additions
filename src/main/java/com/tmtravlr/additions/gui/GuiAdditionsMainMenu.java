package com.tmtravlr.additions.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.tmtravlr.additions.addon.potiontypes.PotionTypeAdded;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiViewAddon;
import com.tmtravlr.additions.gui.view.GuiViewProblemNotifications;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;
import com.tmtravlr.additions.util.ProblemNotifier;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Additions Main Menu
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class GuiAdditionsMainMenu extends GuiScreen {
	
    private static final ResourceLocation TITLE_TEXTURE = new ResourceLocation("additions:textures/gui/additions_title_big.png");

    private static final int BUTTON_DONE = 0;
	private static final int BUTTON_PROBLEMS = 1;
	
	private GuiScreen parentScreen;
	
	private ArrayList<Addon> addonsToDisplay;
	public Addon createNew = new Addon();
	
	private GuiScrollingAddonList addonList;
	
	public GuiAdditionsMainMenu(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
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
		if (ConfigLoader.showProblemNotificationsMainMenu.getBoolean() && ProblemNotifier.hasProblems()) {
			this.buttonList.add(new GuiSideButtonViewProblems(BUTTON_PROBLEMS, this.width - 18, this.height / 2, this));
		}
    }
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        
        int midX = this.width / 2;
        GlStateManager.enableAlpha();
        
        this.addonList.drawScreen(mouseX, mouseY, partialTicks);
        
        this.mc.getTextureManager().bindTexture(TITLE_TEXTURE);
        GlStateManager.enableBlend();
        this.drawModalRectWithCustomSizedTexture(midX - 160, 10, 0, 0, 320, 80, 320, 80);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void updateScreen() {
    	if (ConfigLoader.showProblemNotificationsMainMenu.getBoolean() && ProblemNotifier.showInAdditionsMenu()) {
    		this.showProblemNotifications();
    	}
    }
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_DONE) {
    		this.mc.displayGuiScreen(this.parentScreen);
    	} else if (button.id == BUTTON_PROBLEMS) {
    		this.showProblemNotifications();
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
		CommonGuiUtils.playClickSound();
    	if (addon == createNew) {
    		this.mc.displayGuiScreen(new GuiEditAddon(this, I18n.format("gui.edit.addon.title")));
    	} else {
    		this.mc.displayGuiScreen(new GuiViewAddon(this, addon));
    	}
    }
    
    public FontRenderer getFontRenderer() {
    	return this.fontRenderer;
    }
    
    private void showProblemNotifications() {
    	this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, new GuiViewProblemNotifications(this, I18n.format("gui.notification.title")), I18n.format("gui.notification.title"), ProblemNotifier.notificationProblemCount, I18n.format("gui.buttons.back"), I18n.format("gui.notification.button.view")));
    }
}
