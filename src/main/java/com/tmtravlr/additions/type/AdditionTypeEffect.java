package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockBroken;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockContact;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockDigging;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockPlaced;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRandom;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityAttacked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityDeath;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntitySpawned;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityUpdate;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemAttack;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemBreakBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemDiggingBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemEquipped;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInInventory;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemKill;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemLeftClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickEntity;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemUsing;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
	
	private Map<EffectCauseBlockPlaced, List<Effect>> blockPlacedEffects = new HashMap<>();
	private Map<EffectCauseBlockDigging, List<Effect>> blockDiggingEffects = new HashMap<>();
	private Map<EffectCauseBlockBroken, List<Effect>> blockBrokenEffects = new HashMap<>();
	private Map<EffectCauseBlockRightClicked, List<Effect>> blockRightClickedEffects = new HashMap<>();
	private Map<EffectCauseBlockRandom, List<Effect>> blockRandomEffects = new HashMap<>();
	private Map<EffectCauseBlockContact, List<Effect>> blockContactEffects = new HashMap<>();
	
	private Map<EffectCauseEntitySpawned, List<Effect>> entitySpawnedEffects = new HashMap<>();
	private Map<EffectCauseEntityUpdate, List<Effect>> entityUpdateEffects = new HashMap<>();
	private Map<EffectCauseEntityRightClicked, List<Effect>> entityRightClickedEffects = new HashMap<>();
	private Map<EffectCauseEntityAttacked, List<Effect>> entityAttackedEffects = new HashMap<>();
	private Map<EffectCauseEntityDeath, List<Effect>> entityDeathEffects = new HashMap<>();
	
	private Map<IBlockAdded, Map<EffectCauseBlockRandom, List<Effect>>> blockAddedRandomEffects = new HashMap<>();
	
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
					
						ResourceLocation effectListId = new ResourceLocation(AdditionsMod.MOD_ID, effectListName);
						effectList.id = effectListId;
						
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
	
	//// Item ////
	
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
	
	//// Block ////
	
	public Map<EffectCauseBlockPlaced, List<Effect>> getBlockPlacedEffects() {
		return this.blockPlacedEffects;
	}
	
	public Map<EffectCauseBlockDigging, List<Effect>> getBlockDiggingEffects() {
		return this.blockDiggingEffects;
	}
	
	public Map<EffectCauseBlockBroken, List<Effect>> getBlockBrokenEffects() {
		return this.blockBrokenEffects;
	}
	
	public Map<EffectCauseBlockRightClicked, List<Effect>> getBlockRightClickedEffects() {
		return this.blockRightClickedEffects;
	}
	
	public Map<EffectCauseBlockRandom, List<Effect>> getBlockRandomEffects() {
		return this.blockRandomEffects;
	}
	
	public Map<EffectCauseBlockContact, List<Effect>> getBlockContactEffects() {
		return this.blockContactEffects;
	}
	
	//// Entity ////
	
	public Map<EffectCauseEntitySpawned, List<Effect>> getEntitySpawnedEffects() {
		return this.entitySpawnedEffects;
	}
	
	public Map<EffectCauseEntityUpdate, List<Effect>> getEntityUpdateEffects() {
		return this.entityUpdateEffects;
	}
	
	public Map<EffectCauseEntityRightClicked, List<Effect>> getEntityRightClickedEffects() {
		return this.entityRightClickedEffects;
	}
	
	public Map<EffectCauseEntityAttacked, List<Effect>> getEntityAttackedEffects() {
		return this.entityAttackedEffects;
	}
	
	public Map<EffectCauseEntityDeath, List<Effect>> getEntityDeathEffects() {
		return this.entityDeathEffects;
	}
	
	//// Other ////
	
	public Map<IBlockAdded, Map<EffectCauseBlockRandom, List<Effect>>> getBlockAddedRandomEffects() {
		return this.blockAddedRandomEffects;
	}
	
	private void refreshCachedEffects() {
		this.inHandEffects.clear();
		this.equippedEffects.clear();
		this.inInventoryEffects.clear();
		this.itemRightClickEffects.clear();
		this.itemRightClickBlockEffects.clear();
		this.itemRightClickEntityEffects.clear();
		this.usingEffects.clear();
		this.itemLeftClickEffects.clear();
		this.itemDiggingBlockEffects.clear();
		this.itemBreakBlockEffects.clear();
		this.itemAttackEffects.clear();
		this.itemKillEffects.clear();
		
		this.blockPlacedEffects.clear();
		this.blockDiggingEffects.clear();
		this.blockBrokenEffects.clear();
		this.blockRightClickedEffects.clear();
		this.blockRandomEffects.clear();
		this.blockContactEffects.clear();
		
		this.entitySpawnedEffects.clear();
		this.entityUpdateEffects.clear();
		this.entityRightClickedEffects.clear();
		this.entityAttackedEffects.clear();
		this.entityDeathEffects.clear();
		
		this.blockAddedRandomEffects.clear();
		
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
			
			} else if (addition.cause instanceof EffectCauseBlockPlaced) {
				this.blockPlacedEffects.put(((EffectCauseBlockPlaced)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseBlockDigging) {
				this.blockDiggingEffects.put(((EffectCauseBlockDigging)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseBlockBroken) {
				this.blockBrokenEffects.put(((EffectCauseBlockBroken)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseBlockRightClicked) {
				this.blockRightClickedEffects.put(((EffectCauseBlockRightClicked)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseBlockRandom) {
				if (((EffectCauseBlockRandom)addition.cause).blockState.getBlock() instanceof IBlockAdded) {
					IBlockAdded addedBlock = (IBlockAdded) ((EffectCauseBlockRandom)addition.cause).blockState.getBlock();
					Map<EffectCauseBlockRandom, List<Effect>> effects = this.blockAddedRandomEffects.get(addedBlock);
					
					if (effects == null) {
						effects = new HashMap<>();
						this.blockAddedRandomEffects.put(addedBlock, effects);
					}
					
					effects.put(((EffectCauseBlockRandom)addition.cause), addition.effects);
				} else {
					this.blockRandomEffects.put(((EffectCauseBlockRandom)addition.cause), addition.effects);
				}
			} else if (addition.cause instanceof EffectCauseBlockContact) {
				this.blockContactEffects.put(((EffectCauseBlockContact)addition.cause), addition.effects);
				
			} else if (addition.cause instanceof EffectCauseEntitySpawned) {
				this.entitySpawnedEffects.put(((EffectCauseEntitySpawned)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseEntityUpdate) {
				this.entityUpdateEffects.put(((EffectCauseEntityUpdate)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseEntityRightClicked) {
				this.entityRightClickedEffects.put(((EffectCauseEntityRightClicked)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseEntityAttacked) {
				this.entityAttackedEffects.put(((EffectCauseEntityAttacked)addition.cause), addition.effects);
			} else if (addition.cause instanceof EffectCauseEntityDeath) {
				this.entityDeathEffects.put(((EffectCauseEntityDeath)addition.cause), addition.effects);
			}
		}
	}
}
