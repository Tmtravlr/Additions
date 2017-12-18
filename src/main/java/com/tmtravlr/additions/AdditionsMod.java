package com.tmtravlr.additions;

import org.apache.logging.log4j.Logger;

import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
	 dependencies = "after:*"
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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigLoader.loadConfigFile(event);
        
        proxy.registerEventHandlers();
        
        AdditionTypeManager.registerDefaultAdditionTypes();
        proxy.registerGuiFactories();
        
        if (!AddonLoader.loadAddons()) {
        	return;
        }
        
        proxy.refreshResources();
        
        AddonLoader.loadAdditionsPreInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	if (proxy.checkForLoadingException()) {
    		return;
    	}
    	
    	AddonLoader.loadAdditionsInit(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	if (proxy.checkForLoadingException()) {
    		return;
    	}
    	
    	AddonLoader.loadAdditionsPostInit(event);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	if (proxy.checkForLoadingException()) {
    		return;
    	}
    	
    	AddonLoader.loadAdditionsServerStarting(event);
    }
}
