package com.tmtravlr.additions.addon.functions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeFunction;
import com.tmtravlr.additions.type.AdditionTypeLootTable;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Replaces the vanilla function manager, so it can load
 * and reload addon functions with the vanilla ones
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2018 
 */
public class ExtendedFunctionManager extends FunctionManager {

	private final MinecraftServer server;
	private final Map<FunctionObject, ICommandSender> addonLoopFunctions = new HashMap<>();

	public ExtendedFunctionManager(File functionDirectory, MinecraftServer server) {
		super(functionDirectory, server);
		this.server = server;
		this.reload();
	}
	
	@Override
	public void reload() {
		if (this.server != null) {
			super.reload();
			this.reloadAddonFunctions();
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		for (FunctionObject function : this.addonLoopFunctions.keySet()) {			
			this.execute(function, this.addonLoopFunctions.get(function));
		}
	}
	
	private void reloadAddonFunctions() {
		AdditionTypeFunction.INSTANCE.reloadAllFunctions(AddonLoader.addonsLoaded);
		
		Map<ResourceLocation, FunctionAdded> functionsToLoad = new HashMap<>();
		
		for (Addon addon : AddonLoader.addonsLoaded) {
			functionsToLoad.putAll(AdditionTypeFunction.INSTANCE.getAllAdditions(addon).stream().collect(Collectors.toMap(function -> function.id, Functions.identity())));
		}
		
		for (ResourceLocation location : functionsToLoad.keySet()) {
			// Assume functions already in the list should not be overwritten (they were loaded from the world)
			if (!this.getFunctions().containsKey(location)) {
				this.getFunctions().put(location, FunctionObject.create(this, functionsToLoad.get(location).commands));
			}
		}
		
		this.reloadAddonLoopFunctions();
	}
	
	public void reloadAddonLoopFunctions() {
		this.addonLoopFunctions.clear();
		
		for (Addon addon : AddonLoader.addonsLoaded) {
			if (addon.loopFunction != null) {
				FunctionObject function = this.getFunction(addon.loopFunction);
				
				if (function != null) {
					ICommandSender sender = new ICommandSender() {
				        
				        public String getName() {
				            return addon.name;
				        }
				        
				        public boolean canUseCommand(int permLevel, String commandName) {
				            return permLevel <= 2;
				        }
				        
				        public World getEntityWorld() {
				            return ExtendedFunctionManager.this.server.worlds[0];
				        }
				        
				        public MinecraftServer getServer() {
				            return ExtendedFunctionManager.this.server;
				        }
				    };
				    
				    this.addonLoopFunctions.put(function, sender);
				}
			}
		}
	}
}
