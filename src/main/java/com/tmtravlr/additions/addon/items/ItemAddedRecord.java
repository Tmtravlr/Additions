package com.tmtravlr.additions.addon.items;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Record you can play in the jukebox
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2017 
 */
public class ItemAddedRecord extends ItemRecord implements IItemAdded {

	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "record");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();

	private SoundEvent music;
	public String description = "";
	
	public ItemAddedRecord() {
		this(SoundEvents.AMBIENT_CAVE);
	}
	
	public ItemAddedRecord(SoundEvent sound) {
		super("", sound);
		
		this.music = sound;
	}
	
	public boolean setMusic(SoundEvent sound) {
		Map<SoundEvent, ItemRecord> recordsList = ObfuscationReflectionHelper.getPrivateValue(ItemRecord.class, null, "field_150928_b", "RECORDS");
		
		if (recordsList.containsKey(sound)) {
			return false;
		}
		
		recordsList.remove(this.music);
		recordsList.put(sound, this);
		
		this.music = sound;
		ObfuscationReflectionHelper.setPrivateValue(ItemRecord.class, this, sound, "field_185076_b", "sound");
		
		return true;
	}
	
	@Override
	public void setTooltip(List<String> infoToAdd) {
		this.extraTooltip = infoToAdd;
	}
	
	@Override
	public void setOreDict(List<String> oreDict) {
		this.oreDictEntries = oreDict;
	}
	
	@Override
	public void setAlwaysShines(boolean alwaysShines) {
		this.shines = alwaysShines;
	}
	
	@Override
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifierList) {
		this.attributeModifiers = attributeModifierList;
	}
	
	public SoundEvent getMusic() {
		return this.music;
	}

	@Override
	public List<String> getTooltip() {
		return this.extraTooltip;
	}

	@Override
	public List<String> getOreDict() {
		return this.oreDictEntries;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public boolean getAlwaysShines() {
		return this.shines;
	}
	
	@Override
	public int getBurnTime() {
		return this.burnTime;
	}

	@Override
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return shines ? true : super.hasEffect(stack);
    }
	
	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.burnTime;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
    	for (String line : extraTooltip) {
    		if(I18n.canTranslate(line)) {
    			line = I18n.translateToLocal(line);
    		}
    		tooltip.add(line);
    	}
    	super.addInformation(stack, world, tooltip, flag);
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(displayName)) {
			return (I18n.translateToLocal(displayName)).trim();
		} else {
			return displayName;
		}
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public String getRecordNameLocal() {
    	if (I18n.canTranslate(description)) {
			return (I18n.translateToLocal(description)).trim();
		} else {
			return description;
		}
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modifiersForSlot = HashMultimap.create();
		
		if(this.attributeModifiers.containsKey(slot)) {
			for(AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				boolean isWeaponModifier = modifier.getID() == ATTACK_DAMAGE_MODIFIER;
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedRecord> {
		
		public Serializer() {
			super(TYPE, ItemAddedRecord.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedRecord itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
        	json.addProperty("music", itemAdded.music.getRegistryName().toString());
        	json.addProperty("description", itemAdded.description);
			
			return json;
		}
		
		@Override
		public ItemAddedRecord deserialize(JsonObject json, JsonDeserializationContext context) {
			SoundEvent music;
			
			ResourceLocation soundEventName = new ResourceLocation(JsonUtils.getString(json, "music"));
			music = SoundEvent.REGISTRY.getObject(soundEventName);
	    	if (music == null) {
	    		music = new SoundEvent(soundEventName).setRegistryName(soundEventName);
        		ForgeRegistries.SOUND_EVENTS.register(music);
	    	}
			
			ItemAddedRecord itemAdded = new ItemAddedRecord(music);
			
			itemAdded.description = JsonUtils.getString(json, "description", "");
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
    }
}
