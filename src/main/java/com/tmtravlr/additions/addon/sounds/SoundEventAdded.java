package com.tmtravlr.additions.addon.sounds;

import com.google.gson.JsonSerializer;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;

import net.minecraft.client.audio.SoundList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added sound event, with extra details on the client-side
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since October 2018 
 */
public class SoundEventAdded extends SoundEvent {
	
	//Only need to see the sound list on the client side
	@SideOnly(Side.CLIENT)
	private SoundList soundList;

	public SoundEventAdded(ResourceLocation soundName) {
		super(soundName);
		this.setRegistryName(soundName);
	}

	@SideOnly(Side.CLIENT)
	public void setSoundList(SoundList list) {
		this.soundList = list;
	}

	@SideOnly(Side.CLIENT)
	public SoundList getSoundList() {
		//lazy load sound list if it hasn't been loaded
		if (this.soundList == null) {
			AdditionTypeSoundEvent.INSTANCE.loadSoundList(this);
		}
		
		return this.soundList;
	}
}
