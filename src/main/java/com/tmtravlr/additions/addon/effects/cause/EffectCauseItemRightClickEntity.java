package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for right clicking an item on an entity.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemRightClickEntity extends EffectCauseItemInHand {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_right_click_entity");
	
	public boolean targetSelf;

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemRightClickEntity> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemRightClickEntity.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemRightClickEntity effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.handType != HandType.BOTH) {
				json.addProperty("hand", effectCause.handType.name());
			}
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemRightClickEntity deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemRightClickEntity effectCause = new EffectCauseItemRightClickEntity();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand", HandType.BOTH.name()));
			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
			
			return effectCause;
		}
    }
}
