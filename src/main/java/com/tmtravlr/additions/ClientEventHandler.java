package com.tmtravlr.additions;

import java.util.List;

import com.tmtravlr.additions.gui.GuiAdditionsButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = AdditionsMod.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {
	
	private static final int ADDITIONS_BUTTON = 29384;
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static List<GuiButton> cachedMainMenuButtons;
	private static GuiAdditionsButton cachedAdditionsButton;
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (ConfigLoader.renderAdditionsButtonInMainMenu.getBoolean()) {
			if (mc.currentScreen instanceof GuiMainMenu) {
				if (cachedMainMenuButtons == null) {
					cachedMainMenuButtons = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, mc.currentScreen, "buttonList", "field_146292_n");
				}
				
				if (cachedAdditionsButton == null || !cachedMainMenuButtons.contains(cachedAdditionsButton)) {
					cachedAdditionsButton = new GuiAdditionsButton(ADDITIONS_BUTTON, mc.currentScreen);
					cachedMainMenuButtons.add(cachedAdditionsButton);
				}
			} else if (cachedMainMenuButtons != null) {
				cachedMainMenuButtons = null;
			}
		}
	}

}
