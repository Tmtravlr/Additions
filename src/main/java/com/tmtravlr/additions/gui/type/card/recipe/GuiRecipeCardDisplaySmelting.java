package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedSmelting;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class GuiRecipeCardDisplaySmelting implements IGuiRecipeCardDisplay {

	private static final int INPUT_SLOT_OFFSET_X = 20;
	private static final int INPUT_SLOT_OFFSET_Y = 0;
	private static final int OUTPUT_SLOT_OFFSET_X = INPUT_SLOT_OFFSET_X + 80;
	private static final int OUTPUT_SLOT_OFFSET_Y = INPUT_SLOT_OFFSET_Y + 8;
	
	private RecipeAddedSmelting recipe;
	
	public GuiRecipeCardDisplaySmelting(RecipeAddedSmelting recipe) {
		this.recipe = recipe;
	}

	@Override
	public int getWidth() {
		return 120;
	}

	@Override
	public int getHeight() {
		return 43;
	}

	@Override
	public void renderDisplay(GuiView viewScreen, int x, int y, int mouseX, int mouseY) {
		//Input slot
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X, y + INPUT_SLOT_OFFSET_Y - 1, x + INPUT_SLOT_OFFSET_X + 22, y + INPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + INPUT_SLOT_OFFSET_X + 1, y + INPUT_SLOT_OFFSET_Y, x + INPUT_SLOT_OFFSET_X + 21, y + INPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		//Flame and Arrow
		viewScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();

        viewScreen.drawTexturedModalRect(x + INPUT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y + 25, 211, 80, 17, 16);
        viewScreen.drawTexturedModalRect(x + INPUT_SLOT_OFFSET_X + 40, y + INPUT_SLOT_OFFSET_Y + 11, 190, 84, 21, 15);
		
		//Output slot
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X, y + OUTPUT_SLOT_OFFSET_Y - 1, x + OUTPUT_SLOT_OFFSET_X + 22, y + OUTPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X + 1, y + OUTPUT_SLOT_OFFSET_Y, x + OUTPUT_SLOT_OFFSET_X + 21, y + OUTPUT_SLOT_OFFSET_Y + 20, 0xFF000000);

		if (!this.recipe.input.isEmpty()) {
			viewScreen.renderItemStack(this.recipe.input, x + INPUT_SLOT_OFFSET_X + 3, y + INPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
		
		if (!this.recipe.output.isEmpty()) {
			viewScreen.renderItemStack(this.recipe.output, x + OUTPUT_SLOT_OFFSET_X + 3, y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
	}

}
