package com.tmtravlr.additions.gui.view.edit.block;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedGrass;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockStateInfoInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputLootTable;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockGrassTexture;
import com.tmtravlr.additions.util.BlockStateInfo;
import com.tmtravlr.additions.util.models.BlockModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import scala.actors.threadpool.Arrays;

/**
 * Page for adding a new grass block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class GuiEditBlockGrass extends GuiEditBlockModifiableBoundingBox<BlockAddedGrass> {
	
	private static final String BONEMEAL_TYPE_NONE = "gui.edit.block.grass.bonemealType.option.none";
	private static final String BONEMEAL_TYPE_VANILLA = "gui.edit.block.grass.bonemealType.option.vanilla";
	private static final String BONEMEAL_TYPE_LIST = "gui.edit.block.grass.bonemealType.option.list";
	private static final String BONEMEAL_TYPE_LOOT_TABLE = "gui.edit.block.grass.bonemealType.option.lootTable";
	
	protected GuiComponentListInput<GuiComponentBlockStateInfoInput> spreadBlocksInput;
	protected GuiComponentBlockStateInfoInput pathBlockInput;
	protected GuiComponentDropdownInput<String> bonemealTypeInput;
	protected GuiComponentListInput<GuiComponentBlockStateInfoInput> bonemealBlocksInput;
	protected GuiComponentSuggestionInputLootTable bonemealLootTableInput;
	protected GuiComponentBooleanInput allowSnowyInput;
	protected GuiComponentBooleanInput allowHoeingInput;
	protected GuiComponentBooleanInput useBiomeColorInput;
	protected GuiComponentFloatInput spreadChanceInput;
	protected GuiComponentIntegerInput minLightInput;
	protected GuiComponentIntegerInput maxLightInput;
	protected GuiComponentIntegerInput minSpreadLightInput;
	protected GuiComponentIntegerInput maxSpreadLightInput;
	
	public GuiEditBlockGrass(GuiScreen parentScreen, String title, Addon addon, BlockAddedGrass block) {
		super(parentScreen, title, addon);
		
		this.isNew = block == null;
		
		if (this.isNew) {
			this.block = new BlockAddedGrass();
			this.block.setItemBlock(new ItemAddedBlockSimple());
		} else {
			this.block = block;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.spreadBlocksInput = new GuiComponentListInput<GuiComponentBlockStateInfoInput>(I18n.format("gui.edit.block.grass.spreadBlocks.label"), this) {

			@Override
			public GuiComponentBlockStateInfoInput createBlankComponent() {
				return new GuiComponentBlockStateInfoInput("", GuiEditBlockGrass.this);
			}
			
		};
		if (!this.isNew) {
			this.block.spreadBlocks.forEach(stateInfo -> {
				GuiComponentBlockStateInfoInput newComponent = this.spreadBlocksInput.createBlankComponent();
				newComponent.setDefaultBlockStateInfo(stateInfo);
				this.spreadBlocksInput.addDefaultComponent(newComponent);
			});
		} else {
			GuiComponentBlockStateInfoInput newComponent = this.spreadBlocksInput.createBlankComponent();
			newComponent.setDefaultBlockStateInfo(BlockAddedGrass.DIRT_BLOCK_STATE);
			this.spreadBlocksInput.addDefaultComponent(newComponent);
		}
		
		this.pathBlockInput = new GuiComponentBlockStateInfoInput(I18n.format("gui.edit.block.grass.pathBlock.label"), GuiEditBlockGrass.this);
		if (!this.isNew) {
			this.pathBlockInput.setDefaultBlockStateInfo(this.block.pathBlock);
		} else {
			this.pathBlockInput.setDefaultBlockStateInfo(BlockAddedGrass.PATH_BLOCK_STATE);
		}
		
		this.bonemealBlocksInput = new GuiComponentListInput<GuiComponentBlockStateInfoInput>(I18n.format("gui.edit.block.grass.bonemealBlocks.label"), this) {

			@Override
			public GuiComponentBlockStateInfoInput createBlankComponent() {
				return new GuiComponentBlockStateInfoInput("", GuiEditBlockGrass.this);
			}
			
		};
		if (!this.isNew) {
			this.block.bonemealBlocks.forEach(stateInfo -> {
				GuiComponentBlockStateInfoInput newComponent = this.bonemealBlocksInput.createBlankComponent();
				newComponent.setDefaultBlockStateInfo(stateInfo);
				this.bonemealBlocksInput.addDefaultComponent(newComponent);
			});
		}
		
		this.bonemealLootTableInput = new GuiComponentSuggestionInputLootTable(I18n.format("gui.edit.block.grass.bonemealLootTable.label"), this);
		if (!this.isNew && this.block.bonemealLootTable != null) {
			this.bonemealLootTableInput.setDefaultText(this.block.bonemealLootTable.toString());
		}
		
		this.bonemealTypeInput = new GuiComponentDropdownInput<String>(I18n.format("gui.edit.block.grass.bonemealType.label"), this) {
			
			@Override
			public void setDefaultSelected(String selected) {
				if (selected != this.getSelected()) {
					GuiEditBlockGrass.this.hideOrUnhideBonemealTypes(selected);
				}
				
				super.setDefaultSelected(selected);
			}
			
			@Override
			public String getSelectionName(String selected) {
				return selected == null ? "" : I18n.format(selected);
			}
			
		};
		this.bonemealTypeInput.setSelections(Arrays.asList(new String[]{BONEMEAL_TYPE_NONE, BONEMEAL_TYPE_VANILLA, BONEMEAL_TYPE_LIST, BONEMEAL_TYPE_LOOT_TABLE}));
		if (this.block.vanillaBonemeal) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_VANILLA);
		} else if (!this.block.bonemealBlocks.isEmpty()) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_LIST);
		} else if (this.block.bonemealLootTable != null) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_LOOT_TABLE);
		} else {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_NONE);
		}
		
		this.allowSnowyInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.grass.allowSnowy.label"), this);
		this.allowSnowyInput.setDefaultBoolean(this.block.allowSnowy);
		
		this.allowHoeingInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.grass.allowHoeing.label"), this);
		this.allowHoeingInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.allowHoeing.info"));
		this.allowHoeingInput.setDefaultBoolean(this.block.allowHoeing);
		
		this.useBiomeColorInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.grass.useBiomeColor.label"), this);
		this.useBiomeColorInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.useBiomeColor.info"));
		this.useBiomeColorInput.setDefaultBoolean(this.block.useBiomeColor);
		
		this.spreadChanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.grass.spreadChance.label"), this, false);
		this.spreadChanceInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.spreadChance.info"));
		this.spreadChanceInput.setMinimum(0);
		this.spreadChanceInput.setMaximum(1);
		this.spreadChanceInput.setDefaultFloat(this.block.spreadChance);
		
		this.minLightInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.grass.minLight.label"), this, false);
		this.minLightInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.minLight.info"));
		this.minLightInput.setMinimum(0);
		this.minLightInput.setMaximum(15);
		this.minLightInput.setDefaultInteger(this.block.minLight);
		
		this.maxLightInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.grass.maxLight.label"), this, false);
		this.maxLightInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.maxLight.info"));
		this.maxLightInput.setMinimum(0);
		this.maxLightInput.setMaximum(15);
		this.maxLightInput.setDefaultInteger(this.block.maxLight);
		
		this.minSpreadLightInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.grass.minSpreadLight.label"), this, false);
		this.minSpreadLightInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.minSpreadLight.info"));
		this.minSpreadLightInput.setMinimum(0);
		this.minSpreadLightInput.setMaximum(15);
		this.minSpreadLightInput.setDefaultInteger(this.block.minSpreadLight);
		
		this.maxSpreadLightInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.grass.maxSpreadLight.label"), this, false);
		this.maxSpreadLightInput.setInfo(new TextComponentTranslation("gui.edit.block.grass.maxSpreadLight.info"));
		this.maxSpreadLightInput.setMinimum(0);
		this.maxSpreadLightInput.setMaximum(15);
		this.maxSpreadLightInput.setDefaultInteger(this.block.maxSpreadLight);
		
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

		this.advancedComponents.add(this.spreadBlocksInput);
		this.advancedComponents.add(this.pathBlockInput);
		this.advancedComponents.add(this.bonemealTypeInput);
		this.advancedComponents.add(this.bonemealBlocksInput);
		this.advancedComponents.add(this.bonemealLootTableInput);
		this.advancedComponents.add(this.allowSnowyInput);
		this.advancedComponents.add(this.allowHoeingInput);
		this.advancedComponents.add(this.useBiomeColorInput);
		this.advancedComponents.add(this.spreadChanceInput);
		this.advancedComponents.add(this.minLightInput);
		this.advancedComponents.add(this.maxLightInput);
		this.advancedComponents.add(this.minSpreadLightInput);
		this.advancedComponents.add(this.maxSpreadLightInput);
		this.advancedComponents.add(this.blockLightLevelInput);
		this.advancedComponents.add(this.blockEffectiveToolsInput);
		this.advancedComponents.add(this.blockFlammabilityInput);
		this.advancedComponents.add(this.blockFireSpreadSpeedInput);
		this.advancedComponents.add(this.blockSlipperinessInput);
		this.advancedComponents.add(this.blockBookshelfStrengthInput);
		this.advancedComponents.add(this.blockIsSlimeInput);
		this.advancedComponents.add(this.blockIsBeaconBaseInput);
		this.advancedComponents.add(this.blockCanPistonsPushInput);
		this.advancedComponents.add(this.blockCanEndermenCarryInput);
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
		this.block.spreadBlocks = this.spreadBlocksInput.getComponents().stream().map(GuiComponentBlockStateInfoInput::getBlockStateInfo).collect(Collectors.toList());
		this.block.pathBlock = this.pathBlockInput.getBlockStateInfo();
		this.block.vanillaBonemeal = this.bonemealTypeInput.getSelected() == BONEMEAL_TYPE_VANILLA;
		this.block.bonemealBlocks = this.bonemealTypeInput.getSelected() == BONEMEAL_TYPE_LIST ? this.bonemealBlocksInput.getComponents().stream().filter(component -> component.getBlockStateInfo() != null)
				.map(GuiComponentBlockStateInfoInput::getBlockStateInfo).collect(Collectors.toList()) : new ArrayList<>();
		this.block.bonemealLootTable = (this.bonemealTypeInput.getSelected() == BONEMEAL_TYPE_LOOT_TABLE && !this.bonemealLootTableInput.getText().isEmpty()) ? 
				new ResourceLocation(this.bonemealLootTableInput.getText()) : null;
		this.block.allowSnowy = this.allowSnowyInput.getBoolean();
		this.block.allowHoeing = this.allowHoeingInput.getBoolean();
		this.block.useBiomeColor = this.useBiomeColorInput.getBoolean();
		this.block.spreadChance = this.spreadChanceInput.getFloat();
		this.block.minLight = this.minLightInput.getInteger();
		this.block.maxLight = Math.max(this.maxLightInput.getInteger(), this.block.minLight);
		this.block.minSpreadLight = this.minSpreadLightInput.getInteger();
		this.block.maxSpreadLight = Math.max(this.maxSpreadLightInput.getInteger(), this.block.minSpreadLight);
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.spreadBlocksInput.removeAllComponents();
		this.copyFrom.spreadBlocks.forEach(stateInfo -> {
			GuiComponentBlockStateInfoInput newComponent = this.spreadBlocksInput.createBlankComponent();
			newComponent.setDefaultBlockStateInfo(stateInfo);
			this.spreadBlocksInput.addDefaultComponent(newComponent);
		});
		
		this.pathBlockInput.setDefaultBlockStateInfo(this.copyFrom.pathBlock);

		this.bonemealBlocksInput.removeAllComponents();
		this.copyFrom.bonemealBlocks.forEach(stateInfo -> {
			GuiComponentBlockStateInfoInput newComponent = this.bonemealBlocksInput.createBlankComponent();
			newComponent.setDefaultBlockStateInfo(stateInfo);
			this.bonemealBlocksInput.addDefaultComponent(newComponent);
		});
		
		if (this.copyFrom.bonemealLootTable != null) {
			this.bonemealLootTableInput.setDefaultText(this.copyFrom.bonemealLootTable.toString());
		}

		if (this.copyFrom.vanillaBonemeal) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_VANILLA);
		} else if (!this.copyFrom.bonemealBlocks.isEmpty()) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_LIST);
		} else if (this.copyFrom.bonemealLootTable != null) {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_LOOT_TABLE);
		} else {
			this.bonemealTypeInput.setDefaultSelected(BONEMEAL_TYPE_NONE);
		}
		
		this.allowSnowyInput.setDefaultBoolean(this.copyFrom.allowSnowy);
		this.allowHoeingInput.setDefaultBoolean(this.copyFrom.allowHoeing);
		this.useBiomeColorInput.setDefaultBoolean(this.copyFrom.useBiomeColor);
		this.spreadChanceInput.setDefaultFloat(this.copyFrom.spreadChance);
		this.minLightInput.setDefaultInteger(this.copyFrom.minLight);
		this.maxLightInput.setDefaultInteger(this.copyFrom.maxLight);
		this.minSpreadLightInput.setDefaultInteger(this.copyFrom.minSpreadLight);
		this.maxSpreadLightInput.setDefaultInteger(this.copyFrom.maxSpreadLight);
		
	    super.copyFromOther();
	}
	
	@Override
	protected void onToggleShowAdvanced(boolean showAdvanced) {
		super.onToggleShowAdvanced(showAdvanced);
		
		if (showAdvanced) {
			this.hideOrUnhideBonemealTypes(this.bonemealTypeInput.getSelected());
		}
	}
	
	private void hideOrUnhideBonemealTypes(String bonemealType) {
		this.bonemealBlocksInput.setHidden(true);
		this.bonemealLootTableInput.setHidden(true);
		
		if (bonemealType == BONEMEAL_TYPE_LIST) {
			this.bonemealBlocksInput.setHidden(false);
		} else if (bonemealType == BONEMEAL_TYPE_LOOT_TABLE) {
			this.bonemealLootTableInput.setHidden(false);
		}
	}
    
    @Override
	protected GuiScreen getTextureDialogue(GuiScreen nextScreen) {
    	return new GuiEditBlockGrassTexture(nextScreen, this.addon, this.block, this.isNew);
    }
}
