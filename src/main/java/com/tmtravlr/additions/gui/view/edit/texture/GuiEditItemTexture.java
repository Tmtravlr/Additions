package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxRefreshingResources;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectItemAnimation;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFileInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Lets you select a texture for a simple item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiEditItemTexture extends GuiEdit {
	
	protected final int BUTTON_CUSTOM_MODEL = this.buttonCount++;
	
	protected final String backButtonText;
	protected final Addon addon;
	protected final IItemAdded item;
	protected final ItemModelManager.ItemModelType itemTextureType;
	protected final boolean isNew;
	
	protected GuiComponentDisplayText baseTextureMessage;
	protected GuiComponentFileInput baseTextureFileInput;
	protected GuiComponentDisplayText colorTextureMessage;
	protected GuiComponentFileInput colorTextureFileInput;
	
	protected GuiComponentBooleanInput isColoredInput;
	
	protected List<IGuiViewComponent> colorComponents = new ArrayList<>();

	@Override
	public void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.update")));
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 140, this.height - 30, 60, 20, backButtonText));
		this.buttonList.add(new GuiButton(BUTTON_CUSTOM_MODEL, this.width - 230, this.height - 30, 80, 20, I18n.format("gui.edit.texture.customModel")));
	}
	
	public GuiEditItemTexture(GuiScreen parentScreen, Addon addon, IItemAdded item, boolean isNew) {
		this(parentScreen, addon, item, isNew, ItemModelManager.ItemModelType.SIMPLE);
	}

	public GuiEditItemTexture(GuiScreen parentScreen, Addon addon, IItemAdded item, boolean isNew, ItemModelManager.ItemModelType itemTextureType) {
		super(parentScreen, I18n.format("gui.edit.texture.item.title"));

		this.addon = addon;
		this.item = item;
		this.itemTextureType = itemTextureType;
		this.isNew = isNew;
		this.backButtonText = I18n.format(this.isNew ? "gui.edit.texture.addTexturesLater" : "gui.buttons.back");
	}
	
	@Override
	public void initComponents() {
		 this.baseTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.item.base.message", this.item.getDisplayName()));
		 
		 this.baseTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.item.base.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemTexture.this.checkTexture(fileChosen, false);
			}
		};
		if (this.isNew) {
			this.baseTextureFileInput.setRequired();
		}
		 
		 this.colorTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.item.color.message"));
		 
		 this.colorTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.item.color.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemTexture.this.checkTexture(fileChosen, true);
			}
		};
		
		this.isColoredInput = new GuiComponentBooleanInput(I18n.format("gui.edit.texture.item.colored.label"), this) {
			
			@Override
			protected void setBoolean(boolean input) {
				super.setBoolean(input);
				
				if (input) {
					GuiEditItemTexture.this.showColorComponents();
				} else {
					GuiEditItemTexture.this.hideColorComponents();
				}
			}
			
		};
		
		this.addBaseComponents();
		
		this.components.add(this.isColoredInput);
		
		this.addColorComponents();
		this.components.addAll(this.colorComponents);
		this.hideColorComponents();
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_CUSTOM_MODEL) {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this.parentScreen, I18n.format("gui.popup.model.title"), new TextComponentTranslation("gui.popup.model.item.message", ItemModelManager.getItemModelName(item)), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.goToFolder")) {

				@Override
				protected void onSecondButtonClicked() {
					File modelFolder = ItemModelManager.getItemModelFolder(GuiEditItemTexture.this.addon);
	
					if (!modelFolder.isDirectory()) {
						modelFolder.mkdirs();
					}

					CommonGuiUtils.openFolder(modelFolder);

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
						this.mc.displayGuiScreen(GuiEditItemTexture.this.parentScreen);
					}
					
				};
				
				if (this.isNew) {
					this.mc.displayGuiScreen(successScreen);
				} else {
					this.mc.displayGuiScreen(new GuiMessageBoxRefreshingResources(successScreen));
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
	
	protected void addColorComponents() {
		this.colorComponents.add(this.colorTextureMessage);
		this.colorComponents.add(this.colorTextureFileInput);
	}
	
	protected void showColorComponents() {
		this.colorComponents.forEach(component -> component.setHidden(false));
	}
	
	protected void hideColorComponents() {
		this.colorComponents.forEach(component -> component.setHidden(true));
	}
	
	protected boolean checkFiles() {
		if (this.baseTextureFileInput.getFile() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.title"), new TextComponentTranslation("gui.edit.texture.problem.noTexture.message"), I18n.format("gui.buttons.back")));
			return false;
		}
		
		return true;
	}
	
	protected void saveFiles() throws IOException {
		ItemModelManager.saveItemTexture(this.addon, this.item, this.baseTextureFileInput.getFile(), this.colorTextureFileInput.getFile(), this.itemTextureType);
		
		if (this.colorTextureFileInput.getFile() == null) {
			ItemModelManager.deleteItemTexture(addon, item, true);
			ItemModelManager.deleteTextureAnimation(addon, item, true);
		}
	}
	
	protected boolean checkTexture(File fileChosen, boolean isColor) {
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
				if (imageWidth != imageHeight) {
					//Animation
					this.mc.displayGuiScreen(new GuiMessageBoxSelectItemAnimation(this, this.addon, this.item, isColor));
				} else {
					ItemModelManager.deleteTextureAnimation(this.addon, this.item, isColor);
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

}
