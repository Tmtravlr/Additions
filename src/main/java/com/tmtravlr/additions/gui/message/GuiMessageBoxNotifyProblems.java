package com.tmtravlr.additions.gui.message;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiMessageBoxNotifyProblems extends GuiMessageBoxTwoButton {

	public GuiMessageBoxNotifyProblems(GuiScreen parentScreen, String title, ITextComponent message) {
		super(parentScreen, parentScreen, title, message, I18n.format("gui.warnDialogue.restart.close"), I18n.format("gui.warnDialogue.restart.continue"));
	}
	
	@Override
	protected void onSecondButtonClicked() {
        this.mc.shutdown();
    }
	
}
