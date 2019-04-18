package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonRecipe;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardColored;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.recipe.IGuiRecipeAddedFactory;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added recipe.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since March 2019 
 */
public class GuiAdditionCardRecipe extends GuiAdditionCardColored {

	private IRecipeAdded addition;
	private String textId;
	private String textType;
	private String filterId;
	private String filterType;
	
	private IGuiRecipeCardDisplay recipeCardDisplay;
	
	public GuiAdditionCardRecipe(GuiView viewScreen, Addon addon, IRecipeAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		ResourceLocation type = RecipeAddedManager.getTypeFor(this.addition);
		IGuiRecipeAddedFactory factory = RecipeAddedManager.getGuiFactoryFor(type);
		
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.getId().getResourcePath() + TextFormatting.GRAY);
		this.textType = TextFormatting.GRAY + I18n.format("gui.view.additionType.type", TextFormatting.RESET + (factory != null ? factory.getTitle() : type.toString()) + TextFormatting.GRAY);
		
		this.filterId = this.addition.getId().getResourcePath().toLowerCase();
		this.filterType = (factory != null ? factory.getTitle() : type.toString()).toLowerCase();
		
		if (factory != null) {
			this.recipeCardDisplay = factory.getRecipeCardDisplay(this.addition);
		}
		
		this.setColors(GuiAdditionTypeButtonRecipe.BUTTON_COLOR_DARK, GuiAdditionTypeButtonRecipe.BUTTON_COLOR_HOVER);
	}
	
	@Override
	public int getCardHeight() {
		int height = this.doIdAndTypeFit() ? 43 : 60;
		
		if (this.recipeCardDisplay != null) {
			int cardContentsHeight = this.recipeCardDisplay.getHeight();
			height += cardContentsHeight + 10;
		}
		
		return height;
	}

	@Override
	protected void drawCardInfo(int mouseX, int mouseY) {
		int displayOffsetY = 27;
		
		if (this.doIdAndTypeFit()) {
			int halfWidth = (this.width - 60) / 2 - 10;
			CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, halfWidth, this.x + 10, this.y + 8, 0xFFFFFF);
			CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textType, halfWidth, this.x + 20 + halfWidth, this.y + 8, 0xFFFFFF);
		} else {
			displayOffsetY += 17;
			CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, this.width - 60, this.x + 10, this.y + 8, 0xFFFFFF);
			CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textType, this.width - 20, this.x + 10, this.y + 24, 0xFFFFFF);
		}
		
		if (this.recipeCardDisplay != null) {
			int displayWidth = this.recipeCardDisplay.getWidth();
			
			if (displayWidth <= this.width - 20) {
				this.recipeCardDisplay.renderDisplay(this.viewScreen, this.x + 10, this.y + displayOffsetY, mouseX, mouseY);
			}
		}
	}

	@Override
	protected void editAddition() {
		IGuiRecipeAddedFactory editFactory = RecipeAddedManager.getGuiFactoryFor(RecipeAddedManager.getTypeFor(this.addition));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getEditScreen(this.viewScreen, this.addon, this.addition));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessageBox(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", RecipeAddedManager.getTypeFor(this.addition)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void duplicateAddition() {
		IGuiRecipeAddedFactory editFactory = RecipeAddedManager.getGuiFactoryFor(RecipeAddedManager.getTypeFor(this.addition));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getDuplicateScreen(this.viewScreen, this.addon, this.addition));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessageBox(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", RecipeAddedManager.getTypeFor(this.addition)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeRecipe.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterType.contains(filter.toLowerCase());
	}
	
	private boolean doIdAndTypeFit() {
		int halfWidth = (this.width - 60) / 2 - 10;
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) <= halfWidth && this.viewScreen.getFontRenderer().getStringWidth(this.textType) <= halfWidth;
	}
}
