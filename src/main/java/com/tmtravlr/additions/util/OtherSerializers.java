package com.tmtravlr.additions.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.addon.blocks.mapcolors.BlockMapColorManager;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.type.attribute.AttributeTypeManager;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class OtherSerializers {
    
	public static class PotionEffectSerializer {
		
		public static JsonElement serialize(PotionEffect effect) {
        	JsonObject json = new JsonObject();
			
        	String name = Potion.REGISTRY.getNameForObject(effect.getPotion()).toString();
        	json.addProperty("name", name);
        	json.addProperty("duration", effect.getDuration());
        	json.addProperty("amplifier", effect.getAmplifier());
        	json.addProperty("ambient", effect.getIsAmbient());
        	json.addProperty("show_particles", effect.doesShowParticles());
        	
        	return json;
        }
		
		public static PotionEffect deserialize(JsonObject json) {
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
		
		public static JsonElement serialize(Multimap<EntityEquipmentSlot, AttributeModifier> modifiers) {
        	JsonArray jsonArray = new JsonArray();
        	
        	for (EntityEquipmentSlot slot : modifiers.keySet()) {
        		for (AttributeModifier modifier : modifiers.get(slot)) {
        			if (modifier != null) {
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
        	}
        	
        	return jsonArray;
        }
		
		public static Multimap<EntityEquipmentSlot, AttributeModifier> deserialize(JsonArray jsonArray) {
			Multimap<EntityEquipmentSlot, AttributeModifier> multimap = HashMultimap.create();
        	
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject json = JsonUtils.getJsonObject(jsonArray.get(i), "member of attribute modifier array");
				
				String name = JsonUtils.getString(json, "attribute_name");
				float amount = JsonUtils.getFloat(json, "amount");
				int operation = JsonUtils.getInt(json, "operation", 0);
				String slotName = JsonUtils.getString(json, "slot");
				EntityEquipmentSlot slot = EntityEquipmentSlot.fromString(slotName);
				
				if (slot == null) {
					throw new JsonSyntaxException("Unknown equipment slot '" + slotName + "'");
				}
				
				String uuidString = JsonUtils.getString(json, "uuid", "");
				
				UUID uuid = AttributeTypeManager.getUUIDfromString(uuidString, name, slot);
				
				multimap.put(slot, new AttributeModifier(uuid, name, amount, operation));
			}
			
        	return multimap;
        }
	}

	public static class StringListSerializer {
		
		public static JsonElement serialize(String[] list) {
			return serialize(Arrays.asList(list));
		}
		
		public static JsonElement serialize(Collection<String> list) {
        	if (list.size() == 1) {
        		return new JsonPrimitive(list.iterator().next());
        	}
        	
        	JsonArray jsonArray = new JsonArray();
        	
        	for (String string : list) {
        		jsonArray.add(string);
        	}
        	
        	return jsonArray;
        }
		
		public static List<String> deserialize(JsonElement jsonElement, String elementName) {
			List<String> stringList = new ArrayList<>();
			
			if (JsonUtils.isString(jsonElement)) {
				stringList.add(JsonUtils.getString(jsonElement, elementName));
        	} else if (jsonElement.isJsonArray()) {
        		JsonArray jsonArray = jsonElement.getAsJsonArray();
        		for (JsonElement jsonArrayElement : jsonArray) {
    				stringList.add(JsonUtils.getString(jsonArrayElement, "member of " + elementName + " array"));
        		}
        	} else {
        		throw new JsonSyntaxException("Expected '" + elementName + "' to be a string or a list of strings.");
        	}
			
			return stringList;
        }
	}
	
	public static class ItemListSerializer {
		
		public static JsonElement serialize(Collection<Item> list) {
			if (list.size() == 1) {
				Item item = list.iterator().next();
				ResourceLocation itemName = Item.REGISTRY.getNameForObject(item);
				
				if (itemName == null) {
					throw new IllegalArgumentException("Can't serialize unknown item " + item);
				}
				
				return new JsonPrimitive(itemName.toString());
			}
			
			JsonArray jsonArray = new JsonArray();
			
			for (Item item : list) {
				ResourceLocation itemName = Item.REGISTRY.getNameForObject(item);
				
				if (itemName == null) {
					throw new IllegalArgumentException("Can't serialize unknown item " + item);
				}
				
				jsonArray.add(Item.REGISTRY.getNameForObject(item).toString());
			}
			
			return jsonArray;
		}
		
		public static List<Item> deserialize(JsonElement jsonElement, String elementName) {
			List<Item> itemList = new ArrayList<>();
			
			if (JsonUtils.isString(jsonElement)) {
				itemList.add(JsonUtils.getItem(jsonElement, elementName));
			} else {
				for (JsonElement jsonArrayElement : JsonUtils.getJsonArray(jsonElement, elementName)) {
					itemList.add(JsonUtils.getItem(jsonArrayElement, "member of " + elementName + " array"));
				}
			}
			
			return itemList;
        }
	}

	public static class ItemStackSerializer {
		
		public static JsonObject serialize(ItemStack itemStack) {
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
		
		public static ItemStack deserialize(JsonObject json) {
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
	
	/**
	 * The CraftingHelper has it's own way of deserializing item stacks, so 
	 * here's a complement to serialize them in the same way.
	 */
	public static class CraftingHelperItemStackSerializer {
		public static JsonObject serialize(ItemStack itemStack) {
        	JsonObject json = new JsonObject();
        	
        	ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(itemStack.getItem());	
        	
        	if (itemName == null) {
        		throw new IllegalArgumentException("Can't serialize unknown item " + itemStack);
        	}
        	
        	json.addProperty("item", itemName.toString());
        	
        	if (itemStack.getCount() != 1) {
        		json.addProperty("count", itemStack.getCount());
        	}
        	
        	if (itemStack.getItemDamage() != 0 || itemStack.getItem().getHasSubtypes()) {
    			json.addProperty("data", itemStack.getItemDamage());
        	}
        	
        	if (itemStack.hasTagCompound()) {
        		json.addProperty("nbt", itemStack.getTagCompound().toString());
        	}
        	
        	return json;
        }
	}
	
	public static class SoundEventSerializer {
		
		public static JsonElement serialize(SoundEvent soundEvent) {
			return new JsonPrimitive(soundEvent.getRegistryName().toString());
		}
		
		public static SoundEvent deserialize(JsonObject json, String elementName) {
			if (json.has(elementName)) {
	            return deserialize(json.get(elementName), elementName);
	        } else {
	            throw new JsonSyntaxException("Missing " + elementName + ", expected to find a sound event");
	        }
		}
		
		public static SoundEvent deserialize(JsonElement jsonElement, String elementName) {
			String soundName = JsonUtils.getString(jsonElement, elementName);
			SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
			
			if (soundEvent == null) {
				throw new JsonSyntaxException("Expected '" + elementName + "' to be a sound event, was unknown string '" + soundName + "'");
			}
			
			return soundEvent;
		}
	}
	
	public static class BlockMaterialSerializer {
		
		public static JsonElement serialize(Material blockMaterial) {
			return new JsonPrimitive(BlockMaterialManager.getBlockMaterialName(blockMaterial).toString());
		}
		
		public static Material deserialize(JsonObject json, String elementName) {
			if (json.has(elementName)) {
	            return deserialize(json.get(elementName), elementName);
	        } else {
	            throw new JsonSyntaxException("Missing " + elementName + ", expected to find a block material");
	        }
		}
		
		public static Material deserialize(JsonElement jsonElement, String elementName) {
			String blockMaterialName = JsonUtils.getString(jsonElement, elementName);
			Material blockMaterial = BlockMaterialManager.getBlockMaterial(new ResourceLocation(blockMaterialName));
			
			if (blockMaterial == null) {
				throw new JsonSyntaxException("Expected '" + elementName + "' to be a block material, was unknown string '" + blockMaterialName + "'");
			}
			
			return blockMaterial;
		}
	}
	
	public static class BlockMapColorSerializer {
		
		public static JsonElement serialize(MapColor blockMapColor) {
			return new JsonPrimitive(BlockMapColorManager.getBlockMapColorName(blockMapColor).toString());
		}
		
		public static MapColor deserialize(JsonObject json, String elementName) {
			if (json.has(elementName)) {
	            return deserialize(json.get(elementName), elementName);
	        } else {
	            throw new JsonSyntaxException("Missing " + elementName + ", expected to find a block map color");
	        }
		}
		
		public static MapColor deserialize(JsonElement jsonElement, String elementName) {
			String blockMapColorName = JsonUtils.getString(jsonElement, elementName);
			MapColor blockMapColor = BlockMapColorManager.getBlockMaterial(new ResourceLocation(blockMapColorName));
			
			if (blockMapColor == null) {
				throw new JsonSyntaxException("Expected '" + elementName + "' to be a block map color, was unknown string '" + blockMapColorName + "'");
			}
			
			return blockMapColor;
		}
	}
	
}
