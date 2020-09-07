package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewCreativeTabs;
import com.tmtravlr.additions.type.AdditionType;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeManager;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Button to see the list of creative tabs.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiAdditionTypeButtonCreativeTab extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff161d69;
	public static final int BUTTON_COLOR_LIGHT = 0xff3036a3;
	public static final int BUTTON_COLOR_DARK = 0xff08082b;

	public GuiAdditionTypeButtonCreativeTab(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeCreativeTab.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.creativeTabs.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewCreativeTabs(this.viewScreen, I18n.format("gui.view.addon.creativeTabs.title", this.addon.name), this.addon));
	}

}
