package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButton;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.util.ResourceLocation;

/**
 * Displays a list of every type of addition in an addon (like items, creative tabs, etc.)
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiComponentAdditionTypeList implements IGuiViewComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private GuiView viewScreen;
	private final Addon addon;
	private int x;
	private int y;
	private int width;
	private List<GuiAdditionTypeButton> typeButtons = new ArrayList<>();
	
	public GuiComponentAdditionTypeList(GuiView viewScreen, Addon addon) {
		this.viewScreen = viewScreen;
		this.addon = addon;
		
		for (IAdditionTypeGuiFactory guiFactory : AdditionTypeManager.getAdditionTypeGuiFactories()) {
			this.addTypeButton(guiFactory.createButton(this.viewScreen, this.addon));
		}
	}
	
	public void addTypeButton(GuiAdditionTypeButton button) {
		this.typeButtons.add(button);
	}
	
	@Override
	public int getHeight(int left, int right) {
		int height = 0;
		for (GuiAdditionTypeButton button : this.typeButtons) {
			height += button.getButtonHeight();
		}
		return height;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;
		
		int currentHeight = this.y;
		
		for (int i = 0; i < this.typeButtons.size(); i++) {
			GuiAdditionTypeButton button = this.typeButtons.get(i);
			button.drawButton(x, currentHeight, right, mouseX, mouseY);
			
			currentHeight += button.getButtonHeight();
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiAdditionTypeButton button : this.typeButtons) {
			if (button.isHovering(mouseX, mouseY)) {
				button.onClick();
			}
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}

}
