package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputPotion;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditBlockStateInfo;
import com.tmtravlr.additions.util.BlockStateInfo;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Lets you create or edit a block state info.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2021
 */
public class GuiComponentBlockStateInfoInput implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected ItemStack displayStack = ItemStack.EMPTY;
	protected BlockStateInfo stateInfo;
	
	public GuiComponentBlockStateInfoInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
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
		int top = y + 10;
		
		if (!this.displayStack.isEmpty()) {
			Gui.drawRect(x, top - 1, x + 22, top + 21, 0xFFA0A0A0);
			Gui.drawRect(x + 1, top, x + 21, top + 20, 0xFF000000);
			
			this.editScreen.renderItemStack(this.displayStack, x + 3, top + 2, mouseX, mouseY, true);
			
			this.selectedText.x = x + 30;
		} else {
			this.selectedText.x = x;
		}
		
		this.selectedText.y = top;
		this.selectedText.width = right - 90 - x;
		
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
		
		if (!this.selectedText.getText().isEmpty() && CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 13, 13)) {
			this.setBlockStateInfo(null);
		} else {		
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y + 10, this.selectedText.x + this.selectedText.width - this.x, this.selectedText.height)) {
				this.editScreen.mc.displayGuiScreen(new GuiEditBlockStateInfo(this.editScreen.mc.currentScreen, I18n.format("gui.edit.blockStateInfo.title"), this));
			}
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
	
	public void setDefaultBlockStateInfo(BlockStateInfo stateInfo) {
		this.stateInfo = stateInfo;
		
		if (stateInfo == null) {
			this.selectedText.setText("");
			this.displayStack = ItemStack.EMPTY;
		} else {
			String displayText = ((stateInfo.getBlock().getRegistryName() == null) ? "?" : (TextFormatting.GRAY + stateInfo.getBlock().getRegistryName().toString() + TextFormatting.RESET));
			
			if (!stateInfo.getStateMap().isEmpty()) {
				StringJoiner stringJoiner = new StringJoiner(", ", ": ", "");
				
				stateInfo.getStateMap().entrySet().forEach(entry -> stringJoiner.add(TextFormatting.GRAY + entry.getKey() + TextFormatting.RESET + "=" + entry.getValue()));
				
				displayText += stringJoiner.toString();
			}
			
			this.selectedText.setText(displayText);
			this.displayStack = this.stateInfo.getDisplayStack();
		}
		
		this.selectedText.setCursorPositionZero();
	}
	
	public void setBlockStateInfo(BlockStateInfo stateInfo) {
		this.setDefaultBlockStateInfo(stateInfo);
		this.editScreen.notifyHasChanges();
	}
	
	public BlockStateInfo getBlockStateInfo() {
		return this.stateInfo;
	}
}
