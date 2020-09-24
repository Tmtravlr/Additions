package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewing;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Renders a brewing recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date April 2019
 */
public class GuiRecipeCardDisplayBrewing implements IGuiRecipeCardDisplay {

	private static final int INGREDIENT_SLOT_OFFSET_X = 20;
	private static final int INGREDIENT_SLOT_OFFSET_Y = 0;
	private static final int INPUT_SLOT_OFFSET_X = 20;
	private static final int INPUT_SLOT_OFFSET_Y = 40;
	private static final int OUTPUT_SLOT_OFFSET_X = 120;
	private static final int OUTPUT_SLOT_OFFSET_Y = 20;
	
	private RecipeAddedBrewing recipe;
	
	private ItemStackDisplay stackDisplay = new ItemStackDisplay();
	
	public GuiRecipeCardDisplayBrewing(RecipeAddedBrewing recipe) {
		this.recipe = recipe;
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
		
		this.stackDisplay.updateDisplay(this.recipe.ingredient.getMatchingStacks());

		if (!this.stackDisplay.getDisplayStack().isEmpty()) {
			viewScreen.renderItemStack(this.stackDisplay.getDisplayStack(), x + INGREDIENT_SLOT_OFFSET_X + 3, y + INGREDIENT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}

		if (!this.recipe.input.isEmpty()) {
			viewScreen.renderItemStack(this.recipe.input, x + INPUT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
		
		if (!this.recipe.output.isEmpty()) {
			viewScreen.renderItemStack(this.recipe.output, x + OUTPUT_SLOT_OFFSET_X + 3, y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
	}

}
