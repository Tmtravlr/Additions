package com.tmtravlr.additions.gui.view.edit.item;

import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.IItemAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputToolMaterial;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;
import com.tmtravlr.additions.type.AdditionTypeItemMaterial;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new projectile or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2018
 */
public abstract class GuiEditItemBaseProjectile<T extends IItemAdded & IItemAddedProjectile> extends GuiEditItem<T> {
	
	protected GuiComponentFloatInput itemBaseDamageInput;
	protected GuiComponentFloatInput itemBasePunchInput;
	protected GuiComponentFloatInput itemGravityInput;
	protected GuiComponentBooleanInput itemWorksInWaterInput;
	protected GuiComponentBooleanInput itemSticksInGroundInput;
	protected GuiComponentBooleanInput itemHasPotionEffectsInput;
	protected GuiComponentBooleanInput itemWorksWithInfinityInput;
	protected GuiComponentBooleanInput itemRenders3DInput;
	protected GuiComponentBooleanInput itemPiercesEntitiesInput;
	protected GuiComponentBooleanInput itemDamageIgnoresSpeedInput;
	protected GuiComponentDropdownInputSoundEvent itemHitSoundInput;
	protected GuiComponentListInput<GuiComponentEffectInput> itemHitEffectsInput;
    
	public GuiEditItemBaseProjectile(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemBaseDamageInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.projectile.baseDamage.label"), this, false);
		this.itemBaseDamageInput.setMinimum(0);
		if (!this.isNew) {
			this.itemBaseDamageInput.setDefaultFloat(this.item.getBaseDamage());
		}

		this.itemBasePunchInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.projectile.basePunch.label"), this, false);
		this.itemBasePunchInput.setMinimum(0);
		if (!this.isNew) {
			this.itemBasePunchInput.setDefaultFloat(this.item.getBasePunch());
		}

		this.itemGravityInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.projectile.gravity.label"), this, true);
		if (!this.isNew) {
			this.itemGravityInput.setDefaultFloat(this.item.getGravity());
		} else {
			this.itemGravityInput.setDefaultFloat(this.item instanceof ItemAddedThrowable ? 0.03f : 0.05f);
		}
		
		this.itemWorksInWaterInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.worksInWater.label"), this);
		if (!this.isNew) {
			this.itemWorksInWaterInput.setDefaultBoolean(this.item.worksInWater());
		}
		
		this.itemSticksInGroundInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.sticksInGround.label"), this);
		if (!this.isNew) {
			this.itemSticksInGroundInput.setDefaultBoolean(this.item.sticksInGround());
		} else {
			this.itemSticksInGroundInput.setDefaultBoolean(this.item instanceof ItemAddedThrowable ? false : true);
		}
		
		this.itemHasPotionEffectsInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.hasPotionEffects.label"), this);
		this.itemHasPotionEffectsInput.setInfo(new TextComponentTranslation("gui.edit.item.hasPotionEffect.info"));
		if (!this.isNew) {
			this.itemHasPotionEffectsInput.setDefaultBoolean(this.item.hasPotionEffects());
		}
		
		this.itemWorksWithInfinityInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.worksWithInfinity.label"), this);
		if (!this.isNew) {
			this.itemWorksWithInfinityInput.setDefaultBoolean(this.item.worksWithInfinity());
		}
		
		this.itemRenders3DInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.renders3D.label"), this);
		if (!this.isNew) {
			this.itemRenders3DInput.setDefaultBoolean(this.item.renders3D());
		} else {
			this.itemSticksInGroundInput.setDefaultBoolean(this.item instanceof ItemAddedThrowable ? false : true);
		}
		
		this.itemPiercesEntitiesInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.piercesEntities.label"), this);
		if (!this.isNew) {
			this.itemPiercesEntitiesInput.setDefaultBoolean(this.item.piercesEntities());
		}
		
		this.itemDamageIgnoresSpeedInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.projectile.damageIgnoresSpeed.label"), this);
		this.itemDamageIgnoresSpeedInput.setInfo(new TextComponentTranslation("gui.edit.item.projectile.damageIgnoresSpeed.info"));
		if (!this.isNew) {
			this.itemDamageIgnoresSpeedInput.setDefaultBoolean(this.item.damageIgnoresSpeed());
		}
		
		this.itemHitSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.item.projectile.hitSound.label"), this.addon, this);
		if (!this.isNew) {
			this.itemHitSoundInput.setDefaultSelected(this.item.getHitSound());
		} else if (!(this.item instanceof ItemAddedThrowable)) {
			this.itemHitSoundInput.setDefaultSelected(SoundEvents.ENTITY_ARROW_HIT);
		}
		
		this.itemHitEffectsInput = new GuiComponentListInput<GuiComponentEffectInput>(I18n.format("gui.edit.item.projectile.hitEffects.label"), this) {

			@Override
			public GuiComponentEffectInput createBlankComponent() {
				return new GuiComponentEffectInput("", GuiEditItemBaseProjectile.this.addon, this.editScreen);
			}
			
		};
		if (!this.isNew) {
			this.item.getHitEffects().forEach(toAdd -> {
				GuiComponentEffectInput input = this.itemHitEffectsInput.createBlankComponent();
				input.setDefaultEffect(toAdd);
				this.itemHitEffectsInput.addDefaultComponent(input);
			});
		}
	}
	
