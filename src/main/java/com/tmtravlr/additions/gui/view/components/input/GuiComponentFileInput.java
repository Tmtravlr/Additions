package com.tmtravlr.additions.gui.view.components.input;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectFile;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Allows you to select a file.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2018 
 */
public abstract class GuiComponentFileInput implements IGuiViewComponent {

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	public Addon addon;
	
	private int x;
	private int y;
	private int width;
	private boolean hidden = false;
	private String label = "";
	private boolean required = false;
	private FileNameExtensionFilter fileExtensions;
	
	private File file = null;
	
	public GuiComponentFileInput(GuiEdit editScreen, String label, Addon addon, FileNameExtensionFilter fileExtensions) {
		this.editScreen = editScreen;
		this.label = label;
		this.addon = addon;
		this.fileExtensions = fileExtensions;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public int getHeight(int left, int right) {
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;
		
		this.selectedText.x = x;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if (mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.setFile(null);
			}
		}
		
		if (mouseX >= this.selectedText.x - 30 && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			File defaultFolder = this.getFileDialogueFolder();

			JFileChooser chooser = CommonGuiUtils.createFileChooser(this.fileExtensions, defaultFolder);
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxSelectFile(this.editScreen, chooser, chooserState -> handleFileChosen(chooserState, chooser, defaultFolder)));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultFile(File file) {
		this.file = file;
		this.selectedText.setText(file == null ? "" : file.toString());
		this.selectedText.setCursorPositionZero();
	}
	
	public void setFile(File file) {
		this.editScreen.notifyHasChanges();
		this.setDefaultFile(file);
	}
	
	public File getFile() {
		return this.file;
	}
	
	protected void handleFileChosen(int chooserState, JFileChooser chooser, File defaultFolder) {
		if (chooserState == JFileChooser.APPROVE_OPTION) {
			File fileChosen = chooser.getSelectedFile();
			
			if (!fileChosen.getParentFile().equals(defaultFolder)) {
				this.setFileDialogueFolder(fileChosen.getParentFile());
			}
			
			if (this.validateFileChosen(fileChosen)) {
				this.setFile(fileChosen);
			}
		}
	}
	
	protected abstract File getFileDialogueFolder();
	
	protected abstract void setFileDialogueFolder(File folder);
	
	protected abstract boolean validateFileChosen(File fileChosen);

}
