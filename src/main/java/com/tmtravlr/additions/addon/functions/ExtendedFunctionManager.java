package com.tmtravlr.additions.addon.functions;

import com.google.common.base.Functions;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeFunction;
import com.tmtravlr.additions.util.ProblemNotifier;
import mcp.MethodsReturnNonnullByDefault;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Replaces the vanilla function manager, so it can load
 * and reload addon functions with the vanilla ones
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2018
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
		
			ProblemNotifier.onReload(server);
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
					@MethodsReturnNonnullByDefault
					ICommandSender sender = new ICommandSender() {
				        
				        @Override
						public String getName() {
				            return addon.name;
				        }
				        
				        @Override
						public boolean canUseCommand(int permLevel, @Nonnull String commandName) {
				            return permLevel <= 2;
				        }
				        
				        @Override
						public World getEntityWorld() {
				            return ExtendedFunctionManager.this.server.worlds[0];
				        }
				        
				        @Override
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
