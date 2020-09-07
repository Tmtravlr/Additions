package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewRecipes;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.resources.I18n;

/**
 * Button to see the list of creative tabs.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiAdditionTypeButtonRecipe extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff651D1C;
	public static final int BUTTON_COLOR_LIGHT = 0xff9D2220;
	public static final int BUTTON_COLOR_DARK = 0xff360606;

	public GuiAdditionTypeButtonRecipe(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeRecipe.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.recipes.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewRecipes(this.viewScreen, I18n.format("gui.view.addon.recipes.title", this.addon.name), this.addon));
	}

}
