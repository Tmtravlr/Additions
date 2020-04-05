package com.tmtravlr.additions.addon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.toposort.ModSortingException;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;

/**
 * Loads addons from folders in the folder addons/Additions.
 * Also tries loading addons from the mods and addons/lucky_block folder,
 * but doesn't load resource packs for the lucky_block ones, since
 * the lucky block mod loads them as resource packs already.
 * 
 * Also calls the addition type manager to load each type of addition.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class AddonLoader {
	
    private static final Gson GSON = new GsonBuilder()
    		.registerTypeAdapter(Addon.class, new Addon.Serializer())
    		.setPrettyPrinting()
    		.create();
    
    private static final boolean ZIP_SEPARATOR_DIFFERENT = !"/".equals(File.separator);
    private static final String UTF_8_ENCODING = "UTF-8";
	
    public static final String ADDON_LOCATION = "/addons/Additions";
	public static final String LUCKY_BLOCK_LOCATION = "/addons/lucky_block";
    public static final String MODS_LOCATION = "/mods";
	public static final Map<String, Addon> ADDONS_NAMED = new TreeMap<>();
	
	public static List<Addon> addonsLoaded = new ArrayList<>();
	public static File additionsFolder;
	public static File luckyBlockFolder;
	public static File modsFolder;
	
	public static void loadAddons() {
		
		//Load the addon folders
		try {
			additionsFolder = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, ADDON_LOCATION);
			luckyBlockFolder = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, LUCKY_BLOCK_LOCATION);
			modsFolder = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, MODS_LOCATION);
		} catch (NoClassDefFoundError e) {
			//Must be on a dedicated server
			additionsFolder = new File(new File("."), ADDON_LOCATION);
			luckyBlockFolder = new File(new File("."), LUCKY_BLOCK_LOCATION);
			modsFolder = new File(new File("."), MODS_LOCATION);
		}
		
		if (!additionsFolder.exists()) {
			additionsFolder.mkdirs();
		}
		
		loadAddonsFromFolder(additionsFolder, true);
		loadAddonsFromFolder(luckyBlockFolder, false);
		loadAddonsFromFolder(modsFolder, true);
		
		//Check for missing dependencies
		for (Addon addon : addonsLoaded) {
			
			if (!addon.requiredMods.isEmpty()) {
                    
				BiMap<String, ArtifactVersion> modVersions = HashBiMap.create();
	            for (ModContainer mod : Iterables.concat(Loader.instance().getActiveModList(), ModAPIManager.INSTANCE.getAPIList())) {
	                modVersions.put(mod.getModId(), mod.getProcessedVersion());
	            }
				
				for (ArtifactVersion acceptedVersion : addon.requiredMods.stream().map(VersionParser::parseVersionReference).collect(Collectors.toSet())) {
                    ArtifactVersion currentVersion = modVersions.get(acceptedVersion.getLabel());
                    
                    if (currentVersion == null || !acceptedVersion.containsVersion(currentVersion)) {
                    	throw createNotFoundException(addon, acceptedVersion, true);
                    }
                }
			}
				
			if (!addon.requiredAddons.isEmpty()) {
				
				BiMap<String, ArtifactVersion> addonVersions = HashBiMap.create();
	            ADDONS_NAMED.forEach((addonName, addonNamed) -> {
	            	addonVersions.put(addonName,  addonNamed.version.isEmpty() ? new DefaultArtifactVersion(addonName, true) : new DefaultArtifactVersion(addonName, addonNamed.version));
	            });
				
				for (ArtifactVersion acceptedVersion : addon.requiredAddons.stream().map(VersionParser::parseVersionReference).collect(Collectors.toSet())) {
                    ArtifactVersion currentVersion = addonVersions.get(acceptedVersion.getLabel());
                    
                    if (currentVersion == null || !acceptedVersion.containsVersion(currentVersion)) {
                    	throw createNotFoundException(addon, acceptedVersion, false);
                    }
                }
			}
		}
		
		//Re-organize the addons if needed
		AddonSorter sorter = new AddonSorter();
		
		try {
			addonsLoaded = sorter.sort();
		} catch (ModSortingException e) {
			throw AdditionsMod.proxy.createLoadingException(I18n.translateToLocalFormatted("gui.loadingError.dependancyCycle", (Addon)e.getExceptionData().getFirstBadNode()));
		}
	}
	
	/**
	 * Returns true if a file exists at the given path.
	 * 
	 * @param addonFolder The addon's folder, or zip file
	 * @param path The file's path
	 */
	public static boolean addonFileExists(File addonFolder, String path) throws IOException {
		if (addonFolder.isDirectory()) {
			File file = new File(addonFolder, path);
			return file.exists();
		} else {
			boolean exists = false;
			
			try (final ZipFile zipFile = new ZipFile(addonFolder)) {
				String zipPath = path;
				
				if (ZIP_SEPARATOR_DIFFERENT) {
					zipPath = zipPath.replace(File.separatorChar, '/');
				}
				
				ZipEntry entry = zipFile.getEntry(zipPath);
				
				exists = entry != null;
			} catch (ZipException e) {
				// Ignore, since this means it isn't a zipped file.
			}
			
			return exists;
		}
	}
	
	/**
	 * Returns list of all files in the addon as string paths, starting from the given path.
	 * 
	 * @param addonFolder The addon's folder, or zip file
	 * @param path The path to start getting files from. This is usually a sub-folder.
	 */
	public static List<String> getAddonFilePaths(File addonFolder, String path) throws IOException {
		List<String> filePaths = new ArrayList<>();
		
		if (addonFolder.isDirectory()) {
			File folder = new File(addonFolder, path);
			
			if (folder.isDirectory()) {
				filePaths = Files.find(folder.toPath(), 1000, (filePath, fileAttributes) -> fileAttributes.isRegularFile())
						.map(filePath -> {
							String pathString = filePath.toString();
							int pathStart = pathString.indexOf(File.separator + path + File.separator);
							
							if (pathStart >= 0) {
								pathString = pathString.substring(pathStart + 1);
							}
							
							return pathString;
						})
						.collect(Collectors.toList());
			}
		} else {
			try (final ZipFile zipFile = new ZipFile(addonFolder)) {
				Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
				
				String zipPath = path;
				
				if (ZIP_SEPARATOR_DIFFERENT) {
					zipPath = zipPath.replace(File.separatorChar, '/');
				}
				
				while (zipEntries.hasMoreElements()) {
					ZipEntry entry = zipEntries.nextElement();
					
					if (entry.getName().startsWith(zipPath) && !entry.isDirectory()) {
						String entryPath = entry.getName();
						
						if (ZIP_SEPARATOR_DIFFERENT) {
							entryPath = entryPath.replace('/', File.separatorChar);
						}
						
						filePaths.add(entryPath);
					}
				}
			}
		}
		
		return filePaths;
	}
	
	/**
	 * Reads the file in the addon at the path to a string.
	 * 
	 * @param addonFolder The addon's folder, or zip file
	 * @param path The path of the file to read.
	 */
	public static String readAddonFile(File addonFolder, String path) throws IOException {
		if (addonFolder.isDirectory()) {
			File file = new File(addonFolder, path);
			return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		} else {
			String fileString = "";
			
			try (final ZipFile zipFile = new ZipFile(addonFolder)) {
				String zipPath = path;
				
				if (ZIP_SEPARATOR_DIFFERENT) {
					zipPath = zipPath.replace(File.separatorChar, '/');
				}
				
				ZipEntry entry = zipFile.getEntry(zipPath);
				
				if (entry != null) {
					try (final BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), UTF_8_ENCODING))) {
						StringBuilder fileStringBuilder = new StringBuilder();
						
						String line;
						while ((line = reader.readLine()) != null) {
							fileStringBuilder.append(line);
						}
						
						fileString = fileStringBuilder.toString();
					}
				}
			}
			
			return fileString;
		}
	}
	
	/**
	 * Reads the file in the addon at the path to a list of strings, which are 
	 * the lines of the file. Makes it a lot easier to deal with different line
	 * endings in different OS's.
	 * 
	 * @param addonFolder The addon's folder, or zip file
	 * @param path The path of the file to read.
	 */
	public static List<String> readAddonFileLines(File addonFolder, String path) throws IOException {
		List<String> lines = new ArrayList<String>();
		if (addonFolder.isDirectory()) {
			File file = new File(addonFolder, path);
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} else {
			try (final ZipFile zipFile = new ZipFile(addonFolder)) {
				String zipPath = path;
				
				if (ZIP_SEPARATOR_DIFFERENT) {
					zipPath = zipPath.replace(File.separatorChar, '/');
				}
				
				ZipEntry entry = zipFile.getEntry(zipPath);
				
				if (entry != null) {
					try (final BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), UTF_8_ENCODING))) {
						String line;
						
						while ((line = reader.readLine()) != null) {
							lines.add(line.trim());
						}
					}
				}
			}
		}
		
		return lines;
	}
	
	public static void saveAddon(Addon addon) {
		File infoFile = new File(addon.addonFolder, "addon.json");
		
		try {
			String fileContents = GSON.toJson(addon);
			FileUtils.write(infoFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.warn("Error saving addon " + addon.name, e);
		}
	}
	
	public static void deleteAddon(Addon addon) throws IOException {
		for (Path path : Files.walk(addon.addonFolder.toPath()).sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
			Files.delete(path);
		}
	}
	
	public static void setupNewAddon(Addon addon) {
		addonsLoaded.add(addon);
		ADDONS_NAMED.put(addon.id, addon);
		AdditionTypeManager.setupNewAddon(addon);
		saveResourcePackFile(addon);
	}
	
	public static void lockAddon(Addon addon) throws IOException {
		AdditionsMod.proxy.unRegisterResourcePack(addon.addonFolder);
		AdditionsMod.proxy.refreshResources();
		
		File zippedAddon = new File(addon.addonFolder.getParentFile(), addon.addonFolder.getName() + " v" + addon.version + ".zip");
		
		try (final FileOutputStream fileOutput = new FileOutputStream(zippedAddon); final ZipOutputStream zipOutput = new ZipOutputStream(fileOutput)) {
			zipFolder(addon.addonFolder, "", zipOutput);
		}
		
		File oldFolder = addon.addonFolder;
		addon.addonFolder = zippedAddon;
		addon.locked = true;
		AdditionsMod.proxy.registerAsResourcePack(addon.addonFolder);
		AdditionsMod.proxy.refreshResources();
		
		for (Path path : Files.walk(oldFolder.toPath()).sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
			Files.delete(path);
		}
	}
	
	public static void unlockAddon(Addon addon) throws IOException {
		AdditionsMod.proxy.unRegisterResourcePack(addon.addonFolder);
		AdditionsMod.proxy.refreshResources();
		
        byte[] readBuffer = new byte[1024];
        int bytesRead = 0;
		String folderName = addon.addonFolder.getName();
		int versionIndex = folderName.indexOf(" v");
		
		if (versionIndex >= 0) {
			folderName = folderName.substring(0, versionIndex);
		} else if (folderName.endsWith(".zip")) {
			folderName = folderName.substring(0, folderName.length() - 4);
		}
		
		File addonFolder = new File(additionsFolder, folderName);
		addonFolder.mkdirs();
		
		try (final ZipFile zipFile = new ZipFile(addon.addonFolder)) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			
			while(zipEntries.hasMoreElements()) {
				ZipEntry zipEntry = zipEntries.nextElement();
				String name = zipEntry.getName();
				File file = new File(addonFolder, name);
				
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}
				
				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}
				
				try (final InputStream zipInput = zipFile.getInputStream(zipEntry); final FileOutputStream fileOutput = new FileOutputStream(file)) {
					while((bytesRead = zipInput.read(readBuffer)) != -1) {
						fileOutput.write(readBuffer, 0, bytesRead);
					}
				}
			}
		}

		File oldZippedFile = addon.addonFolder;
		addon.addonFolder = addonFolder;
		addon.locked = false;
		AdditionsMod.proxy.registerAsResourcePack(addon.addonFolder);
		AdditionsMod.proxy.refreshResources();
		Files.delete(oldZippedFile.toPath());
	}
	
	public static void loadAdditionsPreInit(FMLPreInitializationEvent event) {
		AdditionsMod.logger.info("Preinitializing addons.");
		AdditionTypeManager.loadPreInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsInit(FMLInitializationEvent event) {
		AdditionsMod.logger.info("Initializing addons.");
		AdditionTypeManager.loadInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsPostInit(FMLPostInitializationEvent event) {
		AdditionsMod.logger.info("Postinitializing addons.");
		AdditionTypeManager.loadPostInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsServerStarting(FMLServerStartingEvent event) {
		AdditionsMod.logger.info("Loading addons as server starts.");
		AdditionTypeManager.loadServerStarting(addonsLoaded, event);
	}
	
	private static void saveResourcePackFile(Addon addon) {
		File packMeta = new File(addon.addonFolder, "pack.mcmeta");
		
		if (!packMeta.exists()) {
			String packMetaString = "{\n" +
									"  \"pack\": {\n" +
									"    \"pack_format\": 3,\n" +
									"    \"description\": \"Additions Addon: " + addon.id + "\"\n" +
									"  }\n" +
									"}";
			
			try {
				FileUtils.write(packMeta, packMetaString, StandardCharsets.UTF_8);
			} catch (IOException | IllegalArgumentException e) {
				AdditionsMod.logger.warn("Error creating pack meta for " + addon.name, e);
			}
		}
	}
	
	private static void loadAddonsFromFolder(File folder, boolean registerResourcePack) {
		AdditionsMod.logger.info("Loading addon files from location " + folder);
		
		if (folder.exists()) {
			File[] addonFiles = folder.listFiles();
			if (addonFiles != null && addonFiles.length > 0) {
				
				for (File addonFile : addonFiles) {
					try {
						if (addonFileExists(addonFile, "addon.json")) {
								String fileString = readAddonFile(addonFile, "addon.json");
								Addon addon = GSON.fromJson(fileString, Addon.class);
								addon.setAddonFolder(addonFile);
								addon.setLocked(!addonFile.isDirectory());
								
								addonsLoaded.add(addon);
								ADDONS_NAMED.put(addon.id, addon);
								
								if (registerResourcePack) {
									AdditionsMod.proxy.registerAsResourcePack(addonFile);
								}
		
								AdditionsMod.logger.info("Loaded addon '" + addon.id + "'");
						} else {
							AdditionsMod.logger.debug("Non-addon file or folder found in: " + addonFile.getName());
						}
					} catch (IOException | JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading addon " + addonFile + ". The addon will not load.", e);
					}
				}
			}
		}
	}
	
	private static void zipFolder(File folderToZip, String path, ZipOutputStream zipOutput) throws IOException {
        byte[] readBuffer = new byte[1024];
        int bytesRead = 0;
		File[] subFiles = folderToZip.listFiles();
		
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				zipFolder(subFile, path + subFile.getName() + "/", zipOutput);
				continue;
			} else {
				try (final FileInputStream fileInput = new FileInputStream(subFile)) {
					ZipEntry zipEntry = new ZipEntry(path + subFile.getName());
					zipOutput.putNextEntry(zipEntry);
					while ((bytesRead = fileInput.read(readBuffer)) != -1) {
						zipOutput.write(readBuffer, 0, bytesRead);
					}
				}
			}
		}
	}
	
	private static RuntimeException createNotFoundException(Addon addon, ArtifactVersion acceptedVersion, boolean mod) {
		String versionString;
		
		if (acceptedVersion instanceof DefaultArtifactVersion) {
			DefaultArtifactVersion acceptedVersionDefault = (DefaultArtifactVersion) acceptedVersion;
			if (acceptedVersionDefault.getRange() == null) {
				versionString = I18n.translateToLocal("gui.loadingError.anyVersion");
			} else {
				versionString = I18n.translateToLocalFormatted("gui.loadingError.version", acceptedVersionDefault.getRange().toStringFriendly());
			}
		} else {
			versionString = acceptedVersion.toString();
		}
		
    	return AdditionsMod.proxy.createLoadingException(I18n.translateToLocalFormatted("gui.loadingError." + (mod ? "mod" : "addon") + "NotFound", addon, versionString, acceptedVersion.getLabel()));
	}
	
}
