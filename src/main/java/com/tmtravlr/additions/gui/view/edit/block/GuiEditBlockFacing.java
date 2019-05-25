package com.tmtravlr.additions.gui.view.edit.block;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedFacing;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockFacingTexture;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new block that can face different directions or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class GuiEditBlockFacing extends GuiEditBlockModifiableBoundingBox<BlockAddedFacing> {

	protected GuiComponentBooleanInput blockCanFaceVerticallyInput;
    
	public GuiEditBlockFacing(GuiScreen parentScreen, String title, Addon addon, BlockAddedFacing block) {
		super(parentScreen, title, addon);
		
		this.isNew = block == null;
		
		if (this.isNew) {
			this.block = new BlockAddedFacing();
			this.block.setItemBlock(new ItemAddedBlockSimple());
		} else {
			this.block = block;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.blockCanFaceVerticallyInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.facing.canFaceVertically.label"), this);
		this.blockCanFaceVerticallyInput.setInfo(new TextComponentTranslation("gui.edit.block.facing.canFaceVertically.info"));
		this.blockCanFaceVerticallyInput.setDefaultBoolean(this.block.canFaceVertically);
		
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

		this.advancedComponents.add(this.blockCanFaceVerticallyInput);
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
	
	@Override
	public void saveObject() {
		this.block.canFaceVertically = this.blockCanFaceVerticallyInput.getBoolean();
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
	    this.blockCanFaceVerticallyInput.setDefaultBoolean(this.copyFrom.canFaceVertically);
	    super.copyFromOther();
	}
    
    @Override
	protected GuiScreen getTextureDialogue(GuiScreen nextScreen) {
    	return new GuiEditBlockFacingTexture(nextScreen, this.addon, this.block, this.isNew);
    }
}
