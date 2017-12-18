package com.tmtravlr.additions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;

public class OtherSerializers {

	public static final Map<EntityEquipmentSlot, UUID> ARMOR_MODIFIERS = new HashMap<>();
	static {
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.FEET, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.LEGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.CHEST, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.HEAD, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.MAINHAND, UUID.fromString("3FCB55D3-564C-F348-4A79-B5C9A33C13DF"));
		ARMOR_MODIFIERS.put(EntityEquipmentSlot.OFFHAND, UUID.fromString("AC233E1C-4180-4865-B01B-BCFE9785ACA3"));
	}
    public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    
	public static class PotionEffectSerializer {
		
		public static JsonElement serialize(PotionEffect effect, JsonSerializationContext context) {
        	JsonObject json = new JsonObject();
			
        	String name = Potion.REGISTRY.getNameForObject(effect.getPotion()).toString();
        	json.addProperty("name", name);
        	json.addProperty("duration", effect.getDuration());
        	json.addProperty("amplifier", effect.getAmplifier());
        	json.addProperty("ambient", effect.getIsAmbient());
        	json.addProperty("show_particles", effect.doesShowParticles());
        	
        	return json;
        }
		
		public static PotionEffect deserialize(JsonObject json, JsonDeserializationContext context) {
        	String name = JsonUtils.getString(json, "name");
        	Potion potion = Potion.getPotionFromResourceLocation(name);
        	
        	if (potion == null) {
        		throw new JsonSyntaxException("Unknown potion " + name);
        	}
        	
        	int duration = JsonUtils.getInt(json, "duration", 1);
        	int amplifier = JsonUtils.getInt(json, "amplifier", 0);
        	boolean ambient = JsonUtils.getBoolean(json, "ambient", false);
        	boolean showParticles = JsonUtils.getBoolean(json, "show_particles", true);
        	
        	return new PotionEffect(potion, duration, amplifier, ambient, showParticles);
        }
	}

	public static class AttributeModifierListSerializer {
		
		public static JsonElement serialize(Multimap<EntityEquipmentSlot, AttributeModifier> modifiers, JsonSerializationContext context) {
        	JsonArray jsonArray = new JsonArray();
        	
        	for (EntityEquipmentSlot slot : modifiers.keySet()) {
        		for (AttributeModifier modifier : modifiers.get(slot)) {
					JsonObject json = new JsonObject();
					
		        	json.addProperty("attribute_name", modifier.getName());
		        	json.addProperty("amount", modifier.getAmount());
		        	if (modifier.getOperation() != 0) {
		        		json.addProperty("operation", modifier.getOperation());
		        	}
		        	json.addProperty("slot", slot.getName());
		        	json.addProperty("uuid", modifier.getID().toString());
	        		
		        	jsonArray.add(json);
        		}
        	}
        	
        	return jsonArray;
        }
		
		public static Multimap<EntityEquipmentSlot, AttributeModifier> deserialize(JsonArray jsonArray, JsonDeserializationContext context) {
			Multimap<EntityEquipmentSlot, AttributeModifier> multimap = HashMultimap.create();
        	
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject json = JsonUtils.getJsonObject(jsonArray.get(i), "member of attribute modifier array");
				
				String name = JsonUtils.getString(json, "attribute_name");
				int amount = JsonUtils.getInt(json, "amount");
				int operation = JsonUtils.getInt(json, "operation", 0);
				String slotName = JsonUtils.getString(json, "slot");
				EntityEquipmentSlot slot = EntityEquipmentSlot.fromString(slotName);
				
				if (slot == null) {
					throw new JsonSyntaxException("Unknown equipment slot '" + slotName + "'");
				}
				
				UUID uuid;
				if (JsonUtils.hasField(json, "uuid")) {
					uuid = UUID.fromString(JsonUtils.getString(json, "uuid"));
				} else if (name.equals("generic.attackDamage") && slot == EntityEquipmentSlot.MAINHAND) {
					uuid = ATTACK_DAMAGE_MODIFIER;
				} else if (name.equals("generic.attackSpeed") && slot == EntityEquipmentSlot.MAINHAND) {
					uuid = ATTACK_SPEED_MODIFIER;
				} else if ((name.equals("generic.armor") || name.equals("generic.armorToughness")) && ARMOR_MODIFIERS.containsKey(slot)) {
					uuid = ARMOR_MODIFIERS.get(slot);
				} else {
					uuid = UUID.randomUUID();
				}
				
				multimap.put(slot, new AttributeModifier(uuid, name, amount, operation));
			}
			
        	return multimap;
        }
	}

	public static class StringListSerializer {
		
		public static JsonElement serialize(List<String> list, JsonSerializationContext context) {
        	if(list.size() == 1) {
        		return new JsonPrimitive(list.get(0));
        	}
        	
        	JsonArray json = new JsonArray();
        	
        	for(String string : list) {
        		json.add(string);
        	}
        	
        	return json;
        }
		
		public static List<String> deserialize(JsonElement json, String elementName, JsonDeserializationContext context) {
			List<String> stringList = new ArrayList<>();
			
			if (JsonUtils.isString(json)) {
				stringList.add(json.getAsString());
        	} else if (json.isJsonArray()) {
        		JsonArray jsonArray = json.getAsJsonArray();
        		for (int i = 0; i < jsonArray.size(); i++) {
    				stringList.add(JsonUtils.getString(jsonArray.get(i), "member of " + elementName + " array"));
        		}
        	} else {
        		throw new JsonSyntaxException("Expected '" + elementName + "' to be a string or a list of strings.");
        	}
			
			return stringList;
        }
	}

	public static class ItemStackSerializer {
		
		public static JsonObject serialize(ItemStack itemStack, JsonSerializationContext context) {
        	JsonObject json = new JsonObject();
        	
        	ResourceLocation itemName = Item.REGISTRY.getNameForObject(itemStack.getItem());	
        	
        	if (itemName == null) {
        		throw new IllegalArgumentException("Can't serialize unknown item " + itemStack);
        	}
        	
        	json.addProperty("item", itemName.toString());
        	
        	if (itemStack.getCount() != 1) {
        		json.addProperty("count", itemStack.getCount());
        	}
        	
        	if (itemStack.getItemDamage() != 0) {
        		if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
        			json.addProperty("damage", "any");
        		} else {
        			json.addProperty("damage", itemStack.getItemDamage());
        		}
        	}
        	
        	if (itemStack.hasTagCompound()) {
        		json.addProperty("tag", itemStack.getTagCompound().toString());
        	}
        	
        	return json;
        }
		
		public static ItemStack deserialize(JsonObject json, JsonDeserializationContext context) {
			Item item = JsonUtils.getItem(json, "item");
			int count = JsonUtils.getInt(json, "count", 1);
			
			int damage;
			if (JsonUtils.isString(json, "damage")) {
				damage = OreDictionary.WILDCARD_VALUE;
			} else {
				damage = JsonUtils.getInt(json, "damage", 0);
			}
			
			ItemStack itemStack = new ItemStack(item, count, damage);
			
			if (JsonUtils.isString(json, "tag")) {
				try {
				itemStack.setTagCompound(JsonToNBT.getTagFromJson(JsonUtils.getString(json, "tag")));
                } catch (NBTException nbtexception) {
                    throw new JsonSyntaxException(nbtexception);
                }
			}
			
			return itemStack;
        }
	}
	
}
