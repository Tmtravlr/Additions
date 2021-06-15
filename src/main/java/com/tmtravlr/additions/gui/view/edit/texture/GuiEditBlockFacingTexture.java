package com.tmtravlr.additions.gui.view.edit.texture;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
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
public class GuiEditBlockFacingTexture extends GuiEditBlockTexture {

	protected GuiComponentDisplayText blockTextureMessage;
	protected GuiComponentDisplayText sideTextureMessage;
	protected GuiComponentFileInput sideTextureFileInput;
	protected GuiComponentDisplayText topTextureMessage;
	protected GuiComponentFileInput topTextureFileInput;

	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.update")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, backButtonText));
		this.buttonList.add(new GuiButton(BUTTON_CUSTOM_MODEL, this.width - 230, this.height - 30, 80, 20, I18n.format("gui.edit.texture.customModel")));
	}
	
	public GuiEditBlockFacingTexture(GuiScreen parentScreen, Addon addon, IBlockAdded block, boolean isNew) {
		super(parentScreen, addon, block, isNew, BlockModelManager.BlockModelType.FACING);
	}
	
	@Override
	public void initComponents() {
		this.blockTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.message.plural", this.block.getDisplayName()));
		
		this.baseTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.facing.front.message", this.block.getDisplayName()));
		
		this.baseTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.facing.front.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockFacingTexture.this.checkTexture(fileChosen, "");
			}
		};
		if (this.isNew) {
			this.baseTextureFileInput.setRequired();
		}

		this.topTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.block.facing.topBottom.message", this.block.getDisplayName()));
		
		this.topTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.block.facing.topBottom.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditBlockFacingTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_TOP_ENDING);
			}
		};
		if (this.isNew) {
			this.topTextureFileInput.setRequired();
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
				return GuiEditBlockFacingTexture.this.checkTexture(fileChosen, BlockModelManager.TEXTURE_SIDE_ENDING);
			}
		};
		if (this.isNew) {
			this.sideTextureFileInput.setRequired();
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
		this.components.add(this.topTextureMessage);
		this.components.add(this.topTextureFileInput);
	}
	
	@Override
	protected boolean checkFiles() {
		if (this.baseTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.front.label")), I18n.format("gui.buttons.back")));
			return false;
		}
		
		if (this.topTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.topBottom.label")), I18n.format("gui.buttons.back")));
			return false;
		}

		if (this.sideTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.specific.message", I18n.format("gui.edit.texture.block.facing.side.label")), I18n.format("gui.buttons.back")));
			return false;
		}

		return true;
	}
	
	@Override
	protected void saveFiles() throws IOException {
		super.saveFiles();
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.topTextureFileInput.getFile(), BlockModelManager.TEXTURE_TOP_ENDING);
		BlockModelManager.saveBlockTextureWithEnding(this.addon, this.block, this.sideTextureFileInput.getFile(), BlockModelManager.TEXTURE_SIDE_ENDING);
	}

}
