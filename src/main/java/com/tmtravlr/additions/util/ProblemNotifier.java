package com.tmtravlr.additions.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ConfigLoader;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Notifies the user of problems during loading/reloading
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since July 2019
 */
public class ProblemNotifier {

	public static Multimap<String, ITextComponent> notifications = HashMultimap.create();
	public static File problemFolder;
	
	public static void initializeProblemFolder(FMLPreInitializationEvent event) {
		problemFolder = new File(event.getSuggestedConfigurationFile().getParentFile(), AdditionsMod.MOD_ID + "/problems/");
	}
	
	public static void addProblemNotification(String label, ITextComponent notification) {
		if (ConfigLoader.showProblemNotifications.getBoolean()) {
			notifications.put(label, notification);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void showProblemsMainMenu() {
		if (ConfigLoader.showProblemNotifications.getBoolean() && !notifications.isEmpty()) {
			
			notifications.clear();
		}
	}
	
	public static void showProblemsIngame(MinecraftServer server, EntityPlayerMP player) {
		if (ConfigLoader.showProblemNotifications.getBoolean() && !notifications.isEmpty()) {
			
			notifications.clear();
		}
	}
	
	private static File saveProblemsFile() {
		if (!problemFolder.exists()) {
			problemFolder.mkdirs();
		}
		
		File problemFile = new File(problemFolder, "problem-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
		
		return problemFile;
	}

}
