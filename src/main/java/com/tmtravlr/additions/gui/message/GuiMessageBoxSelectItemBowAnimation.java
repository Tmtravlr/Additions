package com.tmtravlr.additions.gui.message;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a popup message to select an animation for an animated texture.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiMessageBoxSelectItemBowAnimation extends GuiMessageBoxSelectItemAnimation {

	protected ItemAddedBow bowItem;
	protected String textureEnding;

	public GuiMessageBoxSelectItemBowAnimation(GuiScreen parentScreen, Addon addon, ItemAddedBow item, String textureEnding, boolean isColor) {
		super(parentScreen, addon, item, isColor);
		this.bowItem = item;
	}
	
	protected void saveTextureAnimation(File fileChosen) throws IOException {
		super.saveTextureAnimation(fileChosen);
		ItemModelManager.saveBowPullingTextureAnimation(this.addon, this.bowItem, textureEnding, this.isColor, fileChosen);
	}
}
