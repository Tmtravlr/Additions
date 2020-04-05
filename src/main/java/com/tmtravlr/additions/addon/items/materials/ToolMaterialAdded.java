package com.tmtravlr.additions.addon.items.materials;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Represents an added item material, for tools and armor
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class ToolMaterialAdded extends ItemMaterialAdded {
	
	protected Item.ToolMaterial toolMaterial;
    
    public ToolMaterialAdded(@Nonnull Item.ToolMaterial toolMaterial) {
    	this.toolMaterial = toolMaterial;
    }
    
    @Override
    public void setEnchantability(int enchantability) {
    	if (enchantability != this.getEnchantability()) { 
    		ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, enchantability, "field_78008_j", "enchantability");
    	}
    }
	
    @Override
	public void setRepairStack(ItemStack repairStack) {
		if (repairStack != this.toolMaterial.getRepairItemStack()) {
			if (this.toolMaterial.getRepairItemStack().isEmpty()) {
				this.toolMaterial.setRepairItem(repairStack);
			} else {
				ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, repairStack, "repairMaterial");
			}
		}
	}
    
    public void setHarvestLevel(int harvestLevel) {
    	if (harvestLevel != this.getHarvestLevel()) {
    		ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, harvestLevel, "field_78001_f", "harvestLevel");
    	}
    }
    
    public void setBaseToolDurability(int baseToolDurability) {
    	if (baseToolDurability != this.getBaseToolDurability()) {
    		ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, baseToolDurability, "field_78002_g", "maxUses");
    	}
    }
    
    public void setEfficiency(float efficiency) {
    	if (efficiency != this.getEfficiency()) {
    		ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, efficiency, "field_78010_h", "efficiency", "efficiencyOnProperMaterial");
    	}
    }
    
    public void setBaseAttackDamage(float baseAttackDamage) {
    	if (baseAttackDamage != this.getBaseAttackDamage()) {
    		ObfuscationReflectionHelper.setPrivateValue(Item.ToolMaterial.class, this.toolMaterial, baseAttackDamage, "field_78011_i", "attackDamage", "damageVsEntity");
    	}
    }
    
    @Override
	public String getId() {
    	return this.toolMaterial.name().replace('$', '.').toLowerCase();
    }
    
    @Override
	public Item.ToolMaterial getToolMaterial() {
    	return this.toolMaterial;
    }
	
	@Override
	public int getEnchantability() {
		return this.toolMaterial.getEnchantability();
	}
	
	@Override
	public ItemStack getRepairStack() {
		return this.toolMaterial.getRepairItemStack();
	}
	
	public int getHarvestLevel() {
		return this.toolMaterial.getHarvestLevel();
	}
	
	public int getBaseToolDurability() {
		return this.toolMaterial.getMaxUses();
	}
	
	public float getEfficiency() {
		return toolMaterial.getEfficiency();
	}
	
	public float getBaseAttackDamage() {
		return toolMaterial.getAttackDamage();
	}
	
	public static class Serializer {

        public static JsonObject serialize(ToolMaterialAdded materialAdded, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            
            json.addProperty("enchantability", materialAdded.getEnchantability());
            json.addProperty("harvest_level", materialAdded.getHarvestLevel());
            json.addProperty("base_tool_durability", materialAdded.getBaseToolDurability());
            json.addProperty("efficiency", materialAdded.getEfficiency());
            json.addProperty("base_attack_damage", materialAdded.getBaseAttackDamage());
            
            if (materialAdded.getRepairStack() != ItemStack.EMPTY) {
            	json.addProperty("repair_item", Item.REGISTRY.getNameForObject(materialAdded.getRepairStack().getItem()).toString());
            	json.addProperty("repair_meta", materialAdded.getRepairStack().getMetadata());
            }
            
            return json;
        }
        
        public static ToolMaterialAdded deserialize(JsonObject json, String materialId, JsonDeserializationContext context) throws JsonParseException {
            int enchantability = JsonUtils.getInt(json, "enchantability");
            int harvestLevel = JsonUtils.getInt(json, "harvest_level");
            int baseToolDurability = JsonUtils.getInt(json, "base_tool_durability");
            float efficiency = JsonUtils.getFloat(json, "efficiency");
            float baseAttackDamage = JsonUtils.getFloat(json, "base_attack_damage");
		    
            if (materialId.isEmpty()) {
		    	throw new JsonSyntaxException("Trying to deserialize an item material without having an id.");
		    }
		    
		    String name = (AdditionsMod.MOD_ID + "-" + materialId);
		    
		    Item.ToolMaterial toolMaterial = EnumHelper.addToolMaterial(name.toUpperCase(), harvestLevel, baseToolDurability, efficiency, baseAttackDamage, enchantability);
		    
		    ToolMaterialAdded newMaterial = new ToolMaterialAdded(toolMaterial);

            return newMaterial;
        }
        
        public static void postDeserialize(JsonObject json, ToolMaterialAdded material) {
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
