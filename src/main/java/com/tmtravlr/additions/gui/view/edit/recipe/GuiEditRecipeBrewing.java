package com.tmtravlr.additions.gui.view.edit.recipe;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewing;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a brewing recipe.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date April 2019
 */
public class GuiEditRecipeBrewing extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedBrewing addition;
    protected RecipeAddedBrewing copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentIngredientOreNBTInput recipeIngredientInput;
    protected GuiComponentItemStackInput recipeInputInput;
    protected GuiComponentItemStackInput recipeOutputInput;
    
	public GuiEditRecipeBrewing(GuiScreen parentScreen, String title, Addon addon, RecipeAddedBrewing recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedBrewing() : recipe;
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
		
		this.recipeInputInput = new GuiComponentItemStackInput(I18n.format("gui.edit.recipe.brewing.input.label"), this);
		this.recipeInputInput.disableCount();
		this.recipeInputInput.enableAnyDamage();
		this.recipeInputInput.setRequired();
		this.recipeInputInput.setDefaultItemStack(this.addition.input);
		
		this.recipeOutputInput = new GuiComponentItemStackInput(I18n.format("gui.edit.recipe.brewing.output.label"), this);
		this.recipeOutputInput.disableCount();
		this.recipeOutputInput.setRequired();
		this.recipeOutputInput.setDefaultItemStack(this.addition.output);
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeIngredientInput);
		this.components.add(this.recipeInputInput);
		this.components.add(this.recipeOutputInput);
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
		
		if (this.recipeInputInput.getItemStack().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.noInput"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.recipeOutputInput.getItemStack().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.noOutput"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.ingredient = this.recipeIngredientInput.getIngredient();
		this.addition.input = this.recipeInputInput.getItemStack();
		this.addition.output = this.recipeOutputInput.getItemStack();
		
		if (this.isNew) {
			this.addition.setId(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	public void copyFrom(RecipeAddedBrewing addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeIngredientInput.setDefaultIngredient(this.copyFrom.ingredient);
		this.recipeInputInput.setDefaultItemStack(this.copyFrom.input);
		this.recipeOutputInput.setDefaultItemStack(this.copyFrom.output);
		
		this.copyFrom = null;
	}

}
