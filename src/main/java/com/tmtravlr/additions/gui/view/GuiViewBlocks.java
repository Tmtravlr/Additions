package com.tmtravlr.additions.gui.view;

import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardBlock;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditSelectTypeBlock;
import com.tmtravlr.additions.type.AdditionTypeBlock;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the blocks added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class GuiViewBlocks extends GuiViewAdditionType {

	public GuiViewBlocks(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.block.title"), new TextComponentTranslation("gui.view.addon.blocks.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<IBlockAdded> additions = AdditionTypeBlock.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.getId() == null || second == null || second.getId() == null) {
				return 0;
			}
			return first.getId().compareTo(second.getId());
		});
		
		for (IBlockAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardBlock(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditSelectTypeBlock(this, I18n.format("gui.edit.block.title"), this.addon);
	}

}
