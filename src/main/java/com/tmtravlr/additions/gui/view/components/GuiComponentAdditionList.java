package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCard;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.util.ResourceLocation;

/**
 * Displays a list of cards showing info about additions in an addon (like items or creative tabs).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiComponentAdditionList implements IGuiViewComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private GuiView viewScreen;
	private final Addon addon;
	private int x;
	private int y;
	private int width;
	private String lastFilter = "";
	private List<GuiAdditionCard> additionCards = new ArrayList<>();
	private List<GuiAdditionCard> filteredAdditionCards = new ArrayList<>();
	
	public GuiComponentAdditionList(GuiView viewScreen, Addon addon) {
		this.viewScreen = viewScreen;
		this.addon = addon;
	}
	
	public void addAdditionCard(GuiAdditionCard card) {
		this.additionCards.add(card);
		if (card.filterApplies(lastFilter)) {
			this.filteredAdditionCards.add(card);
		}
	}
	
	@Override
	public int getHeight(int left, int right) {
		int height = 0;
		for (GuiAdditionCard card : this.filteredAdditionCards) {
			height += card.getCardHeight();
		}
		return height;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;
		
		int currentHeight = this.y;
		
		for (int i = 0; i < this.filteredAdditionCards.size(); i++) {
			GuiAdditionCard card = this.filteredAdditionCards.get(i);
			card.drawCard(x, currentHeight, right, mouseX, mouseY);
			
			currentHeight += card.getCardHeight();
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiAdditionCard card : this.filteredAdditionCards) {
			card.onClick(mouseX, mouseY);
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void filter(String newFilter) {
		this.lastFilter = newFilter;
		this.filteredAdditionCards.clear();
		
		for (GuiAdditionCard card : this.additionCards) {
			if (card.filterApplies(newFilter)) {
				this.filteredAdditionCards.add(card);
			}
		}
	}
}
