package com.tmtravlr.additions.gui;

import java.util.Set;

import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * Gui Factory for additions (displays the Additions main menu when you click Config for it in the mods list)
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class AdditionsGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiAdditionsMainMenu(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}
