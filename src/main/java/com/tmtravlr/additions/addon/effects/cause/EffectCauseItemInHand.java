package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for killing an entity with an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectCauseItemInHand extends EffectCauseItem {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_in_hand");
	
	public HandType handType = HandType.BOTH;
	
	public boolean applies(EntityLivingBase entity) {
		return !this.getRelevantStack(entity).isEmpty();
	}
	
	public ItemStack getRelevantStack(EntityLivingBase entity) {
		if ((this.handType == HandType.BOTH || this.handType == HandType.MAINHAND) && itemMatches(entity.getHeldItemMainhand())) {
			return entity.getHeldItemMainhand();
		} else if ((this.handType == HandType.BOTH || this.handType == HandType.OFFHAND) && itemMatches(entity.getHeldItemOffhand())) {
			return entity.getHeldItemOffhand();
		}
		
		return ItemStack.EMPTY;
	}
	
	public boolean applies(EnumHand hand, ItemStack stack) {
		return this.itemMatches(stack) && this.handType == HandType.BOTH || hand == EnumHand.MAIN_HAND && this.handType == HandType.MAINHAND || hand == EnumHand.OFF_HAND && this.handType == HandType.OFFHAND;
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemInHand> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemInHand.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemInHand effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.handType != HandType.BOTH) {
				json.addProperty("hand", effectCause.handType.name());
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemInHand deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemInHand effectCause = new EffectCauseItemInHand();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand", HandType.BOTH.name()));
			
			return effectCause;
		}
    }
}
