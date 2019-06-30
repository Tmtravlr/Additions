package com.tmtravlr.additions.gui.view.edit.block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentAttributeModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockDropInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentColorInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMapColor;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMaterial;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputOreDict;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputToolType;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditBlockTexture;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditBlockDropInitial;
import com.tmtravlr.additions.type.AdditionTypeBlock;
import com.tmtravlr.additions.type.AdditionTypeLootTable;

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
	protected GuiComponentIntegerInput blockLightLevelInput;
	protected GuiComponentIntegerInput blockFlammabilityInput;
	protected GuiComponentIntegerInput blockFireSpreadSpeedInput;
	protected GuiComponentIntegerInput blockBookshelfStrengthInput;
	protected GuiComponentColorInput blockBeaconColorMultiplierInput;
	protected GuiComponentFloatInput blockSlipperinessInput;
	protected GuiComponentBooleanInput blockIsSlimeInput;
	protected GuiComponentBooleanInput blockIsBeaconBaseInput;
	protected GuiComponentBooleanInput blockCanPistonsPushInput;
	protected GuiComponentDropdownInputSoundEvent blockPlaceSoundInput;
	protected GuiComponentDropdownInputSoundEvent blockBreakSoundInput;
	protected GuiComponentDropdownInputSoundEvent blockHitSoundInput;
	protected GuiComponentDropdownInputSoundEvent blockStepSoundInput;
	protected GuiComponentDropdownInputSoundEvent blockFallSoundInput;
	protected GuiComponentBlockDropInput blockDropInput;
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
			this.blockTextureButton = new GuiComponentButton(this, BUTTON_TEXTURE, I18n.format("gui.edit.block.updateTexture.label"));
			this.blockTextureButton.visible = true;
			
			this.blockDropInput = new GuiComponentBlockDropInput(I18n.format("gui.edit.block.drop.label"), this.addon, this.block, this.createDefaultLootTable(), this);
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
		
		this.blockLightLevelInput = new GuiComponentIntegerInput(I18n.format("gui.edit.block.lightLevel.label"), this, false);
		this.blockLightLevelInput.setInfo(new TextComponentTranslation("gui.edit.block.lightLevel.info"));
		this.blockLightLevelInput.setMinimum(0);
		this.blockLightLevelInput.setMaximum(15);
		this.blockLightLevelInput.setDefaultInteger(this.block.getLightLevel());
		
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
		
		this.blockPlaceSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.block.placeSound.label"), this.addon, this);
		this.blockPlaceSoundInput.setDefaultSelected(this.block.getPlaceSound());
		
		this.blockBreakSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.block.breakSound.label"), this.addon, this);
		this.blockBreakSoundInput.setDefaultSelected(this.block.getBreakSound());
		
		this.blockHitSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.block.hitSound.label"), this.addon, this);
		this.blockHitSoundInput.setDefaultSelected(this.block.getHitSound());
		
		this.blockStepSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.block.stepSound.label"), this.addon, this);
		this.blockStepSoundInput.setDefaultSelected(this.block.getStepSound());
		
		this.blockFallSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.block.fallSound.label"), this.addon, this);
		this.blockFallSoundInput.setDefaultSelected(this.block.getFallSound());
		
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
		this.block.getAsBlock().setLightLevel(this.blockLightLevelInput.getInteger() / 15F);
		
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
		this.block.setPlaceSound(this.blockPlaceSoundInput.getSelected());
		this.block.setBreakSound(this.blockBreakSoundInput.getSelected());
		this.block.setHitSound(this.blockHitSoundInput.getSelected());
		this.block.setStepSound(this.blockStepSoundInput.getSelected());
		this.block.setFallSound(this.blockFallSoundInput.getSelected());
		
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
		} else {
			if (this.blockDropInput.hasChanges()) {
				AdditionTypeLootTable.INSTANCE.saveAddition(this.addon, this.blockDropInput.getBlockDropTable());
			}
		}
		
		AdditionTypeBlock.INSTANCE.saveAddition(this.addon, this.block);
			
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		if (this.isNew) {
    		this.mc.displayGuiScreen(this.getBlockDropDialogue());
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
	    this.blockLightLevelInput.setDefaultInteger(this.copyFrom.getLightLevel());
	    this.blockFlammabilityInput.setDefaultInteger(Blocks.FIRE.getFlammability(this.copyFrom.getAsBlock()));
	    this.blockFireSpreadSpeedInput.setDefaultInteger(Blocks.FIRE.getEncouragement(this.copyFrom.getAsBlock()));
	    this.blockBookshelfStrengthInput.setDefaultInteger(this.copyFrom.getBookshelfStrength());
	    this.blockBeaconColorMultiplierInput.setDefaultColorArray(this.copyFrom.getBeaconColorMultiplier());
	    this.blockSlipperinessInput.setDefaultFloat(this.copyFrom.getSlipperiness());
	    this.blockIsSlimeInput.setDefaultBoolean(this.copyFrom.isSlime());
	    this.blockIsBeaconBaseInput.setDefaultBoolean(this.copyFrom.isBeaconBase());
	    this.blockCanPistonsPushInput.setDefaultBoolean(this.copyFrom.canPistonsPush());
	    this.blockPlaceSoundInput.setDefaultSelected(this.copyFrom.getPlaceSound());
	    this.blockBreakSoundInput.setDefaultSelected(this.copyFrom.getBreakSound());
	    this.blockHitSoundInput.setDefaultSelected(this.copyFrom.getHitSound());
	    this.blockStepSoundInput.setDefaultSelected(this.copyFrom.getStepSound());
	    this.blockFallSoundInput.setDefaultSelected(this.copyFrom.getFallSound());
		
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
    		this.mc.displayGuiScreen(this.getTextureDialogue(this));
    	} else {
    		super.actionPerformed(button);
    	}
    }
    
    protected GuiScreen getTextureDialogue(GuiScreen nextScreen) {
    	return new GuiEditBlockTexture(nextScreen, this.addon, this.block, this.isNew);
    }
    
    protected GuiScreen getBlockDropDialogue() {
    	GuiScreen textureScreen = this.getTextureDialogue(this.getBlockCreatedPopup());
    	LootTableAdded defaultLootTable = this.createDefaultLootTable();
    	return new GuiMessageBoxTwoButton(this, new GuiEditBlockDropInitial(this, textureScreen, defaultLootTable, this.block, this.addon), I18n.format("gui.edit.blockDrop.popup.title"), new TextComponentTranslation("gui.edit.blockDrop.popup.message"), I18n.format("gui.edit.blockDrop.popup.useDefault"), I18n.format("gui.edit.blockDrop.popup.customize")) {
    		
    		@Override
    		public void onFirstButtonClicked() {
    			AdditionTypeLootTable.INSTANCE.saveAddition(GuiEditBlock.this.addon, defaultLootTable);
    			this.mc.displayGuiScreen(textureScreen);
    		}
    	};
    }
    
    protected GuiScreen getBlockCreatedPopup() {
    	return new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.created.title"), new TextComponentTranslation("gui.warnDialogue.restart.created.message"));
    }
    
    protected ResourceLocation getBlockDropLocation() {
    	return new ResourceLocation(AdditionsMod.MOD_ID, "blocks/" + this.block.getId());
    }
    
    protected LootTableAdded createDefaultLootTable() {
    	ResourceLocation blockDropLocation = this.getBlockDropLocation();
    	
    	LootTablePresetBlockItself blockDropPreset = new LootTablePresetBlockItself();
    	blockDropPreset.id = blockDropLocation;
    	blockDropPreset.block = this.block.getAsBlock();
    	
    	return new LootTableAdded(blockDropLocation, blockDropPreset);
    }

}
