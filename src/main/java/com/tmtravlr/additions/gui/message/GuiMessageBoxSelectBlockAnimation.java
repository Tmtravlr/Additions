package com.tmtravlr.additions.gui.message;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.BlockModelManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a popup message to select an animation for an animated texture.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class GuiMessageBoxSelectBlockAnimation extends GuiMessageBoxTwoButton {

	protected Addon addon;
	protected IBlockAdded block;
	protected String textureEnding;

	public GuiMessageBoxSelectBlockAnimation(GuiScreen parentScreen, Addon addon, IBlockAdded block) {
		this(parentScreen, addon, block, "");
	}

	public GuiMessageBoxSelectBlockAnimation(GuiScreen parentScreen, Addon addon, IBlockAdded block, String textureEnding) {
		super(parentScreen, parentScreen, I18n.format("gui.popup.texture.animation.title"), new TextComponentTranslation("gui.popup.texture.animation.message", block.getDisplayName()), I18n.format("gui.popup.texture.animation.button.generate"), I18n.format("gui.buttons.selectFile"));

		this.addon = addon;
		this.block = block;
		this.textureEnding = textureEnding;
	}
	
	@Override
	protected void onFirstButtonClicked() {
		try {
			this.saveTextureAnimation(null);
		} catch (IOException e) {
			AdditionsMod.logger.error("Unable to generate animation for block " + this.block.getId() + " for addon " + this.addon.id, e);
		}
		
		this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.texture.animation.generated.title"), new TextComponentTranslation("gui.popup.texture.animation.generated.message"), I18n.format("gui.buttons.continue")));
	}
	
	@Override
	protected void onSecondButtonClicked() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MCMETA File", "mcmeta", "txt");
		File defaultFolder = ClientConfigLoader.getFileDialogueFolderTextures();

		JFileChooser chooser = CommonGuiUtils.createFileChooser(filter, defaultFolder);
		this.mc.displayGuiScreen(new GuiMessageBoxSelectFile(this, chooser, chooserState -> handleFileChosen(chooserState, chooser, defaultFolder)));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}
	
	protected void saveTextureAnimation(File fileChosen) throws IOException {
		BlockModelManager.saveTextureAnimation(this.addon, this.block, this.textureEnding, fileChosen);
	}
	
	protected void handleFileChosen(int chooserOption, JFileChooser chooser, File defaultFolder) {
		if (chooserOption == JFileChooser.APPROVE_OPTION) {
			File fileChosen = chooser.getSelectedFile();
			
			if (!fileChosen.getParentFile().equals(defaultFolder)) {
				ClientConfigLoader.setFileDialogueFolderTextures(fileChosen.getParentFile());
			}
			
			try {
				this.saveTextureAnimation(fileChosen);
				
				this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.texture.animation.loaded.title"), new TextComponentTranslation("gui.popup.texture.animation.loaded.message"), I18n.format("gui.buttons.continue")));
			} catch (IOException e) {
				AdditionsMod.logger.error("Unable to save animation for block " + this.block.getId() + " for addon " + this.addon.id, e);
				this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.popup.texture.animation.problem.title"), new TextComponentTranslation("gui.popup.texture.animation.problem.message"), I18n.format("gui.buttons.continue")));
			}
		}
	}
}
