package com.tmtravlr.additions.addon.structures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.advancements.ExtendedAdvancementManager;
import com.tmtravlr.additions.addon.functions.ExtendedFunctionManager;
import com.tmtravlr.additions.type.AdditionTypeStructure;

import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Loads and manages addon structures
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018
 */
public class AddonStructureManager {
	private static ExtendedStructureManager extendedStructureManager;
	
	public static void replaceStructureManager(@Nonnull MinecraftServer server) {
		String structureDir = ObfuscationReflectionHelper.getPrivateValue(TemplateManager.class, server.worlds[0].getStructureTemplateManager(), "field_186241_b", "baseFolder");
		DataFixer structureFixer = ObfuscationReflectionHelper.getPrivateValue(TemplateManager.class, server.worlds[0].getStructureTemplateManager(), "field_191154_c", "fixer");
		extendedStructureManager = new ExtendedStructureManager(structureDir, structureFixer);
		for (WorldServer world : server.worlds) {
			replaceStructureManager(world);
		}
	}
	
	// Need a separate one here in case there are any worlds not loaded when the server loads (like mystcraft and dimensional doors)
	public static void replaceStructureManager(@Nonnull WorldServer world) {
		if (extendedStructureManager != null && world.getSaveHandler() instanceof SaveHandler && !(world.getSaveHandler().getStructureTemplateManager() instanceof ExtendedStructureManager)) {
			ObfuscationReflectionHelper.setPrivateValue(SaveHandler.class, (SaveHandler)world.getSaveHandler(), extendedStructureManager, "field_186342_h", "structureTemplateManager");
		}
	}
	
	public static void deleteStructureManager() {
		extendedStructureManager = null;
	}
	
	public static void reloadStructures() {
		if (extendedStructureManager != null) {
			extendedStructureManager.reload();
		} else {
			AdditionTypeStructure.INSTANCE.reloadAllStructures(AddonLoader.addonsLoaded);
		}
	}
}
