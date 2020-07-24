package com.tmtravlr.additions.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.cause.*;
import com.tmtravlr.additions.util.GeneralUtils;
import com.tmtravlr.additions.util.ProblemNotifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Added effects
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019 
 */
public class AdditionTypeEffect extends AdditionType<EffectList> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "effect");
	public static final String FOLDER_NAME = "data" + File.separator + "effects";
	public static final String FILE_POSTFIX = JSON_POSTFIX;
	public static final AdditionTypeEffect INSTANCE = new AdditionTypeEffect();
	
	public static final Gson GSON = GeneralUtils.newBuilder()
			.registerTypeHierarchyAdapter(EffectList.class, new EffectList.Serializer())
			.registerTypeHierarchyAdapter(Effect.class, new EffectManager.Serializer())
			.registerTypeHierarchyAdapter(EffectCause.class, new EffectCauseManager.Serializer())
			.create();
	
	private Multimap<Addon, EffectList> loadedEffects = HashMultimap.create();
	
	private Map<EffectCauseItemInHand, List<Effect>> inHandEffects = new HashMap<>();
	private Map<EffectCauseItemInInventory, List<Effect>> inInventoryEffects = new HashMap<>();
	private Map<EffectCauseItemEquipped, List<Effect>> equippedEffects = new HashMap<>();
	private Map<EffectCauseItemRightClick, List<Effect>> itemRightClickEffects = new HashMap<>();
	private Map<EffectCauseItemRightClickBlock, List<Effect>> itemRightClickBlockEffects = new HashMap<>();
	private Map<EffectCauseItemRightClickEntity, List<Effect>> itemRightClickEntityEffects = new HashMap<>();
	private Map<EffectCauseItemUsing, List<Effect>> usingEffects = new HashMap<>();
	private Map<EffectCauseItemLeftClick, List<Effect>> itemLeftClickEffects = new HashMap<>();
	private Map<EffectCauseItemDiggingBlock, List<Effect>> itemDiggingBlockEffects = new HashMap<>();
	private Map<EffectCauseItemBreakBlock, List<Effect>> itemBreakBlockEffects = new HashMap<>();
	private Map<EffectCauseItemAttack, List<Effect>> itemAttackEffects = new HashMap<>();
	private Map<EffectCauseItemKill, List<Effect>> itemKillEffects = new HashMap<>();
	
	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon effects.");
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading effect files for addon " + addon.id + ". The effects will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.effects.title", addon.id), new TextComponentString(e.getMessage()));
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

						effectList.id = new ResourceLocation(AdditionsMod.MOD_ID, effectListName);
						
						this.loadedEffects.put(addon, effectList);
					}
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading effect " + filePath + " for addon " + addon.id + ". The effect will not load.", e);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
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
	
	public Map<EffectCauseItemInInventory, List<Effect>> getInInventoryEffects() {
		return this.inInventoryEffects;
	}
	
	public Map<EffectCauseItemEquipped, List<Effect>> getEquippedEffects() {
		return this.equippedEffects;
	}
	
	public Map<EffectCauseItemRightClick, List<Effect>> getItemRightClickEffects() {
		return this.itemRightClickEffects;
	}
	
	public Map<EffectCauseItemRightClickBlock, List<Effect>> getItemRightClickBlockEffects() {
		return this.itemRightClickBlockEffects;
	}
	
	public Map<EffectCauseItemRightClickEntity, List<Effect>> getItemRightClickEntityEffects() {
		return this.itemRightClickEntityEffects;
	}
	
	public Map<EffectCauseItemUsing, List<Effect>> getUsingEffects() {
		return this.usingEffects;
	}
	
	public Map<EffectCauseItemLeftClick, List<Effect>> getItemLeftClickEffects() {
		return this.itemLeftClickEffects;
	}
	
	public Map<EffectCauseItemDiggingBlock, List<Effect>> getItemDiggingBlockEffects() {
		return this.itemDiggingBlockEffects;
	}
	
	public Map<EffectCauseItemBreakBlock, List<Effect>> getItemBreakBlockEffects() {
		return this.itemBreakBlockEffects;
	}
	
	public Map<EffectCauseItemAttack, List<Effect>> getItemAttackEffects() {
		return this.itemAttackEffects;
	}
	
	public Map<EffectCauseItemKill, List<Effect>> getItemKillEffects() {
		return this.itemKillEffects;
	}
	
	private void refreshCachedEffects() {
		this.inHandEffects.clear();
		this.equippedEffects.clear();
		this.inInventoryEffects.clear();
		
		for (EffectList addition : this.loadedEffects.values()) {
			if (addition.cause instanceof EffectCauseItemInInventory) {
				this.inInventoryEffects.put(((EffectCauseItemInInventory)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemEquipped) {
				this.equippedEffects.put(((EffectCauseItemEquipped)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemRightClick) {
				this.itemRightClickEffects.put(((EffectCauseItemRightClick)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemRightClickBlock) {
				this.itemRightClickBlockEffects.put(((EffectCauseItemRightClickBlock)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemRightClickEntity) {
				this.itemRightClickEntityEffects.put(((EffectCauseItemRightClickEntity)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemUsing) {
				this.usingEffects.put(((EffectCauseItemUsing)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemLeftClick) {
				this.itemLeftClickEffects.put(((EffectCauseItemLeftClick)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemDiggingBlock) {
				this.itemDiggingBlockEffects.put(((EffectCauseItemDiggingBlock)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemBreakBlock) {
				this.itemBreakBlockEffects.put(((EffectCauseItemBreakBlock)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemAttack) {
				this.itemAttackEffects.put(((EffectCauseItemAttack)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemKill) {
				this.itemKillEffects.put(((EffectCauseItemKill)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseItemInHand) {
				this.inHandEffects.put(((EffectCauseItemInHand)addition.cause), addition.effects);
			}
		}
	}
}
