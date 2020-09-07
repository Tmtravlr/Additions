package com.tmtravlr.additions.gui.view.components.input.effect;

import java.io.IOException;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditEffectCauseInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Lets you build an effect cause.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019 
 */
public class GuiComponentEffectCauseInput implements IGuiViewComponent {

	protected final GuiEdit editScreen;
	protected final GuiTextField selectedText;
	protected final Addon addon;
	protected EffectCause effectCause = null;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	private NonNullList<ItemStack> displayStacks = NonNullList.create();
	private ItemStackDisplay stackDisplay = new ItemStackDisplay();
	
	public GuiComponentEffectCauseInput(String label, Addon addon, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.addon = addon;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
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

		this.selectedText.x = x + this.getItemDisplayOffset();
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - this.getItemDisplayOffset() - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}

		if (!this.displayStacks.isEmpty()) {
			int itemDisplayTop = this.y + 10;
			
			Gui.drawRect(this.x, itemDisplayTop - 1, this.x + 22, itemDisplayTop + 21, 0xFFA0A0A0);
			Gui.drawRect(this.x + 1, itemDisplayTop, this.x + 21, itemDisplayTop + 20, 0xFF000000);
			
			this.stackDisplay.updateDisplay(this.displayStacks);
			
			this.editScreen.renderItemStack(this.stackDisplay.getDisplayStack(), this.x + 3, itemDisplayTop + 2, mouseX, mouseY, true);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 15, 15)) {
				this.setEffectCause(null);
			}
		}
		
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.selectedText.x - this.getItemDisplayOffset(), this.selectedText.y, this.selectedText.width + this.getItemDisplayOffset() - 15, this.selectedText.height)) {
			this.editScreen.mc.displayGuiScreen(new GuiEditEffectCauseInput(this.editScreen, this, this.effectCause, this.addon));
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
	
	public void setDefaultEffectCause(EffectCause effectCause) {
		this.effectCause = effectCause;
		
		if (effectCause == null) {
			this.displayStacks = NonNullList.create();
			this.selectedText.setText("");
		} else {
			IGuiEffectCauseFactory factory = EffectCauseManager.getGuiFactoryFor(EffectCauseManager.getTypeFor(effectCause));
			
			if (factory == null) {
				this.displayStacks = NonNullList.create();
				this.selectedText.setText(EffectCauseManager.getTypeFor(effectCause).toString());
			} else {
				this.displayStacks = factory.getDisplayStacks(effectCause);
				this.selectedText.setText(factory.getTitle(effectCause));
			}
		}

		this.stackDisplay = new ItemStackDisplay();
		this.selectedText.setCursorPositionZero();
	}
	
	public void setEffectCause(EffectCause effectCause) {
		this.setDefaultEffectCause(effectCause);
		this.editScreen.notifyHasChanges();
	}
	
	public EffectCause getEffectCause() {
		return this.effectCause;
	}

	private int getItemDisplayOffset() {
		return this.displayStacks.isEmpty() ? 0 : 30;
	}
}
