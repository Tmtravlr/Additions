package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectItemBowAnimation;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Lets you select a texture for a simple item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiEditItemBowTexture extends GuiEditItemTexture {
	
	protected GuiComponentDisplayText baseBowPulling0TextureMessage;
	protected GuiComponentFileInput baseBowPulling0TextureFileInput;
	protected GuiComponentDisplayText colorBowPulling0TextureMessage;
	protected GuiComponentFileInput colorBowPulling0TextureFileInput;
	
	protected GuiComponentDisplayText baseBowPulling1TextureMessage;
	protected GuiComponentFileInput baseBowPulling1TextureFileInput;
	protected GuiComponentDisplayText colorBowPulling1TextureMessage;
	protected GuiComponentFileInput colorBowPulling1TextureFileInput;
	
	protected GuiComponentDisplayText baseBowPulling2TextureMessage;
	protected GuiComponentFileInput baseBowPulling2TextureFileInput;
	protected GuiComponentDisplayText colorBowPulling2TextureMessage;
	protected GuiComponentFileInput colorBowPulling2TextureFileInput;
	
	private final ItemAddedBow bowItem;
	
	public GuiEditItemBowTexture(GuiScreen parentScreen, Addon addon, ItemAddedBow item, boolean isNew) {
		super(parentScreen, addon, item, isNew, ItemModelManager.ItemModelType.BOW);
		this.bowItem = item;
	}
	
	@Override
	public void initComponents() {
		 this.baseBowPulling0TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.base0.message"));
		 
		 this.baseBowPulling0TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.base0.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_0_ENDING, false);
			}
		};
		if (this.isNew) {
			this.baseBowPulling0TextureFileInput.setRequired();
		}
		 
		 this.colorBowPulling0TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.color0.message"));
		 
		 this.colorBowPulling0TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.color0.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_0_ENDING, true);
			}
		};
		
		 this.baseBowPulling1TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.base1.message"));
		 
		 this.baseBowPulling1TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.base1.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_1_ENDING, false);
			}
		};
		if (this.isNew) {
			this.baseBowPulling1TextureFileInput.setRequired();
		}
		 
		 this.colorBowPulling1TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.color1.message"));
		 
		 this.colorBowPulling1TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.color1.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_1_ENDING, true);
			}
		};
		
		 this.baseBowPulling2TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.base2.message"));
		 
		 this.baseBowPulling2TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.base2.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_2_ENDING, false);
			}
		};
		if (this.isNew) {
			this.baseBowPulling2TextureFileInput.setRequired();
		}
		 
		 this.colorBowPulling2TextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.bow.color2.message"));
		 
		 this.colorBowPulling2TextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.bow.color2.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemBowTexture.this.checkBowPullingTexture(fileChosen, ItemModelManager.MODEL_BOW_PULLING_2_ENDING, true);
			}
		};
		
		super.initComponents();
	}
	
	@Override
	protected void addBaseComponents() {
		super.addBaseComponents();
		this.components.add(this.baseBowPulling0TextureMessage);
		this.components.add(this.baseBowPulling0TextureFileInput);
		this.components.add(this.baseBowPulling1TextureMessage);
		this.components.add(this.baseBowPulling1TextureFileInput);
		this.components.add(this.baseBowPulling2TextureMessage);
		this.components.add(this.baseBowPulling2TextureFileInput);
	}
	
	@Override
	protected void addColorComponents() {
		super.addColorComponents();
		this.colorComponents.add(this.colorBowPulling0TextureMessage);
		this.colorComponents.add(this.colorBowPulling0TextureFileInput);
		this.colorComponents.add(this.colorBowPulling1TextureMessage);
		this.colorComponents.add(this.colorBowPulling1TextureFileInput);
		this.colorComponents.add(this.colorBowPulling2TextureMessage);
		this.colorComponents.add(this.colorBowPulling2TextureFileInput);
	}
	
	@Override
	protected boolean checkFiles() {
		if (this.baseBowPulling0TextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.bow.base0.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.baseBowPulling1TextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.bow.base1.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.baseBowPulling2TextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.bow.base2.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		return super.checkFiles();
	}
	
	@Override
	protected void saveFiles() throws IOException {
		super.saveFiles();
		ItemModelManager.saveBowPullingTexture(this.addon, this.bowItem, this.baseBowPulling0TextureFileInput.getFile(), this.colorBowPulling0TextureFileInput.getFile(), ItemModelManager.MODEL_BOW_PULLING_0_ENDING);
		ItemModelManager.saveBowPullingTexture(this.addon, this.bowItem, this.baseBowPulling1TextureFileInput.getFile(), this.colorBowPulling1TextureFileInput.getFile(), ItemModelManager.MODEL_BOW_PULLING_1_ENDING);
		ItemModelManager.saveBowPullingTexture(this.addon, this.bowItem, this.baseBowPulling2TextureFileInput.getFile(), this.colorBowPulling2TextureFileInput.getFile(), ItemModelManager.MODEL_BOW_PULLING_2_ENDING);
		
		if (this.colorBowPulling0TextureFileInput.getFile() == null) {
			ItemModelManager.deleteBowPullingTexture(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_0_ENDING, true);
			ItemModelManager.deleteBowPullingTextureAnimation(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_0_ENDING, true);
		}
		
		if (this.colorBowPulling1TextureFileInput.getFile() == null) {
			ItemModelManager.deleteBowPullingTexture(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_1_ENDING, true);
			ItemModelManager.deleteBowPullingTextureAnimation(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_1_ENDING, true);
		}
		
		if (this.colorBowPulling2TextureFileInput.getFile() == null) {
			ItemModelManager.deleteBowPullingTexture(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_2_ENDING, true);
			ItemModelManager.deleteBowPullingTextureAnimation(addon, this.bowItem, ItemModelManager.MODEL_BOW_PULLING_2_ENDING, true);
		}
	}
	
	protected boolean checkBowPullingTexture(File fileChosen, String textureEnding, boolean isColor) {
		try {
			BufferedImage image = ImageIO.read(fileChosen);
			if (image == null) {
				throw new IOException("Couldn't read image.");
			}
			int imageWidth = image.getTileWidth();
			int imageHeight = image.getTileHeight();
			if ((imageWidth & (imageWidth - 1)) != 0 || ((imageHeight & (imageHeight - 1)) != 0 && imageHeight % imageWidth != 0)) {
				//Wrong image dimensions
				this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.notSidesx2.message"), I18n.format("gui.buttons.back")));
				return false;
			} else if (imageHeight % imageWidth != 0) {
				if (imageWidth > imageHeight) {
					//Width can't be larger than height
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.tooWide.message"), I18n.format("gui.buttons.back")));
				} else {
					//Bad animation
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.popup.texture.problem.wrongDimensions.badAnimation.message"), I18n.format("gui.buttons.back")));
				}
				return false;
			} else {
				if (imageWidth != imageHeight) {
					//Animation
					this.mc.displayGuiScreen(new GuiMessageBoxSelectItemBowAnimation(this, this.addon, this.bowItem, textureEnding, isColor));
				} else {
					ItemModelManager.deleteTextureAnimation(this.addon, this.item, isColor);
				}
				
				return true;
			}
		} catch (IOException e) {
			//Not a valid image?
			AdditionsMod.logger.warn("Error loading texture: ", e);
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.texture.problem.cantLoad.title"), new TextComponentTranslation("gui.popup.texture.problem.cantLoad.message", fileChosen.getName()), I18n.format("gui.buttons.back")));
		}
		
		return false;
	}
}
