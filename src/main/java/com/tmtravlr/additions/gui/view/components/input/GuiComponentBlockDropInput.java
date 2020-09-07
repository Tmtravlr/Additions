package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.Optional;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItemDrop;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.addon.loottables.LootTablePresetEmpty;
import com.tmtravlr.additions.addon.loottables.LootTablePresetOtherLootTable;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditBlockDrop;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditBlockDropInput;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBTInput;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
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
import net.minecraft.util.ResourceLocation;

/**
 * Allows you to edit block drops
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date April 2019
 */
public class GuiComponentBlockDropInput implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	
	protected final Addon addon;
	protected final IBlockAdded blockAdded;
	protected final LootTablePreset defaultPreset;
	protected final ResourceLocation dropLocation;
	
	protected LootTableAdded blockDropTable;
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected boolean hasChanges;
	protected ItemStack displayStack = ItemStack.EMPTY;
	
	public GuiComponentBlockDropInput(String label, Addon addon, IBlockAdded blockAdded, LootTableAdded defaultLootTable, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
		this.addon = addon;
		this.blockAdded = blockAdded;
		this.dropLocation = defaultLootTable.location;
		this.defaultPreset = defaultLootTable.preset;
		this.initializeDefaultDropTable();
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
		
		if (this.blockDropTable.preset != this.defaultPreset) {
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
		
		if (this.blockDropTable.preset != this.defaultPreset && CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 13, 13)) {
			this.setBlockDropPreset(null);
		} else {		
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y + 10, this.selectedText.x + this.selectedText.width - this.x, this.selectedText.height)) {
				this.editScreen.mc.displayGuiScreen(this.getUpdateScreen());
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
	
	public void setBlockDropPreset(LootTablePreset preset) {
		if (preset == null) {
			this.blockDropTable.preset = this.defaultPreset;
		} else {
			this.blockDropTable.preset = preset;
		}

		this.updateDisplayStack();
		this.updateDisplayText();
		this.setHasChanges();
		this.editScreen.notifyHasChanges();
	}
	
	public LootTableAdded getBlockDropTable() {
		return this.blockDropTable;
	}
	
	public void setHasChanges() {
		this.hasChanges = true;
	}
	
	public boolean hasChanges() {
		return this.hasChanges;
	}
	
	protected GuiScreen getUpdateScreen() {
		return new GuiEditBlockDropInput(this.editScreen, this.blockDropTable, this.blockAdded, this.addon, this);
	}
	
	private void initializeDefaultDropTable() {
		this.blockDropTable = AdditionTypeLootTable.INSTANCE.getLootTableForLocation(this.addon, this.dropLocation).orElse(new LootTableAdded(this.dropLocation, this.defaultPreset));
		this.updateDisplayStack();
		this.updateDisplayText();
	}
	
	private void updateDisplayText() {
		String displayText = I18n.format("gui.edit.block.drop.option.custom");
		
		if (this.blockDropTable.preset instanceof LootTablePresetEmpty) {
			displayText = I18n.format("gui.edit.block.drop.option.nothing");
		} else if (this.blockDropTable.preset instanceof LootTablePresetOtherLootTable) {
			displayText = I18n.format("gui.edit.block.drop.option.lootTable", ((LootTablePresetOtherLootTable)this.blockDropTable.preset).otherLootTable);
		} else if (this.blockDropTable.preset instanceof LootTablePresetBlockItself) {
			if (((LootTablePresetBlockItself)this.blockDropTable.preset).block == this.blockAdded.getAsBlock()) {
				displayText = I18n.format("gui.edit.block.drop.option.itself");
			}
		} else if (this.blockDropTable.preset instanceof LootTablePresetBlockItemDrop) {
			if (((LootTablePresetBlockItemDrop)this.blockDropTable.preset).block == this.blockAdded.getAsBlock() && !((LootTablePresetBlockItemDrop)this.blockDropTable.preset).dropStack.isEmpty()) {
				displayText = I18n.format("gui.edit.block.drop.option.item", ((LootTablePresetBlockItemDrop)this.blockDropTable.preset).dropStack.getDisplayName());
			}
		}
		
		this.selectedText.setText(displayText);
		this.selectedText.setCursorPositionZero();
	}
	
	private void updateDisplayStack() {
		this.displayStack = ItemStack.EMPTY;
		
		if (this.blockDropTable.preset instanceof LootTablePresetBlockItself) {
			Block block = ((LootTablePresetBlockItself)this.blockDropTable.preset).block;
			
			if (block == this.blockAdded.getAsBlock() && block != null && Item.getItemFromBlock(block) != Items.AIR) {
				this.displayStack = new ItemStack(Item.getItemFromBlock(block));
			}
		} else if (this.blockDropTable.preset instanceof LootTablePresetBlockItemDrop) {
			ItemStack stack = ((LootTablePresetBlockItemDrop)this.blockDropTable.preset).dropStack;
			
			if (((LootTablePresetBlockItemDrop)this.blockDropTable.preset).block == this.blockAdded.getAsBlock() && !stack.isEmpty()) {
				this.displayStack = stack;
			}
		}
	}

}
