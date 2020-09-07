package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for using an item (like drawing a bow).
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemUsing extends EffectCauseItemInHand {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_using");
	
	public int timeRequired = 0;
		
	@Override
	public boolean applies(EntityLivingBase entity) {
		if (entity.getItemInUseMaxCount()  >= timeRequired) {
			if (entity.getActiveHand() == EnumHand.MAIN_HAND && (this.handType == HandType.BOTH || this.handType == HandType.MAINHAND) && itemMatches(entity.getHeldItemMainhand())) {
				return true;
			} else return entity.getActiveHand() == EnumHand.OFF_HAND && (this.handType == HandType.BOTH || this.handType == HandType.OFFHAND) && itemMatches(entity.getHeldItemOffhand());
		}
		return false;
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemUsing> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemUsing.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemUsing effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.handType != HandType.BOTH) {
				json.addProperty("hand", effectCause.handType.name());
			}
			
			if (effectCause.timeRequired > 0) {
				json.addProperty("time_required", effectCause.timeRequired);
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemUsing deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemUsing effectCause = new EffectCauseItemUsing();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand", HandType.BOTH.name()));
			effectCause.timeRequired = JsonUtils.getInt(json, "time_required", 0);
			
			return effectCause;
		}
    }
}
