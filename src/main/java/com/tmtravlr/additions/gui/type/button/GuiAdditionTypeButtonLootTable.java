package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewLootTables;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.resources.I18n;

/**
 * Button to see the list of loot tables.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018
 */
public class GuiAdditionTypeButtonLootTable extends GuiAdditionTypeButtonColored {
	
	public static final int BUTTON_COLOR_HOVER = 0xff166a51;
	public static final int BUTTON_COLOR_LIGHT = 0xff30a487;
	public static final int BUTTON_COLOR_DARK = 0xff082c21;

	public GuiAdditionTypeButtonLootTable(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
		
		int numAdded = AdditionTypeLootTable.INSTANCE.getAllAdditions(this.addon).size();
		
		this.setLabel(I18n.format("gui.view.addon.lootTables.label", numAdded));
		this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
	}

	@Override
	public void onClick() {
		CommonGuiUtils.playClickSound();
		this.viewScreen.mc.displayGuiScreen(new GuiViewLootTables(this.viewScreen, I18n.format("gui.view.addon.lootTables.title", this.addon.name), this.addon));
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}

}
