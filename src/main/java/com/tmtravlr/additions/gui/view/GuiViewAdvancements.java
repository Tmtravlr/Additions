package com.tmtravlr.additions.gui.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.advancements.AdvancementAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardAdvancement;
import com.tmtravlr.additions.type.AdditionTypeAdvancement;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the advancements added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class GuiViewAdvancements extends GuiViewAdditionType {

	public GuiViewAdvancements(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.popup.advancement.addAdvancement.title"), new TextComponentTranslation("gui.view.addon.advancements.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<AdvancementAdded> additions = AdditionTypeAdvancement.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort((first, second) -> {
			if (first == null || first.id == null || second == null || second.id == null) {
				return 0;
			}
			return first.id.compareTo(second.id);
		});
		
		for (AdvancementAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardAdvancement(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiMessageBoxAddAdvancement(this);
	}
	
	protected class GuiMessageBoxAddAdvancement extends GuiMessageBoxTwoButton {
		
		public GuiMessageBoxAddAdvancement(GuiScreen parentScreen) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.advancement.addAdvancement.title"), new TextComponentTranslation("gui.popup.advancement.addAdvancement.message"), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.goToFolder"));
		}
		
		@Override
	    protected void onSecondButtonClicked() {
			File advancementFolder = AdditionTypeAdvancement.INSTANCE.getAdvancementFolder(GuiViewAdvancements.this.addon);
			
			if (!advancementFolder.isDirectory()) {
				advancementFolder.mkdirs();
			}

			CommonGuiUtils.openFolder(advancementFolder);

			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

}
