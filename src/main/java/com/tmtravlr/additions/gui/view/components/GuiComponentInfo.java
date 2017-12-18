package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;

public abstract class GuiComponentInfo implements IGuiViewComponent {

	private static final int INFO_HEIGHT = 20;
	
	private GuiView viewScreen;
	private Addon addon;
	
	int x;
	int y;
	int width;
	
	@Override
	public int getHeight(int left, int right) {
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public abstract String getName();
	
	public abstract String getType();
	
	public abstract String getExtraInfo();

}
