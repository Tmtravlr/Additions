package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
import com.tmtravlr.additions.gui.message.edit.GuiMessageBoxEditBlockStateValue;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditEffectCauseInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

/**
 * Lets you edit a block state key and value
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2021
 */
public class GuiComponentBlockStateValueInput implements IGuiViewComponent {
	
	private final GuiTextField blockStateValueDisplay;

	private GuiEdit editScreen;
	private Block block;
	private IProperty key;
	private Comparable value;

	protected int x;
	protected int y;
	protected int width;
	private String label = "";
	private boolean required = false;
	private boolean hidden = false;
	
	
	public GuiComponentBlockStateValueInput(String label, GuiEdit editScreen, Block block) {
		this.editScreen = editScreen;
		this.label = label;
		this.block = block;
		this.blockStateValueDisplay = new GuiTextField(0, editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.blockStateValueDisplay.setMaxStringLength(1024);
		this.blockStateValueDisplay.setEnabled(false);
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
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
	public String getLabel() {
		return this.label;
	}

	@Override
	public int getHeight(int left, int right) {
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;

		this.blockStateValueDisplay.x = x;
		this.blockStateValueDisplay.y = y + 10;
		this.blockStateValueDisplay.width = right - 60 - x;
		
		this.blockStateValueDisplay.drawTextBox();
		
		if (!this.blockStateValueDisplay.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.blockStateValueDisplay.x + this.blockStateValueDisplay.width - 15;
			int deleteY = this.blockStateValueDisplay.y + (this.blockStateValueDisplay.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.blockStateValueDisplay.x + this.blockStateValueDisplay.width - 15;
		int deleteY = this.blockStateValueDisplay.y + (this.blockStateValueDisplay.height / 2 - 6);
		
		if (!this.blockStateValueDisplay.getText().isEmpty()) {
			
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 15, 15)) {
				this.setKeyAndValue(null, null);
			}
		}
		
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.blockStateValueDisplay.x, this.blockStateValueDisplay.y, this.blockStateValueDisplay.width - 15, this.blockStateValueDisplay.height)) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditBlockStateValue(this.editScreen, this.editScreen, this.block, this.key, this.value) {
				
				@Override
				public void saveBlockState(IProperty key, Comparable value) {
					GuiComponentBlockStateValueInput.this.setKeyAndValue(key, value);
				}
				
			});
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultKeyAndValue(IProperty key, Comparable value) {
		this.key = key;
		this.value = value;
		
		String keyName = key == null ? "" : key.getName();
		String valueName = value == null ? "" : value.toString();
		
		if (keyName.isEmpty() && valueName.isEmpty()) {
			this.blockStateValueDisplay.setText("");
		} else {
			this.blockStateValueDisplay.setText(TextFormatting.GRAY + keyName + TextFormatting.RESET + "=" + valueName);
		}
		
		this.blockStateValueDisplay.setCursorPositionZero();
	}
	
	public void setKeyAndValue(IProperty key, Comparable value) {
		this.setDefaultKeyAndValue(key, value);
		this.editScreen.notifyHasChanges();
	}
	
	public IProperty getKey() {
		return this.key;
	}
	
	public Comparable getValue() {
		return this.value;
	}
}
