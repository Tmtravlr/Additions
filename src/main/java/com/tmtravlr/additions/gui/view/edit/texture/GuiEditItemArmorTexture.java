package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Lets you select a texture for a simple item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiEditItemArmorTexture extends GuiEditItemTexture {
	
	protected GuiComponentDisplayText baseArmorTextureMessage;
	protected GuiComponentFileInput baseArmorTextureFileInput;
	protected GuiComponentDisplayText overlayArmorTextureMessage;
	protected GuiComponentFileInput overlayArmorTextureFileInput;
	protected GuiComponentDisplayText enableColorMessage;
	
	private final ItemAddedArmor armorItem;
	private final boolean isColored;
	
	public GuiEditItemArmorTexture(GuiScreen parentScreen, Addon addon, ItemAddedArmor item, boolean isNew, boolean isColored) {
		super(parentScreen, addon, item, isNew, ItemModelManager.ItemModelType.SIMPLE);
		this.armorItem = item;
		this.isColored = isColored;
	}
	
	@Override
	public void initComponents() {		
		 this.baseArmorTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.armor.base.message", this.armorItem.getDisplayName()));
		 
		 this.baseArmorTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.armor.base.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
			@Override
			protected void setFileDialogueFolder(File folder) {
				ClientConfigLoader.setFileDialogueFolderTextures(folder);
			}
			
			@Override
			protected File getFileDialogueFolder() {
				return ClientConfigLoader.getFileDialogueFolderTextures();
			}

			@Override
			protected boolean validateFileChosen(File fileChosen) {
				return GuiEditItemArmorTexture.this.checkArmorTexture(fileChosen, false);
			}
		};
		if (this.isNew) {
			this.baseArmorTextureFileInput.setRequired();
		}
		 
		 this.overlayArmorTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.armor.overlay.message"));
		 
		 this.overlayArmorTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.armor.overlay.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
			@Override
			protected void setFileDialogueFolder(File folder) {
				ClientConfigLoader.setFileDialogueFolderTextures(folder);
			}
			
			@Override
			protected File getFileDialogueFolder() {
				return ClientConfigLoader.getFileDialogueFolderTextures();
			}

			@Override
			protected boolean validateFileChosen(File fileChosen) {
				return GuiEditItemArmorTexture.this.checkArmorTexture(fileChosen, true);
			}
		};
		
		super.initComponents();
		
		this.components.remove(this.isColoredInput);
		
		if (isColored) {
			this.showColorComponents();
		}
	}
	
	@Override
	protected void addBaseComponents() {
		super.addBaseComponents();
		this.components.add(this.baseArmorTextureMessage);
		this.components.add(this.baseArmorTextureFileInput);
	}
	
	@Override
	protected void addColorComponents() {
		super.addColorComponents();
		this.colorComponents.add(this.overlayArmorTextureMessage);
		this.colorComponents.add(this.overlayArmorTextureFileInput);
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_CUSTOM_MODEL) {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this.parentScreen, I18n.format("gui.popup.model.title"), new TextComponentTranslation("gui.popup.model.item.armor.message", ItemModelManager.getItemModelName(item), ItemModelManager.getArmorModelName(item, false), ItemModelManager.getArmorModelName(item, true)), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.goToFolder")) {

				@Override
				protected void onSecondButtonClicked() {
					File modelFolder = ItemModelManager.getItemModelFolder(GuiEditItemArmorTexture.this.addon);
	
					if (!modelFolder.isDirectory()) {
						modelFolder.mkdirs();
					}

					CommonGuiUtils.openFolder(modelFolder);

					this.mc.displayGuiScreen(this.parentScreen);
				}
	
			});
    	} else {
    		super.actionPerformed(button);
    	}
    }
	
	@Override
	protected boolean checkFiles() {
		if (this.baseArmorTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.armor.problem.noTexture.message", I18n.format("gui.edit.texture.armor.base.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		return super.checkFiles();
	}
	
	@Override
	protected void saveFiles() throws IOException {
		super.saveFiles();
		
		ItemModelManager.saveArmorTexture(this.addon, this.armorItem, this.baseArmorTextureFileInput.getFile(), false);
		
		if (this.overlayArmorTextureFileInput.getFile() != null) {
			ItemModelManager.saveArmorTexture(this.addon, this.armorItem, this.colorTextureFileInput.getFile(), true);
		} else {
			ItemModelManager.deleteArmorOverlayTexture(this.addon, this.armorItem);
		}
	}
	
	protected boolean checkArmorTexture(File fileChosen, boolean isColor) {
		try {
			BufferedImage image = ImageIO.read(fileChosen);
			if (image == null) {
				throw new IOException("Couldn't read image.");
			}
			int imageWidth = image.getTileWidth();
			int imageHeight = image.getTileHeight();
			if ((imageWidth & (imageWidth - 1)) != 0 || (imageHeight & (imageHeight - 1)) != 0 || imageWidth != imageHeight*2) {
				//Wrong image dimensions
				this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.edit.texture.armor.problem.wrongDimensions.notSidesx2.message"), I18n.format("gui.buttons.back")));
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			//Not a valid image?
			AdditionsMod.logger.warn("Error loading texture: ", e);
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.cantLoad.title"), new TextComponentTranslation("gui.edit.texture.problem.cantLoad.message", fileChosen.getName()), I18n.format("gui.buttons.back")));
		}
		
		return false;
	}
	
	

}
