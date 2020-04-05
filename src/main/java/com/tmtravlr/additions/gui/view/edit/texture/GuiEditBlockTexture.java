package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxReloadingResources;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectBlockAnimation;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlock;
import com.tmtravlr.additions.type.AdditionTypeBlock;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.BlockModelManager;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Lets you select a texture for a simple block.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class GuiEditBlockTexture extends GuiEdit {
	
	protected final int BUTTON_CUSTOM_MODEL = this.buttonCount++;
	
	protected final String backButtonText;
	protected final Addon addon;
	protected final IBlockAdded block;
	protected final BlockModelManager.BlockModelType blockTextureType;
	protected final boolean isNew;
	
	protected GuiComponentDisplayText baseTextureMessage;
	protected GuiComponentFileInput baseTextureFileInput;

	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.update")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, backButtonText));
		this.buttonList.add(new GuiButton(BUTTON_CUSTOM_MODEL, this.width - 230, this.height - 30, 80, 20, I18n.format("gui.edit.texture.customModel")));
	}
	
	public GuiEditBlockTexture(GuiScreen parentScreen, Addon addon, IBlockAdded block, boolean isNew) {
		this(parentScreen, addon, block, isNew, BlockModelManager.BlockModelType.SIMPLE);
	}

	public GuiEditBlockTexture(GuiScreen parentScreen, Addon addon, IBlockAdded block, boolean isNew, BlockModelManager.BlockModelType blockTextureType) {
		super(parentScreen, I18n.format("gui.edit.texture.block.title"));

		this.addon = addon;
		this.block = block;
		this.blockTextureType = blockTextureType;
		this.isNew = isNew;
		this.backButtonText = I18n.format(this.isNew ? "gui.edit.texture.addTexturesLater" : "gui.buttons.back");
	}
	
	@Override
	public void initComponents() {
		 this.baseTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.message", this.block.getDisplayName()));
		 
		 this.baseTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockTexture.this.checkTexture(fileChosen);
			}
		};
		if (this.isNew) {
			this.baseTextureFileInput.setRequired();
		}
		
		this.addBaseComponents();
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_CUSTOM_MODEL) {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this.parentScreen, I18n.format("gui.popup.model.title"), 
    				this.block.getItemBlock() == null ? new TextComponentTranslation("gui.popup.model.block.message", BlockModelManager.getBlockModelName(this.block)) : 
    					new TextComponentTranslation("gui.popup.model.block.withItem.message", BlockModelManager.getBlockModelName(this.block), ItemModelManager.getItemModelName(this.block.getItemBlock())), 
					I18n.format("gui.buttons.back"), I18n.format("gui.buttons.goToFolders")) {

				@Override
				protected void onSecondButtonClicked() {
					File blockStateFolder = BlockModelManager.getBlockStateFolder(GuiEditBlockTexture.this.addon);
					
					if (!blockStateFolder.isDirectory()) {
						blockStateFolder.mkdirs();
					}

					CommonGuiUtils.openFolder(blockStateFolder);
					
					if (GuiEditBlockTexture.this.block.getItemBlock() != null) {
						File modelFolder = ItemModelManager.getItemModelFolder(GuiEditBlockTexture.this.addon);
		
						if (!modelFolder.isDirectory()) {
							modelFolder.mkdirs();
						}
	
						CommonGuiUtils.openFolder(modelFolder);
					}

					super.onSecondButtonClicked();
				}
	
			});
    	} else {
    		super.actionPerformed(button);
    	}
    }

	@Override
	public void saveObject() {
		if (this.checkFiles()) {
			//Load image
			try {
				this.saveFiles();
				
				//Success
				GuiScreen successScreen = new GuiMessageBox(this, I18n.format("gui.edit.texture.success.title"), new TextComponentTranslation("gui.edit.texture.success.message"), I18n.format("gui.buttons.continue")) {
					
					@Override
					public void onFirstButtonClicked() {
						this.mc.displayGuiScreen(GuiEditBlockTexture.this.parentScreen);
					}
					
				};
				
				if (this.isNew || ConfigLoader.skipReloadingResources.getBoolean()) {
					this.mc.displayGuiScreen(successScreen);
				} else {
					this.mc.displayGuiScreen(new GuiMessageBoxReloadingResources(successScreen));
				}
			} catch (IOException e) {
				//Unable to load images
				AdditionsMod.logger.error("Error loading texture: ", e);
				this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.cantLoad.message"), I18n.format("gui.buttons.back")));
			}
		}
	}
	
	protected void addBaseComponents() {
		this.components.add(this.baseTextureMessage);
		this.components.add(this.baseTextureFileInput);
	}
	
	protected boolean checkFiles() {
		if (this.baseTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.message"), I18n.format("gui.buttons.back")));
			return false;
		}
		
		return true;
	}
	
	protected void saveFiles() throws IOException {
		BlockModelManager.saveBlockTexture(this.addon, this.block, this.baseTextureFileInput.getFile(), this.blockTextureType);
	}
	
	protected boolean checkTexture(File fileChosen) {
		return this.checkTexture(fileChosen, "");
	}
	
	protected boolean checkTexture(File fileChosen, String textureEnding) {
		try {
			BufferedImage image = ImageIO.read(fileChosen);
			if (image == null) {
				throw new IOException("Couldn't read image.");
			}
			int imageWidth = image.getTileWidth();
			int imageHeight = image.getTileHeight();
			
			if ((imageWidth & (imageWidth - 1)) != 0 || ((imageHeight & (imageHeight - 1)) != 0 && imageHeight % imageWidth != 0)) {
				//Wrong image dimensions
				this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.edit.texture.problem.wrongDimensions.notSidesx2.message"), I18n.format("gui.buttons.back")));
				return false;
			} else if (imageHeight % imageWidth != 0) {
				if (imageWidth > imageHeight) {
					//Width can't be larger than height
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.edit.texture.problem.wrongDimensions.tooWide.message"), I18n.format("gui.buttons.back")));
				} else {
					//Bad animation
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.edit.texture.problem.wrongDimensions.badAnimation.message"), I18n.format("gui.buttons.back")));
				}
				return false;
			} else {
				//Check for transparency/semi-transparency
				GuiScreen nextScreen = warnAboutAlpha(image);
			
				if (imageWidth != imageHeight) {
					//Animation
					nextScreen = new GuiMessageBoxSelectBlockAnimation(nextScreen == null ? this : nextScreen, this.addon, this.block, textureEnding);
				} else {
					BlockModelManager.deleteTextureAnimation(this.addon, this.block, textureEnding);
				}
				
				if (nextScreen != null) {
					this.mc.displayGuiScreen(nextScreen);
				}
				
				return true;
			}
		} catch (IOException e) {
			//Not a valid image?
			AdditionsMod.logger.warn("Error loading texture: ", e);
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.cantLoad.title"), new TextComponentTranslation("gui.edit.texture.problem.cantLoad.message", fileChosen.getName()), I18n.format("gui.buttons.back")));
		}
		
		return false;
	}
	
	/**
	 * Checks for partial or full alpha in the image and creates a warning screen
	 * that can automatically set the opacity/semi-transparency
	 */
	private GuiScreen warnAboutAlpha(BufferedImage image) {
		GuiScreen warningScreen = null;
		
		if (!this.isBlockSemiTransparent()) {
			boolean hasSemiTransparent = false;
			
			for (int x = 0; x < image.getTileWidth() && !hasSemiTransparent; x++) {
				for (int y = 0; y < image.getTileHeight() && !hasSemiTransparent; y++) {
					int alpha = image.getRGB(x, y) & 0xFF000000;
					
					if (alpha != 0 && alpha != 0xFF000000) {
						hasSemiTransparent = true;
					}
				}
			}
			
			if (hasSemiTransparent) {
				warningScreen = new GuiMessageBoxTwoButton(this, this, I18n.format("gui.edit.texture.block.problem.semiTransparent.title"), new TextComponentTranslation("gui.edit.texture.block.problem.semiTransparent.message"), I18n.format("gui.buttons.no"), I18n.format("gui.buttons.yes")) {
					
					@Override
				    protected void onSecondButtonClicked() {
						GuiEditBlockTexture.this.setBlockSemiTransparent();
				    	super.onSecondButtonClicked();
				    }
					
				};
			}
		}
		
		if (warningScreen == null && !this.isBlockTransparent()) {
			boolean hasEmptySpace = false;
			
			for (int x = 0; x < image.getTileWidth() && !hasEmptySpace; x++) {
				for (int y = 0; y < image.getTileHeight() && !hasEmptySpace; y++) {
					int alpha = image.getRGB(x, y) & 0xFF000000;
					
					if (alpha == 0) {
						hasEmptySpace = true;
					}
				}
			}
			
			if (hasEmptySpace) {
				warningScreen = new GuiMessageBoxTwoButton(this, this, I18n.format("gui.edit.texture.block.problem.transparent.title"), new TextComponentTranslation("gui.edit.texture.block.problem.transparent.message"), I18n.format("gui.buttons.no"), I18n.format("gui.buttons.yes")) {
						
					@Override
				    protected void onSecondButtonClicked() {
						GuiEditBlockTexture.this.setBlockTransparent();
				    	super.onSecondButtonClicked();
				    }
					
				};
			}
		}
		
		return warningScreen;
	}
	
	private boolean isBlockSemiTransparent() {
		boolean semiTransparent = true;
		
		if (this.isNew) {
			semiTransparent = block.isSemiTransparent();
		} else if (this.parentScreen instanceof GuiEditBlock) {
			semiTransparent = ((GuiEditBlock)this.parentScreen).isSemiTransparent();
		}
		
		return semiTransparent;
	}
	
	private boolean isBlockTransparent() {
		boolean transparent = true;
		
		if (this.isNew) {
			transparent = block.getOpacity() < 15;
		} else if (this.parentScreen instanceof GuiEditBlock) {
			transparent = ((GuiEditBlock)this.parentScreen).isTransparent();
		}
		
		return transparent;
	}
	
	private void setBlockSemiTransparent() {
		if (this.isNew) {
			this.block.setSemiTransparent(true);
			
			if (this.block.getOpacity() == 15) {
				this.block.getAsBlock().setLightOpacity(0);
			}
			
			AdditionTypeBlock.INSTANCE.saveAddition(addon, this.block);
		} else if (this.parentScreen instanceof GuiEditBlock) {
			((GuiEditBlock)this.parentScreen).setSemiTransparent();
		}
	}
	
	private void setBlockTransparent() {
		if (this.isNew) {
			this.block.getAsBlock().setLightOpacity(0);
			
			AdditionTypeBlock.INSTANCE.saveAddition(addon, this.block);
		} else if (this.parentScreen instanceof GuiEditBlock) {
			((GuiEditBlock)this.parentScreen).setTransparent();
		}
	}

}
