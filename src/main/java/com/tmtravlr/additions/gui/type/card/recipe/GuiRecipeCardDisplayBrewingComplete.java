package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewingComplete;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

/**
 * Renders a brewing recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since April 2019
 */
public class GuiRecipeCardDisplayBrewingComplete implements IGuiRecipeCardDisplay {

	private static final int INGREDIENT_SLOT_OFFSET_X = 20;
	private static final int INGREDIENT_SLOT_OFFSET_Y = 0;
	private static final int INPUT_SLOT_OFFSET_X = 20;
	private static final int INPUT_SLOT_OFFSET_Y = 40;
	private static final int OUTPUT_SLOT_OFFSET_X = 120;
	private static final int OUTPUT_SLOT_OFFSET_Y = 20;
	
	private RecipeAddedBrewingComplete recipe;
	
	private ItemStack displayIngredient = ItemStack.EMPTY;
	private ItemStack displayInput = ItemStack.EMPTY;
	private ItemStack displayOutput = ItemStack.EMPTY;
	private List<DisplayRecipe> cachedDisplayRecipes = new ArrayList<>();
	private int displayRefreshTime = 0;
	private int recipeRefreshCount = 0;
	
	public GuiRecipeCardDisplayBrewingComplete(RecipeAddedBrewingComplete recipe) {
		this.recipe = recipe;
		this.createDisplayRecipes();
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	public int getHeight() {
		return 64;
	}

	@Override
	public void renderDisplay(GuiView viewScreen, int x, int y, int mouseX, int mouseY) {
		//Ingredient slot
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X, y + INGREDIENT_SLOT_OFFSET_Y - 1, x + INGREDIENT_SLOT_OFFSET_X + 22, y + INGREDIENT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 1, y + INGREDIENT_SLOT_OFFSET_Y, x + INGREDIENT_SLOT_OFFSET_X + 21, y + INGREDIENT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		//Input slot
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X, y + INPUT_SLOT_OFFSET_Y - 1, x + INPUT_SLOT_OFFSET_X + 22, y + INPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 1, y + INPUT_SLOT_OFFSET_Y, x + INPUT_SLOT_OFFSET_X + 21, y + INPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		//The other brewing stand bits
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 9, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 10, y + INPUT_SLOT_OFFSET_Y - 1, 0xFFA0A0A0);
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 12, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 13, y + INPUT_SLOT_OFFSET_Y - 1, 0xFFA0A0A0);
		
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 2, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y - 7, 0xFFA0A0A0);
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 5, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 6, y + INPUT_SLOT_OFFSET_Y - 4, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 3, y + INPUT_SLOT_OFFSET_Y - 6, x + INGREDIENT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y - 7, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 3, y + INPUT_SLOT_OFFSET_Y - 3, x + INGREDIENT_SLOT_OFFSET_X + 6, y + INPUT_SLOT_OFFSET_Y - 4, 0xFFA0A0A0);
		
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 18, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X - 3, y + INPUT_SLOT_OFFSET_Y - 8, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 18, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X - 17, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 18, y + INPUT_SLOT_OFFSET_Y + 5, x + INPUT_SLOT_OFFSET_X - 3, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X - 3, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X - 4, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 16, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 17, y + INPUT_SLOT_OFFSET_Y - 4, 0xFFA0A0A0);
		Gui.drawRect(x + INGREDIENT_SLOT_OFFSET_X + 19, y + INGREDIENT_SLOT_OFFSET_Y + 21, x + INGREDIENT_SLOT_OFFSET_X + 20, y + INPUT_SLOT_OFFSET_Y - 7, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 19, y + INPUT_SLOT_OFFSET_Y - 6, x + INGREDIENT_SLOT_OFFSET_X + 25, y + INPUT_SLOT_OFFSET_Y - 7, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 16, y + INPUT_SLOT_OFFSET_Y - 3, x + INGREDIENT_SLOT_OFFSET_X + 25, y + INPUT_SLOT_OFFSET_Y - 4, 0xFFA0A0A0);
		
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 25, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X + 39, y + INPUT_SLOT_OFFSET_Y - 8, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 25, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X + 26, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 25, y + INPUT_SLOT_OFFSET_Y + 5, x + INPUT_SLOT_OFFSET_X + 39, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 39, y + INPUT_SLOT_OFFSET_Y - 9, x + INPUT_SLOT_OFFSET_X + 40, y + INPUT_SLOT_OFFSET_Y + 6, 0xFFA0A0A0);
		
		//Arrow
		viewScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();

        viewScreen.drawTexturedModalRect(x + 80, y + 23, 190, 84, 21, 15);
		
		//Output slot
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X, y + OUTPUT_SLOT_OFFSET_Y - 1, x + OUTPUT_SLOT_OFFSET_X + 22, y + OUTPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X + 1, y + OUTPUT_SLOT_OFFSET_Y, x + OUTPUT_SLOT_OFFSET_X + 21, y + OUTPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		if (this.displayRefreshTime-- <= 0) {
			this.displayRefreshTime = 40;
			
			if (this.cachedDisplayRecipes.size() > 0) {
				if (++this.recipeRefreshCount >= this.cachedDisplayRecipes.size()) {
					this.recipeRefreshCount = 0;
				}
				
				DisplayRecipe displayRecipe = this.cachedDisplayRecipes.get(this.recipeRefreshCount);
				
				this.displayIngredient = displayRecipe.ingredient.getDisplayStack();
				this.displayInput = displayRecipe.input;
				this.displayOutput = displayRecipe.output;
			}
		}

		if (!this.displayIngredient.isEmpty()) {
			viewScreen.renderItemStack(this.displayIngredient, x + INGREDIENT_SLOT_OFFSET_X + 3, y + INGREDIENT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}

		if (!this.displayInput.isEmpty()) {
			viewScreen.renderItemStack(this.displayInput, x + INPUT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
		
		if (!this.displayOutput.isEmpty()) {
			viewScreen.renderItemStack(this.displayOutput, x + OUTPUT_SLOT_OFFSET_X + 3, y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
	}
	
	private void createDisplayRecipes() {
		this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.POTIONITEM, PotionTypes.AWKWARD), this.stackWithTag(Items.POTIONITEM, this.recipe.outputTag)));
		
		if (this.recipe.allowMundane) {
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.POTIONITEM, PotionTypes.WATER), this.stackWithPotion(Items.POTIONITEM, PotionTypes.MUNDANE)));
		}
		
		if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.POTIONITEM, this.recipe.outputTag), this.stackWithTag(Items.POTIONITEM, this.recipe.outputExtendedTag)));
			
			if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.POTIONITEM, this.recipe.outputPoweredTag), this.stackWithTag(Items.POTIONITEM, this.recipe.outputExtendedTag)));
			}
		}
		
		if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.POTIONITEM, this.recipe.outputTag), this.stackWithTag(Items.POTIONITEM, this.recipe.outputPoweredTag)));
			
			if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.POTIONITEM, this.recipe.outputExtendedTag), this.stackWithTag(Items.POTIONITEM, this.recipe.outputPoweredTag)));
			}
		}
		
		if (this.recipe.allowSplash) {
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.SPLASH_POTION, PotionTypes.AWKWARD), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputTag)));
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER), this.stackWithTag(Items.POTIONITEM, this.recipe.outputTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputTag)));
			
			if (this.recipe.allowMundane) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.SPLASH_POTION, PotionTypes.WATER), this.stackWithPotion(Items.SPLASH_POTION, PotionTypes.MUNDANE)));
			}
			
			if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputExtendedTag)));
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER), this.stackWithTag(Items.POTIONITEM, this.recipe.outputExtendedTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputExtendedTag)));
				
				if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
					this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputPoweredTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputExtendedTag)));
				}
			}
			
			if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputPoweredTag)));
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER), this.stackWithTag(Items.POTIONITEM, this.recipe.outputPoweredTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputPoweredTag)));
				
				if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
					this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputExtendedTag), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputPoweredTag)));
				}
			}
		}
		
		if (this.recipe.allowLingering) {
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.LINGERING_POTION, PotionTypes.AWKWARD), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputTag))));
			this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH))), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputTag), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputTag))));
			
			if (this.recipe.allowMundane) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(this.recipe.ingredient), this.stackWithPotion(Items.LINGERING_POTION, PotionTypes.WATER), this.stackWithPotion(Items.LINGERING_POTION, PotionTypes.MUNDANE)));
			}
			
			if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputTag)), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputExtendedTag))));
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH))), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputExtendedTag), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputExtendedTag))));
				
				if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
					this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputPoweredTag)), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputExtendedTag))));
				}
			}
			
			if (this.recipe.outputPoweredTag != null && !this.recipe.outputPoweredTag.hasNoTags()) {
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputTag)), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputPoweredTag))));
				this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH))), this.stackWithTag(Items.SPLASH_POTION, this.recipe.outputPoweredTag), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputPoweredTag))));
				
				if (this.recipe.outputExtendedTag != null && !this.recipe.outputExtendedTag.hasNoTags()) {
					this.cachedDisplayRecipes.add(new DisplayRecipe(new DisplayStack(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputExtendedTag)), this.stackWithTag(Items.LINGERING_POTION, this.recipe.getLingeringTag(this.recipe.outputPoweredTag))));
				}
			}
		}
	}
	
	private ItemStack stackWithPotion(Item item, PotionType potionType) {
		return PotionUtils.addPotionToItemStack(new ItemStack(item), potionType);
	}
	
	private ItemStack stackWithTag(Item item, NBTTagCompound tag) {
		ItemStack stack = new ItemStack(item);
		stack.setTagCompound(tag);
		return stack;
	}
	
	private static class DisplayStack {
		public ItemStack[] stacks = new ItemStack[0];
		public int refreshCount = 0;
		
		public DisplayStack(IngredientOreNBT ingredient) {
			this.stacks = ingredient.getMatchingStacks();
		}
		
		public DisplayStack(ItemStack... stacks) {
			this.stacks = stacks;
		}
		
		public ItemStack getDisplayStack() {
			ItemStack displayStack = ItemStack.EMPTY;
			
			if (stacks.length > 0) {
				if (++this.refreshCount >= stacks.length) {
					this.refreshCount = 0;
				}
				
				int index = this.refreshCount % stacks.length;
				displayStack = stacks[index];
			} else {
				this.refreshCount = 0;
			}
			
			return displayStack;
		}
	}
	
	private static class DisplayRecipe {
		public DisplayStack ingredient;
		public ItemStack input;
		public ItemStack output;
		
		public DisplayRecipe(DisplayStack ingredient, ItemStack input, ItemStack output) {
			this.ingredient = ingredient;
			this.input = input;
			this.output = output;
		}
	}

}
