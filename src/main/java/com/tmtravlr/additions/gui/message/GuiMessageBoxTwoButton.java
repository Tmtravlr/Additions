package com.tmtravlr.additions.gui.message;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;

/**
 * Adds a second button to the popup message.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017 
 */
public class GuiMessageBoxTwoButton extends GuiMessageBox {
	
	protected static final int SECOND_BUTTON = 1;
	
	protected GuiScreen nextScreen;
	protected String secondButtonText;

    public GuiMessageBoxTwoButton(GuiScreen parentScreen, GuiScreen nextScreen, String title, ITextComponent message, String firstButtonText, String secondButtonText) {
        super(parentScreen, title, message, firstButtonText);
        this.secondButtonText = secondButtonText;
        this.nextScreen = nextScreen;
    }

    @Override
    public void initButtons(int buttonY) {
        this.buttonList.add(new GuiButton(FIRST_BUTTON, this.width / 2 - 100, buttonY, 95, 20, firstButtonText));
        this.buttonList.add(new GuiButton(SECOND_BUTTON, this.width / 2 + 5, buttonY, 95, 20, secondButtonText));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == SECOND_BUTTON) {
        	this.onSecondButtonClicked();
        }
    }
    
    protected void onSecondButtonClicked() {
    	this.mc.displayGuiScreen(nextScreen);
    }
}
