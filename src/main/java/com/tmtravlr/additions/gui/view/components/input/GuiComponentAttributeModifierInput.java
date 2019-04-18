package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.attribute.AttributeTypeManager;
import com.tmtravlr.additions.util.OtherSerializers;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class GuiComponentAttributeModifierInput implements IGuiViewComponent {

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	
	private int x;
	private int y;
	private int width;
	private boolean hidden = false;
	private String label = "";
	private boolean required = false;
	private AttributeModifier modifier = null;
	private EntityEquipmentSlot slot = null;
	private boolean hasMeta = true;
	private boolean hasCount = true;
	private boolean hasNBT = true;
	
	public GuiComponentAttributeModifierInput(String label, GuiEdit editScreen) {
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
		
		this.selectedText.x = x;
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
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.clearAttributeModifier();
			}
		}
		
		if (mouseX >= this.selectedText.x && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditAttributeModifier(this.editScreen.mc.currentScreen, this));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setDefaultAttributeModifier(AttributeModifier modifier, EntityEquipmentSlot slot) {
		this.modifier = modifier;
		this.slot = slot;
		this.selectedText.setText(this.getDisplayText());
		this.selectedText.setCursorPositionZero();
	}

	public void clearAttributeModifier() {
		this.modifier = null;
		this.slot = null;
		this.selectedText.setText("");
		this.editScreen.notifyHasChanges();
	}
	
	public AttributeModifier getAttributeModifier() {
		return this.modifier;
	}
	
	public EntityEquipmentSlot getSlot() {
		return this.slot;
	}
	
	private String getDisplayText() {
		if (this.modifier == null) {
			return "";
		}
		
		String display = I18n.format("item.modifiers." + (this.slot == null ? "any" : this.slot.getName())) + " ";

		double amount = this.modifier.getAmount();
        double displayAmount;

        if (this.modifier.getOperation() != 1 && this.modifier.getOperation() != 2) {
        	displayAmount = amount;
        } else {
        	displayAmount = amount * 100.0D;
        }
		
		if (amount > 0.0D) {
            display += TextFormatting.BLUE + I18n.format("attribute.modifier.plus." + this.modifier.getOperation(), ItemStack.DECIMALFORMAT.format(displayAmount), I18n.format("attribute.name." + this.modifier.getName()));
        } else if (amount < 0.0D) {
        	displayAmount = displayAmount * -1.0D;
            display += TextFormatting.RED + I18n.format("attribute.modifier.take." + this.modifier.getOperation(), ItemStack.DECIMALFORMAT.format(displayAmount), I18n.format("attribute.name." + this.modifier.getName()));
        } else {
        	display += I18n.format("attribute.modifier.noEffect");
        }
		
		return display;
	}
	
	protected class GuiMessageBoxEditAttributeModifier extends GuiMessageBoxTwoButton {
		
		private GuiScreen parentScreen;
		private GuiComponentAttributeModifierInput parent;
		
		private GuiComponentSuggestionInput attributeNameInput;
		private GuiComponentFloatInput amountInput;
		private GuiComponentIntegerInput operationInput;
		private GuiComponentDropdownInput<EntityEquipmentSlot> slotInput;
		private GuiComponentStringInput uuidInput;
		
		private ArrayList<IGuiViewComponent> components = new ArrayList<>();

		public GuiMessageBoxEditAttributeModifier(GuiScreen parentScreen, GuiComponentAttributeModifierInput parent) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.attributeModifier.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
			this.parentScreen = parentScreen;
			this.parent = parent;
			
			this.attributeNameInput = new GuiComponentSuggestionInput(I18n.format("gui.popup.attributeModifier.attributeName.label"), this.parent.editScreen);
			this.attributeNameInput.setSuggestions(AttributeTypeManager.knownAttributes.stream().map(attribute -> attribute.getName()).collect(Collectors.toList()));
			if (parent.modifier != null) {
				this.attributeNameInput.setDefaultText(parent.modifier.getName());
			}
			
			this.amountInput = new GuiComponentFloatInput(I18n.format("gui.popup.attributeModifier.amount.label"), this.parent.editScreen, true);
			if (parent.modifier != null) {
				this.amountInput.setDefaultFloat((float) parent.modifier.getAmount());
			} else {
				this.amountInput.setDefaultFloat(0f);
			}
			
			this.operationInput = new GuiComponentIntegerInput(I18n.format("gui.popup.attributeModifier.operation.label"), this.parent.editScreen, false);
			this.operationInput.setInfo(new TextComponentTranslation("gui.popup.attributeModifier.operation.info", new TextComponentString("https://minecraft.gamepedia.com/Attribute#Operations").setStyle(new Style().setColor(TextFormatting.AQUA).setUnderlined(true))));
			this.operationInput.setMaximum(2);
			if (parent.modifier != null) {
				this.operationInput.setDefaultInteger(parent.modifier.getOperation());
			} else {
				this.operationInput.setDefaultInteger(0);
			}
			
			this.slotInput = new GuiComponentDropdownInput<EntityEquipmentSlot>(I18n.format("gui.popup.attributeModifier.slot.label"), this.parent.editScreen) {

				@Override
				public String getSelectionName(EntityEquipmentSlot selected) {
					return selected.getName();
				}
				
			};
			this.slotInput.setRequired();
			for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
				this.slotInput.addSelection(slot);
			}
			if (parent.modifier != null) {
				this.slotInput.setDefaultSelected(parent.slot);
			}
			
			this.uuidInput = new GuiComponentStringInput(I18n.format("gui.popup.attributeModifier.uuid.label"), this.parent.editScreen);
			this.uuidInput.setInfo(new TextComponentTranslation("gui.popup.attributeModifier.uuid.info"));
			this.uuidInput.setMaxStringLength(36);
			if (parent.modifier != null) {
				this.uuidInput.setDefaultText(parent.modifier.getID().toString());
			}
			
			this.components.add(this.attributeNameInput);
			this.components.add(this.amountInput);
			this.components.add(this.operationInput);
			this.components.add(this.slotInput);
			this.components.add(this.uuidInput);
		}

		@Override
	    protected void actionPerformed(GuiButton button) throws IOException {
	        if (button.id == SECOND_BUTTON) {
	        	if (this.attributeNameInput.getText().isEmpty()) {
	        		this.parent.clearAttributeModifier();
	        	} else {
	        		if (this.slotInput.getSelected() == null) {
	        			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.attributeModifier.problem.slotRequired.title"), new TextComponentTranslation("gui.popup.attributeModifier.problem.slotRequired.message"), I18n.format("gui.buttons.back")));
	        			return;
	        		}
	        		UUID uuid = AttributeTypeManager.getUUIDfromString(this.uuidInput.getText(), this.attributeNameInput.getText(), this.slotInput.getSelected());
					
	        		this.parent.modifier = new AttributeModifier(uuid, this.attributeNameInput.getText(), this.amountInput.getFloat(), this.operationInput.getInteger());
	        		this.parent.slot = this.slotInput.getSelected();
	        		this.parent.selectedText.setText(this.parent.getDisplayText());
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
