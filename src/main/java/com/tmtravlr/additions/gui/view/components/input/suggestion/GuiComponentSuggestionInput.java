package com.tmtravlr.additions.gui.view.components.input.suggestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.GuiScreenOverlay;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.GuiScrollingList;

/**
 * Input field with a dropdown list displaying suggestions.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class GuiComponentSuggestionInput extends GuiTextField implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiScrollingSuggestion dropdown;
	protected GuiScreenSuggestion dropdownScreen;
	protected List<String> suggestions = new ArrayList<>();
	
	private String label = "";
	private List<String> info = new ArrayList<>();
	private boolean required = false;
	private boolean hidden = false;
	
	public GuiComponentSuggestionInput(String label, GuiEdit editScreen) {
		super(0, editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.setMaxStringLength(1024);
		this.editScreen = editScreen;
		this.label = label;
	}
	
	public void setSuggestions(Collection<String> suggestions) {
		this.suggestions = new ArrayList<>(suggestions);
	}
	
	public void addSuggestion(String suggestion) {
		this.suggestions.add(suggestion);
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultText(String text) {
		this.setText(text);
		this.setCursorPositionZero();
	}
	
	public void setInfo(ITextComponent info) {
		if (info == null) {
			this.info.clear();
		} else {
			this.info = this.editScreen.getFontRenderer().listFormattedStringToWidth(info.getFormattedText(), 200);
		}
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
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
	}
	
	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		
		this.x = x;
		this.y = y + 10;
		this.width = right - 60 - x;
		
		this.drawTextBox();
		
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
		
		// Add info icon
		if (!this.info.isEmpty()) {
			int infoX = this.x + this.width + 4;
			int infoY = this.y + (this.height / 2 - 6);

			this.editScreen.drawTexturedModalRect(infoX, infoY, 21, 64, 13, 13);
			
			if (mouseX > infoX && mouseX < infoX + 13 && mouseY > infoY && mouseY < infoY + 13) {
				this.editScreen.renderInfoTooltip(info, mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void onKeyTyped(char keyTyped, int keyCode) {
		this.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (this.dropdownScreen == null && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height) {
			this.createDropdown();
			this.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}
	
	protected void createDropdown() {
		int dropdownHeight = Math.max(22, Math.min(this.suggestions.size() * 20, 20 * 6));
		int dropdownWidth = this.width;
		
		boolean above = this.editScreen.height - 40 - (this.y + this.height) < dropdownHeight;
		
		int dropdownScreenX = this.x;
		int dropdownScreenY = above ? this.y - dropdownHeight : this.y;
		
		this.dropdown = this.createScrollingSuggestion(dropdownHeight, above);
		
		this.dropdownScreen = new GuiScreenSuggestion(this, this.editScreen.mc.currentScreen, dropdownScreenX, dropdownScreenY, dropdownWidth, dropdownHeight + this.height);
		this.editScreen.mc.displayGuiScreen(this.dropdownScreen);
	}
	
	protected GuiScrollingSuggestion createScrollingSuggestion(int dropdownHeight, boolean above) {
		return new GuiScrollingSuggestion(this, this.suggestions, dropdownHeight, above);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		
		if (this.dropdown != null) {
			this.dropdown.updateFilter();
		}
	}
	
	@Override
    public void setResponderEntryValue(int idIn, String textIn) {
    	super.setResponderEntryValue(idIn, textIn);
    	
    	this.editScreen.notifyHasChanges();
		
		if (this.dropdown != null) {
			this.dropdown.updateFilter();
		}
    }
	
	
	/**
	 * Screen for the suggestions. Has the input field and a scrolling list of suggestions.
	 * 
	 * @author Tmtravlr (Rebeca Rey)
	 * @date November 2017
	 */
	protected class GuiScreenSuggestion extends GuiScreenOverlay {
		
		private GuiComponentSuggestionInput parentInput;
		private int x;
		private int y;
		private int dropdownWidth;
		private int dropdownHeight;
		private boolean hasInitialized;
		private boolean closeOnMouseUp = false;
		
		public GuiScreenSuggestion(GuiComponentSuggestionInput parentInput, GuiScreen parentScreen, int x, int y, int width, int height) {
			super(parentScreen);
			this.parentInput = parentInput;
			this.x = x;
			this.y = y;
			this.dropdownWidth = width;
			this.dropdownHeight = height;
			
			this.parentInput.dropdown.updateFilter();
		}
		
		@Override
		public void initGui() {
			if (this.hasInitialized) { 
				this.removeDropdown();
			} else {
				this.hasInitialized = true;
			}
		}
		
		@Override
		public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
			if (!Mouse.isButtonDown(0) && this.closeOnMouseUp) {
				this.removeDropdown();
				return;
			}
			
			this.parentInput.dropdown.drawScreen(mouseX, mouseY, 0);
			this.parentInput.drawTextBox();
		}
		
		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
				this.parentInput.mouseClicked(mouseX, mouseY, mouseButton);
			} else {
				this.removeDropdown();
				this.parentInput.editScreen.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		
		@Override
		public void keyTyped(char keyTyped, int keyCode) throws IOException {
			this.parentInput.textboxKeyTyped(keyTyped, keyCode);
		}
		
		@Override
	    public void handleMouseInput() throws IOException {
			super.handleMouseInput();
		
	        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
	        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
	        int scroll = Mouse.getEventDWheel();
	        
	        if (scroll != 0) {
		        if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
		        	this.parentInput.dropdown.handleMouseInput(mouseX, mouseY);
				} else {
					this.removeDropdown();
					this.parentInput.editScreen.handleMouseInput();
				}
	        }
		}
	
		public void removeDropdown() {
			this.parentInput.editScreen.mc.displayGuiScreen(this.parentScreen);
			this.parentInput.dropdown = null;
			this.parentInput.setFocused(false);
			this.parentInput.dropdownScreen = null;
		}
		
		public void closeOnMouseUp() {
			this.closeOnMouseUp = true;
		}
	}
	
	
	/**
	 * Shows a scrolling list, displaying suggestions for the given type of object.
	 * 
	 * @author Tmtravlr (Rebeca Rey)
	 * @date November 2017
	 */
	protected class GuiScrollingSuggestion extends GuiScrollingList {
	
		public GuiComponentSuggestionInput parentInput;
		public List<String> suggestions;
		public List<String> suggestionDisplay;
		public String newFilter;
		
		public GuiScrollingSuggestion(GuiComponentSuggestionInput parentInput, List<String> suggestions, int height, boolean above) {
			super(parentInput.editScreen.mc, parentInput.width, height,
					above ? parentInput.y - height : parentInput.y + parentInput.height, 
					above ? parentInput.y : parentInput.y + parentInput.height + height, 
					parentInput.x, 18, parentInput.editScreen.width, parentInput.editScreen.height);
			
			this.parentInput = parentInput;
			this.suggestions = suggestions;
			this.suggestionDisplay = this.suggestions;
		}
	
		@Override
		protected int getSize() {
			return Math.max(1, this.suggestionDisplay.size());
		}
	
		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			if (!this.suggestionDisplay.isEmpty()) {
				this.parentInput.setText(this.suggestionDisplay.get(index));
				this.parentInput.editScreen.notifyHasChanges();
				this.parentInput.dropdownScreen.closeOnMouseUp();
			}
		}
	
		@Override
		protected boolean isSelected(int index) {
			return false;
		}
	
		@Override
		protected void drawBackground() {}
	
		@Override
		protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
			if (!this.suggestionDisplay.isEmpty()) {
				String toDisplay = this.suggestionDisplay.get(slot);
				if (toDisplay != null) {
					this.parentInput.editScreen.getFontRenderer().drawString(toDisplay, this.left + 5, top + 2, 0xFFFFFF);
				}
			} else {
				this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.suggestion.noSuggestions"), this.left + 5, top + 2, 0x888888);
			}
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {

			CommonGuiUtils.drawOutline(this.left - 1, this.top - 1, this.listWidth + 2, this.listHeight + 2, 0xFFA0A0A0);
			
			if (this.newFilter != null) {
				this.suggestionDisplay = suggestions.stream().filter(s -> s == null ? false : s.toLowerCase().contains(this.newFilter.toLowerCase())).collect(Collectors.toList());
				this.newFilter = null;
			}
			
			try {
				super.drawScreen(mouseX, mouseY, partialTicks);
			} catch (IllegalStateException e) {
				AdditionsMod.logger.error("Failed to draw scrolling list", e);
			}
		}
		
		public boolean isHovering() {
			return this.mouseX >= this.left && this.mouseX <= this.left + this.listWidth && this.mouseY >= this.top && this.mouseY <= this.bottom;
		}
		
		public void updateFilter() {
			this.newFilter = this.parentInput.getText();
		}
	}
}
