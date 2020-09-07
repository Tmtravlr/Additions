package com.tmtravlr.additions.addon.functions;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Loads and manages addon functions
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2018 
 */
public class AddonFunctionManager {
	
	private static ExtendedFunctionManager extendedFunctionManager;
	
	public static void replaceFunctionManager(@Nonnull MinecraftServer server) {
		File functionDir = ObfuscationReflectionHelper.getPrivateValue(FunctionManager.class, server.getFunctionManager(), "field_193068_b", "functionDir");
		extendedFunctionManager = new ExtendedFunctionManager(functionDir, server);
		ObfuscationReflectionHelper.setPrivateValue(World.class, server.getWorld(0), extendedFunctionManager, "field_193036_D", "functionManager");
	}
	
	public static void deleteFunctionManager() {
		extendedFunctionManager = null;
	}
}
