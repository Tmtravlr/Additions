package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Cause for holding an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectCauseItemInHand extends EffectCause {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_in_hand");
	
	public ItemStack itemStack;
	public HandType handType = HandType.BOTH;
	
	public boolean applies(EntityLivingBase entity) {
		if ((handType == HandType.BOTH || handType == HandType.MAINHAND) && itemMatches(entity.getHeldItemMainhand())) {
			return true;
		} else if ((handType == HandType.BOTH || handType == HandType.OFFHAND) && itemMatches(entity.getHeldItemOffhand())) {
			return true;
		}
		return false;
	}
	
	private boolean itemMatches(ItemStack stackToCheck) {
		if (OreDictionary.itemMatches(stackToCheck, itemStack, false) && NBTUtil.areNBTEquals(itemStack.getTagCompound(), stackToCheck.getTagCompound(), true)) {
			return true;
		}
		return false;
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemInHand> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemInHand.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemInHand effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			json.addProperty("hand", effectCause.handType.name());
			
			return json;
		}
		
		@Override
		public EffectCauseItemInHand deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemInHand effectCause = new EffectCauseItemInHand();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand"));
			
			return effectCause;
		}
    }
	
	public static enum HandType {
		MAINHAND,
		OFFHAND,
		BOTH
	}
}
