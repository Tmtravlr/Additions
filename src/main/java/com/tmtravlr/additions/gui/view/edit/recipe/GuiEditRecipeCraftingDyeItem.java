package com.tmtravlr.additions.gui.view.edit.recipe;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShaped;
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
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a crafting recipe to dye an item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since April 2019
 */
public class GuiEditRecipeCraftingDyeItem extends GuiEdit {
	
	protected Addon addon;
	
    protected boolean isNew;
    protected RecipeAddedCraftingDyeItem addition;
    protected RecipeAddedCraftingDyeItem copyFrom;

    protected GuiComponentStringInput recipeIdInput;
    protected GuiComponentDropdownInputItem recipeItemInput;
    protected GuiComponentBooleanInput recipeWashInCauldronInput;
    protected GuiComponentListInput<GuiComponentStringInput> recipeColorTagPathInput;
    
	public GuiEditRecipeCraftingDyeItem(GuiScreen parentScreen, String title, Addon addon, RecipeAddedCraftingDyeItem recipe) {
		super(parentScreen, title);
		this.addon = addon;
		
		this.isNew = recipe == null;
		this.addition = this.isNew ? new RecipeAddedCraftingDyeItem() : recipe;
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
		
		this.recipeItemInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.recipe.crafting.dyeItem.item.label"), this);
		this.recipeItemInput.setRequired();
		this.recipeItemInput.setDefaultSelected(this.addition.itemToDye);
		
		this.recipeWashInCauldronInput = new GuiComponentBooleanInput(I18n.format("gui.edit.recipe.crafting.dyeItem.washInCauldron.label"), this);
		this.recipeWashInCauldronInput.setInfo(new TextComponentTranslation("gui.edit.recipe.crafting.dyeItem.washInCauldron.info"));
		this.recipeWashInCauldronInput.setDefaultBoolean(this.addition.washInCauldron);
		
		this.recipeColorTagPathInput = new GuiComponentListInput<GuiComponentStringInput>(I18n.format("gui.edit.recipe.crafting.dyeItem.colorTagPath.label"), this) {

			@Override
			public GuiComponentStringInput createBlankComponent() {
				GuiComponentStringInput input = new GuiComponentStringInput("", this.editScreen);
				input.setMaxStringLength(256);
				return input;
			}
			
		};
		this.recipeColorTagPathInput.setRequired();
		
		for (String toAdd : this.addition.colorTagPath) {
			GuiComponentStringInput input = this.recipeColorTagPathInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.recipeColorTagPathInput.addDefaultComponent(input);
		};
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.recipeIdInput);
		this.components.add(this.recipeItemInput);
		this.components.add(this.recipeWashInCauldronInput);
		
		this.advancedComponents.add(this.recipeColorTagPathInput);
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
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.dyeItem.problem.noItemToDye.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		String[] colorTagPath = this.recipeColorTagPathInput.getComponents().stream().filter(component -> !component.getText().isEmpty()).map(GuiComponentStringInput::getText).collect(Collectors.toList()).toArray(new String[0]);
		
		if (colorTagPath.length == 0) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.recipe.problem.title"), new TextComponentTranslation("gui.edit.recipe.crafting.dyeItem.problem.noColorTagPath.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.itemToDye = this.recipeItemInput.getSelected();
		this.addition.washInCauldron = this.recipeWashInCauldronInput.getBoolean();
		this.addition.colorTagPath = colorTagPath;
		
		if (this.isNew) {
			this.addition.setId(name);
		}
		
		AdditionTypeRecipe.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	public void copyFrom(RecipeAddedCraftingDyeItem addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.recipeItemInput.setDefaultSelected(this.copyFrom.itemToDye);
		this.recipeWashInCauldronInput.setDefaultBoolean(this.copyFrom.washInCauldron);
		
		this.recipeColorTagPathInput.removeAllComponents();
		
		for (String toAdd : this.copyFrom.colorTagPath) {
			GuiComponentStringInput input = this.recipeColorTagPathInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.recipeColorTagPathInput.addDefaultComponent(input);
		};
		
		this.copyFrom = null;
	}

}
