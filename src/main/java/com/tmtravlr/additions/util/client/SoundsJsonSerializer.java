package com.tmtravlr.additions.util.client;

import com.google.gson.*;
import com.tmtravlr.additions.addon.sounds.SoundEventAdded;

import net.minecraft.client.audio.Sound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;


/**
 * Serializes the sounds.json file for an addon
 * TODO: keep up to date with {@link net.minecraft.client.audio.SoundListSerializer}
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date October 2018
 */
@SideOnly(Side.CLIENT)
public class SoundsJsonSerializer implements JsonSerializer<Collection<SoundEventAdded>> {
	
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(Collection.class, new SoundsJsonSerializer())
			.setPrettyPrinting()
			.create();

	@Override
	public JsonElement serialize(Collection<SoundEventAdded> soundEvents, Type type, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		
		for (SoundEventAdded soundEvent : soundEvents) {
			JsonObject jsonSoundList = new JsonObject();
			
			jsonSoundList.add("sounds", serializeSounds(soundEvent.getSoundList().getSounds()));
			if (soundEvent.getSoundList().canReplaceExisting()) {
				jsonSoundList.addProperty("replace", true);
			}
			if (soundEvent.getSoundList().getSubtitle() != null) {
				jsonSoundList.addProperty("subtitle", soundEvent.getSoundList().getSubtitle());
			}
			
			if (soundEvent.getRegistryName() != null) json.add(soundEvent.getRegistryName().getResourcePath(), jsonSoundList);
		}
		
		return json;
	}

    private JsonArray serializeSounds(List<Sound> soundList) {
    	JsonArray jsonArray = new JsonArray();

        if (!soundList.isEmpty()) {
            for (Sound sound : soundList) {
                JsonElement soundElement = this.serializeSound(sound);
                jsonArray.add(soundElement);
            }
        }

        return jsonArray;
    }

    private JsonElement serializeSound(Sound sound) {
    	if (sound.getType() == Sound.Type.FILE && sound.getVolume() == 1.0F && sound.getPitch() == 1.0F && sound.getWeight() == 1 && !sound.isStreaming()) {
    		return new JsonPrimitive(sound.getSoundLocation().toString());
    	}
    	
		JsonObject json = new JsonObject();
		
		json.addProperty("name", sound.getSoundLocation().toString());
		if (sound.getType() == Sound.Type.SOUND_EVENT) {
			json.addProperty("type", "event");
		}
		if (sound.getVolume() != 1.0F) {
			json.addProperty("volume", sound.getVolume());
		}
		if (sound.getPitch() != 1.0F) {
			json.addProperty("pitch", sound.getPitch());
		}
		if (sound.getWeight() != 1) {
			json.addProperty("weight", sound.getWeight());
		}
		if (sound.isStreaming()) {
			json.addProperty("stream", true);
		}
		
		return json;
    }
}
