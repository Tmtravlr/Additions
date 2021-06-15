package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntity;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityWithAttacker;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.DamageTypeChecker;
import com.tmtravlr.additions.util.EntityCategoryChecker;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the effect cause with an attacker.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2021
 */
public abstract class GuiEditHandlerEffectCauseEntityWithAttacker implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentBooleanInput checkEntityInput;
	private GuiComponentListInput<GuiComponentDropdownInput<ResourceLocation>> entityTypeInput;
	private GuiComponentListInput<GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory>> entityCategoryInput;
	private GuiComponentNBTInput entityTagInput;
	private GuiComponentBooleanInput checkAttackerInput;
	private GuiComponentListInput<GuiComponentDropdownInput<ResourceLocation>> attackerTypeInput;
	private GuiComponentListInput<GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory>> attackerCategoryInput;
	private GuiComponentNBTInput attackerTagInput;
	private GuiComponentBooleanInput checkDamageSourceInput;
	private GuiComponentDisplayText damageTypeInfo;
	private GuiComponentListInput<GuiComponentDropdownInput<DamageTypeChecker.DamageType>> damageTypeInput;
	private GuiComponentListInput<GuiComponentDropdownInput<DamageTypeChecker.DamageCategory>> damageCategoryInput;
	private GuiComponentBooleanInput targetSelfInput;
	
	protected abstract TextComponentBase getDescription();

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseEntityWithAttacker specificCause = (cause instanceof EffectCauseEntityWithAttacker) ? (EffectCauseEntityWithAttacker) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, this.getDescription());

		this.entityTypeInput = new GuiComponentListInput<GuiComponentDropdownInput<ResourceLocation>>(I18n.format("gui.edit.effectCause.entity.entityTypes.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<ResourceLocation> createBlankComponent() {
				GuiComponentDropdownInput<ResourceLocation> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				
				dropdown.addSelection(EffectCauseEntity.PLAYER_ENTITY);
				for (ResourceLocation type : EntityList.getEntityNameList()) {
					Class entityClass = EntityList.getClass(type);
					
					if (entityClass != null && EntityLivingBase.class.isAssignableFrom(entityClass)) {
						dropdown.addSelection(type);
					}
				}
				return dropdown;
			}
			
		};
		this.entityTypeInput.setHidden(true);
		if (specificCause != null && !specificCause.entityTypes.isEmpty()) {
			specificCause.entityTypes.forEach(entityType -> {
				GuiComponentDropdownInput<ResourceLocation> input = this.entityTypeInput.createBlankComponent();
				input.setDefaultSelected(entityType);
				this.entityTypeInput.addDefaultComponent(input);
			});
		}

		this.entityCategoryInput = new GuiComponentListInput<GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory>>(I18n.format("gui.edit.effectCause.entity.entityCategories.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> createBlankComponent() {
				GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				dropdown.setSelections(EntityCategoryChecker.EntityCategory.values());
				return dropdown;
			}
			
		};
		this.entityCategoryInput.setHidden(true);
		if (specificCause != null && !specificCause.entityCategories.isEmpty()) {
			specificCause.entityCategories.forEach(entityCategory -> {
				GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> input = this.entityCategoryInput.createBlankComponent();
				input.setDefaultSelected(entityCategory);
				this.entityCategoryInput.addDefaultComponent(input);
			});
		}
		
		this.entityTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.effectCause.entity.entityTag.label"), editScreen);
		this.entityTagInput.setHidden(true);
		if (specificCause != null && specificCause.entityTag != null && !specificCause.entityTag.hasNoTags()) {
			this.entityTagInput.setDefaultText(specificCause.entityTag.toString());
		}
		
		this.checkEntityInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.entityAttacked.checkEntity.label"), editScreen) {
			
			@Override
			public void setDefaultBoolean(boolean checked) {
				if (checked) {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityTypeInput.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityCategoryInput.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityTagInput.setHidden(false);
				} else {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityTypeInput.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityCategoryInput.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.entityTagInput.setHidden(true);
				}
				
				super.setDefaultBoolean(checked);
			}
			
		};
		if (specificCause != null) {
			this.checkEntityInput.setDefaultBoolean(specificCause.checkEntity);
		}

		this.attackerTypeInput = new GuiComponentListInput<GuiComponentDropdownInput<ResourceLocation>>(I18n.format("gui.edit.effectCause.entityAttacked.attackerTypes.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<ResourceLocation> createBlankComponent() {
				GuiComponentDropdownInput<ResourceLocation> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);

				dropdown.addSelection(EffectCauseEntity.PLAYER_ENTITY);
				for (ResourceLocation type : EntityList.getEntityNameList()) {
					Class entityClass = EntityList.getClass(type);
					
					if (entityClass != null && EntityLivingBase.class.isAssignableFrom(entityClass)) {
						dropdown.addSelection(type);
					}
				}
				return dropdown;
			}
			
		};
		this.attackerTypeInput.setHidden(true);
		if (specificCause != null && !specificCause.attackerTypes.isEmpty()) {
			specificCause.attackerTypes.forEach(entityType -> {
				GuiComponentDropdownInput<ResourceLocation> input = this.attackerTypeInput.createBlankComponent();
				input.setDefaultSelected(entityType);
				this.attackerTypeInput.addDefaultComponent(input);
			});
		}

		this.attackerCategoryInput = new GuiComponentListInput<GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory>>(I18n.format("gui.edit.effectCause.entityAttacked.attackerCategories.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> createBlankComponent() {
				GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				dropdown.setSelections(EntityCategoryChecker.EntityCategory.values());
				return dropdown;
			}
			
		};
		this.attackerCategoryInput.setHidden(true);
		if (specificCause != null && !specificCause.attackerCategories.isEmpty()) {
			specificCause.attackerCategories.forEach(entityCategory -> {
				GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> input = this.attackerCategoryInput.createBlankComponent();
				input.setDefaultSelected(entityCategory);
				this.attackerCategoryInput.addDefaultComponent(input);
			});
		}
		
		this.attackerTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.effectCause.entityAttacked.attackerTag.label"), editScreen);
		this.attackerTagInput.setHidden(true);
		if (specificCause != null && specificCause.attackerTag != null && !specificCause.attackerTag.hasNoTags()) {
			this.attackerTagInput.setDefaultText(specificCause.attackerTag.toString());
		}
		
		this.checkAttackerInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.entityAttacked.checkAttacker.label"), editScreen) {
			
			@Override
			public void setDefaultBoolean(boolean checked) {
				if (checked) {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerTypeInput.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerCategoryInput.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerTagInput.setHidden(false);
				} else {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerTypeInput.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerCategoryInput.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.attackerTagInput.setHidden(true);
				}
				
				super.setDefaultBoolean(checked);
			}
			
		};
		if (specificCause != null) {
			this.checkAttackerInput.setDefaultBoolean(specificCause.checkAttacker);
		}

		this.damageTypeInfo = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("gui.edit.effectCause.entityAttacked.damageTypes.info"));
		this.damageTypeInfo.setHidden(true);
		
		this.damageTypeInput = new GuiComponentListInput<GuiComponentDropdownInput<DamageTypeChecker.DamageType>>(I18n.format("gui.edit.effectCause.entityAttacked.damageTypes.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<DamageTypeChecker.DamageType> createBlankComponent() {
				GuiComponentDropdownInput<DamageTypeChecker.DamageType> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				dropdown.setSelections(DamageTypeChecker.DamageType.values());
				return dropdown;
			}
			
		};
		this.damageTypeInput.setHidden(true);
		if (specificCause != null && !specificCause.attackerCategories.isEmpty()) {
			specificCause.damageTypes.forEach(type -> {
				GuiComponentDropdownInput<DamageTypeChecker.DamageType> input = this.damageTypeInput.createBlankComponent();
				input.setDefaultSelected(type);
				this.damageTypeInput.addDefaultComponent(input);
			});
		}

		this.damageCategoryInput = new GuiComponentListInput<GuiComponentDropdownInput<DamageTypeChecker.DamageCategory>>(I18n.format("gui.edit.effectCause.entityAttacked.damageCategories.label"), this.editScreen) {

			@Override
			public GuiComponentDropdownInput<DamageTypeChecker.DamageCategory> createBlankComponent() {
				GuiComponentDropdownInput<DamageTypeChecker.DamageCategory> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				dropdown.setSelections(DamageTypeChecker.DamageCategory.values());
				return dropdown;
			}
			
		};
		this.damageCategoryInput.setHidden(true);
		if (specificCause != null && !specificCause.attackerCategories.isEmpty()) {
			specificCause.damageCategories.forEach(category -> {
				GuiComponentDropdownInput<DamageTypeChecker.DamageCategory> input = this.damageCategoryInput.createBlankComponent();
				input.setDefaultSelected(category);
				this.damageCategoryInput.addDefaultComponent(input);
			});
		}
		
		this.checkDamageSourceInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.entityAttacked.checkDamageSource.label"), editScreen) {
			
			@Override
			public void setDefaultBoolean(boolean checked) {
				if (checked) {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageTypeInfo.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageTypeInput.setHidden(false);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageCategoryInput.setHidden(false);
				} else {
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageTypeInfo.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageTypeInput.setHidden(true);
					GuiEditHandlerEffectCauseEntityWithAttacker.this.damageCategoryInput.setHidden(true);
				}
				
				super.setDefaultBoolean(checked);
			}
			
		};
		if (specificCause != null) {
			this.checkDamageSourceInput.setDefaultBoolean(specificCause.checkDamageSource);
		}
		
		this.targetSelfInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.targetSelf.label"), editScreen);
		if (specificCause != null) {
			this.targetSelfInput.setDefaultBoolean(specificCause.targetSelf);
		}
		
		return Arrays.asList(this.description, this.checkEntityInput, this.entityTypeInput, this.entityCategoryInput, this.entityTagInput, 
				this.checkAttackerInput, this.attackerTypeInput, this.attackerCategoryInput, this.attackerTagInput, 
				this.checkDamageSourceInput, this.damageTypeInfo, this.damageTypeInput, this.damageCategoryInput, this.targetSelfInput);
	}

	public EffectCause populateEffectCause(EffectCauseEntityWithAttacker cause) {
		if (this.editScreen == null || this.checkEntityInput == null || this.entityTypeInput == null || this.entityCategoryInput == null || this.entityTagInput == null 
				|| this.checkAttackerInput == null || this.attackerTypeInput == null || this.attackerCategoryInput == null || this.attackerTagInput == null 
				|| this.checkDamageSourceInput == null || this.damageTypeInfo == null || this.damageTypeInput == null || this.damageCategoryInput == null || this.targetSelfInput == null) {
			return null;
		}
		
		if (this.checkEntityInput.getBoolean()) {
			cause.checkEntity = true;
			
			cause.entityTypes = new HashSet<>();
			this.entityTypeInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.entityTypes.add(component.getSelected());
				}
			});
			
			cause.entityCategories = new HashSet<>();
			this.entityCategoryInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.entityCategories.add(component.getSelected());
				}
			});
			
			cause.entityTag = this.entityTagInput.getTag();
		}
		
		if (this.checkAttackerInput.getBoolean()) {
			cause.checkAttacker = true;
			
			cause.attackerTypes = new HashSet<>();
			this.attackerTypeInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.attackerTypes.add(component.getSelected());
				}
			});
			
			cause.attackerCategories = new HashSet<>();
			this.attackerCategoryInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.attackerCategories.add(component.getSelected());
				}
			});
			
			cause.attackerTag = this.attackerTagInput.getTag();
		}
		
		if (this.checkDamageSourceInput.getBoolean()) {
			cause.checkDamageSource = true;
			
			cause.damageTypes = new HashSet<>();
			this.damageTypeInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.damageTypes.add(component.getSelected());
				}
			});

			cause.damageCategories = new HashSet<>();
			this.damageCategoryInput.getComponents().forEach(component -> {
				if (component.getSelected() != null) {
					cause.damageCategories.add(component.getSelected());
				}
			});
		}
		
		cause.targetSelf = this.targetSelfInput.getBoolean();
		
		return cause;
	}

}
