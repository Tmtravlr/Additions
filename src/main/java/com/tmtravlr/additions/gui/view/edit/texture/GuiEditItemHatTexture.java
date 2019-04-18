package com.tmtravlr.additions.gui.view.edit.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedHat;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
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
public class GuiEditItemHatTexture extends GuiEditItemTexture {
	
	protected GuiComponentDisplayText hatOverlayTextureMessage;
	protected GuiComponentFileInput hatOverlayTextureFileInput;
	
	protected GuiComponentBooleanInput hasOverlayInput;
	
	protected List<IGuiViewComponent> hatOverlayComponents;
	
	private final ItemAddedHat hatItem;
	
	public GuiEditItemHatTexture(GuiScreen parentScreen, Addon addon, ItemAddedHat item, boolean isNew) {
		super(parentScreen, addon, item, isNew, ItemModelManager.ItemModelType.HAT);
		this.hatItem = item;
	}
	
	@Override
	public void initComponents() {
		super.initComponents();
		
		 this.hatOverlayTextureMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.texture.hatOverlay.message"));
		 
		 this.hatOverlayTextureFileInput = new GuiComponentFileInput(this, I18n.format("gui.edit.texture.hatOverlay.label"), this.addon, new FileNameExtensionFilter("PNG File", "png")) {
			
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
				return GuiEditItemHatTexture.this.checkHatOverlayTexture(fileChosen, false);
			}
		};
		
		this.hasOverlayInput = new GuiComponentBooleanInput(I18n.format("gui.edit.texture.hatOverlay.hasHatOverlay.input"), this) {
			
			@Override
			protected void setBoolean(boolean input) {
				super.setBoolean(input);
				
				if (input) {
					GuiEditItemHatTexture.this.showHatOverlayComponents();
				} else {
					GuiEditItemHatTexture.this.hideHatOverlayComponents();
				}
			}
			
		};
		
		this.components.add(this.hasOverlayInput);
		
		this.hatOverlayComponents.add(this.hatOverlayTextureMessage);
		this.hatOverlayComponents.add(this.hatOverlayTextureFileInput);
		this.components.addAll(this.hatOverlayComponents);
		this.hideHatOverlayComponents();
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_CUSTOM_MODEL) {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
    		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this.parentScreen, I18n.format("gui.popup.model.title"), new TextComponentTranslation("gui.popup.model.item.hatOverlay.message", ItemModelManager.getItemModelName(item), ItemModelManager.getHatOverlayTextureName(item)), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.goToFolder")) {

				@Override
				protected void onSecondButtonClicked() {
					File modelFolder = ItemModelManager.getItemModelFolder(GuiEditItemHatTexture.this.addon);
	
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
	protected void saveFiles() throws IOException {
		super.saveFiles();
		
		if (this.hatOverlayTextureFileInput.getFile() != null) {
			ItemModelManager.saveHatOverlayTexture(this.addon, this.hatItem, this.hatOverlayTextureFileInput.getFile());
		} else {
			ItemModelManager.deleteHatOverlayTexture(this.addon, this.hatItem);
		}
	}
	
	protected void showHatOverlayComponents() {
		this.hatOverlayComponents.forEach(component -> component.setHidden(false));
	}
	
	protected void hideHatOverlayComponents() {
		this.hatOverlayComponents.forEach(component -> component.setHidden(true));
	}
	
	protected boolean checkHatOverlayTexture(File fileChosen, boolean isColor) {
		try {
			BufferedImage image = ImageIO.read(fileChosen);
			if (image == null) {
				throw new IOException("Couldn't read image.");
			}
			int imageWidth = image.getTileWidth();
			int imageHeight = image.getTileHeight();
			if (imageWidth != imageHeight) {
				//Not square
				this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.texture.problem.wrongDimensions.title"), new TextComponentTranslation("gui.edit.texture.hatOverlay.problem.wrongDimensions.notSquare.message"), I18n.format("gui.buttons.back")));
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
