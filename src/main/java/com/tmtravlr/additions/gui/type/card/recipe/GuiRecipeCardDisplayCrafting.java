package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public abstract class GuiRecipeCardDisplayCrafting implements IGuiRecipeCardDisplay {
	
	protected static final int OUTPUT_SLOT_OFFSET_X = 120;
	protected static final int OUTPUT_SLOT_OFFSET_Y = 21;
	protected static final int MAX_INGREDIENTS = 9;
	
	protected NonNullList<IngredientOreNBT> displayIngredients = NonNullList.withSize(MAX_INGREDIENTS, IngredientOreNBT.EMPTY);
	protected List<ItemStackDisplay> stackDisplays = new ArrayList<>();
	
	public GuiRecipeCardDisplayCrafting() {
		for (int i = 0; i < MAX_INGREDIENTS; i++) {
			this.stackDisplays.add(new ItemStackDisplay());
		}
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
		//Crafting grid
		Gui.drawRect(x, y - 1, x + 64, y + 63, 0xFFA0A0A0);
		Gui.drawRect(x + 1, y, x + 63, y + 62, 0xFF000000);
		Gui.drawRect(x + 1, y + 20, x + 63, y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + 1, y + 41, x + 63, y + 42, 0xFFA0A0A0);
		Gui.drawRect(x + 21, y, x + 22, y + 62, 0xFFA0A0A0);
		Gui.drawRect(x + 42, y, x + 43, y + 62, 0xFFA0A0A0);
		
		//Arrow
		viewScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();
        
        viewScreen.drawTexturedModalRect(x + 82, y + 24, 190, 84, 21, 15);
		
		//Output slot
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X, y + OUTPUT_SLOT_OFFSET_Y - 1, x + OUTPUT_SLOT_OFFSET_X + 22, y + OUTPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(x + OUTPUT_SLOT_OFFSET_X + 1, y + OUTPUT_SLOT_OFFSET_Y, x + OUTPUT_SLOT_OFFSET_X + 21, y + OUTPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		NonNullList<ItemStack> inputs = NonNullList.create();
		for (int i = 0; i < MAX_INGREDIENTS; i++) {
			int offsetX = 3 + (i % 3) * 21;
			int offsetY = 2 + MathHelper.floor(i / 3) * 21;
			
			ItemStackDisplay stackDisplay = this.stackDisplays.get(i);
			stackDisplay.updateDisplay(this.displayIngredients.get(i).getMatchingStacks());
			ItemStack displayStack = stackDisplay.getDisplayStack();
			inputs.add(displayStack);
			
			if (!displayStack.isEmpty()) {
				viewScreen.renderItemStack(displayStack, x + offsetX, y + offsetY, mouseX, mouseY, true, true);
			}
		}
		
		ItemStack output = this.getOutput(inputs);
		
		if (!output.isEmpty()) {
			viewScreen.renderItemStack(output, x + OUTPUT_SLOT_OFFSET_X + 2, y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		}
	}
	
	protected abstract ItemStack getOutput(NonNullList<ItemStack> inputs);

}
