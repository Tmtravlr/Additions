package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;

import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Lets you edit an entity equipment entry, for armor sets
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiComponentEquipmentItemInput implements IGuiViewComponent {

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	
	private int x;
	private int y;
	private int width;
	private boolean hidden = false;
	private String label = "";
	private boolean required = false;
	private ItemStack equipment = ItemStack.EMPTY;
	private EntityEquipmentSlot slot = null;
	
	public GuiComponentEquipmentItemInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
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
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;

		int itemDisplayTop = this.y + 10;
		
		Gui.drawRect(this.x, itemDisplayTop - 1, this.x + 22, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 1, itemDisplayTop, this.x + 21, itemDisplayTop + 20, 0xFF000000);
		
		this.selectedText.x = x + 30;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}

		this.editScreen.renderItemStack(this.equipment, this.x + 3, itemDisplayTop + 2, mouseX, mouseY, true, true);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.clearEquipmentItem();
			}
		}
		
		if (mouseX >= this.selectedText.x && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditEquipmentItem(this.editScreen.mc.currentScreen, this));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setDefaultEquipmentStack(ItemStack equipment, EntityEquipmentSlot slot) {
		this.equipment = equipment;
		this.slot = slot;
		this.selectedText.setText(this.getDisplayText());
		this.selectedText.setCursorPositionZero();
	}
	
	public void setEquipmentStack(ItemStack equipment, EntityEquipmentSlot slot) {
		this.setDefaultEquipmentStack(equipment, slot);
		this.editScreen.notifyHasChanges();
	}

	public void clearEquipmentItem() {
		this.equipment = ItemStack.EMPTY;
		this.slot = null;
		this.selectedText.setText("");
		this.editScreen.notifyHasChanges();
	}
	
	public ItemStack getEquipmentStack() {
		return this.equipment;
	}
	
	public EntityEquipmentSlot getSlot() {
		return this.slot;
	}
	
	private String getDisplayText() {
		if (this.equipment.isEmpty() || this.slot == null) {
			return "";
		}
		
		return I18n.format("gui.popup.equipmentItem.display." + this.slot.getName(), this.equipment.getDisplayName());
	}
	
	protected class GuiMessageBoxEditEquipmentItem extends GuiMessageBoxTwoButton {
		
		private GuiScreen parentScreen;
		private GuiComponentEquipmentItemInput parent;
		
		private GuiComponentDropdownInput<EntityEquipmentSlot> slotInput;
		private GuiComponentItemStackInput equipmentInput;
		
		private ArrayList<IGuiViewComponent> components = new ArrayList<>();

		public GuiMessageBoxEditEquipmentItem(GuiScreen parentScreen, GuiComponentEquipmentItemInput parent) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.equipmentItem.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
			this.parentScreen = parentScreen;
			this.parent = parent;
			
			this.slotInput = new GuiComponentDropdownInput<EntityEquipmentSlot>(I18n.format("gui.popup.equipmentItem.slot.label"), this.parent.editScreen) {

				@Override
				public String getSelectionName(EntityEquipmentSlot selected) {
					return selected.getName();
				}
				
			};
			this.slotInput.setRequired();
			this.slotInput.disallowDelete();
			EntityEquipmentSlot[] slots = EntityEquipmentSlot.values();
			for (int i = slots.length - 1; i >= 0; i--) {
				this.slotInput.addSelection(slots[i]);
			}
			if (parent.slot != null) {
				this.slotInput.setDefaultSelected(parent.slot);
			}
			
			this.equipmentInput = new GuiComponentItemStackInput(I18n.format("gui.popup.equipmentItem.item.label"), this.parent.editScreen);
			this.equipmentInput.enableAnyDamage();
			this.equipmentInput.disableCount();
			this.equipmentInput.setOtherParentScreen(this);
			if (parent.equipment != null) {
				this.equipmentInput.setDefaultItemStack(parent.equipment);
			}
			
			this.components.add(this.slotInput);
			this.components.add(this.equipmentInput);
		}

		@Override
	    protected void actionPerformed(GuiButton button) throws IOException {
	        if (button.id == SECOND_BUTTON) {
	        	if (this.equipmentInput.getItemStack().isEmpty()) {
	        		this.parent.clearEquipmentItem();
	        	} else {
	        		if (this.slotInput.getSelected() == null) {
	        			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.equipmentItem.problem.slotRequired.title"), new TextComponentTranslation("gui.popup.equipmentItem.problem.slotRequired.message"), I18n.format("gui.buttons.back")));
	        			return;
	        		}
					
	        		this.parent.setEquipmentStack(this.equipmentInput.getItemStack(), this.slotInput.getSelected());
	        	}
	        	this.mc.displayGuiScreen(parentScreen);
	        } else {
	        	super.actionPerformed(button);
	        }
	    }
	    
		@Override
	    protected int getPopupWidth() {
	    	return 350;
	    }
		
	    @Override
	    protected int getPopupHeight() {
	    	return this.getComponentsHeight() + 60;
	    }

	    @Override
	    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
			super.drawScreenOverlay(mouseX, mouseY, partialTicks);
			
			int popupWidth = this.getPopupWidth();
	    	int popupHeight = this.getPopupHeight();
	    	int popupX = this.width / 2 - popupWidth / 2;
	    	int popupY = this.height / 2 - popupHeight / 2;
	    	int popupRight = popupX + popupWidth;
			
			int componentY = popupY + 30;
			int labelOffset = 80;

			for (IGuiViewComponent component : this.components) {
				this.drawString(this.fontRenderer, component.getLabel(), popupX + 10, componentY + component.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				if (component.isRequired()) {
					this.drawString(this.fontRenderer, "*", popupX + labelOffset + 2, componentY + component.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				}
				component.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
				componentY += component.getHeight(popupX, popupRight);
			}
			
			this.drawPostRender();
	    }
	    
	    @Override
	    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    	
	    	for (IGuiViewComponent component : this.components) {
	    		component.onMouseClicked(mouseX, mouseY, mouseButton);
	    	}
	    	
	    	super.mouseClicked(mouseX, mouseY, mouseButton);
	    }

	    @Override
	    public void keyTyped(char keyTyped, int keyCode) throws IOException {

	    	for (IGuiViewComponent component : this.components) {
	    		component.onKeyTyped(keyTyped, keyCode);
	    	}
	    	
	    	if (keyCode != 1) {
	    		super.keyTyped(keyTyped, keyCode);
	    	}
	    }
	    
	    private int getComponentsHeight() {
	    	int popupWidth = this.getPopupWidth();
	    	int popupLeft = this.width / 2 - popupWidth / 2;
	    	int popupRight = popupLeft + popupWidth;
	    	int height = 0;
	    	
	    	for (IGuiViewComponent component : this.components) {
	    		height += component.getHeight(popupLeft, popupRight);
	    	}
	    	
	    	return height;
	    }
		
	}

}
