package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.advancements.AdvancementAdded;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added advancements
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class AdditionTypeAdvancement extends AdditionType<AdvancementAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "advancement");
	public static final String FOLDER_NAME = "data" + File.separator + "advancements";
	public static final String FILE_POSTFIX = ".json";
	public static final AdditionTypeAdvancement INSTANCE = new AdditionTypeAdvancement();
	
	private final Multimap<Addon, AdvancementAdded> advancementsLoaded = HashMultimap.create();

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		this.reloadAllAdvancements(addons);
	}
	
	@Override
	public List<AdvancementAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.advancementsLoaded.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, AdvancementAdded addition) {
		throw new IllegalStateException("Saving advancements isn't supported yet!");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, AdvancementAdded addition) {
		if (this.advancementsLoaded.containsEntry(addon, addition)) {
			this.advancementsLoaded.remove(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, addition.id.getResourceDomain() + File.separator + addition.id.getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public void reloadAllAdvancements(List<Addon> addons) {
		AdditionsMod.logger.info("Loading addon advancements.");
		this.advancementsLoaded.clear();
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading advancement files for addon " + addon.id + ". The advancements will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.advancements.title", addon.id), new TextComponentString(e.getMessage()));
			}
			
			for (String filePath : filePaths) {
				String advancementName = filePath;
				
				int advancementsFolderIndex = 0;
				if (advancementName.startsWith(FOLDER_NAME + File.separator)) {
					advancementsFolderIndex = FOLDER_NAME.length() + 1;
				}
				
				advancementName = advancementName.substring(advancementsFolderIndex, advancementName.length() - FILE_POSTFIX.length());
				
				if (advancementName.contains(File.separator)) {
					String[] locationStrings = advancementName.split(Pattern.quote(File.separator), 2);
					String locationPath = locationStrings[1];
					
					if (!"/".equals(File.separator)) {
						locationPath = locationPath.replace(File.separatorChar, '/');
					}
					
					ResourceLocation location = new ResourceLocation(locationStrings[0], locationPath);
					
					try {
						Advancement.Builder builder = JsonUtils.gsonDeserialize(AdvancementManager.GSON, AddonLoader.readAddonFile(addon.addonFolder, filePath), Advancement.Builder.class);
						this.advancementsLoaded.put(addon, new AdvancementAdded(location, builder));
					} catch (IOException | JsonParseException e) {
						AdditionsMod.logger.error("Error loading advancement " + location + " for addon " + addon.id + ". It will be skipped.", e);
						ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
					}
				} else {
					AdditionsMod.logger.error("Addon advancement " + filePath + " can't be directly in the advancements folder. It must be inside another folder.");
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentTranslation("gui.notification.problem.directlyInFolder", "functions"));
				}
			}
		}
	}

	public File getAdvancementFolder(Addon addon) {
		return new File(addon.addonFolder, FOLDER_NAME + File.separator + "additions");
	}
}
