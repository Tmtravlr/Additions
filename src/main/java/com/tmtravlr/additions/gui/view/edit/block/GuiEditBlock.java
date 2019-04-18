package com.tmtravlr.additions.gui.view.edit.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentAttributeModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentColorInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMapColor;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMaterial;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputOreDict;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputToolType;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockTexture;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.type.AdditionTypeBlock;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a new simple block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018
 */
public abstract class GuiEditBlock<T extends IBlockAdded> extends GuiEdit {
	
	protected final int BUTTON_TEXTURE = this.buttonCount++;
	
	protected Addon addon;
	
    protected boolean isNew;
    protected T block;
    protected T copyFrom;

    protected GuiComponentStringInput blockIdInput;
    protected GuiComponentStringInput blockNameInput;
    protected GuiComponentDropdownInputBlockMaterial blockMaterialInput;
    protected GuiComponentDropdownInputBlockMapColor blockMapColorInput;
	protected GuiComponentFloatInput blockHardnessInput;
	protected GuiComponentFloatInput blockResistanceInput;
	protected GuiComponentIntegerInput blockHarvestLevelInput;
	protected GuiComponentSuggestionInputToolType blockHarvestToolInput;
	protected GuiComponentListInput<GuiComponentSuggestionInputToolType> blockEffectiveToolsInput;
	protected GuiComponentIntegerInput blockOpacityInput;
	protected GuiComponentIntegerInput blockFlammabilityInput;
	protected GuiComponentIntegerInput blockFireSpreadSpeedInput;
	protected GuiComponentIntegerInput blockBookshelfStrengthInput;
	protected GuiComponentColorInput blockBeaconColorMultiplierInput;
	protected GuiComponentFloatInput blockSlipperinessInput;
	protected GuiComponentBooleanInput blockIsSlimeInput;
	protected GuiComponentBooleanInput blockIsBeaconBaseInput;
	protected GuiComponentBooleanInput blockCanPistonsPushInput;
	protected GuiComponentBooleanInput blockHasCollisionBoxInput;
	protected GuiComponentBooleanInput blockSameCollisionBoundingBoxInput;
	protected GuiComponentFloatInput blockBoundingBoxMinXInput;
	protected GuiComponentFloatInput blockBoundingBoxMinYInput;
	protected GuiComponentFloatInput blockBoundingBoxMinZInput;
	protected GuiComponentFloatInput blockBoundingBoxMaxXInput;
	protected GuiComponentFloatInput blockBoundingBoxMaxYInput;
	protected GuiComponentFloatInput blockBoundingBoxMaxZInput;
	protected GuiComponentFloatInput blockCollisionBoxMinXInput;
	protected GuiComponentFloatInput blockCollisionBoxMinYInput;
	protected GuiComponentFloatInput blockCollisionBoxMinZInput;
	protected GuiComponentFloatInput blockCollisionBoxMaxXInput;
	protected GuiComponentFloatInput blockCollisionBoxMaxYInput;
	protected GuiComponentFloatInput blockCollisionBoxMaxZInput;
	protected GuiComponentButton blockTextureButton;
	
    protected GuiComponentIntegerInput itemBlockStackSizeInput;
    protected GuiComponentBooleanInput itemBlockShinesInput;
    protected GuiComponentIntegerInput itemBlockBurnTimeInput;
    protected GuiComponentListInput<GuiComponentStringInput> itemBlockTooltipInput;
	protected GuiComponentListInput<GuiComponentSuggestionInputOreDict> itemBlockOreDictInput;
	protected GuiComponentDropdownInputItem itemBlockContainerInput;
	protected GuiComponentListInput<GuiComponentAttributeModifierInput> itemBlockAttributesInput;
    
