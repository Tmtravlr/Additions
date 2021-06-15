package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntity;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityRightClicked;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.EntityCategoryChecker;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the entity right clicked effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class GuiEditHandlerEffectCauseEntityRightClicked implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentListInput<GuiComponentDropdownInput<ResourceLocation>> entityTypeInput;
	private GuiComponentListInput<GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory>> entityCategoryInput;
	private GuiComponentNBTInput tagInput;
	private GuiComponentBooleanInput targetSelfInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseEntityRightClicked specificCause = (cause instanceof EffectCauseEntityRightClicked) ? (EffectCauseEntityRightClicked) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.entityRightClicked.description"));

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
		if (specificCause != null && !specificCause.entityCategories.isEmpty()) {
			specificCause.entityCategories.forEach(entityCategory -> {
				GuiComponentDropdownInput<EntityCategoryChecker.EntityCategory> input = this.entityCategoryInput.createBlankComponent();
				input.setDefaultSelected(entityCategory);
				this.entityCategoryInput.addDefaultComponent(input);
			});
		}
		
		this.tagInput = new GuiComponentNBTInput(I18n.format("gui.edit.effectCause.entity.entityTag.label"), editScreen);
		if (specificCause != null && specificCause.entityTag != null && !specificCause.entityTag.hasNoTags()) {
			this.tagInput.setDefaultText(specificCause.entityTag.toString());
		}
		
		this.targetSelfInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.targetSelf.label"), editScreen);
		if (specificCause != null) {
			this.targetSelfInput.setDefaultBoolean(specificCause.targetSelf);
		}
		
		return Arrays.asList(this.description, this.entityTypeInput, this.entityCategoryInput, this.tagInput, this.targetSelfInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.entityTypeInput == null || this.entityCategoryInput == null || this.tagInput == null || this.targetSelfInput == null) {
			return null;
		}
		
		EffectCauseEntityRightClicked cause = new EffectCauseEntityRightClicked();
		
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
		
		cause.entityTag = this.tagInput.getTag();
		cause.targetSelf = this.targetSelfInput.getBoolean();
		
		return cause;
	}

}
