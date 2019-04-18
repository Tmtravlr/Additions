package com.tmtravlr.additions.gui.view.edit.recipe;

import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingPotionTipping;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeRecipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a crafting recipe to dye an item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since April 2019
 */
public class GuiEditRecipeCraftingPotionTipping extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedCraftingPotionTipping addition;
    protected RecipeAddedCraftingPotionTipping copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentDropdownInputItem recipeItemUntippedInput;
    protected GuiComponentDropdownInputItem recipeItemTippedInput;
    
	public GuiEditRecipeCraftingPotionTipping(GuiScreen parentScreen, String title, Addon addon, RecipeAddedCraftingPotionTipping recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedCraftingPotionTipping() : recipe;
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
		
		this.recipeItemUntippedInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.recipe.crafting.tipProjectile.untippedProjectile.label"), this);
		this.recipeItemUntippedInput.setRequired();
		this.recipeItemUntippedInput.setDefaultSelected(this.addition.untippedProjectile);
		
		this.recipeItemTippedInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.recipe.crafting.tipProjectile.tippedProjectile.label"), this);
		this.recipeItemTippedInput.setRequired();
		this.recipeItemTippedInput.setDefaultSelected(this.addition.tippedProjectile);
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeItemUntippedInput);
		this.components.add(this.recipeItemTippedInput);
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
		
		if (this.recipeItemUntippedInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.tipProjectile.problem.noUntipped.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.recipeItemTippedInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.tipProjectile.problem.noTipped.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.untippedProjectile = this.recipeItemUntippedInput.getSelected();
		this.addition.tippedProjectile = this.recipeItemTippedInput.getSelected();
		
		if (this.isNew) {
			this.addition.setId(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	public void copyFrom(RecipeAddedCraftingPotionTipping addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeItemUntippedInput.setDefaultSelected(this.copyFrom.untippedProjectile);
		this.recipeItemTippedInput.setDefaultSelected(this.copyFrom.tippedProjectile);
		
		this.copyFrom = null;
	}

}
