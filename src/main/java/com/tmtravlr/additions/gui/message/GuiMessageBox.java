package com.tmtravlr.additions.gui.message;

import java.io.IOException;
import java.util.List;

import com.tmtravlr.additions.gui.GuiScreenOverlay;
import com.tmtravlr.additions.gui.view.GuiView;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;

/**
 * Shows a simple notification-type popup message.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017 
 */
public class GuiMessageBox extends GuiScreenOverlay {
	
	protected static final int FIRST_BUTTON = 0;

	protected final String title;
	protected final ITextComponent message;
	protected List<String> multilineMessage;
	protected int textHeight;
	protected String firstButtonText;
	protected GuiView viewScreen;

    public GuiMessageBox(GuiScreen parentScreen, String title, ITextComponent message, String firstButtonText) {
        super(parentScreen);
        this.title = title;
        this.message = message;
        this.firstButtonText = firstButtonText;
    }
    
    public void setViewScreen(GuiView viewScreen) {
    	this.viewScreen = viewScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        int popupWidth = this.getPopupWidth();      
        
        this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), popupWidth - 50);
        this.textHeight = this.multilineMessage.size() * (this.fontRenderer.FONT_HEIGHT + 3);
        
    	int popupHeight = this.getPopupHeight();
    	int popupY = this.height / 2 - popupHeight / 2;
        
        this.initButtons(popupY + popupHeight - 30);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == FIRST_BUTTON) {
            this.onFirstButtonClicked();
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(0, 0, this.width, this.height + 63, 0x66000000);
		
    	int popupWidth = this.getPopupWidth();
    	int popupHeight = this.getPopupHeight();
    	int popupX = this.width / 2 - popupWidth / 2;
    	int popupY = this.height / 2 - popupHeight / 2;

    	this.drawPopupBackground(popupX, popupY, popupWidth, popupHeight);
    	
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, popupY + 10, 11184810);
        int i = popupY + 50;

        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
                i += this.fontRenderer.FONT_HEIGHT + 3;
            }
        }
    }
    
    @Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
	}
    
    protected void drawPostRender() {
    	if (this.viewScreen != null) {
        	this.viewScreen.drawPostRender();
        }
    }
    
    protected int getPopupWidth() {
    	return 250;
    }
    
    protected int getPopupHeight() {
    	return this.textHeight + 90;
    }
	
	protected void initButtons(int buttonY) {
		this.buttonList.add(new GuiButton(FIRST_BUTTON, this.width / 2 - 100, buttonY, firstButtonText));
	}
    
    protected void onFirstButtonClicked() {
    	this.mc.displayGuiScreen(this.parentScreen);
    }
    
    protected void drawPopupBackground(int popupX, int popupY, int popupWidth, int popupHeight) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        
    	drawRect(popupX - 1, popupY - 1, popupX + popupWidth + 1, popupY + popupHeight + 1, 0xFFA0A0A0);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(popupX,              popupY + popupHeight, 0).tex(0,                       (double)popupHeight / 32).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(popupX + popupWidth, popupY + popupHeight, 0).tex((double)popupWidth / 32, (double)popupHeight / 32).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(popupX + popupWidth, popupY,               0).tex((double)popupWidth / 32, 0                       ).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(popupX,              popupY,               0).tex(0,                       0                       ).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        
        this.drawHorizontalLine(popupX, popupX + popupWidth, popupY + 30, 0xFFA0A0A0);
    }
    
}