	public GuiEditBlock(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title);
		this.addon = addon;
	}

	@Override
	public void initComponents() {
		
		this.blockIdInput = new GuiComponentStringInput(I18n.format("gui.edit.block.blockId.label"), this);
		if (this.isNew) {
			this.blockIdInput.setRequired();
			this.blockIdInput.setMaxStringLength(32);
			this.blockIdInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.blockIdInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.blockIdInput.setEnabled(false);
			this.blockIdInput.setMaxStringLength(1024);
			this.blockIdInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.blockIdInput.setDefaultText(this.block.getId());
		}
		
		this.blockNameInput = new GuiComponentStringInput(I18n.format("gui.edit.block.blockName.label"), this);
		this.blockNameInput.setRequired();
		this.blockNameInput.setMaxStringLength(128);
		this.blockNameInput.setHasColorSelect();
		this.blockNameInput.setDefaultText(this.block.getDisplayName());
		
		if (!this.isNew) {
			this.blockTextureButton = new GuiComponentButton(this, BUTTON_TEXTURE, I18n.format("gui.edit.item.updateTexture.label"));
			this.blockTextureButton.visible = true;
		}
		
	    this.blockMaterialInput = new GuiComponentDropdownInputBlockMaterial(I18n.format("gui.edit.block.blockMaterial.label"), this);
	    this.blockMaterialInput.setRequired();
	    this.blockMaterialInput.setDefaultMaterialSelected(this.block.getBlockMaterial());
		
	    this.blockMapColorInput = new GuiComponentDropdownInputBlockMapColor(I18n.format("gui.edit.block.mapColor.label"), this);
	    this.blockMapColorInput.setDefaultMapColorSelected(this.block.getBlockMapColor());
	    
		this.blockHardnessInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.hardness.label"), this, true);
		this.blockHardnessInput.setInfo(new TextComponentTranslation("gui.edit.block.hardness.info"));
		this.blockHardnessInput.setMinimum(-1);
		this.blockHardnessInput.setDefaultFloat(this.block.getHardness());
	    
		this.blockResistanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.resistance.label"), this, false);
		this.blockResistanceInput.setInfo(new TextComponentTranslation("gui.edit.block.resistance.info"));
		this.blockResistanceInput.setMinimum(0);
		this.blockResistanceInput.setMaximum(6000000);
		this.blockResistanceInput.setDefaultFloat(this.block.getResistance() / 3.0f);
		
		this.blockHarvestLevelInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.harvestLevel.label"), this, false);
		this.blockHarvestLevelInput.setInfo(new TextComponentTranslation("gui.edit.block.harvestLevel.info"));
		this.blockHarvestLevelInput.setMinimum(0);
		this.blockHarvestLevelInput.setDefaultInteger(this.block.getHarvestLevel());

		this.blockHarvestToolInput = new GuiComponentSuggestionInputToolType(I18n.format("gui.edit.block.harvestTool.label"), this);
		this.blockHarvestToolInput.setInfo(new TextComponentTranslation("gui.edit.block.harvestTool.info"));
		this.blockHarvestToolInput.setDefaultText(this.block.getHarvestTool());
		
		this.blockEffectiveToolsInput = new GuiComponentListInput<GuiComponentSuggestionInputToolType>(I18n.format("gui.edit.block.effectiveTools.label"), this) {
			
			@Override
			public GuiComponentSuggestionInputToolType createBlankComponent() {
				GuiComponentSuggestionInputToolType suggestionInput = new GuiComponentSuggestionInputToolType("", editScreen);
				return suggestionInput;
			}
			
		};
		this.block.getEffectiveTools().forEach(type -> {
			GuiComponentSuggestionInputToolType suggestionInput = this.blockEffectiveToolsInput.createBlankComponent();
			suggestionInput.setDefaultText(type);
			this.blockEffectiveToolsInput.addDefaultComponent(suggestionInput);
		});
		
		this.blockOpacityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.opacity.label"), this, false);
		this.blockOpacityInput.setInfo(new TextComponentTranslation("gui.edit.block.opacity.info"));
		this.blockOpacityInput.setMinimum(0);
		this.blockOpacityInput.setMaximum(15);
		this.blockOpacityInput.setDefaultInteger(this.block.getOpacity());
		
		this.blockFlammabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.flammability.label"), this, false);
		this.blockFlammabilityInput.setInfo(new TextComponentTranslation("gui.edit.block.flammability.info"));
		this.blockFlammabilityInput.setMinimum(0);
		this.blockFlammabilityInput.setDefaultInteger(Blocks.FIRE.getFlammability(this.block.getAsBlock()));
		
		this.blockFireSpreadSpeedInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.fireSpreadSpeed.label"), this, false);
		this.blockFireSpreadSpeedInput.setInfo(new TextComponentTranslation("gui.edit.block.fireSpreadSpeed.info"));
		this.blockFireSpreadSpeedInput.setMinimum(0);
		this.blockFireSpreadSpeedInput.setDefaultInteger(Blocks.FIRE.getEncouragement(this.block.getAsBlock()));
		
		this.blockBookshelfStrengthInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.bookshelfStrength.label"), this, false);
		this.blockBookshelfStrengthInput.setInfo(new TextComponentTranslation("gui.edit.block.bookshelfStrength.info"));
		this.blockBookshelfStrengthInput.setMinimum(0);
		this.blockBookshelfStrengthInput.setDefaultInteger(this.block.getBookshelfStrength());
		
		this.blockBeaconColorMultiplierInput = new GuiComponentColorInput(I18n.format("gui.edit.block.beaconColorMultiplier.label"), this);
		this.blockBeaconColorMultiplierInput.setDefaultColorArray(this.block.getBeaconColorMultiplier());
		
		this.blockSlipperinessInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.slipperiness.label"), this, false);
		this.blockSlipperinessInput.setInfo(new TextComponentTranslation("gui.edit.block.slipperiness.info"));
		this.blockSlipperinessInput.setMinimum(0);
		this.blockSlipperinessInput.setMaximum(1);
		this.blockSlipperinessInput.setDefaultFloat(this.block.getSlipperiness());
		
		this.blockIsSlimeInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.isSlime.label"), this);
		this.blockIsSlimeInput.setDefaultBoolean(this.block.isSlime());
		
		this.blockIsBeaconBaseInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.isBeaconBase.label"), this);
		this.blockIsBeaconBaseInput.setDefaultBoolean(this.block.isBeaconBase());
		
		this.blockCanPistonsPushInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.canPistonsPush.label"), this);
		this.blockCanPistonsPushInput.setDefaultBoolean(this.block.canPistonsPush());
		
		this.blockBoundingBoxMinXInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMinX.label"), this, false);
		this.blockBoundingBoxMinXInput.setMinimum(0);
		this.blockBoundingBoxMinXInput.setMaximum(1);
		this.blockBoundingBoxMinXInput.setDefaultFloat(this.block.getBoundingBoxMinX());
		
		this.blockBoundingBoxMinYInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMinY.label"), this, false);
		this.blockBoundingBoxMinYInput.setMinimum(0);
		this.blockBoundingBoxMinYInput.setMaximum(1);
		this.blockBoundingBoxMinYInput.setDefaultFloat(this.block.getBoundingBoxMinY());
		
		this.blockBoundingBoxMinZInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMinZ.label"), this, false);
		this.blockBoundingBoxMinZInput.setMinimum(0);
		this.blockBoundingBoxMinZInput.setMaximum(1);
		this.blockBoundingBoxMinZInput.setDefaultFloat(this.block.getBoundingBoxMinZ());
		
		this.blockBoundingBoxMaxXInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMaxX.label"), this, false);
		this.blockBoundingBoxMaxXInput.setMinimum(0);
		this.blockBoundingBoxMaxXInput.setMaximum(1);
		this.blockBoundingBoxMaxXInput.setDefaultFloat(this.block.getBoundingBoxMaxX());
		
		this.blockBoundingBoxMaxYInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMaxY.label"), this, false);
		this.blockBoundingBoxMaxYInput.setMinimum(0);
		this.blockBoundingBoxMaxYInput.setMaximum(1);
		this.blockBoundingBoxMaxYInput.setDefaultFloat(this.block.getBoundingBoxMaxY());
		
		this.blockBoundingBoxMaxZInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.boundingBoxMaxZ.label"), this, false);
		this.blockBoundingBoxMaxZInput.setMinimum(0);
		this.blockBoundingBoxMaxZInput.setMaximum(1);
		this.blockBoundingBoxMaxZInput.setDefaultFloat(this.block.getBoundingBoxMaxZ());
	    
		this.blockCollisionBoxMinXInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMinX.label"), this, false);
		this.blockCollisionBoxMinXInput.setMinimum(0);
		this.blockCollisionBoxMinXInput.setMaximum(1);
		this.blockCollisionBoxMinXInput.setDefaultFloat(this.block.getCollisionBoxMinX());
	    
		this.blockCollisionBoxMinYInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMinY.label"), this, false);
		this.blockCollisionBoxMinYInput.setMinimum(0);
		this.blockCollisionBoxMinYInput.setMaximum(1);
		this.blockCollisionBoxMinYInput.setDefaultFloat(this.block.getCollisionBoxMinY());
	    
		this.blockCollisionBoxMinZInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMinZ.label"), this, false);
		this.blockCollisionBoxMinZInput.setMinimum(0);
		this.blockCollisionBoxMinZInput.setMaximum(1);
		this.blockCollisionBoxMinZInput.setDefaultFloat(this.block.getCollisionBoxMinZ());
	    
		this.blockCollisionBoxMaxXInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMaxX.label"), this, false);
		this.blockCollisionBoxMaxXInput.setMinimum(0);
		this.blockCollisionBoxMaxXInput.setMaximum(1);
		this.blockCollisionBoxMaxXInput.setDefaultFloat(this.block.getCollisionBoxMaxX());
	    
		this.blockCollisionBoxMaxYInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMaxY.label"), this, false);
		this.blockCollisionBoxMaxYInput.setMinimum(0);
		this.blockCollisionBoxMaxYInput.setMaximum(1);
		this.blockCollisionBoxMaxYInput.setDefaultFloat(this.block.getCollisionBoxMaxY());
	    
		this.blockCollisionBoxMaxZInput = new GuiComponentFloatInput(I18n.format("gui.edit.block.collisionBoxMaxZ.label"), this, false);
		this.blockCollisionBoxMaxZInput.setMinimum(0);
		this.blockCollisionBoxMaxZInput.setMaximum(1);
		this.blockCollisionBoxMaxZInput.setDefaultFloat(this.block.getCollisionBoxMaxZ());
		
		this.blockHasCollisionBoxInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.hasCollisionBox.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				super.setDefaultBoolean(input);
				GuiEditBlock.this.blockCollisionBoxMinXInput.setHidden(!input);
				GuiEditBlock.this.blockCollisionBoxMinYInput.setHidden(!input);
				GuiEditBlock.this.blockCollisionBoxMinZInput.setHidden(!input);
				GuiEditBlock.this.blockCollisionBoxMaxXInput.setHidden(!input);
				GuiEditBlock.this.blockCollisionBoxMaxYInput.setHidden(!input);
				GuiEditBlock.this.blockCollisionBoxMaxZInput.setHidden(!input);
			}
			
		};
		this.blockHasCollisionBoxInput.setDefaultBoolean(this.block.hasCollisionBox());
		
		this.blockSameCollisionBoundingBoxInput = new GuiComponentBooleanInput(I18n.format("gui.edit.block.sameCollisionBoundingBox.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				super.setDefaultBoolean(input);
				if (input) {
					GuiEditBlock.this.blockHasCollisionBoxInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMinXInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMinYInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMinZInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMaxXInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMaxYInput.setHidden(true);
					GuiEditBlock.this.blockCollisionBoxMaxZInput.setHidden(true);
				} else {
					GuiEditBlock.this.blockHasCollisionBoxInput.setHidden(false);
					GuiEditBlock.this.blockHasCollisionBoxInput.setDefaultBoolean(GuiEditBlock.this.blockHasCollisionBoxInput.getBoolean());
				}
			}
			
		};
		this.blockSameCollisionBoundingBoxInput.setInfo(new TextComponentTranslation("gui.edit.block.sameCollisionBoundingBox.info"));
		this.blockSameCollisionBoundingBoxInput.setDefaultBoolean(this.block.hasCollisionBox());
		
		if (this.block.getItemBlock() != null) {
			IItemAddedBlock itemBlock = this.block.getItemBlock();
			this.itemBlockStackSizeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.maxStackSize.label"), this, false);
			this.itemBlockStackSizeInput.setMinimum(1);
			this.itemBlockStackSizeInput.setMaximum(64);
			this.itemBlockStackSizeInput.setDefaultInteger(itemBlock.getAsItem().getItemStackLimit());
			
			this.itemBlockShinesInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.itemShines.label"), this);
			this.itemBlockShinesInput.setDefaultBoolean(itemBlock.getAlwaysShines());
			
			this.itemBlockBurnTimeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.burnTime.label"), this, false);
			this.itemBlockBurnTimeInput.setMinimum(0);
			this.itemBlockBurnTimeInput.setMaximum(Integer.MAX_VALUE);
			this.itemBlockBurnTimeInput.setInfo(new TextComponentTranslation("gui.edit.item.burnTime.info"));
			this.itemBlockBurnTimeInput.setDefaultInteger(Math.max(itemBlock.getBurnTime(), 0));
			
			this.itemBlockTooltipInput = new GuiComponentListInput<GuiComponentStringInput>(I18n.format("gui.edit.item.itemTooltip.label"), this) {
	
				@Override
				public GuiComponentStringInput createBlankComponent() {
					GuiComponentStringInput input = new GuiComponentStringInput("", this.editScreen);
					input.setMaxStringLength(256);
					input.setHasColorSelect();
					return input;
				}
				
			};
			itemBlock.getTooltip().forEach(toAdd->{
				GuiComponentStringInput input = this.itemBlockTooltipInput.createBlankComponent();
				input.setDefaultText(toAdd);
				this.itemBlockTooltipInput.addDefaultComponent(input);
			});
			
			this.itemBlockOreDictInput = new GuiComponentListInput<GuiComponentSuggestionInputOreDict>(I18n.format("gui.edit.item.oreDictEntries.label"), this) {
	
				@Override
				public GuiComponentSuggestionInputOreDict createBlankComponent() {
					GuiComponentSuggestionInputOreDict input = new GuiComponentSuggestionInputOreDict("", this.editScreen);
					return input;
				}
				
			};
			itemBlock.getOreDict().forEach(toAdd->{
				GuiComponentSuggestionInputOreDict input = this.itemBlockOreDictInput.createBlankComponent();
				input.setDefaultText(toAdd);
				this.itemBlockOreDictInput.addDefaultComponent(input);
			});
			
			this.itemBlockContainerInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.item.itemContainer.label"), this);
			this.itemBlockContainerInput.setDefaultSelected(itemBlock.getAsItem().getContainerItem());
			
			this.itemBlockAttributesInput = new GuiComponentListInput<GuiComponentAttributeModifierInput>(I18n.format("gui.edit.item.attributeModifiers.label"), this) {
	
				@Override
				public GuiComponentAttributeModifierInput createBlankComponent() {
					return new GuiComponentAttributeModifierInput("", this.editScreen);
				}
				
			};
			itemBlock.getAttributeModifiers().forEach((slot, modifier) -> {
				GuiComponentAttributeModifierInput modifierInput = this.itemBlockAttributesInput.createBlankComponent();
				modifierInput.setDefaultAttributeModifier(modifier, slot);
				this.itemBlockAttributesInput.addDefaultComponent(modifierInput);
			});
		}
	}
	
	@Override
	public void onToggleShowAdvanced(boolean showAdvanced) {
		if (showAdvanced) {
			this.blockHasCollisionBoxInput.setDefaultBoolean(this.blockHasCollisionBoxInput.getBoolean());
			this.blockSameCollisionBoundingBoxInput.setDefaultBoolean(this.blockSameCollisionBoundingBoxInput.getBoolean());
		}
	}
	
	@Override
	public void saveObject() {
		String name = this.isNew ? this.addon.id + "-" + this.blockIdInput.getText() : this.block.getId();
		String displayName = this.blockNameInput.getText();
		
		if (this.blockIdInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.block.problem.title"), new TextComponentTranslation("gui.edit.block.problem.noId", displayName), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (displayName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.block.problem.title"), new TextComponentTranslation("gui.edit.block.problem.noName", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.blockMaterialInput.getMaterialSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.block.problem.title"), new TextComponentTranslation("gui.edit.block.problem.noMaterial", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		ResourceLocation registryName = new ResourceLocation(AdditionsMod.MOD_ID, name);
		if (this.isNew && (ForgeRegistries.ITEMS.containsKey(registryName) || ForgeRegistries.BLOCKS.containsKey(registryName))) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.block.problem.title"), new TextComponentTranslation("gui.edit.block.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.block.setDisplayName(displayName);
		this.block.setBlockMaterial(this.blockMaterialInput.getMaterialSelected());
		this.block.setBlockMapColor(this.blockMapColorInput.getMapColorSelected());
		this.block.getAsBlock().setHardness(this.blockHardnessInput.getFloat());
		this.block.getAsBlock().setResistance(this.blockResistanceInput.getFloat());
		this.block.setHarvestLevel(this.blockHarvestLevelInput.getInteger());
		this.block.setHarvestTool(this.blockHarvestToolInput.getText());
		this.block.setEffectiveTools(this.blockEffectiveToolsInput.getComponents().stream().map(GuiComponentSuggestionInputToolType::getText).collect(Collectors.toList()));
		this.block.getAsBlock().setLightOpacity(this.blockOpacityInput.getInteger());
		
		int flammability = this.blockFlammabilityInput.getInteger();
		int fireSpreadSpeed = this.blockFireSpreadSpeedInput.getInteger();
		if (!this.isNew || flammability > 0 || fireSpreadSpeed > 0) {
			Blocks.FIRE.setFireInfo(this.block.getAsBlock(), fireSpreadSpeed, flammability);
		}
		
		this.block.setBookshelfStrength(this.blockBookshelfStrengthInput.getInteger());
		this.block.setBeaconColorMultiplier(this.blockBeaconColorMultiplierInput.getColorArray());
		this.block.setSlipperiness(this.blockSlipperinessInput.getFloat());
		this.block.setIsSlime(this.blockIsSlimeInput.getBoolean());
		this.block.setIsBeaconBase(this.blockIsBeaconBaseInput.getBoolean());
		this.block.setCanPistonsPush(this.blockCanPistonsPushInput.getBoolean());
		this.block.setHasCollisionBox(this.blockHasCollisionBoxInput.getBoolean());
		this.block.setHasSameCollisionBoundingBox(this.blockSameCollisionBoundingBoxInput.getBoolean());
		this.block.setBoundingBoxMinX(this.blockBoundingBoxMinXInput.getFloat());
		this.block.setBoundingBoxMinY(this.blockBoundingBoxMinYInput.getFloat());
		this.block.setBoundingBoxMinZ(this.blockBoundingBoxMinZInput.getFloat());
		this.block.setBoundingBoxMaxX(Math.max(this.blockBoundingBoxMaxXInput.getFloat(), this.block.getBoundingBoxMinX()));
		this.block.setBoundingBoxMaxY(Math.max(this.blockBoundingBoxMaxYInput.getFloat(), this.block.getBoundingBoxMinY()));
		this.block.setBoundingBoxMaxZ(Math.max(this.blockBoundingBoxMaxZInput.getFloat(), this.block.getBoundingBoxMinZ()));
		this.block.setCollisionBoxMinX(this.blockCollisionBoxMinXInput.getFloat());
		this.block.setCollisionBoxMinY(this.blockCollisionBoxMinYInput.getFloat());
		this.block.setCollisionBoxMinZ(this.blockCollisionBoxMinZInput.getFloat());
		this.block.setCollisionBoxMaxX(Math.max(this.blockCollisionBoxMaxXInput.getFloat(), this.block.getCollisionBoxMinX()));
		this.block.setCollisionBoxMaxY(Math.max(this.blockCollisionBoxMaxYInput.getFloat(), this.block.getCollisionBoxMinY()));
		this.block.setCollisionBoxMaxZ(Math.max(this.blockCollisionBoxMaxZInput.getFloat(), this.block.getCollisionBoxMinZ()));
		
		if (this.block.getItemBlock() != null) {
			IItemAddedBlock itemBlock = this.block.getItemBlock();
			itemBlock.setDisplayName(displayName);
			itemBlock.getAsItem().setMaxStackSize(this.itemBlockStackSizeInput.getInteger());
			itemBlock.setAlwaysShines(this.itemBlockShinesInput.getBoolean());
			itemBlock.setBurnTime(this.itemBlockBurnTimeInput.getInteger() == 0 ? -1 : this.itemBlockBurnTimeInput.getInteger());
			
			List<String> tooltips = new ArrayList<>();
			this.itemBlockTooltipInput.getComponents().forEach(stringInput -> tooltips.add(stringInput.getText()));
			itemBlock.setTooltip(tooltips);
			
			List<String> oreDicts = new ArrayList<>();
			this.itemBlockOreDictInput.getComponents().forEach(stringInput -> oreDicts.add(stringInput.getText()));
			itemBlock.setOreDict(oreDicts);
			
			itemBlock.getAsItem().setContainerItem(this.itemBlockContainerInput.getSelected());
			
			Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
			if (!this.itemBlockAttributesInput.getComponents().isEmpty()) {
				for (GuiComponentAttributeModifierInput modifierInput : this.itemBlockAttributesInput.getComponents()) {
					attributeModifiers.put(modifierInput.getSlot(), modifierInput.getAttributeModifier());
				}
			}
			itemBlock.setAttributeModifiers(attributeModifiers);
			
			if (this.isNew) {
				itemBlock.getAsItem().setUnlocalizedName(name);
				itemBlock.getAsItem().setRegistryName(registryName);
			}
		}
		
		if (this.isNew) {
			this.block.getAsBlock().setUnlocalizedName(name);
			this.block.getAsBlock().setRegistryName(registryName);
		}
		
		AdditionTypeBlock.INSTANCE.saveAddition(this.addon, this.block);
			
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		if (this.isNew) {
    		this.openTextureDialogue();
		} else {
			this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.updated.title"), new TextComponentTranslation("gui.warnDialogue.restart.updated.message")));
		}
	}
	
	@Override
	public void refreshView() {
		this.itemBlockOreDictInput.getComponents().forEach(input -> {
			input.refreshOreNames();
		});
	}
	
	public void copyFrom(T item) {
		this.copyFrom = item;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.blockNameInput.setDefaultText(this.copyFrom.getDisplayName());
	    this.blockMaterialInput.setDefaultMaterialSelected(this.copyFrom.getBlockMaterial());
	    this.blockMapColorInput.setDefaultMapColorSelected(this.copyFrom.getBlockMapColor());
	    this.blockHardnessInput.setDefaultFloat(this.copyFrom.getHardness());
	    this.blockResistanceInput.setDefaultFloat(this.copyFrom.getResistance());
	    this.blockHarvestLevelInput.setDefaultInteger(this.copyFrom.getHarvestLevel());
	    this.blockHarvestToolInput.setDefaultText(this.copyFrom.getHarvestTool());
	    
		this.blockEffectiveToolsInput.removeAllComponents();
		this.copyFrom.getEffectiveTools().forEach(type -> {
			GuiComponentSuggestionInputToolType suggestionInput = this.blockEffectiveToolsInput.createBlankComponent();
			suggestionInput.setDefaultText(type);
			this.blockEffectiveToolsInput.addDefaultComponent(suggestionInput);
		});
		
	    this.blockOpacityInput.setDefaultInteger(this.copyFrom.getOpacity());
	    this.blockFlammabilityInput.setDefaultInteger(Blocks.FIRE.getFlammability(this.copyFrom.getAsBlock()));
	    this.blockFireSpreadSpeedInput.setDefaultInteger(Blocks.FIRE.getEncouragement(this.copyFrom.getAsBlock()));
	    this.blockBookshelfStrengthInput.setDefaultInteger(this.copyFrom.getBookshelfStrength());
	    this.blockBeaconColorMultiplierInput.setDefaultColorArray(this.copyFrom.getBeaconColorMultiplier());
	    this.blockSlipperinessInput.setDefaultFloat(this.copyFrom.getSlipperiness());
	    this.blockIsSlimeInput.setDefaultBoolean(this.copyFrom.isSlime());
	    this.blockIsBeaconBaseInput.setDefaultBoolean(this.copyFrom.isBeaconBase());
	    this.blockCanPistonsPushInput.setDefaultBoolean(this.copyFrom.canPistonsPush());
	    this.blockHasCollisionBoxInput.setDefaultBoolean(this.copyFrom.hasCollisionBox());
	    this.blockSameCollisionBoundingBoxInput.setDefaultBoolean(this.copyFrom.hasSameCollisionBoundingBox());
	    this.blockBoundingBoxMinXInput.setDefaultFloat(this.copyFrom.getBoundingBoxMinX());
	    this.blockBoundingBoxMinYInput.setDefaultFloat(this.copyFrom.getBoundingBoxMinY());
	    this.blockBoundingBoxMinZInput.setDefaultFloat(this.copyFrom.getBoundingBoxMinZ());
	    this.blockBoundingBoxMinXInput.setDefaultFloat(this.copyFrom.getBoundingBoxMinX());
	    this.blockBoundingBoxMaxYInput.setDefaultFloat(this.copyFrom.getBoundingBoxMaxY());
	    this.blockBoundingBoxMaxZInput.setDefaultFloat(this.copyFrom.getBoundingBoxMaxZ());
	    this.blockCollisionBoxMinXInput.setDefaultFloat(this.copyFrom.getCollisionBoxMinX());
	    this.blockCollisionBoxMinYInput.setDefaultFloat(this.copyFrom.getCollisionBoxMinY());
	    this.blockCollisionBoxMinZInput.setDefaultFloat(this.copyFrom.getCollisionBoxMinZ());
	    this.blockCollisionBoxMinXInput.setDefaultFloat(this.copyFrom.getCollisionBoxMinX());
	    this.blockCollisionBoxMaxYInput.setDefaultFloat(this.copyFrom.getCollisionBoxMaxY());
	    this.blockCollisionBoxMaxZInput.setDefaultFloat(this.copyFrom.getCollisionBoxMaxZ());
		
		if (this.copyFrom.getItemBlock() != null && this.block.getItemBlock() != null) {
			IItemAddedBlock itemBlock = this.copyFrom.getItemBlock();
			this.itemBlockStackSizeInput.setDefaultInteger(itemBlock.getAsItem().getItemStackLimit());
			this.itemBlockShinesInput.setDefaultBoolean(itemBlock.getAlwaysShines());
			this.itemBlockBurnTimeInput.setDefaultInteger(Math.max(itemBlock.getBurnTime(), 0));
			
			this.itemBlockTooltipInput.removeAllComponents();
			itemBlock.getTooltip().forEach(toAdd -> {
				GuiComponentStringInput input = this.itemBlockTooltipInput.createBlankComponent();
				input.setDefaultText(toAdd);
				this.itemBlockTooltipInput.addDefaultComponent(input);
			});
	
			this.itemBlockOreDictInput.removeAllComponents();
			itemBlock.getOreDict().forEach(toAdd -> {
				GuiComponentSuggestionInputOreDict input = this.itemBlockOreDictInput.createBlankComponent();
				input.setDefaultText(toAdd);
				this.itemBlockOreDictInput.addDefaultComponent(input);
			});
			
			this.itemBlockContainerInput.setDefaultSelected(itemBlock.getAsItem().getContainerItem());
			this.itemBlockAttributesInput.removeAllComponents();
			itemBlock.getAttributeModifiers().forEach((slot, modifier) -> {
				GuiComponentAttributeModifierInput modifierInput = this.itemBlockAttributesInput.createBlankComponent();
				modifierInput.setDefaultAttributeModifier(modifier, slot);
				this.itemBlockAttributesInput.addDefaultComponent(modifierInput);
			});
		}
		
		this.copyFrom = null;
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_TEXTURE) {
    		this.openTextureDialogue();
    	} else {
    		super.actionPerformed(button);
    	}
    }
    
    protected void openTextureDialogue() {
    	GuiScreen nextScreen;
    	if (this.isNew) {
    		 nextScreen = getBlockCreatedPopup();
    	} else {
    		nextScreen = this;
    	}
    	
    	this.mc.displayGuiScreen(new GuiEditBlockTexture(nextScreen, this.addon, this.block, this.isNew));
    }
    
    protected GuiScreen getBlockCreatedPopup() {
    	return new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.created.title"), new TextComponentTranslation("gui.warnDialogue.restart.created.message"));
    }

}
