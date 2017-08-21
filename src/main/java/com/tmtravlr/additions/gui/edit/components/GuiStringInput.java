package com.tmtravlr.additions.gui.edit.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.IGuiEditComponent;
import com.tmtravlr.additions.gui.edit.components.helpers.GuiColorSelect;

/**
 * Extension of the vanilla text field, for edit object guis.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class GuiStringInput extends GuiTextField implements IGuiEditComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	private GuiEdit parent;
	private Predicate<String> validator;
	
	private String label = "";
	private List<String> info = new ArrayList<>();
	private int colorOffset = 0;
	private boolean hasColorSelect = false;
	private GuiColorSelect colorSelect;
	private boolean required = false;
	
	
	public GuiStringInput(String label, GuiEdit parentScreen) {
		super(0, parentScreen.getFontRenderer(), 0, 0, 0, 20);
		this.parent = parentScreen;
		this.label = label;
		this.colorSelect = new GuiColorSelect(parentScreen);
	}
	
	@Override
	public void setValidator(Predicate<String> validator) {
		super.setValidator(validator);
		this.validator = validator;
	}
	
	@Override
	public void setText(String text) {
		if (this.hasColorSelect) {
			text = this.colorSelect.stripFormatting(text);
		}
		super.setText(text);
	}
	
	@Override
	public String getText() {
		if (this.hasColorSelect) {
			return this.colorSelect.addFormatting(super.getText());
		} else {
			return super.getText();
		}
	}
	
	public void setDefaultText(String text) {
		this.setText(text);
		this.setCursorPositionZero();
	}
	
	public void setInfo(ITextComponent info) {
		if (info == null) {
			this.info.clear();
		} else {
			this.info = this.parent.getFontRenderer().listFormattedStringToWidth(info.getFormattedText(), 200);
		}
		
		if (this.info.isEmpty()) {
			this.colorOffset = 0;
		} else {
			this.colorOffset = 17;
		}
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setHasColorSelect() {
		this.hasColorSelect = true;
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
		
		this.parent.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
		
		// Add info icon
		if (!this.info.isEmpty()) {
			int infoX = this.x + this.width + 4;
			int infoY = this.y + (this.height / 2 - 6);

			this.parent.drawTexturedModalRect(infoX, infoY, 21, 64, 13, 13);
			
			if (mouseX > infoX && mouseX < infoX + 13 && mouseY > infoY && mouseY < infoY + 13) {
				GuiUtils.drawHoveringText(info, mouseX, mouseY, this.parent.width, this.parent.height, -1, this.parent.getFontRenderer());
	            RenderHelper.disableStandardItemLighting();
			}
		}
		
		// Add color icon
		if (this.hasColorSelect) {
			int colorX = this.x + this.width + 4 + colorOffset;
			int colorY = this.y + (this.height / 2 - 6);
			
			this.parent.drawTexturedModalRect(colorX, colorY, 34, 64, 13, 13);
		}
		
		if (this.hasColorSelect && this.colorSelect.visible) {
			this.colorSelect.x = right - this.colorSelect.width;
			this.colorSelect.y = this.y + this.height / 2 - this.colorSelect.height / 2;
			this.colorSelect.drawColorSelect();
		}
	}
	
	@Override
	public void onKeyTyped(char keyTyped, int keyCode) {
		this.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if (this.hasColorSelect) {
			this.colorSelect.mouseClicked(mouseX, mouseY, mouseButton);
			
			int colorX = this.x + this.width + 4 + colorOffset;
			int colorY = this.y + (this.height / 2 - 6);
			
			if (mouseX > colorX && mouseX < colorX + 13 && mouseY > colorY && mouseY < colorY + 13 ) {
				if (!this.colorSelect.visible) {
					this.colorSelect.visible = true;
				}
			}
    	}
		
    	this.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) {
		return false;
	}
	
	@Override
    public void setResponderEntryValue(int idIn, String textIn) {
    	super.setResponderEntryValue(idIn, textIn);
    	
    	this.parent.setHasUnsavedChanges();
    }

	@Override
    public void drawTextBox() {
    	String text = this.getText();
    	String format = this.colorSelect.getFormatting();
    	
    	if (this.hasColorSelect && !format.isEmpty()) {
    		this.hasColorSelect = false;
    		this.setText(text);
    		this.hasColorSelect = true;
    	}
    	
		super.drawTextBox();
		
		if (this.hasColorSelect && !format.isEmpty()) {
			if (text.startsWith(format)) {
				this.hasColorSelect = false;
				this.setText(text.substring(format.length()));
				this.hasColorSelect = true;
			}
		}
    }
    
    private void setTextForRendering(String newText) {
    	ObfuscationReflectionHelper.setPrivateValue(GuiTextField.class, this, newText, "field_146216_j", "text");
    }
}
