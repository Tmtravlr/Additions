package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.BlockStateInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for being in contact with a block.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class EffectCauseBlockContact extends EffectCauseBlock {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_contact");
	
	public boolean targetSelf = false;
	public ContactType contactType = ContactType.BOTH;
	
	public boolean applies(IBlockState blockState, NBTTagCompound blockTag) {
		return this.blockMatches(blockState, blockTag);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseBlockContact> {
		
		public Serializer() {
			super(TYPE, EffectCauseBlockContact.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseBlockContact effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.add("block_state", BlockStateInfo.Serializer.serialize(effectCause.blockState));
			
			if (effectCause.blockTag != null && !effectCause.blockTag.hasNoTags()) {
				json.addProperty("block_tag", effectCause.blockTag.toString());
			}
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			if (effectCause.contactType != ContactType.BOTH) {
				json.addProperty("contact_type", effectCause.contactType.toString());
			}
			
			return json;
		}
		
		@Override
		public EffectCauseBlockContact deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseBlockContact effectCause = new EffectCauseBlockContact();
			
			effectCause.blockState = BlockStateInfo.Serializer.deserialize(JsonUtils.getJsonObject(json, "block_state"));
			
			if (JsonUtils.isString(json, "block_tag")) {
				try {
					effectCause.blockTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "block_tag"));
                } catch (NBTException nbtexception) {
                    throw new JsonSyntaxException(nbtexception);
                }
			}

			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
			ContactType contactType = ContactType.BOTH;
			
			if (JsonUtils.hasField(json, "contact_type")) {
				String typeString = JsonUtils.getString(json, "contact_type");
				
				try {
					contactType = ContactType.valueOf(typeString.toUpperCase());
				} catch (IllegalArgumentException e) {
					throw new JsonSyntaxException("Unknown contact type '" + typeString + "'.", e);
				}
			}
			effectCause.contactType = contactType;
			
			return effectCause;
		}
    }
	
	public static enum ContactType {
		ON,
		INSIDE,
		BOTH
	}
}
