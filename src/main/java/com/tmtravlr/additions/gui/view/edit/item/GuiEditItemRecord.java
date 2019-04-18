package com.tmtravlr.additions.gui.view.edit.item;

import java.util.ArrayList;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedRecord;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a new record or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017
 */
public class GuiEditItemRecord extends GuiEditItem<ItemAddedRecord> {

    private GuiComponentSuggestionInput itemMusicInput;
	private GuiComponentStringInput itemDescriptionInput;
    
	public GuiEditItemRecord(GuiScreen parentScreen, String title, Addon addon, ItemAddedRecord item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedRecord();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemMusicInput = new GuiComponentSuggestionInput(I18n.format("gui.edit.item.record.music.label"), this);
		ArrayList<String> soundNames = new ArrayList<>();
		SoundEvent.REGISTRY.forEach(s -> soundNames.add(s.getRegistryName().toString()));
		this.itemMusicInput.setSuggestions(soundNames);
		if (!this.isNew) {
			this.itemMusicInput.setDefaultText(this.item.getMusic().getRegistryName().toString());
		}
		
		this.itemDescriptionInput = new GuiComponentStringInput(I18n.format("gui.edit.item.record.description.label"), this);
		this.itemDescriptionInput.setRequired();
		this.itemDescriptionInput.setMaxStringLength(32);
		this.itemDescriptionInput.setInfo(new TextComponentTranslation("gui.edit.item.record.description.info"));
		this.itemDescriptionInput.setHasColorSelect();
		if (!this.isNew) {
			this.itemDescriptionInput.setDefaultText(this.item.description);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemMusicInput);
		this.components.add(this.itemDescriptionInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		if (this.itemMusicInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.record.problem.noMusic"), I18n.format("gui.buttons.back")));
			return;
		}
		
        SoundEvent music = SoundEvents.AMBIENT_CAVE;
        
        ResourceLocation soundEventName = new ResourceLocation(this.itemMusicInput.getText());
    	music = SoundEvent.REGISTRY.getObject(soundEventName);
    	if (music == null) {
    		music = new SoundEvent(soundEventName).setRegistryName(soundEventName);
    		ForgeRegistries.SOUND_EVENTS.register(music);
    	}
    	
    	this.item.setMusic(music);
		
		this.item.description = this.itemDescriptionInput.getText();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemMusicInput.setDefaultText(this.copyFrom.getMusic().getRegistryName().toString());
		this.itemDescriptionInput.setDefaultText(this.copyFrom.description);
		
		super.copyFromOther();
	}
}
