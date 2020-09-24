package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.GuiEditSoundEvent;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Dropdown list specifically for sound events, which lets you create a new sound event on the fly.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2018
 */
public class GuiComponentDropdownInputSoundEvent extends GuiComponentDropdownInput<SoundEvent> {
	
	private final String addNewSoundEventId = I18n.format("gui.dropdown.soundEvent.addNew");
	private Addon addon;
	
	private SoundEvent addNewSoundEvent = new SoundEvent(null) {

		@Override
	    public ResourceLocation getSoundName() {
	    	return new ResourceLocation(" ") {
	    		
	    		@Override
				public String toString() {
					return addNewSoundEventId;
				}
	    		
	    	};
	    }
	    
	};

	public GuiComponentDropdownInputSoundEvent(String label, Addon addon, GuiEdit editScreen) {
		super(label, editScreen);
		this.addon = addon;

		this.refreshSelections();
	}
	
	@Override
	public String getSelectionName(SoundEvent selected) {
		return selected.getSoundName().toString();
	}
	
	@Override
	public void setSelected(SoundEvent selected) {
		if (selected != null && selected.getSoundName().equals(addNewSoundEventId)) {
			this.editScreen.mc.displayGuiScreen(new GuiEditSoundEvent(this.editScreen, I18n.format("gui.edit.soundEvent.title"), this.addon, null));
		} else {
			super.setSelected(selected);
		}
	}
	
	public void refreshSelections() {
		List<SoundEvent> soundEvents = new ArrayList<>();
		soundEvents.add(this.addNewSoundEvent);
		soundEvents.addAll(ForgeRegistries.SOUND_EVENTS.getValuesCollection());
		
		this.setSelections(soundEvents);
	}

}
