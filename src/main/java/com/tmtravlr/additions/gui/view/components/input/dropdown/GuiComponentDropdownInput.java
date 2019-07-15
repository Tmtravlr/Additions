package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.GuiScreenOverlay;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;

/**
 * Dropdown list with a text field displaying the selection.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentDropdownInput<T> extends Gui implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	protected GuiTextField filter;
	protected GuiScrollingDropdown dropdown;
	protected GuiScreenDropdown dropdownScreen;
	protected List<T> selections = new ArrayList<>();
	
	private String label = "";
	private boolean required = false;
	private boolean hidden = false;
	private boolean allowDelete = true;
	private T selected;
	
	public GuiComponentDropdownInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
		this.filter = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 19);
		this.filter.setMaxStringLength(1024);
	}
	
	public void setSelections(Collection<T> selections) {
		this.selections = new ArrayList<>(selections);
	}
	
	public void setSelections(T ...selections) {
		this.selections = Arrays.asList(selections);
	}
	
	public void addSelection(T selection) {
		this.selections.add(selection);
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void disallowDelete() {
		this.allowDelete = false;
	}
	
	public void setDefaultSelected(T selected) {
		this.selected = selected;
		if (this.selected == null) {
			this.selectedText.setText("");
		} else {
			this.selectedText.setText(this.getSelectionName(selected));
		}
		this.selectedText.setCursorPositionZero();
	}
	
	public void setSelected(T selected) {
		this.setDefaultSelected(selected);
		this.selectedText.drawTextBox();
		this.editScreen.notifyHasChanges();
	}
	
	public String getSelectionName(T selected) {
		return selected == null ? "" : selected.toString();
	}
	
	public T getSelected() {
		return this.selected;
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
		
		this.selectedText.x = x;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - x;
		
		this.selectedText.drawTextBox();
		
		if (this.allowDelete && !this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}
	
	@Override
	public void onKeyTyped(char keyTyped, int keyCode) {
		this.selectedText.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (this.allowDelete && !this.selectedText.getText().isEmpty()) {
			
			if (mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.setSelected(null);
			}
		}
		
		if (mouseX >= this.selectedText.x && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.createDropdown();
			this.filter.setFocused(true);
		}
	}
	
	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}
	
	protected void createDropdown() {
		int dropdownHeight = Math.max(22, Math.min(this.selections.size() * 17, 17 * 6));
		int dropdownWidth = this.selectedText.width;
		
		boolean above = this.editScreen.height - 40 - (this.selectedText.y + this.selectedText.height) < dropdownHeight;
		
		int dropdownX = this.selectedText.x;
		int dropdownY = above ? this.selectedText.y - this.filter.height - dropdownHeight : this.selectedText.y + this.selectedText.height;
		int filterY = above ? this.selectedText.y - this.filter.height : this.selectedText.y + this.selectedText.height;
		
		this.dropdown = this.createScrollingDropdown(dropdownHeight, above);
		
		this.filter.x = dropdownX;
		this.filter.y = filterY;
		this.filter.width = dropdownWidth;
		
		this.dropdownScreen = new GuiScreenDropdown(this, this.editScreen.mc.currentScreen, dropdownX, dropdownY, dropdownWidth, dropdownHeight + this.filter.height);
		this.editScreen.mc.displayGuiScreen(this.dropdownScreen);
	}
	
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above);
	}
	
	/**
	 * Screen for the dropdown list input. Has a search bar, and a scrolling list of whatever the dropdown displays.
	 * 
	 * @author Tmtravlr (Rebeca Rey)
	 * @since August 2017 
	 */
	class GuiScreenDropdown extends GuiScreenOverlay {
		
		private GuiComponentDropdownInput<T> parentInput;
		private int x;
		private int y;
		private int dropdownWidth;
		private int dropdownHeight;
		private boolean hasInitialized;
		private boolean closeOnMouseUp = false;
		
		public GuiScreenDropdown(GuiComponentDropdownInput<T> parentInput, GuiScreen parentScreen, int x, int y, int width, int height) {
			super(parentScreen);
			this.parentInput = parentInput;
			this.x = x;
			this.y = y;
			this.dropdownWidth = width;
			this.dropdownHeight = height;
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
			this.parentInput.filter.drawTextBox();
	
			this.parentInput.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int searchX = this.parentInput.filter.x + this.parentInput.filter.width - 16;
			int searchY = this.parentInput.filter.y + (this.parentInput.filter.height / 2 - 6);
			this.parentInput.drawTexturedModalRect(searchX, searchY, 47, 64, 13, 13);
		}
		
		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
				this.parentInput.filter.mouseClicked(mouseX, mouseY, mouseButton);
			} else {
				this.removeDropdown();
				this.parentInput.editScreen.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		
		@Override
		public void keyTyped(char keyTyped, int keyCode) throws IOException {
			this.parentInput.filter.textboxKeyTyped(keyTyped, keyCode);
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
			this.parentInput.filter.setFocused(false);
			this.parentInput.dropdownScreen = null;
		}
		
		public void closeOnMouseUp() {
			this.closeOnMouseUp = true;
		}
	}
	
	
	/**
	 * Shows a scrolling list, displaying the given type of object.
	 * 
	 * @author Tmtravlr (Rebeca Rey)
	 * @since August 2017 
	 */
	class GuiScrollingDropdown extends GuiScrollingList {
	
		public GuiComponentDropdownInput parentInput;
		public List<T> selections;
		public List<T> selectionDisplay;
		public String prevFilter = "";
		
		public GuiScrollingDropdown(GuiComponentDropdownInput parentInput, List<T> selections, int height, boolean above) {
			super(parentInput.editScreen.mc, parentInput.selectedText.width, height,
					above ? parentInput.selectedText.y - height - 20 : parentInput.selectedText.y + parentInput.selectedText.height + 20, 
					above ? parentInput.selectedText.y - 20 : parentInput.selectedText.y + parentInput.selectedText.height + height + 20, 
					parentInput.selectedText.x, 18, parentInput.editScreen.width, parentInput.editScreen.height);
			
			this.parentInput = parentInput;
			this.selections = selections;
			this.selectionDisplay = this.selections;
		}
	
		@Override
		protected int getSize() {
			return Math.max(1, this.selectionDisplay.size());
		}
	
		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			if (!this.selectionDisplay.isEmpty()) {
				this.parentInput.setSelected(this.selectionDisplay.get(index));
				this.parentInput.dropdownScreen.closeOnMouseUp();
			}
		}
	
		@Override
		protected boolean isSelected(int index) {
			if (!this.selectionDisplay.isEmpty()) {
				return this.selectionDisplay.get(index).equals(this.parentInput.getSelected());
			}
			return false;
		}
	
		@Override
		protected void drawBackground() {}
	
		@Override
		protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
			if (!this.selectionDisplay.isEmpty()) {
				T toDisplay = this.selectionDisplay.get(slot);
				if (toDisplay != null) {
					this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 5, top + 2, 0xFFFFFF);
				}
			} else {
				this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
			}
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			
			Gui.drawRect(this.left - 1, this.top - 1, this.left + this.listWidth + 1, this.top + this.listHeight + 1, 0xFFA0A0A0);
			
			
			if (!this.parentInput.filter.getText().equals(prevFilter)) {
				String filter = this.parentInput.filter.getText();
				this.selectionDisplay = selections.stream().filter(s -> s == null ? false : this.parentInput.getSelectionName(s).toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
				this.prevFilter = filter;
			}
			
			
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
}
