package com.tmtravlr.additions.gui;

import net.minecraft.client.gui.GuiScreen;

public abstract class GuiEditObject<T> extends GuiScreen {
	
	protected GuiScreen parentScreen;
	protected T toDisplay;
	protected boolean hasUnsavedChanges = false;
	
	public GuiEditObject(GuiScreen parentScreen, T toDisplay) {
		this.parentScreen = parentScreen;
		this.toDisplay = toDisplay;
	}
	
}
