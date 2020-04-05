package com.tmtravlr.additions.gui.registration;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedCarpet;
import com.tmtravlr.additions.addon.blocks.BlockAddedFacing;
import com.tmtravlr.additions.addon.blocks.BlockAddedFalling;
import com.tmtravlr.additions.addon.blocks.BlockAddedFence;
import com.tmtravlr.additions.addon.blocks.BlockAddedLadder;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.BlockAddedPane;
import com.tmtravlr.additions.addon.blocks.BlockAddedPillar;
import com.tmtravlr.additions.addon.blocks.BlockAddedSimple;
import com.tmtravlr.additions.addon.blocks.BlockAddedSlab;
import com.tmtravlr.additions.addon.blocks.BlockAddedStairs;
import com.tmtravlr.additions.addon.blocks.BlockAddedWall;
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockCarpet;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockFacing;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockFalling;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockFence;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockLadder;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockPane;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockPillar;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockSimple;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockSlab;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockStairs;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockWall;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFactoryRegistrationBlockAdded {
	
	public static void registerGuiFactories() {
		BlockAddedManager.registerGuiFactory(BlockAddedSimple.TYPE, new IGuiBlockAddedFactory<BlockAddedSimple>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.simple.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.simple.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedSimple block) {
				return new GuiEditBlockSimple(parent, block == null ? I18n.format("gui.edit.block.simple.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedSimple block) {
				GuiEditBlockSimple editScreen = new GuiEditBlockSimple(parent, I18n.format("gui.edit.block.simple.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedFalling.TYPE, new IGuiBlockAddedFactory<BlockAddedFalling>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.falling.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.falling.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedFalling block) {
				return new GuiEditBlockFalling(parent, block == null ? I18n.format("gui.edit.block.falling.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedFalling block) {
				GuiEditBlockFalling editScreen = new GuiEditBlockFalling(parent, I18n.format("gui.edit.block.falling.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedStairs.TYPE, new IGuiBlockAddedFactory<BlockAddedStairs>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.stairs.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.stairs.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedStairs block) {
				return new GuiEditBlockStairs(parent, block == null ? I18n.format("gui.edit.block.stairs.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedStairs block) {
				GuiEditBlockStairs editScreen = new GuiEditBlockStairs(parent, I18n.format("gui.edit.block.stairs.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedSlab.TYPE, new IGuiBlockAddedFactory<BlockAddedSlab>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.slab.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.slab.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedSlab block) {
				return new GuiEditBlockSlab(parent, block == null ? I18n.format("gui.edit.block.slab.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedSlab block) {
				GuiEditBlockSlab editScreen = new GuiEditBlockSlab(parent, I18n.format("gui.edit.block.slab.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedCarpet.TYPE, new IGuiBlockAddedFactory<BlockAddedCarpet>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.carpet.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.carpet.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedCarpet block) {
				return new GuiEditBlockCarpet(parent, block == null ? I18n.format("gui.edit.block.carpet.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedCarpet block) {
				GuiEditBlockCarpet editScreen = new GuiEditBlockCarpet(parent, I18n.format("gui.edit.block.carpet.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedFacing.TYPE, new IGuiBlockAddedFactory<BlockAddedFacing>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.facing.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.facing.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedFacing block) {
				return new GuiEditBlockFacing(parent, block == null ? I18n.format("gui.edit.block.facing.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedFacing block) {
				GuiEditBlockFacing editScreen = new GuiEditBlockFacing(parent, I18n.format("gui.edit.block.facing.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedPillar.TYPE, new IGuiBlockAddedFactory<BlockAddedPillar>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.pillar.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.pillar.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedPillar block) {
				return new GuiEditBlockPillar(parent, block == null ? I18n.format("gui.edit.block.pillar.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedPillar block) {
				GuiEditBlockPillar editScreen = new GuiEditBlockPillar(parent, I18n.format("gui.edit.block.pillar.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedLadder.TYPE, new IGuiBlockAddedFactory<BlockAddedLadder>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.ladder.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.ladder.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedLadder block) {
				return new GuiEditBlockLadder(parent, block == null ? I18n.format("gui.edit.block.ladder.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedLadder block) {
				GuiEditBlockLadder editScreen = new GuiEditBlockLadder(parent, I18n.format("gui.edit.block.ladder.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedPane.TYPE, new IGuiBlockAddedFactory<BlockAddedPane>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.pane.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.pane.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedPane block) {
				return new GuiEditBlockPane(parent, block == null ? I18n.format("gui.edit.block.pane.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedPane block) {
				GuiEditBlockPane editScreen = new GuiEditBlockPane(parent, I18n.format("gui.edit.block.pane.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedFence.TYPE, new IGuiBlockAddedFactory<BlockAddedFence>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.fence.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.fence.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedFence block) {
				return new GuiEditBlockFence(parent, block == null ? I18n.format("gui.edit.block.fence.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedFence block) {
				GuiEditBlockFence editScreen = new GuiEditBlockFence(parent, I18n.format("gui.edit.block.fence.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedWall.TYPE, new IGuiBlockAddedFactory<BlockAddedWall>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.wall.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.wall.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedWall block) {
				return new GuiEditBlockWall(parent, block == null ? I18n.format("gui.edit.block.wall.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedWall block) {
				GuiEditBlockWall editScreen = new GuiEditBlockWall(parent, I18n.format("gui.edit.block.wall.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
	}

}
