package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added effects
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019 
 */
public class AdditionTypeEffect extends AdditionType<EffectList> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "effect");
	public static final String FOLDER_NAME = "data" + File.separator + "effects";
	public static final String FILE_POSTFIX = ".json";
	public static final AdditionTypeEffect INSTANCE = new AdditionTypeEffect();
	
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(EffectList.class, new EffectList.Serializer())
			.registerTypeHierarchyAdapter(Effect.class, new EffectManager.Serializer())
			.registerTypeHierarchyAdapter(EffectCause.class, new EffectCauseManager.Serializer())
			.setPrettyPrinting()
			.create();
	
	private Multimap<Addon, EffectList> loadedEffects = HashMultimap.create();
	
	private Map<EffectCauseItemInHand, List<Effect>> inHandEffects = new HashMap<>();
	
	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon effects.");
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading effect files for addon " + addon.id + ". The effects will not load.", e);
			}
			
			for (String filePath : filePaths) {
				try {
					EffectList effectList = GSON.fromJson(AddonLoader.readAddonFile(addon.addonFolder, filePath), EffectList.class);
					
					String effectListName = filePath;
					if (effectListName.contains(File.separator)) {
						effectListName = effectListName.substring(effectListName.lastIndexOf(File.separator) + 1);
					}
					
					if (effectListName.endsWith(FILE_POSTFIX)) {
						effectListName = effectListName.substring(0, effectListName.length() - FILE_POSTFIX.length());
					
						ResourceLocation effectListId = new ResourceLocation(AdditionsMod.MOD_ID, effectListName);
						effectList.id = effectListId;
						
						this.loadedEffects.put(addon, effectList);
					}
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading effect " + filePath + " for addon " + addon.id + ". The effect will not load.", e);
				}
			}
		}
		
		this.refreshCachedEffects();
	}
	
	@Override
	public List<EffectList> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedEffects.get(addon));
	}
	
	public List<EffectList> getAllAdditions() {
		return new ArrayList<>(this.loadedEffects.values());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, EffectList addition) {
		if (!this.loadedEffects.containsEntry(addon, addition)) {
			this.loadedEffects.put(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		if (!additionFolder.isDirectory()) {
			additionFolder.mkdir();
		}
		
		File additionFile = new File(additionFolder, addition.id.getResourcePath() + FILE_POSTFIX);
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
		
		this.refreshCachedEffects();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, EffectList addition) {
		if (this.loadedEffects.containsEntry(addon, addition)) {
			this.loadedEffects.remove(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, addition.id.getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
		
		this.refreshCachedEffects();
	}
	
	public boolean hasEffectWithId(Addon addon, ResourceLocation id) {
		return loadedEffects.get(addon).stream().anyMatch(EffectList -> EffectList.id.equals(id));
	}
	
	public Map<EffectCauseItemInHand, List<Effect>> getInHandEffects() {
		return this.inHandEffects;
	}
	
	private void refreshCachedEffects() {
		this.inHandEffects.clear();
		
		for (EffectList addition : this.loadedEffects.values()) {
			if (addition.cause instanceof EffectCauseItemInHand) {
				this.inHandEffects.put(((EffectCauseItemInHand)addition.cause), addition.effects);
			}
		}
	}
}
