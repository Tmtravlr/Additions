package com.tmtravlr.additions.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * These represent addition 'types' (like items or creative tabs).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class AdditionTypeManager {
	
	private static final Map<ResourceLocation, AdditionType> ADDITION_TYPES = new HashMap<>();
	private static final List<IAdditionTypeGuiFactory> ADDITION_TYPE_GUI_FACTORIES = new ArrayList<>();
	
	public static void registerDefaultAdditionTypes() {
		registerAdditionType(AdditionTypeItem.NAME, AdditionTypeItem.INSTANCE);
		registerAdditionType(AdditionTypeCreativeTab.NAME, AdditionTypeCreativeTab.INSTANCE);
	}
	
	public static void registerAdditionType(ResourceLocation name, AdditionType toRegister) {
		ADDITION_TYPES.put(name, toRegister);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerAdditionTypeGuiFactory(IAdditionTypeGuiFactory guiFactory) {
		ADDITION_TYPE_GUI_FACTORIES.add(guiFactory);
	}
	
	public static AdditionType getAdditionType(ResourceLocation key) {
		return ADDITION_TYPES.get(key);
	}
	
	@SideOnly(Side.CLIENT)
	public static Collection<IAdditionTypeGuiFactory> getAdditionTypeGuiFactories() {
		return ADDITION_TYPE_GUI_FACTORIES;
	}
	
	public static Collection<AdditionType> getAllAdditionTypes() {
		return ADDITION_TYPES.values();
	}
	
	public static Collection<ResourceLocation> getAllAdditionTypeNames() {
		return ADDITION_TYPES.keySet();
	}
	
	public static void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
		for (AdditionType type : getAllAdditionTypes()) {
			type.loadPreInit(addons, event);
		}
	}
	
	public static void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		for (AdditionType type : getAllAdditionTypes()) {
			type.loadInit(addons, event);
		}
	}
	
	public static void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event) {
		for (AdditionType type : getAllAdditionTypes()) {
			type.loadPostInit(addons, event);
		}
	}
	
	public static void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event) {
		for (AdditionType type : getAllAdditionTypes()) {
			type.loadServerStarting(addons, event);
		}
	}
	
	public static void setupNewAddon(Addon addon) {
		for (AdditionType type : getAllAdditionTypes()) {
			type.setupNewAddon(addon);
		}
	}
}
