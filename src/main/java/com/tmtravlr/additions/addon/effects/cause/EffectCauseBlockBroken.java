package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.BlockStateInfo;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for breaking a block.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class EffectCauseBlockBroken extends EffectCauseBlock {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_broken");
	
	public boolean targetSelf = false;
	
	public boolean applies(IBlockState blockState, NBTTagCompound blockTag) {
		return this.blockMatches(blockState, blockTag);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseBlockBroken> {
		
		public Serializer() {
			super(TYPE, EffectCauseBlockBroken.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseBlockBroken effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("block_state", BlockStateInfo.Serializer.serialize(effectCause.blockState));
			
			if (effectCause.blockTag != null && !effectCause.blockTag.hasNoTags()) {
				json.addProperty("block_tag", effectCause.blockTag.toString());
			}
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			return json;
		}
		
		@Override
		public EffectCauseBlockBroken deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseBlockBroken effectCause = new EffectCauseBlockBroken();
			
			effectCause.blockState = BlockStateInfo.Serializer.deserialize(JsonUtils.getJsonObject(json, "block_state"));
			
			if (JsonUtils.isString(json, "block_tag")) {
				try {
					effectCause.blockTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "block_tag"));
                } catch (NBTException nbtexception) {
                    throw new JsonSyntaxException(nbtexception);
                }
			}

			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
			
			return effectCause;
		}
    }
}
