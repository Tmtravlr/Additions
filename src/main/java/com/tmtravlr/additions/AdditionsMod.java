package com.tmtravlr.additions;

import org.apache.logging.log4j.Logger;

import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.advancements.AddonAdvancementManager;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.mapcolors.BlockMapColorManager;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;
import com.tmtravlr.additions.addon.functions.AddonFunctionManager;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.loottables.AddonLootTableManager;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.addon.structures.AddonStructureManager;
import com.tmtravlr.additions.commands.CommandAdditionsCondition;
import com.tmtravlr.additions.commands.CommandAdditionsDamage;
import com.tmtravlr.additions.commands.CommandAdditionsDamageItem;
import com.tmtravlr.additions.commands.CommandAdditionsDismount;
import com.tmtravlr.additions.commands.CommandAdditionsEjectPassengers;
import com.tmtravlr.additions.commands.CommandAdditionsEntityData;
import com.tmtravlr.additions.commands.CommandAdditionsExplode;
import com.tmtravlr.additions.commands.CommandAdditionsGrow;
import com.tmtravlr.additions.commands.CommandAdditionsLoot;
import com.tmtravlr.additions.commands.CommandAdditionsMount;
import com.tmtravlr.additions.commands.CommandAdditionsMove;
import com.tmtravlr.additions.commands.CommandAdditionsStructure;
import com.tmtravlr.additions.commands.CommandAdditionsThrow;
import com.tmtravlr.additions.network.CToSMessage;
import com.tmtravlr.additions.network.PacketHandlerClient;
import com.tmtravlr.additions.network.PacketHandlerServer;
import com.tmtravlr.additions.network.SToCMessage;
import com.tmtravlr.additions.type.AdditionTypeManager;
import com.tmtravlr.additions.type.attribute.AttributeTypeManager;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Additions allows non-coders to add items, blocks, and many other things to the game through GUIs.
 * 
 * Main mod class.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
@Mod(modid = AdditionsMod.MOD_ID, 
     name = AdditionsMod.MOD_NAME, 
     version = AdditionsMod.VERSION, 
	 guiFactory = "com.tmtravlr.additions.gui.AdditionsGuiFactory", 
	 dependencies = "after:*;required:lootoverhaul@[1.2,)"
)
public class AdditionsMod {
	
    public static final String MOD_ID = "additions";
    public static final String MOD_NAME = "Additions";
    public static final String VERSION = "@VERSION@";
    
    @Instance(MOD_ID)
	public static AdditionsMod instance;
	
	@SidedProxy(clientSide = "com.tmtravlr.additions.ClientProxy", serverSide = "com.tmtravlr.additions.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	public static SimpleNetworkWrapper networkWrapper;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
		
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		networkWrapper.registerMessage(PacketHandlerServer.class, CToSMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(PacketHandlerClient.class, SToCMessage.class, 1, Side.CLIENT);
		
        ProblemNotifier.initializeProblemFolder(event);
        ConfigLoader.loadConfigFile(event);
        if (event.getSide() == Side.CLIENT) {
        	ClientConfigLoader.loadInternalConfigFile(event);
        }
        
        BlockMaterialManager.registerDefaultBlockMaterials();
        BlockMapColorManager.registerDefaultBlockMapColors();
        BlockAddedManager.registerDefaultBlocks();
        ItemAddedManager.registerDefaultItems();
        RecipeAddedManager.registerDefaultRecipes();
        LootTablePresetManager.registerDefaultLootTablePresets();
        EffectManager.registerDefaultEffects();
        EffectCauseManager.registerDefaultEffectCauses();
        AdditionTypeManager.registerDefaultAdditionTypes();
        AttributeTypeManager.initVanillaAttributes();
        SoundEventLoader.registerSoundEvents();
        proxy.registerGuiFactories();
        
        AddonLoader.loadAddons();
        
        proxy.refreshResources();
        
        AddonLoader.loadAdditionsPreInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "projectile"), EntityAddedProjectile.class, MOD_ID + "_projectile", 0, this, 80, 5, true);
    	proxy.registerEntityRenderers();
        
        CraftingHelper.register(IngredientOreNBT.TYPE, (IIngredientFactory) (context, json) -> IngredientOreNBT.Serializer.deserialize(json));
    	
    	AddonLoader.loadAdditionsInit(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	AddonLoader.loadAdditionsPostInit(event);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	AddonLoader.loadAdditionsServerStarting(event);
    	
    	event.registerServerCommand(new CommandAdditionsDamage());
    	event.registerServerCommand(new CommandAdditionsDamageItem());
    	event.registerServerCommand(new CommandAdditionsThrow());
    	event.registerServerCommand(new CommandAdditionsMove());
    	event.registerServerCommand(new CommandAdditionsExplode());
    	event.registerServerCommand(new CommandAdditionsMount());
    	event.registerServerCommand(new CommandAdditionsDismount());
    	event.registerServerCommand(new CommandAdditionsEjectPassengers());
    	event.registerServerCommand(new CommandAdditionsLoot());
    	event.registerServerCommand(new CommandAdditionsStructure());
    	event.registerServerCommand(new CommandAdditionsEntityData());
    	event.registerServerCommand(new CommandAdditionsCondition());
    	event.registerServerCommand(new CommandAdditionsGrow());
    	
    	if (ConfigLoader.replaceManagers.getBoolean(true)) {
    		AddonFunctionManager.replaceFunctionManager(event.getServer());
    		AddonAdvancementManager.replaceAdvancementManager(event.getServer());
    		AddonStructureManager.replaceStructureManager(event.getServer());
    		AddonLootTableManager.replaceLootTableManager(event.getServer());
    	}
    }
    
    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	if (ConfigLoader.replaceManagers.getBoolean(true)) {
    		AddonFunctionManager.deleteFunctionManager();
    		AddonAdvancementManager.deleteAdvancementManager();
    		AddonStructureManager.deleteStructureManager();
    		AddonLootTableManager.deleteLootTableManger();
    	}
    }
}
