package com.tmtravlr.additions.gui.message;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;

/**
 * Shows a popup message with three buttons.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class GuiMessageBoxThreeButton extends GuiMessageBoxTwoButton {

	protected static final int THIRD_BUTTON = 2;

	protected String thirdButtonText;

	public GuiMessageBoxThreeButton(GuiScreen parentScreen, GuiScreen nextScreen, String title, ITextComponent message, String firstButtonText, String secondButtonText, String thirdButtonText) {
		super(parentScreen, nextScreen, title, message, firstButtonText, secondButtonText);

		this.thirdButtonText = thirdButtonText;
	}

    @Override
    public void initButtons(int buttonY) {
        this.buttonList.add(new GuiButton(FIRST_BUTTON, this.width / 2 - 150, buttonY, 90, 20, firstButtonText));
        this.buttonList.add(new GuiButton(SECOND_BUTTON, this.width / 2 - 45, buttonY, 90, 20, secondButtonText));
        this.buttonList.add(new GuiButton(THIRD_BUTTON, this.width / 2 + 60, buttonY, 90, 20, thirdButtonText));
    }

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == THIRD_BUTTON) {
			this.onThirdButtonClicked();
		}
	}

	@Override
	protected int getPopupWidth() {
		return 350;
	}
	
	protected void initThirdButton(int buttonY) {
		this.buttonList.add(new GuiButton(THIRD_BUTTON, this.width / 2 + 60, buttonY, 90, 20, thirdButtonText));
	}
	
	protected void onThirdButtonClicked() {
		this.mc.displayGuiScreen(this.nextScreen);
	}

}
