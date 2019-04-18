package com.tmtravlr.additions.addon.loottables;

import java.io.File;

import javax.annotation.Nonnull;

import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeLootTable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Loads and manages addon loot tables
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class AddonLootTableManager {
	private static ExtendedLootTableManager extendedLootTableManager;
	
	public static void replaceLootTableManager(@Nonnull MinecraftServer server) {
		File lootTableDir = ObfuscationReflectionHelper.getPrivateValue(LootTableManager.class, server.worlds[0].getLootTableManager(), "field_186528_d", "baseFolder");
		extendedLootTableManager = new ExtendedLootTableManager(lootTableDir);
		ObfuscationReflectionHelper.setPrivateValue(World.class, server.worlds[0], extendedLootTableManager, "field_184151_B", "lootTable");
	}
	
	public static void deleteLootTableManger() {
		extendedLootTableManager = null;
	}
}
