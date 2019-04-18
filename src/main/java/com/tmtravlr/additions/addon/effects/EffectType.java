package com.tmtravlr.additions.addon.effects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import com.tmtravlr.additions.AdditionsMod;

/**
 * Types of effects. Set up so more can be added easily.
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class EffectType extends IForgeRegistryEntry.Impl<EffectType> {
	public static final IForgeRegistry<EffectType> REGISTRY = new RegistryBuilder()
		.setName(new ResourceLocation(AdditionsMod.MOD_ID, "effectType"))
		.setType(EffectType.class)
		.create();

	public static final EffectType POTION = new EffectType(EffectInfoPotion.class).setRegistryName(AdditionsMod.MOD_ID, "potion");
	
	static {
		REGISTRY.register(POTION);
	}
	
	public static EffectType getEffectTypeForClass(Class<?> classToFind) {
		for(EffectType effectType : REGISTRY.getValues()) {
			if(effectType.effectClass == classToFind) {
				return effectType;
			}
		}
		
		return null;
	}
	
	public Class<? extends EffectInfo> effectClass;
	
	public EffectType(Class<? extends EffectInfo> classToSet) {
		this.effectClass = classToSet;
	}
	
	
	
}
