package com.tmtravlr.additions.gui.view.components.input.recipe;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.message.edit.GuiMessageBoxEditItemStack;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiDropdownMenu;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBT;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Lets you edit a smelting recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date April 2019
 */
public class GuiComponentRecipeSmeltingInput implements IGuiViewComponent {
	protected static final int OUTLINE_OFFSET_X = 0;
	protected static final int OUTLINE_OFFSET_Y = 10;
	protected static final int INPUT_SLOT_OFFSET_X = OUTLINE_OFFSET_X + 15;
	protected static final int INPUT_SLOT_OFFSET_Y = OUTLINE_OFFSET_Y + 15;
	protected static final int OUTPUT_SLOT_OFFSET_X = INPUT_SLOT_OFFSET_X + 80;
	protected static final int OUTPUT_SLOT_OFFSET_Y = INPUT_SLOT_OFFSET_Y;
	
	protected GuiEdit editScreen;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected IngredientOreNBT ingredient = IngredientOreNBT.EMPTY;
	protected ItemStack output = ItemStack.EMPTY;
	
	protected ItemStackDisplay stackDisplay = new ItemStackDisplay();
	
	public GuiComponentRecipeSmeltingInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public int getHeight(int left, int right) {
		return 112;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;
		
		//Outline
		Gui.drawRect(this.x + OUTLINE_OFFSET_X, this.y + OUTLINE_OFFSET_Y, this.x + OUTLINE_OFFSET_X + 171, this.y + OUTLINE_OFFSET_Y + 1, 0xFFA0A0A0);
		Gui.drawRect(this.x + OUTLINE_OFFSET_X, this.y + OUTLINE_OFFSET_Y + 91, this.x + OUTLINE_OFFSET_X + 171, this.y + OUTLINE_OFFSET_Y + 92, 0xFFA0A0A0);
		Gui.drawRect(this.x + OUTLINE_OFFSET_X, this.y + OUTLINE_OFFSET_Y, this.x + OUTLINE_OFFSET_X + 1, this.y + OUTLINE_OFFSET_Y + 92, 0xFFA0A0A0);
		Gui.drawRect(this.x + OUTLINE_OFFSET_X + 170, this.y + OUTLINE_OFFSET_Y, this.x + OUTLINE_OFFSET_X + 171, this.y + OUTLINE_OFFSET_Y + 92, 0xFFA0A0A0);
		
		//Input slot
		Gui.drawRect(this.x + INPUT_SLOT_OFFSET_X, this.y + INPUT_SLOT_OFFSET_Y - 1, this.x + INPUT_SLOT_OFFSET_X + 22, this.y + INPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + INPUT_SLOT_OFFSET_X + 1, this.y + INPUT_SLOT_OFFSET_Y, this.x + INPUT_SLOT_OFFSET_X + 21, this.y + INPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();
        
		//Flame
        this.editScreen.drawTexturedModalRect(this.x + INPUT_SLOT_OFFSET_X + 2, this.y + INPUT_SLOT_OFFSET_Y + 28, 211, 80, 17, 16);
        
		//Arrow
        this.editScreen.drawTexturedModalRect(this.x + INPUT_SLOT_OFFSET_X + 40, this.y + INPUT_SLOT_OFFSET_Y + 3, 190, 84, 21, 15);
		
		//Output slot
		Gui.drawRect(this.x + OUTPUT_SLOT_OFFSET_X, this.y + OUTPUT_SLOT_OFFSET_Y - 1, this.x + OUTPUT_SLOT_OFFSET_X + 22, this.y + OUTPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + OUTPUT_SLOT_OFFSET_X + 1, this.y + OUTPUT_SLOT_OFFSET_Y, this.x + OUTPUT_SLOT_OFFSET_X + 21, this.y + OUTPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		this.stackDisplay.updateDisplay(this.ingredient.getMatchingStacks());
		
		if (!this.stackDisplay.getDisplayStack().isEmpty()) {
			this.editScreen.renderItemStack(this.stackDisplay.getDisplayStack(), this.x + INPUT_SLOT_OFFSET_X + 2, this.y + INPUT_SLOT_OFFSET_Y + 1, mouseX, mouseY, true);
		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + INPUT_SLOT_OFFSET_X + 2, this.y + INPUT_SLOT_OFFSET_Y + 1, 16, 16)) {
			this.editScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.edit.recipe.addItem.info")), mouseX, mouseY);
		}
		
		if (!this.output.isEmpty()) {
			this.editScreen.renderItemStack(this.output, this.x + OUTPUT_SLOT_OFFSET_X + 2, this.y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + OUTPUT_SLOT_OFFSET_X + 2, this.y + OUTPUT_SLOT_OFFSET_Y + 2, 16, 16)) {
			this.editScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.edit.recipe.addItem.info")), mouseX, mouseY);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + INPUT_SLOT_OFFSET_X + 2, this.y + INPUT_SLOT_OFFSET_Y + 1, 16, 16)) {
			if (this.ingredient.isEmpty()) {
				this.editIngredient();
			} else {
				this.editScreen.mc.displayGuiScreen(new GuiDropdownMenuEditInputIngredient(this.editScreen, this, this.x + INPUT_SLOT_OFFSET_X, this.y + INPUT_SLOT_OFFSET_Y, this.y + INPUT_SLOT_OFFSET_Y + 20, 100));
			}
		}
		
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + OUTPUT_SLOT_OFFSET_X, this.y + OUTPUT_SLOT_OFFSET_Y, 20, 20)) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditItemStack(this.editScreen, this.editScreen, this.output) {

				@Override
				protected void removeItemStack() {
					GuiComponentRecipeSmeltingInput.this.output = ItemStack.EMPTY;
				}

				@Override
				protected void saveItemStack(ItemStack stack) {
					GuiComponentRecipeSmeltingInput.this.output = stack;
				}
			});
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setIngredient(IngredientOreNBT ingredient) {
		this.ingredient = ingredient == null ? IngredientOreNBT.EMPTY : ingredient;
		this.recreateDisplayStack();
		this.editScreen.notifyHasChanges();
	}
	
	public void removeIngredient() {
		this.setIngredient(IngredientOreNBT.EMPTY);
	}
	
	public void setOutput(ItemStack output) {
		this.output = output;
		this.editScreen.notifyHasChanges();
	}
	
	public void editIngredient() {
		this.editScreen.mc.displayGuiScreen(new GuiEditInputIngredient(this.editScreen, this));
	}
	
	public IngredientOreNBT getIngredient() {
		return this.ingredient;
	}
	
	public ItemStack getOutput() {
		return this.output;
	}
	
	public void setDefaultRecipe(IngredientOreNBT input, ItemStack output) {
		this.ingredient = input;
		this.output = output;
	}
	
	protected void recreateDisplayStack() {
		this.stackDisplay = new ItemStackDisplay();
	}
	
	private static class GuiDropdownMenuEditInputIngredient extends GuiDropdownMenu {
		private static final MenuOption OPTION_EDIT = new MenuOption(new TextComponentTranslation("gui.buttons.edit"));
		private static final MenuOption OPTION_DELETE = new MenuOption(new TextComponentTranslation("gui.buttons.delete").setStyle(new Style().setColor(TextFormatting.RED)));
		private static final List<MenuOption> OPTIONS = Arrays.asList(OPTION_EDIT, OPTION_DELETE);
		
		private final GuiComponentRecipeSmeltingInput parentInput;
		
		public GuiDropdownMenuEditInputIngredient(GuiScreen parentScreen, GuiComponentRecipeSmeltingInput parentInput, int x, int inputTop, int inputBottom, int width) {
			super(parentScreen, x, inputTop, inputBottom, width, OPTIONS);
			this.parentInput = parentInput;
		}

		@Override
		public void onOptionSelected(MenuOption option) {
			if (option == OPTION_EDIT) {
				this.parentInput.editIngredient();
			} else if (option == OPTION_DELETE) {
				this.parentInput.removeIngredient();
				this.closeOnMouseUp();
			}
		}
	}
	
	private static class GuiEditInputIngredient extends GuiEditIngredientOreNBT {
		private GuiComponentRecipeSmeltingInput parentInput;
		
		public GuiEditInputIngredient(GuiScreen parentScreen, GuiComponentRecipeSmeltingInput parentInput) {
			super(parentScreen);
			this.parentInput = parentInput;
		}
		
		@Override
		public void initComponents() {
			super.initComponents();
			
			this.oreDictInput.setDefaultText(this.parentInput.getIngredient().getOreName());
			
			if (!this.parentInput.getIngredient().getStackList().isEmpty()) {
				this.parentInput.getIngredient().getStackList().forEach(toAdd -> {
					GuiComponentItemStackInput input = this.itemGroupInput.createBlankComponent();
					input.setDefaultItemStack(toAdd);
					this.itemGroupInput.addDefaultComponent(input);
				});
			}
		}
		
		@Override
		protected void handleIngredientSaved(IngredientOreNBT ingredient) {
			this.parentInput.setIngredient(ingredient);
			super.handleIngredientSaved(ingredient);
		}
	}
}
