package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for digging a block with an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemDiggingBlock extends EffectCauseItem {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_digging_block");
	
	public boolean targetSelf = false;
	
	public boolean applies(ItemStack stack) {
		return this.itemMatches(stack);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemDiggingBlock> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemDiggingBlock.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemDiggingBlock effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			return json;
		}
		
		@Override
		public EffectCauseItemDiggingBlock deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemDiggingBlock effectCause = new EffectCauseItemDiggingBlock();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
			
			return effectCause;
		}
    }
}
