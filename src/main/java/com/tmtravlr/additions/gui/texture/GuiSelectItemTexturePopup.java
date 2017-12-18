package com.tmtravlr.additions.gui.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
 * Shows a popup message to select a texture.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiSelectItemTexturePopup extends GuiMessagePopupTwoButton {

	private Addon addon;
	private IItemAdded item;
	private static final int BUTTON_SELECT_FILE = 2;
	private boolean hasBackButton = true;
	private String selectFileText = I18n.format("gui.popup.texture.selectFile");

	public GuiSelectItemTexturePopup(GuiScreen parentScreen, Addon addon, IItemAdded item) {
		super(parentScreen, new GuiMessagePopup(parentScreen, I18n.format("gui.popup.custom.item.title"), new TextComponentTranslation("gui.popup.custom.item.message", AdditionTypeItem.INSTANCE.getItemModelName(item)), I18n.format("gui.popup.texture.goToFolder")) {

			@Override
			protected void actionPerformed(GuiButton button) throws IOException {
				if (button.id == BUTTON_CANCEL) {
					File modelFolder = AdditionTypeItem.INSTANCE.getItemModelFolder(addon);

					if (!modelFolder.isDirectory()) {
						modelFolder.mkdirs();
					}

					URI url = modelFolder.getCanonicalFile().toURI();
					this.openFolder(url);

					this.mc.displayGuiScreen(this.parentScreen);
				}
			}

		}, I18n.format("gui.popup.texture.item.title"), new TextComponentTranslation("gui.popup.texture.item.message", item.getDisplayName()), I18n.format("gui.buttons.back"), I18n.format("gui.popup.texture.customModel"));

		this.addon = addon;
		this.item = item;
	}
	
	public void setHasBackButton(boolean hasButton) {
		this.hasBackButton = hasButton;
	}

	@Override
	public void initGui() {
		int popupWidth = this.getPopupWidth();      

		this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), popupWidth - 50);
		this.textHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;

		int popupHeight = this.getPopupHeight();
		int popupY = this.height / 2 - popupHeight / 2;
		int buttonY = popupY + popupHeight - 30;
		int buttonWidth = 90;
		int buttonHeight = 20;

		if (hasBackButton) {
			this.buttonList.add(new GuiButton(BUTTON_CANCEL, this.width / 2 - 150, buttonY, buttonWidth, buttonHeight, buttonText));
		}
		this.buttonList.add(new GuiButton(BUTTON_CONTINUE, this.width / 2 - 45, buttonY, buttonWidth, buttonHeight, buttonContinueText));
		this.buttonList.add(new GuiButton(BUTTON_SELECT_FILE, this.width / 2 + 60, buttonY, buttonWidth, buttonHeight, selectFileText));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == BUTTON_SELECT_FILE) {
			try { 
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			} catch (Exception e) {
				AdditionsMod.logger.warn("Couldn't set system look and feel for file chooser.", e);
			}
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
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
				
				try {
					BufferedImage image = ImageIO.read(fileChosen);
					if (image == null) {
						throw new IOException("Couldn't read image.");
					}
					int imageWidth = image.getTileWidth();
					int imageHeight = image.getTileHeight();
					if ((imageWidth & (imageWidth - 1)) != 0 || ((imageHeight & (imageHeight - 1)) != 0 && imageHeight % imageWidth != 0)) {
						//Wrong image dimensions
						this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.notSidesx2.message"), I18n.format("gui.buttons.back")));
						return;
					} else if (imageHeight % imageWidth != 0) {
						if (imageWidth > imageHeight) {
							//Width can't be larger than height
							this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.tooWide.message"), I18n.format("gui.buttons.back")));
						} else {
							//Bad animation
							this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.badAnimation.message"), I18n.format("gui.buttons.back")));
						}
						return;
					} else {
						//Load image
						AdditionTypeItem.INSTANCE.saveAddonTexture(this.addon, this.item, fileChosen);

						if (imageWidth != imageHeight) {
							//Animation
							this.mc.displayGuiScreen(new GuiSelectAnimationPopup(this.parentScreen, this.addon, this.item));
							return;
						} else {
							//Success
							this.mc.displayGuiScreen(new GuiRefreshingResources(new GuiMessagePopup(this.parentScreen, I18n.format("gui.popup.texture.success.title"), new TextComponentTranslation("gui.popup.texture.success.message"), I18n.format("gui.buttons.continue"))));
							return;
						}

					}
				} catch (IOException e) {
					//Not a valid image?
					AdditionsMod.logger.warn("Error loading texture: ", e);
					this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.popup.texture.problem.cantLoad.title"), new TextComponentTranslation("gui.popup.texture.problem.cantLoad.message", fileChosen.getName()), I18n.format("gui.buttons.back")));
					return;
				}
			}

			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			super.actionPerformed(button);
		}
	}

	@Override
	protected int getPopupWidth() {
		return 350;
	}

}
