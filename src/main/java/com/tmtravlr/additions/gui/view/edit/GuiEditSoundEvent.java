package com.tmtravlr.additions.gui.view.edit;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.sounds.SoundEventAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.sound.GuiComponentSoundInput;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a sound event or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since October 2018
 */
public class GuiEditSoundEvent extends GuiEdit {
	
	private Addon addon;
	
    private boolean isNew;
    private SoundEventAdded oldAddition;
    private SoundEventAdded copyFrom;

    private GuiComponentStringInput idInput;
    private GuiComponentStringInput subtitleInput;
    private GuiComponentBooleanInput replaceExistingInput;
	private GuiComponentListInput<GuiComponentSoundInput> soundListInput;
    
	public GuiEditSoundEvent(GuiScreen parentScreen, String title, Addon addon, SoundEventAdded addition) {
		super(parentScreen, title);
		this.addon = addon;
		this.isNew = addition == null;
		this.oldAddition = addition;
	}

	@Override
	public void initComponents() {
		
		this.idInput = new GuiComponentStringInput(I18n.format("gui.edit.soundEvent.id.label"), this);
		if (this.isNew) {
			this.idInput.setRequired();
			this.idInput.setMaxStringLength(32);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.idInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.idInput.setEnabled(false);
			this.idInput.setMaxStringLength(1024);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.idInput.setDefaultText(this.oldAddition.getRegistryName().toString());
		}
		
		this.subtitleInput = new GuiComponentStringInput(I18n.format("gui.edit.soundEvent.subtitle.label"), this);
		this.subtitleInput.setMaxStringLength(1024);
		if (!this.isNew) {
			this.subtitleInput.setDefaultText(this.oldAddition.getSoundList().getSubtitle());
		}
		
		this.replaceExistingInput = new GuiComponentBooleanInput(I18n.format("gui.edit.soundEvent.replace.label"), this);
		if (!this.isNew) {
			this.replaceExistingInput.setDefaultBoolean(this.oldAddition.getSoundList().canReplaceExisting());
		}
		
		this.soundListInput = new GuiComponentListInput<GuiComponentSoundInput>(I18n.format("gui.edit.soundEvent.sounds.label"), this) {

			@Override
			public GuiComponentSoundInput createBlankComponent() {
				GuiComponentSoundInput input = new GuiComponentSoundInput("", this.editScreen, GuiEditSoundEvent.this.addon);
				return input;
			}
			
		};
		this.soundListInput.setRequired();
		if (!this.isNew) {
			this.oldAddition.getSoundList().getSounds().forEach(toAdd -> {
				GuiComponentSoundInput input = this.soundListInput.createBlankComponent();
				input.setDefaultSound(toAdd);
				this.soundListInput.addDefaultComponent(input);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.idInput);
		this.components.add(this.subtitleInput);
		this.components.add(this.soundListInput);
		
		this.advancedComponents.add(this.replaceExistingInput);
	}
	
	@Override
	public void saveObject() {
		ResourceLocation location = this.isNew ? new ResourceLocation(AdditionsMod.MOD_ID, this.addon.id + "-" + this.idInput.getText()) : this.oldAddition.getRegistryName();
		
		if (this.idInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.soundEvent.problem.title"), new TextComponentTranslation("gui.edit.problem.noId", location), I18n.format("gui.buttons.back")));
			return;
		}
		
		List<Sound> sounds = this.soundListInput.getComponents().stream()
				.filter(soundComponent -> !StringUtils.isNullOrEmpty(soundComponent.getSound().getSoundLocation().getResourcePath()))
				.map(GuiComponentSoundInput::getSound).collect(Collectors.toList());
		if (sounds.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.soundEvent.problem.title"), new TextComponentTranslation("gui.edit.soundEvent.problem.noSounds", location), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && AdditionTypeSoundEvent.INSTANCE.hasSoundEventWithId(this.addon, location)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.soundEvent.problem.title"), new TextComponentTranslation("gui.edit.soundEvent.problem.duplicate", location), I18n.format("gui.buttons.back")));
			return;
		}
		
		SoundList soundList = new SoundList(sounds, this.replaceExistingInput.getBoolean(), this.subtitleInput.getText());
		
		SoundEventAdded soundEventAdded = new SoundEventAdded(location);
		soundEventAdded.setSoundList(soundList);

		if (!this.isNew) {
			AdditionTypeSoundEvent.INSTANCE.deleteAddition(this.addon, this.oldAddition);
		}
		AdditionTypeSoundEvent.INSTANCE.saveAddition(this.addon, soundEventAdded);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".title"), new TextComponentTranslation("gui.warnDialogue.restart." + (this.isNew ? "created" : "updated") + ".message")));
	}
	
	@Override
	public void refreshView() {
		List<Sound> sounds = this.soundListInput.getComponents().stream().map(GuiComponentSoundInput::getSound).collect(Collectors.toList());
		this.soundListInput.removeAllComponents();
		sounds.forEach(toAdd -> {
			GuiComponentSoundInput input = this.soundListInput.createBlankComponent();
			input.setDefaultSound(toAdd);
			this.soundListInput.addDefaultComponent(input);
		});
	}
	
	public void copyFrom(SoundEventAdded addition) {
		this.copyFrom = addition;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	public void copyFromOther() {
		this.soundListInput.removeAllComponents();
		this.copyFrom.getSoundList().getSounds().forEach(toAdd -> {
			GuiComponentSoundInput input = this.soundListInput.createBlankComponent();
			input.setDefaultSound(toAdd);
			this.soundListInput.addDefaultComponent(input);
		});
		
		this.copyFrom = null;
	}

}
