package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewEffects;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.resources.I18n;

/**
 * Button to see the list of effects.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019 
 */
public class GuiAdditionTypeButtonEffect extends GuiAdditionTypeButtonColored {

	public static final int BUTTON_COLOR_HOVER = 0xff34500C;
	public static final int BUTTON_COLOR_LIGHT = 0xff6B8C16;
	public static final int BUTTON_COLOR_DARK = 0xff182503;

	public GuiAdditionTypeButtonEffect(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeEffect.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.effects.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewEffects(this.viewScreen, I18n.format("gui.view.addon.effects.title", this.addon.name), this.addon));
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}

}
