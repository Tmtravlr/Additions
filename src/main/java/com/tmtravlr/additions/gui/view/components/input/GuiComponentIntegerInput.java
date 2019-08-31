package com.tmtravlr.additions.gui.view.components.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

/**
 * Extension of the vanilla text field, for an int input.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentIntegerInput extends GuiTextField implements IGuiViewComponent {

	public GuiEdit editScreen;
	public Predicate<String> validator;
	
	private List<String> info = new ArrayList<>();
	private String label = "";
	private boolean allowNegative = true;
	private int minimum = -99999999;
	private int maximum = 999999999;
	private boolean required = false;
	private boolean hidden = false;
	private int clickUpTime;
	private int clickDownTime;
	
	public GuiComponentIntegerInput(String label, GuiEdit editScreen, boolean allowNegative) {
		super(0, editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.editScreen = editScreen;
		this.label = label;
		this.allowNegative = allowNegative;
		this.setMaxStringLength(9);
		
		if (!this.allowNegative) {
			this.minimum = 0;
		}
		
		this.setValidator(new Predicate<String>() {

			@Override
			public boolean apply(String input) {
				return input.isEmpty() || input.matches((allowNegative ? "[+-]?" : "") + "[0-9]*");
			}
			
		});
		
		this.setInteger(Math.max(0, this.minimum));
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
	public void setValidator(Predicate<String> validator) {
		super.setValidator(validator);
		this.validator = validator;
	}
	
	public void setInfo(ITextComponent info) {
		if (info == null) {
			this.info.clear();
		} else {
			this.info = this.editScreen.getFontRenderer().listFormattedStringToWidth(info.getFormattedText(), 200);
		}
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setMinimum(int min) {
		this.minimum = min;
		this.checkLimits();
	}
	
	public void setMaximum(int max) {
		this.maximum = max;
		this.checkLimits();
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
		this.width = right - 70 - x;
		
		this.drawTextBox();
	    
	    int buttonX = this.x + this.width + 1;
		int buttonUpY = this.y;
		int buttonDownY = this.y + 11;
	    
	    if (Mouse.isButtonDown(0) && mouseX >= buttonX && mouseX <= buttonX + 9 && mouseY >= buttonUpY && mouseY <= buttonUpY + 10) {
			if (this.clickUpTime == 0 || (this.clickUpTime > 10 && this.clickUpTime % 2 == 0)) {
				this.increment();
			}
			this.clickUpTime++;
		} else {
			this.clickUpTime = 0;
		}
		
		if (Mouse.isButtonDown(0) && mouseX >= buttonX && mouseX <= buttonX + 9 && mouseY >= buttonDownY && mouseY <= buttonDownY + 10) {
			if (this.clickDownTime == 0 || (this.clickDownTime > 10 && this.clickDownTime % 2 == 0)) {
				this.decrement();
			}
			this.clickDownTime++;
		} else {
			this.clickDownTime = 0;
		}
		
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    
	    this.editScreen.drawTexturedModalRect(this.x + this.width, this.y - 1, 163, 64, 11, 22);
	    
	    if (this.clickUpTime > 0) {
	    	this.editScreen.drawTexturedModalRect(buttonX, buttonUpY, 174, 74, 9, 10);
	    } else {
	    	this.editScreen.drawTexturedModalRect(buttonX, buttonUpY, 174, 64, 9, 10);
	    }
	    
	    if (this.clickDownTime > 0) {
	    	this.editScreen.drawTexturedModalRect(buttonX, buttonDownY, 183, 74, 9, 10);
	    } else {
	    	this.editScreen.drawTexturedModalRect(buttonX, buttonDownY, 183, 64, 9, 10);
	    }
		
		// Add info icon
		if (!this.info.isEmpty()) {
			int infoX = this.x + this.width + 13;
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
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		this.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) {
		return false;
	}
	
	@Override
    public void setResponderEntryValue(int idIn, String textIn) {
    	super.setResponderEntryValue(idIn, textIn);
    	
    	this.editScreen.notifyHasChanges();
    }
	
	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (!focused) {
			this.checkLimits();
		}
	}
	
	public int getInteger() {
		try {
			return Integer.parseInt(this.getText());
		} catch(NumberFormatException e) {
			AdditionsMod.logger.warn("Tried to parse invalid integer " + this.getText());
			return Math.max(0, this.minimum);
		}
	}
	
	public void setDefaultInteger(int integer) {
		this.setInteger(integer);
		this.checkLimits();
	}
	
	protected void setInteger(int integer) {
		this.setText("" + integer);
		this.setCursorPositionZero();
	}
	
	private void increment() {
		int incremented = this.getInteger() + 1;
		int clamped = MathHelper.clamp(incremented, this.minimum, this.maximum);
		this.setInteger(clamped);
	}
	
	private void decrement() {
		int decremented = this.getInteger() - 1;
		int clamped = MathHelper.clamp(decremented, this.minimum, this.maximum);
		this.setInteger(clamped);
	}
	
	private void checkLimits() {
		int current = this.getInteger();
		int clamped = MathHelper.clamp(current, this.minimum, this.maximum);
		this.setInteger(clamped);
	}
}
