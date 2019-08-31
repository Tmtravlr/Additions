package com.tmtravlr.additions.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ConfigLoader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
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
	
	private static final Style GRAY_STYLE = new Style().setColor(TextFormatting.GRAY).setUnderlined(false);
	
	private static boolean hasShownProblems;

	public static Multimap<TextComponentWrapper, TextComponentWrapper> notifications = LinkedListMultimap.create();
	public static List<ITextComponent> notificationLines = new ArrayList<>();
	public static ITextComponent notificationProblemCount = new TextComponentString("");
	public static ITextComponent notificationFileLocation = new TextComponentString("");
	public static File problemFile;
	public static File problemFolder;
	
	public static void initializeProblemFolder(FMLPreInitializationEvent event) {
		problemFolder = new File(event.getSuggestedConfigurationFile().getParentFile(), AdditionsMod.MOD_ID + "/problems/");
	}
	
	public static boolean hasProblems() {
		return !(notifications.isEmpty() && notificationLines.isEmpty());
	}

	@SideOnly(Side.CLIENT)
	public static boolean showInAdditionsMenu() {
		if (ConfigLoader.showProblemNotificationsMainMenu.getBoolean() && !notifications.isEmpty()) {
			createNotificationsClient();
			return true;
		}
		
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static void createNotificationsClient() {
		if (ConfigLoader.showProblemNotificationsMainMenu.getBoolean()) {
			createNotifications();
		}
	}
	
	public static void onReload(MinecraftServer server) {
		if (server.getCurrentPlayerCount() > 0) {
			showProblemsIngame(server);
		}
	}
	
	public static void showProblemsIngame(MinecraftServer server) {
		if (ConfigLoader.showProblemNotificationsIngame.getBoolean() && !notifications.isEmpty()) {
			createNotificationsServer(server);

			server.getPlayerList().sendMessage(notificationProblemCount);
			server.getPlayerList().sendMessage(notificationFileLocation);
		}
	}
	
	public static void createNotificationsServer(MinecraftServer server) {
		if (ConfigLoader.showProblemNotificationsIngame.getBoolean()) {
			createNotifications();
		}
	}
	
	public static ITextComponent createLabelFromFile(File file) {
		ITextComponent fileLocation = new TextComponentString(file.getPath()).setStyle(new Style().setColor(TextFormatting.WHITE));
		
		try {
			fileLocation.getStyle().setUnderlined(true).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("gui.notification.openFile"))).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getCanonicalPath()));
		} catch (IOException e) {
			AdditionsMod.logger.warn("Unable to add file link to problem notification label: " + file.getPath(), e);
		}
		
		return fileLocation;
	}
	
	public static ITextComponent createLabelFromPath(File addonFolder, String path) {
		File file = new File(addonFolder, path);
		ITextComponent fileLocation = new TextComponentString(file.getPath()).setStyle(new Style().setColor(TextFormatting.WHITE));
		
		if (addonFolder.isDirectory() && file.exists()) {
			try {
				fileLocation.getStyle().setUnderlined(true).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("gui.notification.openFile"))).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getCanonicalPath()));
			} catch (IOException e) {
				AdditionsMod.logger.warn("Unable to add file link to problem notification label: " + file.getPath(), e);
			}
		}
		
		return fileLocation;
	}
	
	public static void addProblemNotification(ITextComponent label, ITextComponent notification) {
		if (ConfigLoader.showProblemNotificationsMainMenu.getBoolean() || ConfigLoader.showProblemNotificationsIngame.getBoolean()) {
			if (notification.getStyle().isEmpty()) {
				notification.setStyle(GRAY_STYLE.createDeepCopy());
			}
			
			notifications.put(new TextComponentWrapper(label), (new TextComponentWrapper(notification)));
		}
	}
	
	private static void createNotifications() {
		if (!notifications.isEmpty()) {

			notificationLines = separateNotificationLines();
			saveProblemFile();
			
			ITextComponent notificationNumber = new TextComponentString(String.valueOf(notifications.size())).setStyle(new Style().setColor(TextFormatting.WHITE));
			ITextComponent fileLocation = new TextComponentString("config" + File.separator + AdditionsMod.MOD_ID + File.separator + "problems" + File.separator + problemFile.getName());
			fileLocation.getStyle().setColor(TextFormatting.WHITE).setUnderlined(true).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("gui.notification.openFile"))).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, problemFile.getPath()));
			
			notificationProblemCount = new TextComponentTranslation("gui.notification.problemCount." + (notifications.size() == 1 ? "singular" : "plural"), notificationNumber).setStyle(new Style().setColor(TextFormatting.RED));
			notificationFileLocation = new TextComponentTranslation("gui.notification.fileLocation", fileLocation).setStyle(new Style().setColor(TextFormatting.RED));
			
			hasShownProblems = false;
			notifications.clear();
		}
	}
	
	private static List<ITextComponent> separateNotificationLines() {
		List<ITextComponent> notificationLines = new ArrayList<>();
		
		notifications.asMap().forEach((category, notifications) -> {
			ITextComponent notificationText = category.text;
			
			for (TextComponentWrapper notification : notifications) {
				notificationText.appendSibling(new TextComponentString("\n- ").setStyle(GRAY_STYLE.createDeepCopy())).appendSibling(notification.text);
			}
			
			notificationLines.add(notificationText);
		});
		
		return notificationLines;
	}
	
	private static void saveProblemFile() {
		if (!problemFolder.exists()) {
			problemFolder.mkdirs();
		}
		
		problemFile = new File(problemFolder, "problems_" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
		
		try (PrintStream printStream = new PrintStream(problemFile);) {
			notificationLines.forEach(line -> printStream.println(line.getUnformattedText() + "\n"));
		} catch (IOException e) {
			AdditionsMod.logger.warn("Couldn't print out problems file", e);
		}
	}
	
	// Because null pointer when trying to call hashcode on text components =(
	public static class TextComponentWrapper {
		public ITextComponent text;
		
		public TextComponentWrapper(ITextComponent text) {
			this.text = text;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((text == null || text.getUnformattedComponentText() == null) ? 0 : text.getUnformattedComponentText().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TextComponentWrapper other = (TextComponentWrapper) obj;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}
	}

}
