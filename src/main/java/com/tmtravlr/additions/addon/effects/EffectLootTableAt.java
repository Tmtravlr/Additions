package com.tmtravlr.additions.addon.effects;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Info about a potion effect to apply.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectLootTableAt extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "loot_table_at");

	public ResourceLocation lootTable;
	public Integer lootSeed;
	public float chance = 1;

	@Override
	public void affectEntity(@Nullable Entity cause, Entity entity) {
		if (entity.world instanceof WorldServer && entity.world.rand.nextFloat() <= this.chance) {
			LootContextExtendedBuilder builder = new LootContextExtendedBuilder((WorldServer) entity.world);
			builder.withLootedEntity(entity);
			
			if (cause != null) {
				builder.withLooter(cause);
				
				if (cause instanceof EntityPlayer) {
					builder.withLuck(((EntityPlayer)cause).getLuck());
					builder.withPlayer((EntityPlayer)cause);
				}
			}
			
			Random random = lootSeed != null ? new Random(lootSeed) : entity.world.rand;
			
			this.spawnItems(entity.world.getLootTableManager().getLootTableFromLocation(this.lootTable).generateLootForPools(random, builder.build()), entity.world, entity.getPosition());
		}
	}

	@Override
	public void affectBlock(@Nullable Entity cause, World world, BlockPos pos) {
		if (world instanceof WorldServer && world.rand.nextFloat() <= this.chance) {
			LootContextExtendedBuilder builder = new LootContextExtendedBuilder((WorldServer) world);
			builder.withPosition(pos);
			
			if (cause != null) {
				builder.withLooter(cause);
				
				if (cause instanceof EntityPlayer) {
					builder.withLuck(((EntityPlayer)cause).getLuck());
					builder.withPlayer((EntityPlayer)cause);
				}
			}
			
			Random random = lootSeed != null ? new Random(lootSeed) : world.rand;
			
			this.spawnItems(world.getLootTableManager().getLootTableFromLocation(this.lootTable).generateLootForPools(random, builder.build()), world, pos);
		}
	}
	
	private void spawnItems(List<ItemStack> stacks, World world, BlockPos pos) {
		for (ItemStack stack : stacks) {
			if (stack != null) {
			    double offsetX = world.rand.nextFloat() * 0.5F + 0.25D;
			    double offsetY = world.rand.nextFloat() * 0.5F + 0.25D;
			    double offsetZ = world.rand.nextFloat() * 0.5F + 0.25D;
			    EntityItem entityitem = new EntityItem(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, stack);
			    entityitem.setDefaultPickupDelay();
			    world.spawnEntity(entityitem);
			}
		}
	}

	public static class Serializer extends Effect.Serializer<EffectLootTableAt> {
		
		public Serializer() {
			super(TYPE, EffectLootTableAt.class);
		}
		
		@Override
		public JsonObject serialize(EffectLootTableAt effect, JsonSerializationContext context) {
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
		public EffectLootTableAt deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectLootTableAt effect = new EffectLootTableAt();
			
			if (json.has("loot_table_seed")) {
				effect.lootSeed = JsonUtils.getInt(json, "loot_table_seed");
			}
			
			effect.lootTable = new ResourceLocation(JsonUtils.getString(json, "loot_table"));
			effect.chance = JsonUtils.getFloat(json, "chance", 1);
			
			return effect;
		}
    }
}
