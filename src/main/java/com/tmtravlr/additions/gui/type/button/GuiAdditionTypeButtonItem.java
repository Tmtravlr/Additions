package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewItems;
import com.tmtravlr.additions.type.AdditionType;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.type.AdditionTypeManager;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Button to see the list of items.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiAdditionTypeButtonItem extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff14495c;
	public static final int BUTTON_COLOR_LIGHT = 0xff3084a3;
	public static final int BUTTON_COLOR_DARK = 0xff08212b;

	public GuiAdditionTypeButtonItem(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeItem.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.items.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewItems(this.viewScreen, I18n.format("gui.view.addon.items.title", this.addon.name), this.addon));
	}

}
