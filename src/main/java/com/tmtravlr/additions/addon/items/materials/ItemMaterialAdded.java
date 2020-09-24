package com.tmtravlr.additions.addon.items.materials;

import com.google.gson.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

/**
 * Represents an added item material, for tools and armor
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public abstract class ItemMaterialAdded {
	
	private static String nextMaterialId = "";
	
	public static void setNextMaterialId(String nextId) {
		nextMaterialId = nextId;
	}
	
	public boolean isToolMaterial() {
		return this instanceof ToolMaterialAdded;
	}
	
	public boolean isArmorMaterial() {
		return this instanceof ArmorMaterialAdded;
	}
	
	@Nullable
	public Item.ToolMaterial getToolMaterial() {
		if (isToolMaterial()) {
			return this.getToolMaterial();
		}
		return null;
	}
	
	@Nullable
	public ItemArmor.ArmorMaterial getArmorMaterial() {
		if (isArmorMaterial()) {
			return this.getArmorMaterial();
		}
		return null;
	}
	
	@Nullable
	public ToolMaterialAdded getToolMaterialAdded() {
		if (isToolMaterial()) {
			return ((ToolMaterialAdded)this);
		}
		return null;
	}
	
	@Nullable
	public ArmorMaterialAdded getArmorMaterialAdded() {
		if (isArmorMaterial()) {
			return ((ArmorMaterialAdded)this);
		}
		return null;
	}
    
    public abstract void setEnchantability(int enchantability);
	
	public abstract void setRepairStack(ItemStack repairStack);
	
	public abstract String getId();
	
	public abstract int getEnchantability();
	
	public abstract ItemStack getRepairStack();
	
	public static class Serializer implements JsonDeserializer<ItemMaterialAdded>, JsonSerializer<ItemMaterialAdded> {

		@Override
        public JsonElement serialize(ItemMaterialAdded materialAdded, Type type, JsonSerializationContext context) {
			boolean isArmor = materialAdded.isArmorMaterial();
            JsonObject json;
            
            if (isArmor) {
            	json = ArmorMaterialAdded.Serializer.serialize((ArmorMaterialAdded)materialAdded, context); 
            } else if (materialAdded.isToolMaterial()) {
            	json = ToolMaterialAdded.Serializer.serialize((ToolMaterialAdded)materialAdded, context);
            } else {
            	throw new IllegalArgumentException("Can only serialize armor and tool materials.");
            }
            
            json.addProperty("type", isArmor ? "armor" : "tool");
            
            return json;
        }
        
		@Override
        public ItemMaterialAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "item_material");
            
            ItemMaterialAdded materialAdded;
            String materialType = JsonUtils.getString(json, "type");
            
            if (materialType.equals("armor")) {
            	materialAdded = ArmorMaterialAdded.Serializer.deserialize(json, nextMaterialId, context);
            } else {
            	materialAdded = ToolMaterialAdded.Serializer.deserialize(json, nextMaterialId, context);
            }
	    	//Kinda hacky... TODO: Find a better way to do this. If at all possible.
            nextMaterialId = "";

            return materialAdded;
        }
		
		public static void postDeserialize(JsonElement jsonElement, ItemMaterialAdded materialAdded) {
			JsonObject json = JsonUtils.getJsonObject(jsonElement, "item_material");
			
			if (materialAdded.isArmorMaterial()) {
				ArmorMaterialAdded.Serializer.postDeserialize(json, materialAdded.getArmorMaterialAdded());
			} else if (materialAdded.isToolMaterial()) {
				ToolMaterialAdded.Serializer.postDeserialize(json, materialAdded.getToolMaterialAdded());
			}
		}
    }
}
