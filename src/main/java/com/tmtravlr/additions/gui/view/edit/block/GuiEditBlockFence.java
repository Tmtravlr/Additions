package com.tmtravlr.additions.gui.view.edit.block;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedFence;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockTexture;
import com.tmtravlr.additions.util.models.BlockModelManager;

import net.minecraft.client.gui.GuiScreen;

/**
 * Page for adding a new fence block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since April 2020
 */
public class GuiEditBlockFence extends GuiEditBlock<BlockAddedFence> {
    
	public GuiEditBlockFence(GuiScreen parentScreen, String title, Addon addon, BlockAddedFence block) {
		super(parentScreen, title, addon);
		
		this.isNew = block == null;
		
		if (this.isNew) {
			this.block = new BlockAddedFence();
			this.block.setItemBlock(new ItemAddedBlockSimple());
		} else {
			this.block = block;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.blockIdInput);
		this.components.add(this.blockNameInput);
		this.components.add(this.blockMaterialInput);
		this.components.add(this.blockTransparentInput);
		this.components.add(this.blockSemiTransparentInput);
		this.components.add(this.blockOpacityInput);
		this.components.add(this.blockHardnessInput);
		this.components.add(this.blockResistanceInput);
		this.components.add(this.blockHarvestLevelInput);
		this.components.add(this.blockHarvestToolInput);
		if (!this.isNew) {
			this.components.add(this.blockDropInput);
			this.components.add(this.blockTextureButton);
		}

		this.advancedComponents.add(this.blockLightLevelInput);
		this.advancedComponents.add(this.blockEffectiveToolsInput);
		this.advancedComponents.add(this.blockFlammabilityInput);
		this.advancedComponents.add(this.blockFireSpreadSpeedInput);
		this.advancedComponents.add(this.blockSlipperinessInput);
		this.advancedComponents.add(this.blockBookshelfStrengthInput);
		this.advancedComponents.add(this.blockIsSlimeInput);
		this.advancedComponents.add(this.blockIsBeaconBaseInput);
		this.advancedComponents.add(this.blockCanPistonsPushInput);
		this.advancedComponents.add(this.blockMapColorInput);
		this.advancedComponents.add(this.blockBeaconColorMultiplierInput);
		this.advancedComponents.add(this.blockPlaceSoundInput);
		this.advancedComponents.add(this.blockBreakSoundInput);
		this.advancedComponents.add(this.blockHitSoundInput);
		this.advancedComponents.add(this.blockStepSoundInput);
		this.advancedComponents.add(this.blockFallSoundInput);
		this.advancedComponents.add(this.itemBlockStackSizeInput);
		this.advancedComponents.add(this.itemBlockShinesInput);
		this.advancedComponents.add(this.itemBlockTooltipInput);
		this.advancedComponents.add(this.itemBlockOreDictInput);
		this.advancedComponents.add(this.itemBlockBurnTimeInput);
		this.advancedComponents.add(this.itemBlockContainerInput);
		this.advancedComponents.add(this.itemBlockAttributesInput);
	}
    
    @Override
	protected GuiScreen getTextureDialogue(GuiScreen nextScreen) {
    	return new GuiEditBlockTexture(nextScreen, this.addon, this.block, this.isNew, BlockModelManager.BlockModelType.FENCE);
    }
}
