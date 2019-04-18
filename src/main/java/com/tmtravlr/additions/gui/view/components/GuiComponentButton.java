package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;

import com.tmtravlr.additions.gui.view.GuiView;

import net.minecraft.client.gui.GuiButton;

public class GuiComponentButton extends GuiButton implements IGuiViewComponent {
	
	private GuiView viewScreen;
	
	private boolean hidden = false;
	
	public GuiComponentButton(GuiView viewScreen, int buttonId, String buttonText) {
        super(buttonId, 0, 0, 0, 20, buttonText);
        this.viewScreen = viewScreen;
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
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.y = y + 10;
		if (right - x < 220) {
			this.x = x + 10;
			this.width = right - x - 20;
		} else {
			this.x = x + (right - x) / 2 - 100;
			this.width = 200;
		}
		
		this.drawButton(this.viewScreen.mc, mouseX, mouseY, 0);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (this.mousePressed(this.viewScreen.mc, mouseX, mouseY)) {
			this.playPressSound(this.viewScreen.mc.getSoundHandler());
			this.viewScreen.buttonClicked(this);
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}

}
