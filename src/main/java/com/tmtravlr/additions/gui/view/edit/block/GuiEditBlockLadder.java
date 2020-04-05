package com.tmtravlr.additions.gui.view.edit.block;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedLadder;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockLadder;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockTexture;
import com.tmtravlr.additions.util.models.BlockModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new block that can face different directions or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since March 2020
 */
public class GuiEditBlockLadder extends GuiEditBlockModifiableBoundingBox<BlockAddedLadder> {

	protected GuiComponentBooleanInput blockNeedsSupportInput;
	protected GuiComponentBooleanInput blockSupportBackInput;
	protected GuiComponentBooleanInput blockSupportTopInput;
	protected GuiComponentBooleanInput blockSupportBottomInput;
	protected GuiComponentBooleanInput blockPlaceDownwardInput;
    
	public GuiEditBlockLadder(GuiScreen parentScreen, String title, Addon addon, BlockAddedLadder block) {
		super(parentScreen, title, addon);
		
		this.isNew = block == null;
		
		if (this.isNew) {
			this.block = new BlockAddedLadder();
			this.block.setItemBlock(new ItemAddedBlockLadder());
		} else {
			this.block = block;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.blockSupportBackInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.ladder.supportBack.label"), this);
		this.blockSupportBackInput.setDefaultBoolean(this.block.supportBack);
		this.blockSupportBackInput.setHidden(true);
		
		this.blockSupportTopInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.ladder.supportTop.label"), this);
		this.blockSupportTopInput.setInfo(new TextComponentTranslation("gui.edit.block.ladder.supportTop.info"));
		this.blockSupportTopInput.setDefaultBoolean(this.block.supportTop);
		this.blockSupportTopInput.setHidden(true);
		
		this.blockSupportBottomInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.ladder.supportBottom.label"), this);
		this.blockSupportBottomInput.setInfo(new TextComponentTranslation("gui.edit.block.ladder.supportBottom.info"));
		this.blockSupportBottomInput.setDefaultBoolean(this.block.supportBottom);
		this.blockSupportBottomInput.setHidden(true);
		
		this.blockNeedsSupportInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.ladder.needsSupport.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				GuiEditBlockLadder.this.blockSupportBackInput.setHidden(!input);
				GuiEditBlockLadder.this.blockSupportTopInput.setHidden(!input);
				GuiEditBlockLadder.this.blockSupportBottomInput.setHidden(!input);
				super.setDefaultBoolean(input);
			}
			
		};
		this.blockNeedsSupportInput.setInfo(new TextComponentTranslation("gui.edit.block.ladder.needsSupport.info"));
		this.blockNeedsSupportInput.setDefaultBoolean(this.block.supportBack || this.block.supportTop || this.block.supportBottom);
		
		this.blockPlaceDownwardInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.ladder.placeDownward.label"), this);
		this.blockPlaceDownwardInput.setInfo(new TextComponentTranslation("gui.edit.block.ladder.placeDownward.info"));
		this.blockPlaceDownwardInput.setDefaultBoolean(this.block.placeDownward);
		
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

		this.advancedComponents.add(this.blockNeedsSupportInput);
		this.advancedComponents.add(this.blockSupportBackInput);
		this.advancedComponents.add(this.blockSupportTopInput);
		this.advancedComponents.add(this.blockSupportBottomInput);
		this.advancedComponents.add(this.blockPlaceDownwardInput);
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
	public void saveObject() {
		if (this.blockNeedsSupportInput.getBoolean() && !(this.blockSupportBackInput.getBoolean() || this.blockSupportTopInput.getBoolean() || this.blockSupportBottomInput.getBoolean())) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.block.problem.title"), new TextComponentTranslation("gui.edit.block.ladder.problem.needsSupport"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.block.supportBack = this.blockSupportBackInput.getBoolean();
		this.block.supportTop = this.blockSupportTopInput.getBoolean();
		this.block.supportBottom = this.blockSupportBottomInput.getBoolean();
		this.block.placeDownward = this.blockPlaceDownwardInput.getBoolean();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.blockNeedsSupportInput.setDefaultBoolean(this.block.supportBack || this.block.supportTop || this.block.supportBottom);
		this.blockSupportBackInput.setDefaultBoolean(this.block.supportBack);
		this.blockSupportTopInput.setDefaultBoolean(this.block.supportTop);
		this.blockSupportBottomInput.setDefaultBoolean(this.block.supportBottom);
		this.blockPlaceDownwardInput.setDefaultBoolean(this.block.placeDownward);
		
	    super.copyFromOther();
	}
    
    @Override
	protected GuiScreen getTextureDialogue(GuiScreen nextScreen) {
    	return new GuiEditBlockTexture(nextScreen, this.addon, this.block, this.isNew, BlockModelManager.BlockModelType.LADDER);
    }
	
    @Override
	protected void onToggleShowAdvanced(boolean showAdvanced) {
    	super.onToggleShowAdvanced(showAdvanced);
    	
    	if (showAdvanced) {
    		this.blockNeedsSupportInput.setDefaultBoolean(this.blockNeedsSupportInput.getBoolean());
    	}
    }
}
