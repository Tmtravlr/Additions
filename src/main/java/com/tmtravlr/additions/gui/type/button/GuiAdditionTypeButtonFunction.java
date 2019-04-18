package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewFunctions;
import com.tmtravlr.additions.type.AdditionTypeFunction;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Button to see the list of functions.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018
 */
public class GuiAdditionTypeButtonFunction extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff683D00;
	public static final int BUTTON_COLOR_LIGHT = 0xffA76000;
	public static final int BUTTON_COLOR_DARK = 0xff301B00;

	public GuiAdditionTypeButtonFunction(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeFunction.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.functions.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewFunctions(this.viewScreen, I18n.format("gui.view.addon.functions.title", this.addon.name), this.addon));
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}

}
