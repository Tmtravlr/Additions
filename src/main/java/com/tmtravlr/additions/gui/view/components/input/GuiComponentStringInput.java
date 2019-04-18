package com.tmtravlr.additions.gui.view.components.input;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiColorSelect;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Extension of the vanilla text field, for edit object guis.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017
 */
public class GuiComponentStringInput extends GuiTextField implements IGuiViewComponent {

	private GuiEdit editScreen;
	private Predicate<String> validator;
	
	private String label = "";
	private List<String> info = new ArrayList<>();
	private int colorOffset = 0;
	private boolean hasColorSelect = false;
	private GuiColorSelect colorSelect;
	private boolean required = false;
	private boolean hidden = false;
	
	
	public GuiComponentStringInput(String label, GuiEdit editScreen) {
		super(0, editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.editScreen = editScreen;
		this.label = label;
		this.colorSelect = new GuiColorSelect(editScreen);
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
	
	@Override
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
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
			this.info = this.editScreen.getFontRenderer().listFormattedStringToWidth(info.getFormattedText(), 200);
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
		
		// Add color icon
		if (this.hasColorSelect) {
			int colorX = this.x + this.width + 4 + colorOffset;
			int colorY = this.y + (this.height / 2 - 6);
			
			this.editScreen.drawTexturedModalRect(colorX, colorY, 34, 64, 13, 13);
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
    	
    	this.editScreen.notifyHasChanges();
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
}
