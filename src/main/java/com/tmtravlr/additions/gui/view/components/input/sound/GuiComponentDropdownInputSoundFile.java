package com.tmtravlr.additions.gui.view.components.input.sound;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditSound;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiComponentDropdownInputSoundFile extends GuiComponentDropdownInput<ResourceLocation> {
	
	private final String addNewSoundFileLabel = I18n.format("gui.dropdown.soundFile.addNew");
	private final GuiEditSound editSoundScreen;
	
	private ResourceLocation addNewSoundFile = new ResourceLocation(AdditionsMod.MOD_ID, "Add New Sound File");
	private Addon addon;

	public GuiComponentDropdownInputSoundFile(String label, GuiEdit editScreen, GuiEditSound editSoundScreen, Addon addon) {
		super(label, editScreen);
		this.editSoundScreen = editSoundScreen;
		this.addon = addon;

		this.refreshSelections();
	}
	
	@Override
	public String getSelectionName(ResourceLocation selected) {
		if (selected == addNewSoundFile) {
			return addNewSoundFileLabel;
		}
		return super.getSelectionName(selected);
	}
	
	@Override
	public void setSelected(ResourceLocation selected) {
		if (selected == addNewSoundFile) {
			List<ResourceLocation> soundFileLocations = new ArrayList<>(this.selections);
			soundFileLocations.remove(addNewSoundFile);
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxSelectSoundFile(this.editSoundScreen, soundFileLocations, this.addon));
		} else {
			super.setSelected(selected);
		}
	}
	
	public void refreshSelections() {
		List<ResourceLocation> soundFileLocations = new ArrayList<>();
		soundFileLocations.add(this.addNewSoundFile);
		soundFileLocations.addAll(AdditionTypeSoundEvent.INSTANCE.getAllSoundFilesForAddon(this.addon));
		
		this.setSelections(soundFileLocations);
	}
}
