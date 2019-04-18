package com.tmtravlr.additions.gui.view.components.input.sound;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.input.Mouse;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectFile;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditSound;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiMessageBoxSelectSoundFile extends GuiMessageBoxTwoButton {
	
	private final String selectSoundLabel = I18n.format("gui.popup.selectSound.title");
	private final GuiEditSound editSoundScreen;
	private final Addon addon;

	private GuiComponentDisplayText selectSoundMessageId;
    private GuiComponentStringInput idInput;
	private GuiComponentDisplayText selectSoundMessageSelect;
	
	private ResourceLocation soundFileLocation;
	private List<ResourceLocation> soundFileLocations;

	private int currentComponentsHeight = 0;

	public GuiMessageBoxSelectSoundFile(GuiEditSound editSoundScreen, List<ResourceLocation> soundFileLocations, Addon addon) {
		super(editSoundScreen, editSoundScreen, I18n.format("gui.popup.selectSound.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.selectFile"));
		this.soundFileLocations = soundFileLocations;
		this.editSoundScreen = editSoundScreen;
		this.addon = addon;
		
		this.selectSoundMessageId = new GuiComponentDisplayText(editSoundScreen, new TextComponentTranslation("gui.popup.selectSound.message.id"));
		
		this.idInput = new GuiComponentStringInput(I18n.format("gui.popup.selectSound.id.label"), editSoundScreen);
		this.idInput.setRequired();
		this.idInput.setMaxStringLength(32);
		this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
		this.idInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		
		this.selectSoundMessageSelect = new GuiComponentDisplayText(editSoundScreen, new TextComponentTranslation("gui.popup.selectSound.message.select"));
		
		this.setViewScreen(editSoundScreen);
	}
	
	@Override
    protected int getPopupWidth() {
    	return 350;
    }
	
    @Override
    protected int getPopupHeight() {
    	return this.getComponentsHeight() + 90;
    }

    @Override
    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
		super.drawScreenOverlay(mouseX, mouseY, partialTicks);
		
		int popupWidth = this.getPopupWidth();
    	int popupHeight = this.getPopupHeight();
    	int popupX = this.width / 2 - popupWidth / 2;
    	int popupY = this.height / 2 - popupHeight / 2;
    	int popupRight = popupX + popupWidth;
		
		int componentY = popupY + 40;
		int labelOffset = 80;
		
    	this.selectSoundMessageId.drawInList(popupX - 10 -  GuiView.LABEL_OFFSET / 2, componentY, popupRight, mouseX, mouseY);
		componentY += this.selectSoundMessageId.getHeight(popupX, popupRight);

		this.drawString(this.fontRenderer, this.idInput.getLabel(), popupX + 20, componentY + this.idInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
    	this.idInput.drawInList(popupX + GuiView.LABEL_OFFSET / 2, componentY, popupRight, mouseX, mouseY);
		componentY += this.idInput.getHeight(popupX, popupRight);
		
    	this.selectSoundMessageSelect.drawInList(popupX - 10 -  GuiView.LABEL_OFFSET / 2, componentY, popupRight, mouseX, mouseY);
		componentY += this.selectSoundMessageSelect.getHeight(popupX, popupRight);
		
		this.drawPostRender();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	this.selectSoundMessageId.onMouseClicked(mouseX, mouseY, mouseButton);
    	this.idInput.onMouseClicked(mouseX, mouseY, mouseButton);
    	this.selectSoundMessageSelect.onMouseClicked(mouseX, mouseY, mouseButton);
    	
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
    	this.selectSoundMessageId.onHandleMouseInput(mouseX, mouseY);
    	this.idInput.onHandleMouseInput(mouseX, mouseY);
    	this.selectSoundMessageSelect.onHandleMouseInput(mouseX, mouseY);
	}

    @Override
    public void keyTyped(char keyTyped, int keyCode) throws IOException {
    	this.selectSoundMessageId.onKeyTyped(keyTyped, keyCode);
    	this.idInput.onKeyTyped(keyTyped, keyCode);
    	this.selectSoundMessageSelect.onKeyTyped(keyTyped, keyCode);

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
    }

	@Override
    protected void onSecondButtonClicked() {
		ResourceLocation location = new ResourceLocation(AdditionsMod.MOD_ID, this.idInput.getText());
		
		if (this.idInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.selectSound.problem.title"), new TextComponentTranslation("gui.edit.problem.noId"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.soundFileLocations.contains(location)) {
			this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this, I18n.format("gui.popup.selectSound.duplicate.title"), new TextComponentTranslation("gui.edit.soundEvent.problem.duplicate", location), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.replace")) {
				
				@Override
				public void onSecondButtonClicked() {
					super.onSecondButtonClicked();
					GuiMessageBoxSelectSoundFile.this.loadSound(location);
				}
				
			});
			return;
		}
		
		this.loadSound(location);
    }
    
    private int getComponentsHeight() {
    	int popupWidth = this.getPopupWidth();
    	int popupLeft = this.width / 2 - popupWidth / 2;
    	int popupRight = popupLeft + popupWidth;
    	
    	int height = this.selectSoundMessageId.getHeight(popupLeft, popupRight);
    	height += this.idInput.getHeight(popupLeft, popupRight);
    	height += this.selectSoundMessageSelect.getHeight(popupLeft, popupRight);
    	
    	if (height != this.currentComponentsHeight) {
    		this.currentComponentsHeight = height;
    		this.buttonList.clear();
    		this.initGui();
    	}
    	
    	return height;
    }
	
	private void loadSound(ResourceLocation location) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("OGG Sounds", "ogg");
		File defaultFolder = ClientConfigLoader.getFileDialogueFolderSounds();

		JFileChooser chooser = CommonGuiUtils.createFileChooser(filter, defaultFolder);
		this.mc.displayGuiScreen(new GuiMessageBoxSelectFile(this, chooser, chooserState -> handleFileChosen(chooserState, chooser, defaultFolder, location)));
	}
	
	private void handleFileChosen(int chooserOption, JFileChooser chooser, File defaultFolder, ResourceLocation location) {
		if (chooserOption == JFileChooser.APPROVE_OPTION) {
			File fileChosen = chooser.getSelectedFile();
			
			if (!fileChosen.getParentFile().equals(defaultFolder)) {
				ClientConfigLoader.setFileDialogueFolderSounds(fileChosen.getParentFile());
			}
			
			if (fileChosen.exists()) {
				try {
					AdditionTypeSoundEvent.INSTANCE.saveSoundFile(this.addon, fileChosen, location);
					this.editSoundScreen.selectSoundFile(location);
					this.editSoundScreen.refreshView();
					
					this.mc.displayGuiScreen(this.editSoundScreen);
				} catch (IOException e) {
					AdditionsMod.logger.warn("Error saving sound file " + location + " for addon " + this.addon.id + ".", e);
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.popup.selectSound.problem.title"), new TextComponentTranslation("gui.popup.selectSound.problem.message", e.getMessage()), I18n.format("gui.buttons.back")));
				}
			}
		}
	}
}
