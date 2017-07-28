package com.tmtravlr.additions;

import org.apache.logging.log4j.Logger;

import com.tmtravlr.additions.addon.AddonLoader;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = AdditionsMod.MOD_ID, name = AdditionsMod.MOD_NAME, version = AdditionsMod.VERSION, 
	guiFactory = "com.tmtravlr.additions.gui.AdditionsGuiFactory", dependencies = "after:*")
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
        
        AddonLoader.loadAddons();
        proxy.refreshResources();
        
        AddonLoader.loadAddonItems();
        
        AddonLoader.loadAddonCreativeTabs();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        AddonLoader.loadAddonItemModels();
    }
}
