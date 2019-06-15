package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for right clicking an item on a block.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemRightClickBlock extends EffectCauseItemInHand {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_right_click_block");

	public boolean targetSelf;
	public boolean againstBlock;
	public boolean exceptReplaceable;

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemRightClickBlock> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemRightClickBlock.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemRightClickBlock effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.handType != HandType.BOTH) {
				json.addProperty("hand", effectCause.handType.name());
			}
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			if (effectCause.againstBlock) {
				json.addProperty("against_block", true);
			}
			
			if (effectCause.exceptReplaceable) {
				json.addProperty("except_replaceable", true);
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemRightClickBlock deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemRightClickBlock effectCause = new EffectCauseItemRightClickBlock();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.handType = HandType.valueOf(JsonUtils.getString(json, "hand", HandType.BOTH.name()));
			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
			effectCause.againstBlock = JsonUtils.getBoolean(json, "against_block", false);
			effectCause.exceptReplaceable = JsonUtils.getBoolean(json, "except_replaceable", false);
			
			return effectCause;
		}
	}
}
