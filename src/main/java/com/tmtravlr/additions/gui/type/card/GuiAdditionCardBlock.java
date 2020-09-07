package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonBlock;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.type.AdditionTypeBlock;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added block.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2018 
 */
public class GuiAdditionCardBlock extends GuiAdditionCardColored {

	private IBlockAdded addition;
	private String textType;
	private String textId;
	private String textName;
	private String filterType;
	private String filterId;
	private String filterName;
	
	public GuiAdditionCardBlock(GuiView viewScreen, Addon addon, IBlockAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		ResourceLocation type = BlockAddedManager.getTypeFor(this.addition);
		IGuiBlockAddedFactory factory = BlockAddedManager.getGuiFactoryFor(type);
		
		this.textType = TextFormatting.GRAY + I18n.format("gui.view.additionType.type", TextFormatting.RESET + (factory != null ? factory.getTitle() : type.toString()) + TextFormatting.GRAY);
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.getId() + TextFormatting.GRAY);
		this.textName = TextFormatting.GRAY + I18n.format("gui.view.additionType.name", TextFormatting.RESET + this.addition.getDisplayName() + TextFormatting.GRAY);
		
		this.filterType = (factory != null ? factory.getTitle() : type.toString()).toLowerCase();
		this.filterId = this.addition.getId().toLowerCase();
		this.filterName = this.addition.getDisplayName().toLowerCase();
		
		this.setColors(GuiAdditionTypeButtonBlock.BUTTON_COLOR_DARK, GuiAdditionTypeButtonBlock.BUTTON_COLOR_HOVER);
	}
	
	@Override
	public int getCardHeight() {
		if (this.needs3Lines()) {
			return 80;
		} else {
			return 60;
		}
	}

	@Override
	protected void drawCardInfo(int mouseX, int mouseY) {
		int itemDisplayTop = this.y + this.height/2 - 10;
		int columnWidth = this.getColumnWidth();
		ItemStack itemStack = this.addition.getItemBlock() == null ? ItemStack.EMPTY : new ItemStack(this.addition.getItemBlock().getAsItem());
		
		Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textName, columnWidth*2 - 60, this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth*2 - 10, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textType, columnWidth*2 - 10, this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth - 10, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textType, columnWidth - 10, this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
    	}
    	
    	this.viewScreen.renderItemStack(itemStack, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
	}

	@Override
	protected void editAddition() {
		IGuiBlockAddedFactory editFactory = BlockAddedManager.getGuiFactoryFor(BlockAddedManager.getTypeFor(this.addition));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getEditScreen(this.viewScreen, this.addon, this.addition));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessageBox(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", BlockAddedManager.getTypeFor(this.addition)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void duplicateAddition() {
		IGuiBlockAddedFactory editFactory = BlockAddedManager.getGuiFactoryFor(BlockAddedManager.getTypeFor(this.addition));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getDuplicateScreen(this.viewScreen, this.addon, this.addition));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessageBox(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", BlockAddedManager.getTypeFor(this.addition)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeBlock.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterType.contains(filter.toLowerCase()) || this.filterId.contains(filter.toLowerCase()) || this.filterName.contains(filter.toLowerCase());
	}
	
	private boolean needs3Lines() {
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textType) > this.getColumnWidth() || this.width < 240;
	}
	
	private int getColumnWidth() {
		return (this.width - 45) / 2;
	}

}
