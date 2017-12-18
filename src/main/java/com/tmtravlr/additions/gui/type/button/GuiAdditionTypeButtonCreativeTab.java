package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewCreativeTabs;
import com.tmtravlr.additions.type.AdditionType;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Button to see the list of creative tabs.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiAdditionTypeButtonCreativeTab extends GuiAdditionTypeButtonColored {

	public GuiAdditionTypeButtonCreativeTab(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeCreativeTab.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.creativeTabs.label", numAdded));
		this.setColors(0xff12124b, 0xff161d69, 0xff3036a3, 0xff08082b);
	}

	@Override
	public void onClick() {
		this.viewScreen.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		this.viewScreen.mc.displayGuiScreen(new GuiViewCreativeTabs(this.viewScreen, I18n.format("gui.view.addon.creativeTabs.title", this.addon.name), this.addon));
	}

}
