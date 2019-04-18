package com.tmtravlr.additions.gui.view.components.input.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.message.edit.GuiMessageBoxEditItemStack;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiDropdownMenu;
import com.tmtravlr.additions.gui.view.components.helpers.GuiDropdownMenu.MenuOption;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBT;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Lets you edit a crafting recipe
 * Kind of a mess... I simplified it as much as I could
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since April 2019
 */
public abstract class GuiComponentCraftingRecipeInput implements IGuiViewComponent {
	protected static final int OUTLINE_OFFSET_X = 0;
	protected static final int OUTLINE_OFFSET_Y = 10;
	protected static final int CRAFTING_GRID_OFFSET_X = OUTLINE_OFFSET_X + 15;
	protected static final int CRAFTING_GRID_OFFSET_Y = OUTLINE_OFFSET_Y + 15;
	protected static final int OUTPUT_SLOT_OFFSET_X = CRAFTING_GRID_OFFSET_X + 120;
	protected static final int OUTPUT_SLOT_OFFSET_Y = CRAFTING_GRID_OFFSET_Y + 21;
	protected static final int MAX_INGREDIENTS = 9;
	
	protected GuiEdit editScreen;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected NonNullList<IngredientOreNBT> ingredients = NonNullList.withSize(MAX_INGREDIENTS, IngredientOreNBT.EMPTY);
	protected ItemStack output = ItemStack.EMPTY;
	
	protected List<ItemStack> displayStacks = new ArrayList<>();
	protected List<DisplayStack> cachedDisplayStacks = new ArrayList<>();
	protected int displayRefreshTime = 0;
	
