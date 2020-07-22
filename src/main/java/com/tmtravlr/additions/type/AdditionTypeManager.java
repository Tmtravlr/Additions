package com.tmtravlr.additions.type;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.api.gui.IAdditionTypeGuiFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * These represent addition 'types' (like items or creative tabs).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class AdditionTypeManager {

	private static final Map<ResourceLocation, AdditionType> ADDITION_TYPES = new HashMap<>();
	private static final List<AdditionType> ADDITION_TYPES_ORDERED = new ArrayList<>();
	private static final List<IAdditionTypeGuiFactory> ADDITION_TYPE_GUI_FACTORIES = new ArrayList<>();
	
	public static void registerDefaultAdditionTypes() {
		registerAdditionType(AdditionTypeSoundEvent.NAME, AdditionTypeSoundEvent.INSTANCE);
		registerAdditionType(AdditionTypeBlock.NAME, AdditionTypeBlock.INSTANCE);
		registerAdditionType(AdditionTypeItem.NAME, AdditionTypeItem.INSTANCE);
		registerAdditionType(AdditionTypeItemMaterial.NAME, AdditionTypeItemMaterial.INSTANCE);
		registerAdditionType(AdditionTypeCreativeTab.NAME, AdditionTypeCreativeTab.INSTANCE);
		registerAdditionType(AdditionTypeLootTable.NAME, AdditionTypeLootTable.INSTANCE);
		registerAdditionType(AdditionTypeRecipe.NAME, AdditionTypeRecipe.INSTANCE);
		registerAdditionType(AdditionTypeStructure.NAME, AdditionTypeStructure.INSTANCE);
		registerAdditionType(AdditionTypeFunction.NAME, AdditionTypeFunction.INSTANCE);
		registerAdditionType(AdditionTypeAdvancement.NAME, AdditionTypeAdvancement.INSTANCE);
		registerAdditionType(AdditionTypeEffect.NAME, AdditionTypeEffect.INSTANCE);
		registerAdditionType(AdditionTypePotionType.NAME, AdditionTypePotionType.INSTANCE);
	}
	
	public static void registerAdditionType(ResourceLocation name, AdditionType toRegister) {
		ADDITION_TYPES.put(name, toRegister);
		ADDITION_TYPES_ORDERED.add(toRegister);
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
		ADDITION_TYPES_ORDERED.forEach(type -> type.loadPreInit(addons, event));
	}
	
	public static void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		ADDITION_TYPES_ORDERED.forEach(type -> type.loadInit(addons, event));
	}
	
	public static void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event) {
		ADDITION_TYPES_ORDERED.forEach(type -> type.loadPostInit(addons, event));
	}
	
	public static void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event) {
		ADDITION_TYPES_ORDERED.forEach(type -> type.loadServerStarting(addons, event));
	}
	
	public static void setupNewAddon(Addon addon) {
		ADDITION_TYPES_ORDERED.forEach(type -> type.setupNewAddon(addon));
	}
}
