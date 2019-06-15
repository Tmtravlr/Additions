package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for an item in your inventory.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectCauseItemInInventory extends EffectCauseItem {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_in_inventory");
	
	public boolean applies(EntityLivingBase entity) {
		return !this.getRelevantStack(entity).isEmpty();
	}
	
	public ItemStack getRelevantStack(EntityLivingBase entity) {
		NonNullList<ItemStack> itemsToCheck = NonNullList.create();
		
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			itemsToCheck.addAll(player.inventory.armorInventory);
			itemsToCheck.addAll(player.inventory.mainInventory);
			itemsToCheck.addAll(player.inventory.offHandInventory);
		} else {
			entity.getEquipmentAndArmor().forEach(itemsToCheck::add);
		}
		
		for (ItemStack stack : itemsToCheck) {
			if (itemMatches(stack)) {
				return stack;
			}
		}
		
		return ItemStack.EMPTY;
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemInInventory> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemInInventory.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemInInventory effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			return json;
		}
		
		@Override
		public EffectCauseItemInInventory deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemInInventory effectCause = new EffectCauseItemInInventory();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			
			return effectCause;
		}
    }
}
