package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;

import com.tmtravlr.additions.gui.view.GuiView;

import net.minecraft.util.text.ITextComponent;

/**
 * Displays text in a view screen
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiComponentDisplayText implements IGuiViewComponent {
	
	private GuiView viewScreen;
	private ITextComponent text;
	
	public GuiComponentDisplayText(GuiView viewScreen, ITextComponent text) {
		this.text = text;
		this.viewScreen = viewScreen;
	}
	
	public void setDisplayText(ITextComponent text) {
		this.text = text;
	}

	@Override
	public int getHeight(int left, int right) {
		return viewScreen.getFontRenderer().getWordWrappedHeight(text.getFormattedText(), right - left - 20) + 20;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		if (this.viewScreen.getFontRenderer().getStringWidth(text.getFormattedText()) < right - x - 20) {
			this.viewScreen.drawCenteredString(this.viewScreen.getFontRenderer(), text.getFormattedText(), x + (right - x)/2, y + 10, 0xFFFFFF);
		} else {
			this.viewScreen.getFontRenderer().drawSplitString(text.getFormattedText(), x + 10, y + 10, right - x - 20, 0xFFFFFF);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}

}
