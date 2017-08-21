package com.tmtravlr.additions.gui.edit.components;

import java.io.IOException;

import net.minecraft.util.text.ITextComponent;

import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.IGuiEditComponent;

public class GuiDisplayText implements IGuiEditComponent {
	
	private GuiEdit parent;
	private ITextComponent text;
	
	public GuiDisplayText(GuiEdit parent, ITextComponent text) {
		this.text = text;
		this.parent = parent;
	}

	@Override
	public int getHeight(int left, int right) {
		return parent.getFontRenderer().getWordWrappedHeight(text.getFormattedText(), right - left - 20) + 20;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		if (this.parent.getFontRenderer().getStringWidth(text.getFormattedText()) < right - x - 20) {
			this.parent.drawCenteredString(this.parent.getFontRenderer(), text.getFormattedText(), x + (right - x)/2, y + 10, 0xFFFFFF);
		} else {
			this.parent.getFontRenderer().drawSplitString(text.getFormattedText(), x + 10, y + 10, right - x - 20, 0xFFFFFF);
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
