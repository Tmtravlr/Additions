package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewAdvancements;
import com.tmtravlr.additions.type.AdditionTypeAdvancement;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Button to see the list of advancements.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018
 */
public class GuiAdditionTypeButtonAdvancement extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff5F5516;
	public static final int BUTTON_COLOR_LIGHT = 0xff9E9330;
	public static final int BUTTON_COLOR_DARK = 0xff281E08;

	public GuiAdditionTypeButtonAdvancement(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeAdvancement.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.advancements.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewAdvancements(this.viewScreen, I18n.format("gui.view.addon.advancements.title", this.addon.name), this.addon));
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}

}
