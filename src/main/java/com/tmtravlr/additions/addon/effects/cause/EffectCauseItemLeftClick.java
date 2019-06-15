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
 * Cause for left clicking an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCauseItemLeftClick extends EffectCauseItem {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_left_click");
	
	public boolean applies(ItemStack stack) {
		return this.itemMatches(stack);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemLeftClick> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemLeftClick.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemLeftClick effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("item", OtherSerializers.ItemStackSerializer.serialize(effectCause.itemStack));
			
			return json;
		}
		
		@Override
		public EffectCauseItemLeftClick deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemLeftClick effectCause = new EffectCauseItemLeftClick();
			
			effectCause.itemStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "item"));
			
			return effectCause;
		}
    }
}
