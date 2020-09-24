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
 * Extension of the vanilla text field, for a float input.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017
 */
public class GuiComponentFloatInput extends GuiTextField implements IGuiViewComponent {

	public GuiEdit editScreen;
	public Predicate<String> validator;
	
	private List<String> info = new ArrayList<>();
	private String label = "";
	private boolean allowNegative = true;
	private float minimum = -9999999f;
	private float maximum = 9999999f;
	private boolean required = false;
	private boolean hidden = false;
	private int clickUpTime;
	private int clickDownTime;
	
	public GuiComponentFloatInput(String label, GuiEdit editScreen, boolean allowNegative) {
		super(0, editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.editScreen = editScreen;
		this.label = label;
		this.allowNegative = allowNegative;
		
		if (!this.allowNegative) {
			this.minimum = 0f;
		}
		
		this.setValidator(new Predicate<String>() {

			@Override
			public boolean apply(String input) {
				return input.isEmpty() || input.matches((allowNegative ? "[+-]?" : "") + "[0-9]*\\.?[0-9]*");
			}
			
		});
		
		this.setFloat(Math.max(0f, this.minimum));
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
	
	public void setMinimum(float min) {
		this.minimum = min;
	}
	
	public void setMaximum(float max) {
		this.maximum = max;
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
		if (this.isFocused() && !focused) {
			this.checkLimits();
		}
		super.setFocused(focused);
	}
	
	public float getFloat() {
		try {
			return Float.parseFloat(this.getText());
		} catch(NumberFormatException e) {
			AdditionsMod.logger.warn("Tried to parse invalid float " + this.getText());
			return Math.max(0f, this.minimum);
		} catch(NullPointerException e) {
			AdditionsMod.logger.warn("Tried to parse null float");
			return Math.max(0f, this.minimum);
		}
	}
	
	public void setDefaultFloat(float toSet) {
		this.setFloat(toSet);
		this.checkLimits();
	}
	
	private void setFloat(float toSet) {
		this.setText("" + toSet);
		this.setCursorPositionZero();
	}
	
	private void increment() {
		float incremented = this.getFloat() + 1;
		float clamped = MathHelper.clamp(incremented, this.minimum, this.maximum);
		this.setFloat(clamped);
    	this.editScreen.notifyHasChanges();
	}
	
	private void decrement() {
		float decremented = this.getFloat() - 1;
		float clamped = MathHelper.clamp(decremented, this.minimum, this.maximum);
		this.setFloat(clamped);
    	this.editScreen.notifyHasChanges();
	}
	
	private void checkLimits() {
		float current = this.getFloat();
		float clamped = MathHelper.clamp(current, this.minimum, this.maximum);
		this.setFloat(clamped);
	}
}
