package com.tmtravlr.additions.addon.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Sword Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class ItemAddedClub extends ItemAddedSimple implements IItemAddedTool {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "club");
	
	public boolean applyVanillaAttributes = true;
	private Item.ToolMaterial material = Item.ToolMaterial.WOOD;
	
	@Override
	public void setToolMaterial(Item.ToolMaterial material) {
		if (material != this.material) {
			this.material = material;
			this.setMaxDamage(material.getMaxUses());
		}
	}
	
	@Override
	public void setApplyVanillaAttributes(boolean applyVanillaAttributes) {
		this.applyVanillaAttributes = applyVanillaAttributes;
	}
	
	@Override
	public Item.ToolMaterial getToolMaterial() {
		return this.material;
	}
	
	@Override
	public boolean shouldApplyVanillaAttributes() {
		return this.applyVanillaAttributes;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modifiersForSlot = HashMultimap.create();
		
		if (this.applyVanillaAttributes && slot == EntityEquipmentSlot.MAINHAND) {
			modifiersForSlot.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.getToolMaterial().getAttackDamage() + 5, 0));
			modifiersForSlot.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.7, 0));
		} 
		
		if (this.attributeModifiers.containsKey(slot)) {
			for (AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (state.getBlockHardness(worldIn, pos) != 0.0f) {
            stack.damageItem(2, entityLiving);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairStack) {
        ItemStack repairMaterial = this.material.getRepairItemStack();
        
        if (!repairMaterial.isEmpty() && OreDictionary.itemMatches(repairMaterial, repairStack, false)) {
        	return true;
        }
        
        return super.getIsRepairable(toRepair, repairStack);
    }
    
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (Items.WOODEN_SWORD.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_SWORD), enchantment)) {
			return true;
		}
		
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedClub> {
		
		public Serializer() {
			super(TYPE, ItemAddedClub.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedClub itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("material", itemAdded.getToolMaterial().toString());
			if (!itemAdded.applyVanillaAttributes) {
				json.addProperty("vanilla_attributes", false);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedClub deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedClub itemAdded = new ItemAddedClub();
			
			itemAdded.applyVanillaAttributes = JsonUtils.getBoolean(json, "vanilla_attributes", true);
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedClub itemAdded) {
			String materialName = JsonUtils.getString(json, "material");
			Item.ToolMaterial toolMaterial = null;
			
			try {
				toolMaterial = Item.ToolMaterial.valueOf(materialName);
			} catch(IllegalArgumentException e) {
				AdditionsMod.logger.warn("Tried to parse invalid tool material " + materialName, e);
				toolMaterial = Item.ToolMaterial.WOOD;
			}
			
			itemAdded.setToolMaterial(toolMaterial);
		}
    }

}
