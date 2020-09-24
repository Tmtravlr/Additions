package com.tmtravlr.additions.gui.view.edit.recipe;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingModifyDamage;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a crafting recipe to dye an item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date April 2019
 */
public class GuiEditRecipeCraftingModifyDamage extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedCraftingModifyDamage addition;
    protected RecipeAddedCraftingModifyDamage copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentDropdownInputItem recipeItemInput;
    protected GuiComponentBooleanInput recipeHasDifferentOutputInput;
    protected GuiComponentItemStackInput recipeOutputInput;
    protected GuiComponentListInput<GuiComponentIngredientModifierInput> recipeModifiersInput;
    
	public GuiEditRecipeCraftingModifyDamage(GuiScreen parentScreen, String title, Addon addon, RecipeAddedCraftingModifyDamage recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedCraftingModifyDamage() : recipe;
	}

	@Override
	public void initComponents() {
		
		this.recipeIdInput = new GuiComponentStringInput(I18n.format("gui.edit.recipe.id.label"), this);
		if (this.isNew) {
			this.recipeIdInput.setRequired();
			this.recipeIdInput.setMaxStringLength(32);
			this.recipeIdInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.recipeIdInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.recipeIdInput.setEnabled(false);
			this.recipeIdInput.setMaxStringLength(1024);
			this.recipeIdInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.recipeIdInput.setDefaultText(this.addition.getId().toString());
		}
		
		this.recipeItemInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.recipe.crafting.modify.itemToModify.label"), this);
		this.recipeItemInput.setRequired();
		this.recipeItemInput.setDefaultSelected(this.addition.itemToModify);
		
		this.recipeHasDifferentOutputInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.crafting.modify.differentOutput.label"), this) {
			
			@Override
			protected void setBoolean(boolean input) {
				super.setBoolean(input);
				
				if (input) {
					GuiEditRecipeCraftingModifyDamage.this.recipeOutputInput.setHidden(false);
				} else {
					GuiEditRecipeCraftingModifyDamage.this.recipeOutputInput.setHidden(true);
				}
			}
			
		};
		this.recipeHasDifferentOutputInput.setInfo(new TextComponentTranslation("gui.edit.recipe.crafting.modify.differentOutput.info"));
		this.recipeHasDifferentOutputInput.setDefaultBoolean(!this.addition.output.isEmpty());
		
		this.recipeOutputInput = new GuiComponentItemStackInput(I18n.format("gui.edit.recipe.crafting.modify.output.label"), this);
		this.recipeOutputInput.setHidden(this.addition.output.isEmpty());
		this.recipeOutputInput.setDefaultItemStack(this.addition.output);
		
		this.recipeModifiersInput = new GuiComponentListInput<GuiComponentIngredientModifierInput>(I18n.format("gui.edit.recipe.crafting.modify.modifiers.label"), this) {

			@Override
			public GuiComponentIngredientModifierInput createBlankComponent() {
				GuiComponentIngredientModifierInput input = new GuiComponentIngredientModifierInput("", this.editScreen);
				return input;
			}
		};
		this.recipeModifiersInput.setRequired();
		
		if (!this.isNew) {
			List<GuiComponentIngredientModifierInput> inputs = this.addition.modifyAmounts.entrySet().stream().map(entry -> {
				GuiComponentIngredientModifierInput input = this.recipeModifiersInput.createBlankComponent();
				input.setDefaultIngredient(entry.getKey());
				input.setDefaultModifier(entry.getValue());
				return input;
			}).collect(Collectors.toList());
			
			this.recipeModifiersInput.setDefaultComponents(inputs);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeItemInput);
		this.components.add(this.recipeHasDifferentOutputInput);
		this.components.add(this.recipeOutputInput);
		this.components.add(this.recipeModifiersInput);
	}
	
	@Override
	public void saveObject() {
		ResourceLocation name = this.isNew ? new ResourceLocation(AdditionsMod.MOD_ID, this.addon.id + "-" + this.recipeIdInput.getText()) : this.addition.getId();
		
		if (this.recipeIdInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.noId"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && ForgeRegistries.RECIPES.containsKey(name)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.recipeItemInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.modify.problem.noItemToModify.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		Map<IngredientOreNBT, Integer> modifiers = this.recipeModifiersInput.getComponents().stream().filter(component -> !component.getIngredient().isEmpty()).collect(Collectors.toMap(component -> component.getIngredient(), component -> component.getModifier()));
		
		if (modifiers.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.modify.problem.noModifiers.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.itemToModify = this.recipeItemInput.getSelected();
		this.addition.output = this.recipeHasDifferentOutputInput.getBoolean() ? this.recipeOutputInput.getItemStack() : ItemStack.EMPTY;
		this.addition.modifyAmounts = modifiers;
		
		if (this.isNew) {
			this.addition.setId(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	public void copyFrom(RecipeAddedCraftingModifyDamage addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeItemInput.setDefaultSelected(this.copyFrom.itemToModify);
		this.recipeHasDifferentOutputInput.setDefaultBoolean(!this.copyFrom.output.isEmpty());
		this.recipeOutputInput.setDefaultItemStack(this.copyFrom.output);
		this.recipeOutputInput.setHidden(this.copyFrom.output.isEmpty());
		
		this.recipeModifiersInput.removeAllComponents();
		
		List<GuiComponentIngredientModifierInput> inputs = this.copyFrom.modifyAmounts.entrySet().stream().map(entry -> {
			GuiComponentIngredientModifierInput input = this.recipeModifiersInput.createBlankComponent();
			input.setDefaultIngredient(entry.getKey());
			input.setDefaultModifier(entry.getValue());
			return input;
		}).collect(Collectors.toList());
		
		this.recipeModifiersInput.setDefaultComponents(inputs);
		
		this.copyFrom = null;
	}

}
