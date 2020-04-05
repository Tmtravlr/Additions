package com.tmtravlr.additions.addon.items.materials;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Represents an added item material, for tools and armor
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class ArmorMaterialAdded extends ItemMaterialAdded {
	
	protected ItemArmor.ArmorMaterial armorMaterial;
    
    public ArmorMaterialAdded(@Nonnull ItemArmor.ArmorMaterial armorMaterial) {
    	this.armorMaterial = armorMaterial;
    }
    
    @Override
    public void setEnchantability(int enchantability) {
    	if (enchantability != this.armorMaterial.getEnchantability()) {
    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, enchantability, "field_78055_h", "enchantability");
    	}
    }
	
    @Override
	public void setRepairStack(ItemStack repairStack) {
		if (repairStack != this.armorMaterial.getRepairItemStack()) {
			if (this.armorMaterial.getRepairItemStack().isEmpty()) {
				this.armorMaterial.setRepairItem(repairStack);
			} else {
	    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, repairStack, "repairMaterial");
			}
		}
	}
    
    public void setBaseArmorDurability(int baseArmorDurability) {
    	if (baseArmorDurability != this.getBaseArmorDurability()) {
    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, baseArmorDurability, "field_78048_f", "maxDamageFactor");
    	}
    }
    
    public void setToughness(float toughness) {
    	if (toughness != this.getToughness()) {
    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, toughness, "field_189415_e", "toughness");
    	}
    }
    
    public void setArmor(int helmetArmor, int chestplateArmor, int leggingsArmor, int bootsArmor) {
    	if (helmetArmor != this.getHelmetArmor() || chestplateArmor != this.getChestplateArmor() || leggingsArmor != this.getLeggingsArmor() || bootsArmor != this.getBootsArmor()) {
    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, new int[]{helmetArmor, chestplateArmor, leggingsArmor, bootsArmor}, "field_78049_g", "damageReductionAmountArray");
    	}
    }
    
    public void setEquipSound(SoundEvent equipSound) {
    	if (equipSound != this.getEquipSound()) {
    		ObfuscationReflectionHelper.setPrivateValue(ItemArmor.ArmorMaterial.class, this.armorMaterial, equipSound, "field_185020_j", "soundEvent");
    	}
    }
    
    @Override
	public String getId() {
    	return this.armorMaterial.name().replace('$', '.').toLowerCase();
    }
    
    @Override
	public ItemArmor.ArmorMaterial getArmorMaterial() {
    	return this.armorMaterial;
    }
	
	@Override
	public int getEnchantability() {
		return this.armorMaterial.getEnchantability();
	}
	
	@Override
	public ItemStack getRepairStack() {
		return this.armorMaterial.getRepairItemStack();
	}
	
	public int getBaseArmorDurability() {
		return armorMaterial.getDurability(EntityEquipmentSlot.FEET) / 11;
	}
	
	public float getToughness() {
		return armorMaterial.getToughness();
	}
	
	public int getHelmetArmor() {
		return armorMaterial.getDamageReductionAmount(EntityEquipmentSlot.HEAD);
	}
	
	public int getChestplateArmor() {
		return armorMaterial.getDamageReductionAmount(EntityEquipmentSlot.CHEST);
	}
	
	public int getLeggingsArmor() {
		return armorMaterial.getDamageReductionAmount(EntityEquipmentSlot.LEGS);
	}
	
	public int getBootsArmor() {
		return armorMaterial.getDamageReductionAmount(EntityEquipmentSlot.FEET);
	}
	
	public SoundEvent getEquipSound() {
		return armorMaterial.getSoundEvent();
	}
	
	public static class Serializer {

        public static JsonObject serialize(ArmorMaterialAdded materialAdded, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            
            json.addProperty("enchantability", materialAdded.getEnchantability());
            json.addProperty("base_armor_durability", materialAdded.getBaseArmorDurability());
            json.addProperty("toughness", materialAdded.getToughness());
            json.addProperty("helmet_armor", materialAdded.getHelmetArmor());
            json.addProperty("chestplate_armor", materialAdded.getChestplateArmor());
            json.addProperty("leggings_armor", materialAdded.getLeggingsArmor());
            json.addProperty("boots_armor", materialAdded.getBootsArmor());
            if (materialAdded.getEquipSound() != null) {
            	json.add("equip_sound", OtherSerializers.SoundEventSerializer.serialize(materialAdded.getEquipSound()));
            }
            
            if (materialAdded.getRepairStack() != ItemStack.EMPTY) {
            	json.addProperty("repair_item", Item.REGISTRY.getNameForObject(materialAdded.getRepairStack().getItem()).toString());
            	json.addProperty("repair_meta", materialAdded.getRepairStack().getMetadata());
            }
            
            return json;
        }
        
        public static ArmorMaterialAdded deserialize(JsonObject json, String materialId, JsonDeserializationContext context) throws JsonParseException {
            SoundEvent equipSound = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            
            int enchantability = JsonUtils.getInt(json, "enchantability");
            int baseArmorDurability = JsonUtils.getInt(json, "base_armor_durability");
            float toughness = JsonUtils.getFloat(json, "toughness");
            int helmetArmor = JsonUtils.getInt(json, "helmet_armor");
            int chestplateArmor = JsonUtils.getInt(json, "chestplate_armor");
            int leggingsArmor = JsonUtils.getInt(json, "leggings_armor");
            int bootsArmor = JsonUtils.getInt(json, "boots_armor");
            
		    if (json.has("equip_sound")) {
		    	equipSound = OtherSerializers.SoundEventSerializer.deserialize(json, "equip_sound");
		    } else {
		    	equipSound = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
		    }
		    
		    if (materialId.isEmpty()) {
		    	throw new JsonSyntaxException("Trying to deserialize an item material without having an id.");
		    }
		    
		    String name = (AdditionsMod.MOD_ID + "-" + materialId);
		    
		    ItemArmor.ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(name.toUpperCase(), "", baseArmorDurability, new int[]{bootsArmor, leggingsArmor, chestplateArmor, helmetArmor}, enchantability, equipSound, toughness);
		    
		    ArmorMaterialAdded newMaterial = new ArmorMaterialAdded(armorMaterial);

            return newMaterial;
        }
        
        public static void postDeserialize(JsonObject json, ArmorMaterialAdded material) {
            Item repairItem = null;
            int repairMeta = 0;
            
            if (json.has("repair_item")) {
            	repairItem = JsonUtils.getItem(json, "repair_item");
            }
            
            if (json.has("repair_meta")) {
            	repairMeta = JsonUtils.getInt(json, "repair_meta");
            }
		    
		    if (repairItem != null) {
		    	material.setRepairStack(new ItemStack(repairItem, 1, repairMeta));
		    }
        }
    }
}
