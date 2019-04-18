package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockDropInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputOreDict;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiEditBlockDropInput extends GuiEditUpdate {
	
	protected GuiComponentBlockDropInput parentInput;
	
	protected GuiComponentDropdownInput dropTypeInput;
	protected GuiComponentDisplayText customDropMessage;

	public GuiEditBlockDropInput(GuiScreen parentScreen, GuiComponentBlockDropInput parentInput) {
		super(parentScreen, I18n.format("gui.edit.blockDrop.title"));
		this.parentInput = parentInput;
	}
	
	@Override
	public void initComponents() {
//		this.oreOrGroupMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.itemStackGroup.message.oreOrGroup"));
//		
//		this.oreDictInput = new GuiComponentSuggestionInputOreDict(I18n.format("gui.edit.itemStackGroup.oreDict"), this);
//		this.itemGroupInput = new GuiComponentListInput<GuiComponentItemStackInput>(I18n.format("gui.edit.itemStackGroup.itemGroup"), this) {
//
//			@Override
//			public GuiComponentItemStackInput createBlankComponent() {
//				GuiComponentItemStackInput input = new GuiComponentItemStackInput("", this.editScreen);
//				input.disableCount();
//				return input;
//			}
//			
//		};
//		
//		this.components.add(this.oreOrGroupMessage);
//		this.components.add(this.oreDictInput);
//		this.components.add(this.itemGroupInput);
	}

	@Override
    public void saveObject() {
		IngredientOreNBT ingredient = new IngredientOreNBT();
		String oreName = "";
		NonNullList<ItemStack> stackList = NonNullList.create();
		
//        if (!this.oreDictInput.getText().isEmpty()) {
//        	oreName = this.oreDictInput.getText();
//    	} 
//
//        if (!this.itemGroupInput.getComponents().isEmpty()) {
//    		this.itemGroupInput.getComponents().forEach(component -> {
//    			if (!component.getItemStack().isEmpty()) {
//    				stackList.add(component.getItemStack());
//    			}
//    		});
//    	}
        
        ingredient.setOreEntryAndStackList(oreName, stackList);
        
        if (this.parentScreen instanceof GuiView) {
        	((GuiView) this.parentScreen).refreshView();
        }
        
        this.handleIngredientSaved(ingredient);
    }
	
	protected void handleIngredientSaved(IngredientOreNBT ingredient) {
    	this.mc.displayGuiScreen(this.parentScreen);
	}

}
