package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;
import java.util.List;

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
	private boolean ignoreLabel = false;
	private boolean centered = false;
	private boolean hidden = false;
	
	public GuiComponentDisplayText(GuiView viewScreen, ITextComponent text) {
		this.text = text;
		this.viewScreen = viewScreen;
	}
	
	public void setDisplayText(ITextComponent text) {
		this.text = text;
	}
	
	public void setIgnoreLabel(boolean ignoreLabel) {
		this.ignoreLabel = ignoreLabel;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public int getHeight(int left, int right) {
		int lines = this.viewScreen.getFontRenderer().listFormattedStringToWidth(text.getFormattedText(), right - left - 20 - (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET)).size();
		return lines * this.viewScreen.getFontRenderer().FONT_HEIGHT + (lines - 1) * 5 + 20;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		int width = right - x - 20 - (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET);
		int textY = y + 10;
		List<String> linesToRender = this.viewScreen.getFontRenderer().listFormattedStringToWidth(text.getFormattedText(), width);
		
		for (String toRender : linesToRender) {
			if (this.centered) { 
				this.viewScreen.drawCenteredString(this.viewScreen.getFontRenderer(), toRender, x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET) + width/2, textY, 0xFFFFFF);
			} else {
				this.viewScreen.drawString(this.viewScreen.getFontRenderer(), toRender, x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET), textY, 0xFFFFFF);
			}
			textY += viewScreen.getFontRenderer().FONT_HEIGHT + 5;
		}
//		if (this.viewScreen.getFontRenderer().getStringWidth(text.getFormattedText()) < right - x - 20 - GuiView.LABEL_OFFSET) {
//			this.viewScreen.drawString(this.viewScreen.getFontRenderer(), text.getFormattedText(), x + 10 + GuiView.LABEL_OFFSET, y + 10, 0xFFFFFF);
//		} else {
//			this.viewScreen.getFontRenderer().drawSplitString(text.getFormattedText(), x + 10 + GuiView.LABEL_OFFSET, y + 10, right - x - 20 - GuiView.LABEL_OFFSET, 0xFFFFFF);
//		}
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
