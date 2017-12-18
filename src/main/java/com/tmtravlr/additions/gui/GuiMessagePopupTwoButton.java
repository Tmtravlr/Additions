package com.tmtravlr.additions.gui;

import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

/**
 * Adds a second button to the popup message.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class GuiMessagePopupTwoButton extends GuiMessagePopup {
	
	protected static final int BUTTON_CONTINUE = 1;
	
	protected String buttonContinueText;
	protected GuiScreen nextScreen;

    public GuiMessagePopupTwoButton(GuiScreen parentScreen, GuiScreen nextScreen, String title, ITextComponent message, String buttonClose, String buttonContinue) {
        super(parentScreen, title, message, buttonClose);
        this.buttonContinueText = buttonContinue;
        this.nextScreen = nextScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        GuiButton closeButton = buttonList.get(BUTTON_CANCEL);
        closeButton.width = 95;
        this.buttonList.add(new GuiButton(BUTTON_CONTINUE, this.width / 2 + 5, closeButton.y, closeButton.width, closeButton.height, buttonContinueText));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == BUTTON_CONTINUE) {
        	this.mc.displayGuiScreen(nextScreen);
        }
    }
}
