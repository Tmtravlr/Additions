package com.tmtravlr.additions.addon.effects;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.ILootContainer;

/**
 * Info about a loot table to generate inside something.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectLootTableInside extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "loot_table_inside");

	public ResourceLocation lootTable;
	public Integer lootSeed;
	public float chance = 1;

	@Override
	public void applyEffect(@Nullable Entity cause, Entity entity) {
		if (!entity.world.isRemote && entity.world.rand.nextFloat() <= this.chance && entity instanceof ILootContainer) {
			try {
				NBTTagCompound entityTag = entity.writeToNBT(new NBTTagCompound());
				entityTag.setString("LootTable", this.lootTable.toString());
				
				if (this.lootSeed != null) {
					entityTag.setLong("LootTableSeed", this.lootSeed);
				}
				
				entity.readFromNBT(entityTag);
			} catch (Exception e) {
				AdditionsMod.logger.warn("Unable to generate loot in entity", e);
			}
		}
	}

	@Override
	public void applyEffect(@Nullable Entity cause, World world, BlockPos pos) {
		if (!world.isRemote && world.rand.nextFloat() <= this.chance) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof ILootContainer) {
				try {
					NBTTagCompound entityTag = tileEntity.writeToNBT(new NBTTagCompound());
					entityTag.setString("LootTable", this.lootTable.toString());
					
					if (this.lootSeed != null) {
						entityTag.setLong("LootTableSeed", this.lootSeed);
					}
					
					tileEntity.readFromNBT(entityTag);
				} catch (Exception e) {
					AdditionsMod.logger.warn("Unable to generate loot in tile entity", e);
				}
			}
		}
	}

	public static class Serializer extends Effect.Serializer<EffectLootTableInside> {
		
		public Serializer() {
			super(TYPE, EffectLootTableInside.class);
		}
		
		@Override
		public JsonObject serialize(EffectLootTableInside effect, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (effect.lootTable == null) {
				throw new IllegalArgumentException("Can't save loot table effect without a loot table");
			}
			
			json.addProperty("loot_table", effect.lootTable.toString());
			
			if (effect.lootSeed != null) {
				json.addProperty("loot_table_seed", effect.lootSeed);
			}
			
			if (effect.chance < 1) {
				json.addProperty("chance", effect.chance);
			}
			
			return json;
		}
		
		@Override
		public EffectLootTableInside deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectLootTableInside effect = new EffectLootTableInside();
			
			if (json.has("loot_table_seed")) {
				effect.lootSeed = JsonUtils.getInt(json, "loot_table_seed");
			}
			
			effect.lootTable = new ResourceLocation(JsonUtils.getString(json, "loot_table"));
			effect.chance = JsonUtils.getFloat(json, "chance", 1);
			
			return effect;
		}
    }
}
