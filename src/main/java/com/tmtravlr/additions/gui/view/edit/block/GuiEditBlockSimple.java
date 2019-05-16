package com.tmtravlr.additions.gui.view.edit.block;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedSimple;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentColorInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMapColor;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputToolType;

import net.minecraft.client.gui.GuiScreen;

/**
 * Page for adding a new simple block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018
 */
public class GuiEditBlockSimple extends GuiEditBlock<BlockAddedSimple> {
    
	public GuiEditBlockSimple(GuiScreen parentScreen, String title, Addon addon, BlockAddedSimple block) {
		super(parentScreen, title, addon);
		
		this.isNew = block == null;
		
		if (this.isNew) {
			this.block = new BlockAddedSimple();
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
		this.components.add(this.blockOpacityInput);
		this.components.add(this.blockHardnessInput);
		this.components.add(this.blockResistanceInput);
		this.components.add(this.blockHarvestLevelInput);
		this.components.add(this.blockHarvestToolInput);
		if (!this.isNew) {
			this.components.add(this.blockDropInput);
			this.components.add(this.blockTextureButton);
		}
		
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
		this.advancedComponents.add(this.blockBoundingBoxMinXInput);
		this.advancedComponents.add(this.blockBoundingBoxMinYInput);
		this.advancedComponents.add(this.blockBoundingBoxMinZInput);
		this.advancedComponents.add(this.blockBoundingBoxMaxXInput);
		this.advancedComponents.add(this.blockBoundingBoxMaxYInput);
		this.advancedComponents.add(this.blockBoundingBoxMaxZInput);
		this.advancedComponents.add(this.blockSameCollisionBoundingBoxInput);
		this.advancedComponents.add(this.blockHasCollisionBoxInput);
		this.advancedComponents.add(this.blockCollisionBoxMinXInput);
		this.advancedComponents.add(this.blockCollisionBoxMinYInput);
		this.advancedComponents.add(this.blockCollisionBoxMinZInput);
		this.advancedComponents.add(this.blockCollisionBoxMaxXInput);
		this.advancedComponents.add(this.blockCollisionBoxMaxYInput);
		this.advancedComponents.add(this.blockCollisionBoxMaxZInput);
		this.advancedComponents.add(this.itemBlockStackSizeInput);
		this.advancedComponents.add(this.itemBlockShinesInput);
		this.advancedComponents.add(this.itemBlockTooltipInput);
		this.advancedComponents.add(this.itemBlockOreDictInput);
		this.advancedComponents.add(this.itemBlockBurnTimeInput);
		this.advancedComponents.add(this.itemBlockContainerInput);
		this.advancedComponents.add(this.itemBlockAttributesInput);
	}
}