	public GuiComponentCraftingRecipeInput(String label, GuiEdit editScreen) {
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
		
		//Crafting grid
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X, this.y + CRAFTING_GRID_OFFSET_Y - 1, this.x + CRAFTING_GRID_OFFSET_X + 64, this.y + CRAFTING_GRID_OFFSET_Y + 63, 0xFFA0A0A0);
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X + 1, this.y + CRAFTING_GRID_OFFSET_Y, this.x + CRAFTING_GRID_OFFSET_X + 63, this.y + CRAFTING_GRID_OFFSET_Y + 62, 0xFF000000);
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X + 1, this.y + CRAFTING_GRID_OFFSET_Y + 20, this.x + CRAFTING_GRID_OFFSET_X + 63, this.y + CRAFTING_GRID_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X + 1, this.y + CRAFTING_GRID_OFFSET_Y + 41, this.x + CRAFTING_GRID_OFFSET_X + 63, this.y + CRAFTING_GRID_OFFSET_Y + 42, 0xFFA0A0A0);
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X + 21, this.y + CRAFTING_GRID_OFFSET_Y, this.x + CRAFTING_GRID_OFFSET_X + 22, this.y + CRAFTING_GRID_OFFSET_Y + 62, 0xFFA0A0A0);
		Gui.drawRect(this.x + CRAFTING_GRID_OFFSET_X + 42, this.y + CRAFTING_GRID_OFFSET_Y, this.x + CRAFTING_GRID_OFFSET_X + 43, this.y + CRAFTING_GRID_OFFSET_Y + 62, 0xFFA0A0A0);
		
		//Arrow
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();
        
        this.editScreen.drawTexturedModalRect(this.x + CRAFTING_GRID_OFFSET_X + 82, this.y + CRAFTING_GRID_OFFSET_Y + 24, 190, 84, 21, 15);
		
		//Output slot
		Gui.drawRect(this.x + OUTPUT_SLOT_OFFSET_X, this.y + OUTPUT_SLOT_OFFSET_Y - 1, this.x + OUTPUT_SLOT_OFFSET_X + 22, this.y + OUTPUT_SLOT_OFFSET_Y + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + OUTPUT_SLOT_OFFSET_X + 1, this.y + OUTPUT_SLOT_OFFSET_Y, this.x + OUTPUT_SLOT_OFFSET_X + 21, this.y + OUTPUT_SLOT_OFFSET_Y + 20, 0xFF000000);
		
		
		if (this.displayRefreshTime-- <= 0) {
			this.displayRefreshTime = 40;
			
			this.displayStacks = this.cachedDisplayStacks.stream().map(DisplayStack::getDisplayStack).collect(Collectors.toList());
		}

		for (int i = 0; i < MAX_INGREDIENTS; i++) {
			int offsetX = 3 + (i % 3) * 21;
			int offsetY = 2 + MathHelper.floor(i / 3) * 21;
			
			if (i < this.displayStacks.size() && !this.displayStacks.get(i).isEmpty()) {
				this.editScreen.renderItemStack(this.displayStacks.get(i), this.x + CRAFTING_GRID_OFFSET_X + offsetX, this.y + CRAFTING_GRID_OFFSET_Y + offsetY, mouseX, mouseY, true);
			} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + CRAFTING_GRID_OFFSET_X + offsetX, this.y + CRAFTING_GRID_OFFSET_Y + offsetY, 16, 16)) {
				this.editScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.edit.recipe.addItem.info")), mouseX, mouseY);
			}
		}
		
		if (!this.output.isEmpty()) {
			this.editScreen.renderItemStack(this.output, this.x + OUTPUT_SLOT_OFFSET_X + 2, this.y + OUTPUT_SLOT_OFFSET_Y + 2, mouseX, mouseY, true, true);
		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + OUTPUT_SLOT_OFFSET_X + 2, this.y + OUTPUT_SLOT_OFFSET_Y + 2, 16, 16)) {
			this.editScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.edit.recipe.addItem.info")), mouseX, mouseY);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = 0; i < MAX_INGREDIENTS; i++) {
			int slotX = this.x + CRAFTING_GRID_OFFSET_X + 3 + (i % 3) * 21;
			int slotY = this.y + CRAFTING_GRID_OFFSET_Y + 2 + MathHelper.floor(i / 3) * 21;
			
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, slotX, slotY, 16, 16)) {
				List<IngredientMenuOption> otherOptions = this.getOtherOptions(i);
				
				if (otherOptions.isEmpty() && this.ingredients.get(i).isEmpty()) {
					this.editIngredient(i);
				} else {
					this.editScreen.mc.displayGuiScreen(new GuiDropdownMenuEditInputIngredient(this.editScreen, this, otherOptions, slotX - 2, slotY - 3, slotY + 19, 100, i));
				}
			}
		}
		
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + OUTPUT_SLOT_OFFSET_X, this.y + OUTPUT_SLOT_OFFSET_Y, 20, 20)) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditItemStack(this.editScreen, this.output) {

				@Override
				protected void removeItemStack() {
					GuiComponentCraftingRecipeInput.this.output = ItemStack.EMPTY;
				}

				@Override
				protected void saveItemStack(ItemStack stack) {
					GuiComponentCraftingRecipeInput.this.output = stack;
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
	
	public boolean setIngredientWithReplaceCheck(int index, IngredientOreNBT ingredient) {
		if (index >= 0 && index < MAX_INGREDIENTS) {
			IngredientOreNBT oldIngredient = this.ingredients.get(index);
			long sameCount = 0;
			
			if (!oldIngredient.isEmpty()) {
				sameCount = this.ingredients.stream().filter(otherIngredient -> otherIngredient.equals(oldIngredient)).count() - 1;
			}
			
			if (sameCount > 0) {
				this.editScreen.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this.editScreen, this.editScreen, I18n.format("gui.edit.recipe.crafting.shaped.replaceAll.title"), new TextComponentTranslation("gui.edit.recipe.crafting.shaped.replaceAll.message1." + (sameCount == 1 ? "singular" : "plural"), sameCount), I18n.format("gui.edit.recipe.crafting.shaped.replaceAll.button.all"), I18n.format("gui.edit.recipe.crafting.shaped.replaceAll.button.one")) {
					
					private final ITextComponent secondMessage = new TextComponentTranslation("gui.edit.recipe.crafting.shaped.replaceAll.message2");
					private List<String> secondMultilineMessage;
					protected int secondTextHeight;

				    @Override
				    public void initGui() {
				    	super.initGui();
				        int popupWidth = this.getPopupWidth();   
				        
				        this.secondMultilineMessage = this.fontRenderer.listFormattedStringToWidth(this.secondMessage.getFormattedText(), popupWidth - 50);
				        this.secondTextHeight = this.secondMultilineMessage.size() * (this.fontRenderer.FONT_HEIGHT + 3);
				    }
				    
					@Override
				    protected int getPopupHeight() {
				    	return super.getPopupHeight() + secondTextHeight + 40;
				    }
					
				    @Override
				    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
				    	super.drawScreenOverlay(mouseX, mouseY, partialTicks);

				    	int popupHeight = this.getPopupHeight();
				    	int popupY = this.height / 2 - popupHeight / 2;
				        int y = popupY + textHeight + 60;
				        
						Gui.drawRect(this.width / 2 - 11, y - 3, this.width / 2 + 11, y + 19, 0xFFA0A0A0);
						Gui.drawRect(this.width / 2 - 10, y - 2, this.width / 2 + 10, y + 18, 0xFF000000);

				        GuiComponentCraftingRecipeInput.this.editScreen.renderItemStack(GuiComponentCraftingRecipeInput.this.displayStacks.get(index), this.width / 2 - 8, y, mouseX, mouseY, true);
						
						y += 30;

				        if (this.secondMultilineMessage != null) {
				            for (String s : this.secondMultilineMessage) {
				                this.drawCenteredString(this.fontRenderer, s, this.width / 2, y, 16777215);
				                y += this.fontRenderer.FONT_HEIGHT + 3;
				            }
				        }
				    }
				    
				    @Override
				    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				    	super.drawScreen(mouseX, mouseY, partialTicks);
				        GuiComponentCraftingRecipeInput.this.editScreen.drawPostRender();
				    }

					@Override
					protected void onFirstButtonClicked() {
						for (int i = 0; i <  GuiComponentCraftingRecipeInput.this.ingredients.size(); i++) {
							if (GuiComponentCraftingRecipeInput.this.ingredients.get(i).equals(oldIngredient)) {
								GuiComponentCraftingRecipeInput.this.setIngredient(i, ingredient);
							}
						}
						super.onFirstButtonClicked();
					}
					
				    @Override
				    protected void onSecondButtonClicked() {
				    	GuiComponentCraftingRecipeInput.this.setIngredient(index, ingredient);
						super.onSecondButtonClicked();
				    }
				});
				
				return true;
			} else {
				this.setIngredient(index, ingredient);
			}
			
		}
		
		return false;
	}
	
	public void setIngredient(int index, IngredientOreNBT ingredient) {
		if (index >= 0 && index < MAX_INGREDIENTS) {
			this.ingredients.set(index, ingredient == null ? IngredientOreNBT.EMPTY : ingredient);
			this.recreateDisplayStacks();
			this.editScreen.notifyHasChanges();
		}
	}
	
	public void removeIngredient(int index) {
		this.setIngredient(index, IngredientOreNBT.EMPTY);
	}
	
	public void editIngredient(int index) {
		this.editScreen.mc.displayGuiScreen(new GuiEditInputIngredient(this.editScreen, this, index));
	}
	
	public void setOutput(ItemStack output) {
		this.output = output;
		this.editScreen.notifyHasChanges();
	}
	
	public IngredientOreNBT getIngredient(int index) {
		return (index >= 0 && index < MAX_INGREDIENTS) ? this.ingredients.get(index) : null;
	}
	
	protected void recreateDisplayStacks() {
		this.cachedDisplayStacks = this.ingredients.stream().map(DisplayStack::new).collect(Collectors.toList());
		this.forceDisplayRefresh();
	}
	
	protected void forceDisplayRefresh() {
		this.displayRefreshTime = 0;
	}
	
	private List<IngredientMenuOption> getOtherOptions(int index) {
		List<IngredientMenuOption> otherOptions = new ArrayList<>();
		Set<IngredientOreNBT> uniqueIngredients = this.ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).collect(Collectors.toSet());
		
		for (IngredientOreNBT ingredient : uniqueIngredients) {
			if (!ingredient.equals(this.ingredients.get(index))) {
				otherOptions.add(new IngredientMenuOption(new TextComponentTranslation("gui.edit.recipe.crafting.shaped.button.useThisItem"), this.ingredients.indexOf(ingredient)));
			}
		}
		
		return otherOptions;
	}
	
	private static class DisplayStack {
		public ItemStack[] stacks = new ItemStack[0];
		public int refreshCount = 0;
		
		public DisplayStack(IngredientOreNBT ingredient) {
			this.stacks = ingredient.getMatchingStacks();
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
	
	private static class GuiDropdownMenuEditInputIngredient extends GuiDropdownMenu {
		private static final MenuOption OPTION_NEW = new MenuOption(new TextComponentTranslation("gui.buttons.new"));
		private static final MenuOption OPTION_EDIT = new MenuOption(new TextComponentTranslation("gui.buttons.edit"));
		private static final MenuOption OPTION_DELETE = new MenuOption(new TextComponentTranslation("gui.buttons.delete").setStyle(new Style().setColor(TextFormatting.RED)));
		
		private final List<IngredientMenuOption> otherOptions;
		private final GuiComponentCraftingRecipeInput parentInput;
		private final int index;
		
		public GuiDropdownMenuEditInputIngredient(GuiScreen parentScreen, GuiComponentCraftingRecipeInput parentInput, List<IngredientMenuOption> otherOptions, int x, int inputTop, int inputBottom, int width, int index) {
			super(parentScreen, x, inputTop, inputBottom, width, createOptions(parentInput, otherOptions, index));
			this.otherOptions = otherOptions;
			this.parentInput = parentInput;
			this.index = index;
		}

		@Override
		public void onOptionSelected(MenuOption option) {
			if (option == OPTION_NEW || option == OPTION_EDIT) {
				this.parentInput.editIngredient(this.index);
			} else if (option == OPTION_DELETE) {
				this.parentInput.removeIngredient(this.index);
				this.closeOnMouseUp();
			} else {
				for (IngredientMenuOption otherOption : otherOptions) {
					if (option == otherOption) {
						this.parentInput.setIngredient(index, this.parentInput.getIngredient(otherOption.displayIndex));
						this.closeOnMouseUp();
					}
				}
			}
		}
		
		@Override
		public void drawOption(MenuOption option, int index, int left, int right, int top) {
			for (IngredientMenuOption otherOption : otherOptions) {
				if (option == otherOption) {
					this.parentInput.editScreen.renderItemStack(this.parentInput.displayStacks.get(otherOption.displayIndex), left + 3, top - 2, 0, 0, false);
					super.drawOption(option, index, left + 18, right, top);
					return;
				}
			}
			
			super.drawOption(option, index, left, right, top);
		}
		
		private static List<MenuOption> createOptions(GuiComponentCraftingRecipeInput parentInput, List<IngredientMenuOption> otherOptions, int index) {
			List<MenuOption> options = new ArrayList<>();
			
			if (parentInput.ingredients.get(index).isEmpty()) {
				options.add(OPTION_NEW);
			} else {
				options.add(OPTION_EDIT);
				options.add(OPTION_DELETE);
			}
			
			options.addAll(otherOptions);
			
			return options;
		}
	}
	
	private static class IngredientMenuOption extends MenuOption {
		private int displayIndex;
		
		public IngredientMenuOption(ITextComponent label, int displayIndex) {
			super(label);
			this.displayIndex = displayIndex;
		}
	}
	
	private static class GuiEditInputIngredient extends GuiEditIngredientOreNBT {
		private GuiComponentCraftingRecipeInput parentInput;
		private int index;
		
		public GuiEditInputIngredient(GuiScreen parentScreen, GuiComponentCraftingRecipeInput parentInput, int index) {
			super(parentScreen);
			this.parentInput = parentInput;
			this.index = index;
		}
		
		@Override
		public void initComponents() {
			super.initComponents();
			
			this.oreDictInput.setDefaultText(this.parentInput.getIngredient(this.index).getOreName());
			
			if (!this.parentInput.getIngredient(this.index).getStackList().isEmpty()) {
				this.parentInput.getIngredient(this.index).getStackList().forEach(toAdd -> {
					GuiComponentItemStackInput input = this.itemGroupInput.createBlankComponent();
					input.setDefaultItemStack(toAdd);
					this.itemGroupInput.addDefaultComponent(input);
				});
			}
		}
		
		@Override
		protected void handleIngredientSaved(IngredientOreNBT ingredient) {
			if (!this.parentInput.setIngredientWithReplaceCheck(index, ingredient)) {
				super.handleIngredientSaved(ingredient);
			}
		}
	}
}
