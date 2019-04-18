package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItemDrop;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.addon.loottables.LootTablePresetEmpty;
import com.tmtravlr.additions.addon.loottables.LootTablePresetOtherLootTable;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditBlockDropInput;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBTInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Allows you to edit block drops
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since April 2019
 */
public class GuiComponentBlockDropInput implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected IngredientOreNBT ingredient = new IngredientOreNBT();
	
	protected IBlockAdded blockAdded;
	protected LootTablePreset blockDropTable;
	protected ItemStack displayStack;
	
	public GuiComponentBlockDropInput(String label, GuiEdit editScreen) {
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

		int displayY = this.y + 10;
		int textX = this.x;
		
		if (!this.displayStack.isEmpty()) {
			Gui.drawRect(this.x, displayY - 1, this.x + 22, displayY + 21, 0xFFA0A0A0);
			Gui.drawRect(this.x + 1, displayY, this.x + 21, displayY + 20, 0xFF000000);
			
			this.editScreen.renderItemStack(this.displayStack, this.x + 3, displayY + 2, mouseX, mouseY, true);
			textX = this.x + 30;
		}
		
		this.selectedText.x = textX;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - textX;
		
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
			
			if(CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 13, 13)) {
				this.setBlockDropTable(null);
			}
		}
		
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y + 10, this.selectedText.x + this.selectedText.width - this.x, this.selectedText.height)) {
			this.editScreen.mc.displayGuiScreen(this.getUpdateScreen());
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
	
	public void setDefaultBlockDropTable(LootTablePreset drop) {
		if (drop == null) {
			this.blockDropTable = LootTablePreset.EMPTY;
		} else {
			this.blockDropTable = drop;
		}
		
	}
	
	public void setBlockDropTable(LootTablePreset drop) {
		this.setDefaultBlockDropTable(drop);
		this.editScreen.notifyHasChanges();
	}
	
	public LootTablePreset getBlockDropTable() {
		return this.blockDropTable;
	}
	
	protected GuiScreen getUpdateScreen() {
		return new GuiEditBlockDropInput(this.editScreen, this);
	}
	
	private void updateDisplayText() {
		String displayText = I18n.format("gui.edit.block.drop.option.custom");
		
		if (this.blockDropTable instanceof LootTablePresetEmpty) {
			displayText = I18n.format("gui.edit.block.drop.option.nothing");
		} else if (this.blockDropTable instanceof LootTablePresetOtherLootTable) {
			displayText = I18n.format("gui.edit.block.drop.option.lootTable", ((LootTablePresetOtherLootTable)this.blockDropTable).otherLootTable);
		} else if (this.blockDropTable instanceof LootTablePresetBlockItself) {
			if (((LootTablePresetBlockItself)this.blockDropTable).block == this.blockAdded.getAsBlock()) {
				displayText = I18n.format("gui.edit.block.drop.option.itself");
			}
		} else if (this.blockDropTable instanceof LootTablePresetBlockItemDrop) {
			if (((LootTablePresetBlockItemDrop)this.blockDropTable).block == this.blockAdded.getAsBlock() && !((LootTablePresetBlockItemDrop)this.blockDropTable).dropStack.isEmpty()) {
				displayText = I18n.format("gui.edit.block.drop.option.item", ((LootTablePresetBlockItemDrop)this.blockDropTable).dropStack.getDisplayName());
			}
		}
	}
	
	private void updateDisplayStack() {
		this.displayStack = ItemStack.EMPTY;
		
		if (this.blockDropTable instanceof LootTablePresetBlockItself) {
			Block block = ((LootTablePresetBlockItself)this.blockDropTable).block;
			
			if (block == this.blockAdded.getAsBlock() && block != null && Item.getItemFromBlock(block) != Items.AIR) {
				this.displayStack = new ItemStack(Item.getItemFromBlock(block));
			}
		} else if (this.blockDropTable instanceof LootTablePresetBlockItemDrop) {
			ItemStack stack = ((LootTablePresetBlockItemDrop)this.blockDropTable).dropStack;
			
			if (((LootTablePresetBlockItemDrop)this.blockDropTable).block == this.blockAdded.getAsBlock() && !stack.isEmpty()) {
				this.displayStack = stack;
			}
		}
	}

}
