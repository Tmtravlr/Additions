package com.tmtravlr.additions.gui.message.edit;

import java.io.IOException;

import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.oredict.OreDictionary;

public abstract class GuiMessageBoxEditItemStack extends GuiMessageBoxTwoButton {
	
	protected GuiComponentDropdownInputItem itemInput;
	protected GuiComponentBooleanInput anyDamageInput;
	protected GuiComponentIntegerInput metaInput;
	protected GuiComponentIntegerInput countInput;
	protected GuiComponentNBTInput nbtInput;
	
	protected boolean hasMeta;
	protected boolean hasAnyDamage;
	protected boolean hasCount;
	protected boolean hasTag;
	
	public GuiMessageBoxEditItemStack(GuiEdit parentScreen, ItemStack stack) {
		this(parentScreen, stack, true, true, true);
	}

	public GuiMessageBoxEditItemStack(GuiEdit parentScreen, ItemStack stack, boolean hasMeta, boolean hasCount, boolean hasTag) {
		super(parentScreen, parentScreen, I18n.format("gui.popup.itemStack.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
		this.hasMeta = hasMeta;
		this.hasCount = hasCount;
		this.hasTag = hasTag;
		
		this.itemInput = new GuiComponentDropdownInputItem(I18n.format("gui.popup.itemStack.item.label"), parentScreen);
		this.itemInput.setDefaultSelected(stack.isEmpty() ? null : stack.getItem());
		
		this.metaInput = new GuiComponentIntegerInput(I18n.format("gui.popup.itemStack.meta.label"), parentScreen, false);
		this.metaInput.setDefaultInteger(stack.getMetadata() == OreDictionary.WILDCARD_VALUE && this.hasAnyDamage ? 0 : stack.getMetadata());
		if (this.hasAnyDamage) {
			this.metaInput.setHidden(true);
		}
		
		this.anyDamageInput = new GuiComponentBooleanInput(I18n.format("gui.popup.itemStack.anyDamage.label"), parentScreen) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				if (input != this.getBoolean()) {
					GuiMessageBoxEditItemStack.this.metaInput.setHidden(input);
				}
				super.setDefaultBoolean(input);
			}
			
		};
		this.anyDamageInput.setDefaultBoolean(this.hasAnyDamage ? stack.getMetadata() == OreDictionary.WILDCARD_VALUE : false);
		
		this.countInput = new GuiComponentIntegerInput(I18n.format("gui.popup.itemStack.count.label"), parentScreen, false);
		this.countInput.setMaximum(64);
		this.countInput.setMinimum(1);
		this.countInput.setDefaultInteger(stack.getCount());
		
		this.nbtInput = new GuiComponentNBTInput(I18n.format("gui.popup.itemStack.nbt.label"), parentScreen);
		this.nbtInput.setDefaultText(stack.hasTagCompound() ? stack.getTagCompound().toString() : "{}");
		
		this.setViewScreen(parentScreen);
	}

	@Override
    protected void onSecondButtonClicked() {
        if (this.itemInput.getSelected() == null) {
    		this.removeItemStack();
    	} else {
    		ItemStack stack = new ItemStack(this.itemInput.getSelected(), this.hasCount ? this.countInput.getInteger() : 1, this.hasAnyDamage && this.anyDamageInput.getBoolean() ? OreDictionary.WILDCARD_VALUE : this.metaInput.getInteger());
    		if (this.nbtInput.getTag() != null) {
    			stack.setTagCompound(this.nbtInput.getTag());
    		}
    		this.saveItemStack(stack);
    	}
    	this.mc.displayGuiScreen(this.parentScreen);
    }
    
	@Override
    protected int getPopupWidth() {
    	return 350;
    }
	
    @Override
    protected int getPopupHeight() {
    	return this.getComponentsHeight() + 90;
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

		this.drawString(this.fontRenderer, this.itemInput.getLabel(), popupX + 10, componentY + this.itemInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
		this.itemInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
		componentY += this.itemInput.getHeight(popupX, popupRight);

		if (this.hasMeta && this.hasAnyDamage) {
			this.drawString(this.fontRenderer, this.anyDamageInput.getLabel(), popupX + 10, componentY + this.anyDamageInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.anyDamageInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.anyDamageInput.getHeight(popupX, popupRight);
		}
		
		if (this.hasMeta) {
			this.drawString(this.fontRenderer, this.metaInput.getLabel(), popupX + 10, componentY + this.metaInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.metaInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.metaInput.getHeight(popupX, popupRight);
		}
		
		if (this.hasCount) {
			this.drawString(this.fontRenderer, this.countInput.getLabel(), popupX + 10, componentY + this.countInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.countInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.countInput.getHeight(popupX, popupRight);
		}
		
		if (this.hasTag) {
			this.drawString(this.fontRenderer, this.nbtInput.getLabel(), popupX + 10, componentY + this.nbtInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.nbtInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.nbtInput.getHeight(popupX, popupRight);
		}
		
		this.drawPostRender();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	this.itemInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	
    	if (this.hasMeta && this.hasAnyDamage) {
    		this.anyDamageInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (this.hasMeta) {
    		this.metaInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}

    	if (this.hasCount) {
    		this.countInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (this.hasTag) {
    		this.nbtInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	this.itemInput.onKeyTyped(keyTyped, keyCode);
    	
    	if (this.hasMeta) {
    		this.metaInput.onKeyTyped(keyTyped, keyCode);
    	}

    	if (this.hasCount) {
    		this.countInput.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (this.hasTag) {
    		this.nbtInput.onKeyTyped(keyTyped, keyCode);
    	}

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
    }
    
    protected abstract void removeItemStack();
    
    protected abstract void saveItemStack(ItemStack stack);
    
    private int getComponentsHeight() {
    	int popupWidth = this.getPopupWidth();
    	int popupLeft = this.width / 2 - popupWidth / 2;
    	int popupRight = popupLeft + popupWidth;
    	
    	int height = this.itemInput.getHeight(popupLeft, popupRight);
    	
    	if (this.hasMeta && this.hasAnyDamage) {
    		height += this.anyDamageInput.getHeight(popupLeft, popupRight);
    	}
    	
    	if (this.hasMeta) {
    		height += this.metaInput.getHeight(popupLeft, popupRight);
    	}
    	
    	if (this.hasCount) {
    		height += this.countInput.getHeight(popupLeft, popupRight);
    	}
    	
    	if (this.hasTag) {
    		height += this.nbtInput.getHeight(popupLeft, popupRight);
    	}
    	
    	return height;
    }
}

