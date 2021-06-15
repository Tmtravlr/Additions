package com.tmtravlr.additions.gui.message.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Message box for editing a single block state value.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2021
 */
public abstract class GuiMessageBoxEditBlockStateValue extends GuiMessageBoxTwoButton {
	
	protected final Block block;
	
	protected GuiComponentDropdownInput<IProperty<?>> propertyInput;
	protected GuiComponentBooleanInput booleanInput;
	protected GuiComponentIntegerInput integerInput;
	protected GuiComponentDropdownInput<Integer> integerListInput;
	protected GuiComponentDropdownInput<Comparable> enumInput;
	protected GuiComponentDisplayText unsupportedPropertyTypeMessage;
	protected GuiComponentDisplayText noValuesMessage;
	
	protected int currentComponentsHeight = 0;
	protected PropertyType propertyType;
	
	public GuiMessageBoxEditBlockStateValue(GuiScreen parentScreen, GuiEdit editScreen, Block block, @Nullable IProperty key, @Nullable Comparable value) {
		super(parentScreen, parentScreen, I18n.format("gui.popup.blockStateValue.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
		
		this.block = block;
		
		if (block.getBlockState().getProperties().isEmpty()) {
			throw new IllegalArgumentException("Trying to edit properties for a block that doesn't have any: " + block.getRegistryName());
		}
		
		this.propertyInput = new GuiComponentDropdownInput<IProperty<?>>(I18n.format("gui.popup.blockStateValue.property.label"), editScreen) {
			
			@Override
			public String getSelectionName(IProperty selected) {
				return selected.getName();
			}
			
			@Override
			public void setDefaultSelected(IProperty selected) {
				if (this.getSelected() != selected) {
					GuiMessageBoxEditBlockStateValue.this.changeProperty(selected);
				}
				super.setDefaultSelected(selected);
			}
			
		};
		this.propertyInput.setSelections(block.getDefaultState().getPropertyKeys());
		
		this.booleanInput = new GuiComponentBooleanInput(I18n.format("gui.popup.blockStateValue.value.label"), editScreen);
		this.booleanInput.setHidden(true);
		
		this.integerInput = new GuiComponentIntegerInput(I18n.format("gui.popup.blockStateValue.value.label"), editScreen, true);
		this.integerInput.setHidden(true);
		
		this.integerListInput = new GuiComponentDropdownInput<>(I18n.format("gui.popup.blockStateValue.value.label"), editScreen);
		this.integerListInput.setHidden(true);
		
		this.enumInput = new GuiComponentDropdownInput<>(I18n.format("gui.popup.blockStateValue.value.label"), editScreen);
		this.enumInput.setHidden(true);
		
		this.unsupportedPropertyTypeMessage = new GuiComponentDisplayText(editScreen, 
				new TextComponentTranslation("gui.popup.blockStateValue.error.unsupportedPropertyType").setStyle(new Style().setColor(TextFormatting.RED)));
		this.unsupportedPropertyTypeMessage.setIgnoreLabel(true);
		this.unsupportedPropertyTypeMessage.setHidden(true);
		
		this.noValuesMessage = new GuiComponentDisplayText(editScreen, 
				new TextComponentTranslation("gui.popup.blockStateValue.error.noValues").setStyle(new Style().setColor(TextFormatting.RED)));
		this.noValuesMessage.setIgnoreLabel(true);
		this.noValuesMessage.setHidden(true);
		
		this.propertyInput.setDefaultSelected(key);
		
		if (this.propertyType == PropertyType.BOOLEAN && value instanceof Boolean) {
			this.booleanInput.setDefaultBoolean((Boolean) value);
			
		} else if (this.propertyType == PropertyType.INTEGER && value instanceof Integer) {
			if (!this.integerInput.isHidden()) {
				this.integerInput.setDefaultInteger((Integer) value);
			} else if (!this.integerListInput.isHidden()) {
				this.integerInput.setDefaultInteger((Integer) value);
			}
		} else if (this.propertyType == PropertyType.ENUM) {
			if (value != null && ((PropertyEnum)key).getAllowedValues().contains(value)) {
				this.enumInput.setDefaultSelected(value);
			}
		}
		
		this.setViewScreen(editScreen);
	}
	
	public abstract void saveBlockState(IProperty key, Comparable value);

	@Override
    protected void onSecondButtonClicked() {
        if (this.propertyInput.getSelected() == null) {
    		this.saveBlockState(null, null);
    	} else {
    		IProperty key = this.propertyInput.getSelected();
    		Comparable value;
    		
    		if (!this.booleanInput.isHidden()) {
        		value = this.booleanInput.getBoolean();
    		} else if (!this.integerInput.isHidden()) {
    			value = this.integerInput.getInteger();
        	} else if (!this.integerListInput.isHidden()) {
        		value = this.integerListInput.getSelected();
        	} else if (!this.enumInput.isHidden()) {
        		value = this.enumInput.getSelected();
        		
        		if (value == null) {
        			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.blockStateValue.problem.title"), new TextComponentTranslation("gui.popup.blockStateValue.problem.noValueSelected"), I18n.format("gui.buttons.back")));
        			return;
        		}
        	} else {
        		key = null;
        		value = null;
        	}
    		
    		this.saveBlockState(key, value);
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

		this.drawString(this.fontRenderer, this.propertyInput.getLabel(), popupX + 10, componentY + this.propertyInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
		this.propertyInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
		componentY += this.propertyInput.getHeight(popupX, popupRight);
		
		if (!this.booleanInput.isHidden()) {
    		this.drawString(this.fontRenderer, this.booleanInput.getLabel(), popupX + 10, componentY + this.booleanInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.booleanInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.booleanInput.getHeight(popupX, popupRight);
			
		} else if (!this.integerInput.isHidden()) {
			this.drawString(this.fontRenderer, this.integerInput.getLabel(), popupX + 10, componentY + this.integerInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.integerInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.integerInput.getHeight(popupX, popupRight);
			
    	} else if (!this.integerListInput.isHidden()) {
    		this.drawString(this.fontRenderer, this.integerListInput.getLabel(), popupX + 10, componentY + this.integerListInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.integerListInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.integerListInput.getHeight(popupX, popupRight);
			
    	} else if (!this.enumInput.isHidden()) {
			this.drawString(this.fontRenderer, this.enumInput.getLabel(), popupX + 10, componentY + this.enumInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.enumInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.enumInput.getHeight(popupX, popupRight);
			
    	} else if (!this.noValuesMessage.isHidden()) {
			this.noValuesMessage.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.noValuesMessage.getHeight(popupX, popupRight);
			
    	} else if (!this.unsupportedPropertyTypeMessage.isHidden()) {
			this.unsupportedPropertyTypeMessage.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.unsupportedPropertyTypeMessage.getHeight(popupX, popupRight);
			
    	} else {
    		componentY += 40;
    	}
		
		this.drawPostRender();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	this.propertyInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	
    	if (!this.booleanInput.isHidden()) {
    		this.booleanInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (!this.integerInput.isHidden()) {
    		this.integerInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (!this.integerListInput.isHidden()) {
    		this.integerListInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (!this.enumInput.isHidden()) {
    		this.enumInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (!this.unsupportedPropertyTypeMessage.isHidden()) {
    		this.unsupportedPropertyTypeMessage.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	if (!this.noValuesMessage.isHidden()) {
    		this.noValuesMessage.onMouseClicked(mouseX, mouseY, mouseButton);
    	}
    	
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	this.propertyInput.onKeyTyped(keyTyped, keyCode);
    	
    	if (!this.booleanInput.isHidden()) {
    		this.booleanInput.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (!this.integerInput.isHidden()) {
    		this.integerInput.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (!this.integerListInput.isHidden()) {
    		this.integerListInput.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (!this.enumInput.isHidden()) {
    		this.enumInput.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (!this.unsupportedPropertyTypeMessage.isHidden()) {
    		this.unsupportedPropertyTypeMessage.onKeyTyped(keyTyped, keyCode);
    	}
    	
    	if (!this.noValuesMessage.isHidden()) {
    		this.noValuesMessage.onKeyTyped(keyTyped, keyCode);
    	}

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
    }
    
    private void changeProperty(IProperty selected) {
    	PropertyType newType = null;
    	
    	if (selected instanceof PropertyBool) {
    		newType = PropertyType.BOOLEAN;
    	} else if (selected instanceof PropertyInteger) {
    		newType = PropertyType.INTEGER;
    	} else if (selected instanceof PropertyEnum) {
    		newType = PropertyType.ENUM;
    	}
    	
    	if (this.propertyType != newType) {
			this.booleanInput.setHidden(true);
			this.integerInput.setHidden(true);
			this.integerListInput.setHidden(true);
			this.enumInput.setHidden(true);
			this.unsupportedPropertyTypeMessage.setHidden(true);
			this.noValuesMessage.setHidden(true);
    	}
    	
    	this.propertyType = newType;
		
    	if (selected != null) {
        	this.booleanInput.setDefaultBoolean(false);
        	this.integerInput.setDefaultInteger(0);
        	this.enumInput.setDefaultSelected(null);
        	
        	if (this.propertyType == PropertyType.BOOLEAN) {
            	this.booleanInput.setHidden(false);
        	} else if (this.propertyType == PropertyType.INTEGER) {
            	int[] integerRange = this.allowedIntegerRange((PropertyInteger)selected);
            	
    			if (integerRange == null) {
    				if (!((PropertyInteger)selected).getAllowedValues().isEmpty()) {
    					this.noValuesMessage.setHidden(false);
    				} else {
	    				this.integerListInput.setSelections(((PropertyInteger)selected).getAllowedValues());
	                	this.integerListInput.setHidden(false);
    				}
    			} else {
    				this.integerInput.setMinimum(integerRange[0]);
    				this.integerInput.setMaximum(integerRange[1]);
                	this.integerInput.setHidden(false);
    			}
        	} else if (this.propertyType == PropertyType.ENUM) {
			
				if (((PropertyEnum)selected).getAllowedValues().isEmpty()) {
					this.noValuesMessage.setHidden(false);
				} else {
					this.enumInput.setSelections(((PropertyEnum)selected).getAllowedValues());
					this.enumInput.setHidden(false);
				}
        	} else {
				this.unsupportedPropertyTypeMessage.setHidden(false);
        	}
    	}
    }
    
    /**
     * Returns an array of two integers (the min and max) if the property has a valid continuous range of allowed integers.
     */
    private int[] allowedIntegerRange(PropertyInteger property) {
    	List<Integer> allowedIntegers = new ArrayList<>(property.getAllowedValues());
    	Collections.sort(allowedIntegers);
    	
    	if (allowedIntegers.size() == 0 || allowedIntegers.get(0) == null) {
    		return null;
    	}
    	
    	int prev = allowedIntegers.get(0);
    	for (int i = 1; i < allowedIntegers.size(); i++) {
    		if (allowedIntegers.get(i) == null || allowedIntegers.get(i) - prev > 1) {
    			// Has a null integer, or not a continuous range of integers!
    			return null;
    		}
    		
    		prev = allowedIntegers.get(i);
    	}
    	
    	return new int[] {allowedIntegers.get(0), allowedIntegers.get(allowedIntegers.size() - 1)};
    }
    
    private int getComponentsHeight() {
    	int popupWidth = this.getPopupWidth();
    	int popupLeft = this.width / 2 - popupWidth / 2;
    	int popupRight = popupLeft + popupWidth;
    	
    	int height = this.propertyInput.getHeight(popupLeft, popupRight);
    	
    	if (!this.booleanInput.isHidden()) {
    		height += this.booleanInput.getHeight(popupLeft, popupRight);
    	} else if (!this.integerInput.isHidden()) {
    		height += this.integerInput.getHeight(popupLeft, popupRight);
    	} else if (!this.integerListInput.isHidden()) {
    		height += this.integerListInput.getHeight(popupLeft, popupRight);
    	} else if (!this.enumInput.isHidden()) {
    		height += this.enumInput.getHeight(popupLeft, popupRight);
    	} else {
    		height += 40;
    	}
    	
    	if (height != this.currentComponentsHeight) {
    		this.currentComponentsHeight = height;
    		this.buttonList.clear();
    		this.initGui();
    	}
    	
    	return height;
    }
    
    protected enum PropertyType {
    	INTEGER,
    	BOOLEAN,
    	ENUM
    }
}

