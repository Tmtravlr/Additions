package com.tmtravlr.additions.gui.registration;

import com.tmtravlr.additions.gui.type.button.*;
import com.tmtravlr.additions.type.AdditionTypeManager;

public class GuiFactoryRegistration {

	public static void registerGuiFactories() {
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonBlock::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonItem::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonCreativeTab::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonRecipe::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonItemMaterial::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonEffect::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonAdvancement::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonFunction::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonLootTable::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonStructure::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonSoundEvent::new);
		AdditionTypeManager.registerAdditionTypeGuiFactory(GuiAdditionTypeButtonPotionType::new);
		
		GuiFactoryRegistrationItemAdded.registerGuiFactories();
		GuiFactoryRegistrationBlockAdded.registerGuiFactories();
		GuiFactoryRegistrationLootTablePreset.registerGuiFactories();
		GuiFactoryRegistrationRecipeAdded.registerGuiFactories();
		GuiFactoryRegistrationEffectCause.registerGuiFactories();
		GuiFactoryRegistrationEffect.registerGuiFactories();
	}
	
}
