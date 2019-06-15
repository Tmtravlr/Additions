package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for right clicking an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemRightClick extends EffectCauseItemInHand {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_right_click");

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemRightClick> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemRightClick.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemRightClick effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.handType != HandType.BOTH) {
				json.addProperty("hand", effectCause.handType.name());
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemRightClick deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemRightClick effectCause = new EffectCauseItemRightClick();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand", HandType.BOTH.name()));
			
			return effectCause;
		}
    }
}
