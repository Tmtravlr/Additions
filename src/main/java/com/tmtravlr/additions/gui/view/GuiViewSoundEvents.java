package com.tmtravlr.additions.gui.view;

import java.util.Comparator;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.sounds.SoundEventAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardSoundEvent;
import com.tmtravlr.additions.gui.view.edit.GuiEditSoundEvent;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a list of cards with info about the functions added
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since October 2018 
 */
public class GuiViewSoundEvents extends GuiViewAdditionType {

	public GuiViewSoundEvents(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon, I18n.format("gui.edit.soundEvent.title"), new TextComponentTranslation("gui.view.addon.soundEvents.none.message", addon.name));
	}
	
	@Override
	public void addAdditions() {
		List<SoundEventAdded> additions = AdditionTypeSoundEvent.INSTANCE.getAllAdditions(this.addon);
		
		additions.sort(new Comparator<SoundEventAdded>(){

			@Override
			public int compare(SoundEventAdded first, SoundEventAdded second) {
				if (first == null || first.getSoundName() == null || second == null || second.getSoundName() == null) {
					return 0;
				}
				return first.getSoundName().compareTo(second.getSoundName());
			}
			
		});
		
		for (SoundEventAdded addition : additions) {
			this.additions.addAdditionCard(new GuiAdditionCardSoundEvent(this, this.addon, addition));
		}
	}

	@Override
	protected GuiScreen getNewAdditionScreen() {
		return new GuiEditSoundEvent(this, I18n.format("gui.edit.soundEvent.title"), this.addon, null);
	}

}
