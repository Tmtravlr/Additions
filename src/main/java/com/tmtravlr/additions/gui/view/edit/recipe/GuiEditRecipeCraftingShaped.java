package com.tmtravlr.additions.gui.view.edit.recipe;

import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShaped;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShapeless;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.recipe.GuiComponentCraftingShapedInput;
import com.tmtravlr.additions.gui.view.components.input.recipe.GuiComponentCraftingShapelessInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Page for adding a shapeless crafting recipe.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date March 2019
 */
public class GuiEditRecipeCraftingShaped extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedCraftingShaped addition;
    protected RecipeAddedCraftingShaped copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentSuggestionInput recipeGroupInput;
    protected GuiComponentCraftingShapedInput recipeItemsInput;
    protected GuiComponentBooleanInput recipeMirrorInput;
    
	public GuiEditRecipeCraftingShaped(GuiScreen parentScreen, String title, Addon addon, RecipeAddedCraftingShaped recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedCraftingShaped() : recipe;
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
		
		this.recipeGroupInput = new GuiComponentSuggestionInput(I18n.format("gui.edit.recipe.crafting.group.label"), this);
		this.recipeGroupInput.setInfo(new TextComponentTranslation("gui.edit.recipe.crafting.group.info", AdditionsMod.MOD_ID));
		this.recipeGroupInput.setSuggestions(ForgeRegistries.RECIPES.getValuesCollection()
				.stream().filter(recipe -> !StringUtils.isNullOrEmpty(recipe.getGroup())).map(recipe -> recipe.getGroup().contains(":") ? recipe.getGroup() : "minecraft:" + recipe.getGroup()).collect(Collectors.toSet()));
			this.recipeGroupInput.setDefaultText(this.addition.getRecipeGroup());
		
		this.recipeItemsInput = new GuiComponentCraftingShapedInput(I18n.format("gui.edit.recipe.crafting.shaped.recipe.label"), this);
		if (this.addition.recipe != null) {
			this.recipeItemsInput.setDefaultRecipe(this.addition.recipe);
		}
		
		this.recipeMirrorInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.crafting.shaped.mirror.label"), this);
		this.recipeMirrorInput.setInfo(new TextComponentTranslation("gui.edit.recipe.crafting.shaped.mirror.info"));
		this.recipeMirrorInput.setDefaultBoolean(this.addition.isMirrored());
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeItemsInput);
		
		this.advancedComponents.add(this.recipeGroupInput);
		this.advancedComponents.add(this.recipeMirrorInput);
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
		
		ShapedOreRecipe recipe = this.recipeItemsInput.getRecipe();
		
		if (recipe.getIngredients().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.noInput"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (recipe.getRecipeOutput().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.problem.noOutput"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.recipe = recipe;
		this.addition.recipe.setMirrored(this.recipeMirrorInput.getBoolean());
		
		if (!this.recipeGroupInput.getText().isEmpty()) {
			this.addition.updateRecipeGroup(this.recipeGroupInput.getText());
		}
		
		if (this.isNew) {
			this.addition.setId(name);
			this.addition.recipe.setRegistryName(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	@Override
	public void refreshView() {
		this.recipeGroupInput.setSuggestions(ForgeRegistries.RECIPES.getValuesCollection()
				.stream().filter(recipe -> !StringUtils.isNullOrEmpty(recipe.getGroup())).map(recipe -> recipe.getGroup().contains(":") ? recipe.getGroup() : "minecraft:" + recipe.getGroup()).collect(Collectors.toSet()));
	}
	
	public void copyFrom(RecipeAddedCraftingShaped addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeGroupInput.setDefaultText(this.copyFrom.getRecipeGroup());
		this.recipeItemsInput.setDefaultRecipe(this.copyFrom.recipe);
		this.recipeMirrorInput.setDefaultBoolean(this.copyFrom.isMirrored());
		
		this.copyFrom = null;
	}

}
