package com.tmtravlr.additions.gui.view.components.input.sound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.input.Mouse;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ClientConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxSelectItemAnimation;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.item.material.GuiEditArmorMaterial;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditSound;
import com.tmtravlr.additions.type.AdditionTypeFunction;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Lets you define a sound (either another sound event or a sound file).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date October 2018
 */
public class GuiComponentSoundInput implements IGuiViewComponent {

	private GuiEdit editScreen;
	private GuiTextField selectedText;
	
	private Sound sound;
	private Addon addon;
	
	private int x;
	private int y;
	private int width;
	private boolean hidden = false;
	private boolean required = false;
	private String label = "";
	
	public GuiComponentSoundInput(String label, GuiEdit editScreen, Addon addon) {
		this.editScreen = editScreen;
		this.label = label;
		
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
		
		this.addon = addon;
		this.setDefaultSound(null);
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
				this.setDefaultSound(null);
			}
		}
		
		if (mouseX >= this.selectedText.x && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiEditSound(this.editScreen, I18n.format("gui.edit.sound.title"), this, this.addon));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setDefaultSound(Sound sound) {
		this.sound = sound == null ? new Sound("", 1f, 1f, 1, null, false) : sound;
		if (!this.sound.getSoundLocation().getResourcePath().isEmpty()) {
			String typeLabel = I18n.format("gui.edit.sound." + (this.sound.getType() == Sound.Type.SOUND_EVENT ? "event" : "sound")  + ".label");
			this.selectedText.setText(typeLabel + ": " + this.sound.getSoundLocation().toString());
			this.selectedText.setCursorPositionZero();
		} else {
			this.selectedText.setText("");
		}
	}
	
	public void setSound(Sound sound) {
		this.setDefaultSound(sound);
		this.editScreen.notifyHasChanges();
	}
	
	public Sound getSound() {
		return this.sound;
	}
}
