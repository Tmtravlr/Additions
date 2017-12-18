package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewItems;
import com.tmtravlr.additions.type.AdditionType;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.type.AdditionTypeManager;

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

	public GuiAdditionTypeButtonItem(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeItem.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.items.label", numAdded));
		this.setColors(0xff12384b, 0xff14495c, 0xff3084a3, 0xff08212b);
	}

	@Override
	public void onClick() {
		this.viewScreen.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		this.viewScreen.mc.displayGuiScreen(new GuiViewItems(this.viewScreen, I18n.format("gui.view.addon.items.title", this.addon.name), this.addon));
	}

}
