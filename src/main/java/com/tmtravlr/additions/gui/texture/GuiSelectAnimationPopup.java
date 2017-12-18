package com.tmtravlr.additions.gui.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.type.AdditionTypeItem;

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
public class GuiSelectAnimationPopup extends GuiMessagePopupTwoButton {

	private Addon addon;
	private IItemAdded item;

	public GuiSelectAnimationPopup(GuiScreen parentScreen, Addon addon, IItemAdded item) {
		super(parentScreen, parentScreen, I18n.format("gui.popup.texture.animation.title"), new TextComponentTranslation("gui.popup.texture.animation.message", item.getDisplayName()), I18n.format("gui.popup.texture.animation.button.generate"), I18n.format("gui.popup.texture.selectFile"));

		this.addon = addon;
		this.item = item;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == BUTTON_CANCEL) {
			AdditionTypeItem.INSTANCE.saveTextureAnimation(addon, item, null);
		} else if (button.id == BUTTON_CONTINUE) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				AdditionsMod.logger.warn("Couldn't set system look and feel for file chooser.", e);
			}

			FileNameExtensionFilter filter = new FileNameExtensionFilter("MCMETA File", "mcmeta", "txt");
			File defaultFolder = ConfigLoader.getDefaultFileDialogueFolder();
			
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(defaultFolder);
			int returnVal = chooser.showOpenDialog(null);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File fileChosen = chooser.getSelectedFile();
				
				if (!fileChosen.getParentFile().equals(defaultFolder)) {
					ConfigLoader.setDefaultFileDialogueFolder(fileChosen.getParentFile());
				}
				
				AdditionTypeItem.INSTANCE.saveTextureAnimation(addon, item, fileChosen);
			}
		}
		
		this.mc.displayGuiScreen(new GuiRefreshingResources(new GuiMessagePopup(this.parentScreen, I18n.format("gui.popup.texture.success.title"), new TextComponentTranslation("gui.popup.texture.success.message"), I18n.format("gui.buttons.continue"))));
		return;
	}
}