	@Override
	public void saveObject() {
		
		this.item.setBaseDamage(this.itemBaseDamageInput.getFloat());
		this.item.setBasePunch(this.itemBasePunchInput.getFloat());
		this.item.setGravity(this.itemGravityInput.getFloat());
		this.item.setWorksInWater(this.itemWorksInWaterInput.getBoolean());
		this.item.setSticksInGround(this.itemSticksInGroundInput.getBoolean());
		this.item.setHasPotionEffects(this.itemHasPotionEffectsInput.getBoolean());
		this.item.setWorksWithInfinity(this.itemWorksWithInfinityInput.getBoolean());
		this.item.setRenders3D(this.itemRenders3DInput.getBoolean());
		this.item.setPiercesEntities(this.itemPiercesEntitiesInput.getBoolean());
		this.item.setDamageIgnoresSpeed(this.itemDamageIgnoresSpeedInput.getBoolean());
		this.item.setHitSound(this.itemHitSoundInput.getSelected());
		
		if (!this.itemHitEffectsInput.getComponents().isEmpty()) {
			this.item.setHitEffects(this.itemHitEffectsInput.getComponents().stream().filter(input -> input.getEffect() != null).map(GuiComponentEffectInput::getEffect).collect(Collectors.toList()));
		}
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemBaseDamageInput.setDefaultFloat(this.copyFrom.getBaseDamage());
		this.itemBasePunchInput.setDefaultFloat(this.copyFrom.getBasePunch());
		this.itemGravityInput.setDefaultFloat(this.copyFrom.getGravity());
		this.itemWorksInWaterInput.setDefaultBoolean(this.copyFrom.worksInWater());
		this.itemSticksInGroundInput.setDefaultBoolean(this.copyFrom.sticksInGround());
		this.itemHasPotionEffectsInput.setDefaultBoolean(this.copyFrom.hasPotionEffects());
		this.itemWorksWithInfinityInput.setDefaultBoolean(this.copyFrom.worksWithInfinity());
		this.itemRenders3DInput.setDefaultBoolean(this.copyFrom.renders3D());
		this.itemPiercesEntitiesInput.setDefaultBoolean(this.copyFrom.piercesEntities());
		this.itemDamageIgnoresSpeedInput.setDefaultBoolean(this.copyFrom.damageIgnoresSpeed());
		this.itemHitSoundInput.setDefaultSelected(this.copyFrom.getHitSound());
		
		this.copyFrom.getHitEffects().forEach(toAdd -> {
			GuiComponentEffectInput input = this.itemHitEffectsInput.createBlankComponent();
			input.setDefaultEffect(toAdd);
			this.itemHitEffectsInput.addDefaultComponent(input);
		});
		
		super.copyFromOther();
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		
		this.itemHitSoundInput.refreshSelections();
	}
}
