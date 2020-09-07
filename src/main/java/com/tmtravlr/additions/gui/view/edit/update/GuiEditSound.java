package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.sound.GuiComponentDropdownInputSoundFile;
import com.tmtravlr.additions.gui.view.components.input.sound.GuiComponentSoundInput;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for updating a sound for the sound component.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date October 2018
 */
public class GuiEditSound extends GuiEditUpdate {

    private final GuiComponentSoundInput soundInput;
	private final Addon addon;
	
	private GuiComponentDisplayText selectTypeText;
	private GuiComponentDropdownInput<Sound.Type> soundTypeInput;
	private GuiComponentDropdownInput<SoundEvent> soundEventInput;
	private GuiComponentDropdownInputSoundFile soundFileInput;
	private GuiComponentBooleanInput streamInput;
	private GuiComponentFloatInput volumeInput;
	private GuiComponentFloatInput pitchInput;
	private GuiComponentIntegerInput weightInput;
    
	public GuiEditSound(GuiScreen parentScreen, String title, GuiComponentSoundInput soundInput, Addon addon) {
		super(parentScreen, title);
		this.addon = addon;
		this.soundInput = soundInput;
	}
	
	@Override
	public void initComponents() {
		this.selectTypeText = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.sound.type.info"));
		
		this.soundTypeInput = new GuiComponentDropdownInput<Sound.Type>(I18n.format("gui.edit.sound.type.label"), this) {
			
			@Override
			public String getSelectionName(Sound.Type selected) {
				return selected == null ? "" : I18n.format(selected == Sound.Type.SOUND_EVENT ? "gui.edit.sound.event.label" : "gui.edit.sound.sound.label");
			}
			
			@Override
			public void setSelected(Sound.Type selected) {
				super.setSelected(selected);
				GuiEditSound.this.onSelectType(selected);
			}
			
		};
		this.soundTypeInput.setSelections(Sound.Type.values());
		this.soundTypeInput.setDefaultSelected(soundInput.getSound().getType() != null ? soundInput.getSound().getType() : Sound.Type.FILE);
		
		this.soundEventInput = new GuiComponentDropdownInput<SoundEvent>(I18n.format("gui.edit.sound.event.label"), this) {
			
			@Override
			public String getSelectionName(SoundEvent selected) {
				return selected == null ? "" : selected.getSoundName().toString();
			}
			
		};
		this.soundEventInput.setSelections(ForgeRegistries.SOUND_EVENTS.getValuesCollection());
		if (soundInput.getSound().getType() == Sound.Type.SOUND_EVENT) {
			this.soundEventInput.setDefaultSelected(ForgeRegistries.SOUND_EVENTS.getValue(soundInput.getSound().getSoundLocation()));
		}
		
		this.soundFileInput = new GuiComponentDropdownInputSoundFile(I18n.format("gui.edit.sound.sound.label"), this, this, this.addon);
		if (soundInput.getSound().getType() == Sound.Type.FILE) {
			this.soundFileInput.setDefaultSelected(soundInput.getSound().getSoundLocation());
		}
		
		this.streamInput = new GuiComponentBooleanInput(I18n.format("gui.edit.sound.stream.label"), this);
		this.streamInput.setDefaultBoolean(soundInput.getSound().isStreaming());
		
		this.volumeInput = new GuiComponentFloatInput(I18n.format("gui.edit.sound.volume.label"), this, false);
		this.volumeInput.setMinimum(0f);
		this.volumeInput.setDefaultFloat(soundInput.getSound().getVolume());
		
		this.pitchInput = new GuiComponentFloatInput(I18n.format("gui.edit.sound.pitch.label"), this, false);
		this.pitchInput.setMinimum(0f);
		this.pitchInput.setMaximum(2f);
		this.pitchInput.setDefaultFloat(soundInput.getSound().getPitch());
		
		this.weightInput = new GuiComponentIntegerInput(I18n.format("gui.edit.sound.weight.label"), this, false);
		this.weightInput.setInfo(new TextComponentTranslation("gui.edit.sound.weight.info"));
		this.weightInput.setMinimum(1);
		this.weightInput.setDefaultInteger(soundInput.getSound().getWeight());
		
		this.components.add(this.selectTypeText);
		this.components.add(this.soundTypeInput);
		
		if (this.soundTypeInput.getSelected() == Sound.Type.SOUND_EVENT) {
			this.components.add(this.soundEventInput);
		} else {
			this.components.add(this.soundFileInput);
		}
		
		this.components.add(this.streamInput);
		this.components.add(this.volumeInput);
		this.components.add(this.pitchInput);
		this.components.add(this.weightInput);
	}
	
	@Override
	public void saveObject() {
		String soundName;
		
		if (this.soundTypeInput.getSelected() == Sound.Type.SOUND_EVENT) {
			soundName = this.soundEventInput.getSelected().getSoundName().toString();
		} else {
			soundName = this.soundFileInput.getSelected().toString();
		}
        
		this.soundInput.setSound(soundName == null ? null : new Sound(soundName, this.volumeInput.getFloat(), this.pitchInput.getFloat(), this.weightInput.getInteger(), this.soundTypeInput.getSelected(), this.streamInput.getBoolean()));
		this.parentScreen.mc.displayGuiScreen(parentScreen);
	}
	
	@Override
	public void refreshView() {
		this.soundFileInput.refreshSelections();
	}
    
    public void selectSoundFile(ResourceLocation newSoundFile) {
    	this.soundFileInput.setSelected(newSoundFile);
    }
	
    private void onSelectType(Sound.Type type) {
    	if (type == Sound.Type.SOUND_EVENT) {
    		this.soundFileInput.setSelected(null);
    		int index = this.components.indexOf(this.soundFileInput);
    		if (index >= 0) {
    			this.components.set(index, this.soundEventInput);
    		}
    	} else {
    		this.soundEventInput.setSelected(null);
    		int index = this.components.indexOf(this.soundEventInput);
    		if (index >= 0) {
    			this.components.set(index, this.soundFileInput);
    		}
    	}
    }

}
