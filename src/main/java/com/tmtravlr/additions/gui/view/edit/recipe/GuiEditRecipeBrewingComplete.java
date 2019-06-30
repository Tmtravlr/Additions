package com.tmtravlr.additions.gui.view.edit.recipe;


import java.util.Optional;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewingComplete;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a brewing recipe.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since April 2019
 */
public class GuiEditRecipeBrewingComplete extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedBrewingComplete addition;
    protected RecipeAddedBrewingComplete copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentIngredientOreNBTInput recipeIngredientInput;
    protected GuiComponentNBTInput recipeInputTagInput;
    protected GuiComponentNBTInput recipeInputExtendedTagInput;
    protected GuiComponentNBTInput recipeInputPoweredTagInput;
    protected GuiComponentNBTInput recipeOutputTagInput;
    protected GuiComponentNBTInput recipeOutputExtendedTagInput;
    protected GuiComponentNBTInput recipeOutputPoweredTagInput;
    protected GuiComponentBooleanInput recipeAllowExtendedInput;
    protected GuiComponentBooleanInput recipeAllowPoweredInput;
    protected GuiComponentBooleanInput recipeAllowMundaneInput;
    protected GuiComponentBooleanInput recipeAllowSplashInput;
    protected GuiComponentBooleanInput recipeAllowLingeringInput;
    
	public GuiEditRecipeBrewingComplete(GuiScreen parentScreen, String title, Addon addon, RecipeAddedBrewingComplete recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedBrewingComplete() : recipe;
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
		
		this.recipeIngredientInput = new GuiComponentIngredientOreNBTInput(I18n.format("gui.edit.recipe.brewing.ingredient.label"), this);
		this.recipeIngredientInput.setRequired();
		this.recipeIngredientInput.setDefaultIngredient(this.addition.ingredient);
		
		this.recipeInputTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.inputTag.label"), this);
		this.recipeInputTagInput.setRequired();
		this.recipeInputTagInput.setDefaultText(this.addition.inputTag == null ? "{}" : this.addition.inputTag.toString());
		
		this.recipeInputExtendedTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.inputExtendedTag.label"), this);
		this.recipeInputExtendedTagInput.setDefaultText(this.addition.inputExtendedTag == null ? "{}" : this.addition.inputExtendedTag.toString());
		
		this.recipeInputPoweredTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.inputPoweredTag.label"), this);
		this.recipeInputPoweredTagInput.setDefaultText(this.addition.inputPoweredTag == null ? "{}" : this.addition.inputPoweredTag.toString());
		
		this.recipeOutputTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.outputTag.label"), this);
		this.recipeOutputTagInput.setRequired();
		this.recipeOutputTagInput.setDefaultText(this.addition.outputTag == null ? "{}" : this.addition.outputTag.toString());
		
		this.recipeOutputExtendedTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.outputExtendedTag.label"), this);
		this.recipeOutputExtendedTagInput.setDefaultText(this.addition.outputExtendedTag == null ? "{}" : this.addition.outputExtendedTag.toString());
		
		this.recipeOutputPoweredTagInput = new GuiComponentNBTInput(I18n.format("gui.edit.recipe.brewing.complete.outputPoweredTag.label"), this);
		this.recipeOutputPoweredTagInput.setDefaultText(this.addition.outputPoweredTag == null ? "{}" : this.addition.outputPoweredTag.toString());
		
		this.recipeAllowExtendedInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.brewing.complete.allowExtended.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				super.setDefaultBoolean(input);
				
				if (input) {
					GuiEditRecipeBrewingComplete.this.recipeOutputExtendedTagInput.setHidden(false);
				} else {
					GuiEditRecipeBrewingComplete.this.recipeOutputExtendedTagInput.setHidden(true);
				}
			}
			
		};
		this.recipeAllowExtendedInput.setInfo(new TextComponentTranslation("gui.edit.recipe.brewing.complete.allowExtended.info"));
		this.recipeAllowExtendedInput.setDefaultBoolean(this.addition.outputExtendedTag != null && !this.addition.outputExtendedTag.hasNoTags());
		
		this.recipeAllowPoweredInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.brewing.complete.allowPowered.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				super.setDefaultBoolean(input);
				
				if (input) {
					GuiEditRecipeBrewingComplete.this.recipeOutputPoweredTagInput.setHidden(false);
				} else {
					GuiEditRecipeBrewingComplete.this.recipeOutputPoweredTagInput.setHidden(true);
				}
			}
			
		};
		this.recipeAllowPoweredInput.setInfo(new TextComponentTranslation("gui.edit.recipe.brewing.complete.allowPowered.info"));
		this.recipeAllowPoweredInput.setDefaultBoolean(this.addition.outputPoweredTag != null && !this.addition.outputPoweredTag.hasNoTags());
		
		this.recipeAllowMundaneInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.brewing.complete.allowMundane.label"), this);
		this.recipeAllowMundaneInput.setInfo(new TextComponentTranslation("gui.edit.recipe.brewing.complete.allowMundane.info"));
		this.recipeAllowMundaneInput.setDefaultBoolean(this.addition.allowMundane);
		
		this.recipeAllowSplashInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.brewing.complete.allowSplash.label"), this);
		this.recipeAllowSplashInput.setDefaultBoolean(this.addition.allowSplash);
		
		this.recipeAllowLingeringInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.brewing.complete.allowLingering.label"), this);
		this.recipeAllowLingeringInput.setDefaultBoolean(this.addition.allowLingering);
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeIngredientInput);
		this.components.add(this.recipeOutputTagInput);
		this.components.add(this.recipeAllowExtendedInput);
		this.components.add(this.recipeOutputExtendedTagInput);
		this.components.add(this.recipeAllowPoweredInput);
		this.components.add(this.recipeOutputPoweredTagInput);
		this.components.add(this.recipeAllowMundaneInput);
		this.components.add(this.recipeAllowSplashInput);
		this.components.add(this.recipeAllowLingeringInput);
		
		this.advancedComponents.add(this.recipeInputTagInput);
		this.advancedComponents.add(this.recipeInputExtendedTagInput);
		this.advancedComponents.add(this.recipeInputPoweredTagInput);
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
		
		if (this.recipeIngredientInput.getIngredient().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.brewing.problem.noIngredient.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.recipeInputTagInput.getTag() == null || this.recipeInputTagInput.getTag().hasNoTags()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.brewing.complete.problem.noInputTag.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.recipeOutputTagInput.getTag() == null || this.recipeOutputTagInput.getTag().hasNoTags()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.brewing.complete.problem.noOutputTag.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.ingredient = this.recipeIngredientInput.getIngredient();
		this.addition.inputTag = this.recipeInputTagInput.getTag();
		this.addition.inputExtendedTag = Optional.ofNullable(this.recipeInputExtendedTagInput.getTag()).orElse(new NBTTagCompound());
		this.addition.inputPoweredTag = Optional.ofNullable(this.recipeInputPoweredTagInput.getTag()).orElse(new NBTTagCompound());
		this.addition.outputTag = this.recipeOutputTagInput.getTag();
		this.addition.outputExtendedTag = (this.recipeAllowExtendedInput.getBoolean() && this.recipeOutputExtendedTagInput.getTag() != null) ? this.recipeOutputExtendedTagInput.getTag() : new NBTTagCompound();
		this.addition.outputPoweredTag = (this.recipeAllowPoweredInput.getBoolean() && this.recipeOutputPoweredTagInput.getTag() != null) ? this.recipeOutputPoweredTagInput.getTag() : new NBTTagCompound();
		this.addition.allowMundane = this.recipeAllowMundaneInput.getBoolean();
		this.addition.allowSplash = this.recipeAllowSplashInput.getBoolean();
		this.addition.allowLingering = this.recipeAllowLingeringInput.getBoolean();
		
		if (this.isNew) {
			this.addition.setId(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	public void copyFrom(RecipeAddedBrewingComplete addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeIngredientInput.setDefaultIngredient(this.copyFrom.ingredient);
		this.recipeInputTagInput.setDefaultText(this.copyFrom.inputTag == null ? "{}" : this.copyFrom.inputTag.toString());
		this.recipeInputExtendedTagInput.setDefaultText(this.copyFrom.inputExtendedTag == null ? "{}" : this.copyFrom.inputExtendedTag.toString());
		this.recipeInputPoweredTagInput.setDefaultText(this.copyFrom.inputPoweredTag == null ? "{}" : this.copyFrom.inputPoweredTag.toString());
		this.recipeOutputTagInput.setDefaultText(this.copyFrom.outputTag == null ? "{}" : this.copyFrom.outputTag.toString());
		this.recipeOutputExtendedTagInput.setDefaultText(this.copyFrom.outputExtendedTag == null ? "{}" : this.copyFrom.outputExtendedTag.toString());
		this.recipeOutputPoweredTagInput.setDefaultText(this.copyFrom.outputPoweredTag == null ? "{}" : this.copyFrom.outputPoweredTag.toString());
		this.recipeAllowExtendedInput.setDefaultBoolean(this.copyFrom.outputExtendedTag != null && !this.copyFrom.outputExtendedTag.hasNoTags());
		this.recipeAllowPoweredInput.setDefaultBoolean(this.copyFrom.outputPoweredTag != null && !this.copyFrom.outputPoweredTag.hasNoTags());
		this.recipeAllowMundaneInput.setDefaultBoolean(this.copyFrom.allowMundane);
		this.recipeAllowSplashInput.setDefaultBoolean(this.copyFrom.allowSplash);
		this.recipeAllowLingeringInput.setDefaultBoolean(this.copyFrom.allowLingering);
		
		this.copyFrom = null;
	}

}
