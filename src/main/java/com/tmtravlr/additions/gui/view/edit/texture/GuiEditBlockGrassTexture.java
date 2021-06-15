package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlock;
import com.tmtravlr.additions.util.models.BlockModelManager;

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
public class GuiEditBlockGrassTexture extends GuiEditBlockTexture {

	protected GuiComponentDisplayText blockTextureMessage;
	protected GuiComponentDisplayText sideTextureMessage;
	protected GuiComponentDisplayText sideOverlayTextureMessage;
	protected GuiComponentDisplayText sideSnowyTextureMessage;
	protected GuiComponentDisplayText bottomTextureMessage;
	protected GuiComponentFileInput sideTextureFileInput;
	protected GuiComponentFileInput sideOverlayTextureFileInput;
	protected GuiComponentFileInput sideSnowyTextureFileInput;
	protected GuiComponentFileInput bottomTextureFileInput;

	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.update")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, backButtonText));
		this.buttonList.add(new GuiButton(BUTTON_CUSTOM_MODEL, this.width - 230, this.height - 30, 80, 20, I18n.format("gui.edit.texture.customModel")));
	}
	
	public GuiEditBlockGrassTexture(GuiScreen parentScreen, Addon addon, IBlockAdded block, boolean isNew) {
		super(parentScreen, addon, block, isNew, BlockModelManager.BlockModelType.GRASS);
	}
	
	@Override
	public void initComponents() {
		this.blockTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.message.plural", this.block.getDisplayName()));
		
		this.baseTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.grass.top.message", this.block.getDisplayName()));
		
		this.baseTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.facing.top.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockGrassTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_TOP_ENDING);
			}
		};
		if (this.isNew) {
			this.baseTextureFileInput.setRequired();
		}

		this.sideTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.facing.side.message", this.block.getDisplayName()));
		
		this.sideTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.facing.side.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockGrassTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_SIDE_ENDING);
			}
		};
		if (this.isNew) {
			this.sideTextureFileInput.setRequired();
		}
		
		this.sideOverlayTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.grass.overlay.message", this.block.getDisplayName()));
		
		this.sideOverlayTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.grass.overlay.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockGrassTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_SIDE_OVERLAY_ENDING);
			}
		};
		if (this.isNew) {
			this.sideOverlayTextureFileInput.setRequired();
		}

		this.sideSnowyTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.grass.snowy.message", this.block.getDisplayName()));
		
		this.sideSnowyTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.grass.snowy.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockGrassTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_SIDE_SNOWY_ENDING);
			}
		};

		this.bottomTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.facing.bottom.message", this.block.getDisplayName()));
		
		this.bottomTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.facing.bottom.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockGrassTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_BOTTOM_ENDING);
			}
		};
		if (this.isNew) {
			this.bottomTextureFileInput.setRequired();
		}
		
		this.addBaseComponents();
	}
	
	@Override
	protected void addBaseComponents() {
		this.components.add(this.blockTextureMessage);
		this.components.add(this.baseTextureMessage);
		this.components.add(this.baseTextureFileInput);
		this.components.add(this.sideTextureMessage);
		this.components.add(this.sideTextureFileInput);
		this.components.add(this.sideOverlayTextureMessage);
		this.components.add(this.sideOverlayTextureFileInput);
		this.components.add(this.sideSnowyTextureMessage);
		this.components.add(this.sideSnowyTextureFileInput);
		this.components.add(this.bottomTextureMessage);
		this.components.add(this.bottomTextureFileInput);
	}
	
	@Override
	protected boolean checkFiles() {
		if (this.baseTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.top.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.sideTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.side.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.sideOverlayTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.grass.overlay.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.bottomTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.bottom.label")), I18n.format("gui.buttons.back")));
			return false;
		}

		return true;
	}
	
	/**
	 * Override this so it only checks for semi-transparent textures, since the cutout textures are always enabled
	 */
	@Override
	protected boolean isBlockTransparent() {
		return true;
	}
	
	@Override
	protected void saveFiles() throws IOException {
		super.saveFiles();
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.sideTextureFileInput.getFile(), BlockModelManager.TEXTURE_SIDE_ENDING);
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.sideOverlayTextureFileInput.getFile(), BlockModelManager.TEXTURE_SIDE_OVERLAY_ENDING);
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.sideSnowyTextureFileInput.getFile() == null ? this.sideTextureFileInput.getFile() : this.sideSnowyTextureFileInput.getFile(), BlockModelManager.TEXTURE_SIDE_SNOWY_ENDING);
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.bottomTextureFileInput.getFile(), BlockModelManager.TEXTURE_BOTTOM_ENDING);
	}

}
