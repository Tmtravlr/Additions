package com.tmtravlr.additions.gui;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

/**
 * Shows a simple notification-type popup message.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class GuiMessagePopup extends GuiScreen {
	
	protected static final int BUTTON_CANCEL = 0;

	public final String title;
	public final ITextComponent message;
    public List<String> multilineMessage;
    public final GuiScreen parentScreen;
    public int textHeight;
    public String buttonText;
    public int backgroundFade = 30;

    public GuiMessagePopup(GuiScreen parentScreen, String title, ITextComponent message, String buttonText) {
        this.parentScreen = parentScreen;
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {  
        int popupWidth = this.getPopupWidth();      
        
        this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), popupWidth - 50);
        this.textHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;
        
    	int popupHeight = this.getPopupHeight();
    	int popupY = this.height / 2 - popupHeight / 2;
        
        this.buttonList.add(new GuiButton(BUTTON_CANCEL, this.width / 2 - 100, popupY + popupHeight - 30, buttonText));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BUTTON_CANCEL) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
    	int popupWidth = this.getPopupWidth();
    	int popupHeight = this.getPopupHeight();
    	int popupX = this.width / 2 - popupWidth / 2;
    	int popupY = this.height / 2 - popupHeight / 2;

    	this.drawPopupBackground(popupX, popupY, popupWidth, popupHeight);
    	
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, popupY + 10, 11184810);
        int i = Math.max(this.height / 2 - this.textHeight / 2, 40);

        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
                i += this.fontRenderer.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
	}
    
    public void openFolder(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object)null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        } catch (Throwable throwable1) {
            Throwable throwable = throwable1.getCause();
            AdditionsMod.logger.error("Couldn't open link: {}", (Object)(throwable == null ? "<UNKNOWN>" : throwable.getMessage()));
        }
    }
    
    protected int getPopupWidth() {
    	return 250;
    }
    
    protected int getPopupHeight() {
    	return this.textHeight + 90;
    }
    
    private void drawPopupBackground(int popupX, int popupY, int popupWidth, int popupHeight) {
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
