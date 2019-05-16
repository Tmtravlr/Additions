package com.tmtravlr.additions.gui.view.edit;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectCauseInput;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;
import com.tmtravlr.additions.type.AdditionTypeEffect;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding an effect list or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class GuiEditEffectList extends GuiEdit {
	
	private Addon addon;
	
    private boolean isNew;
    private EffectList addition;
    private EffectList copyFrom;

    private GuiComponentStringInput idInput;
    private GuiComponentDisplayText whenMessage;
    private GuiComponentEffectCauseInput causeInput;
    private GuiComponentDisplayText thenMessage;
	private GuiComponentListInput<GuiComponentEffectInput> effectsInput;
    
	public GuiEditEffectList(GuiScreen parentScreen, String title, Addon addon, EffectList addition) {
		super(parentScreen, title);
		this.addon = addon;
		this.isNew = addition == null;
		if (this.isNew) {
			this.addition = new EffectList();
		} else {
			this.addition = addition;
		}
	}

	@Override
	public void initComponents() {
		
		this.idInput = new GuiComponentStringInput(I18n.format("gui.edit.effectList.id.label"), this);
		if (this.isNew) {
			this.idInput.setRequired();
			this.idInput.setMaxStringLength(32);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.idInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.idInput.setEnabled(false);
			this.idInput.setMaxStringLength(1024);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.idInput.setDefaultText(this.addition.id.toString());
		}
		
		this.whenMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.effectList.when.message"));
		
		this.causeInput = new GuiComponentEffectCauseInput(I18n.format("gui.edit.effectList.cause.label"), this.addon, this);
		this.causeInput.setRequired();
		if (!this.isNew) {
			this.causeInput.setDefaultEffectCause(this.addition.cause);
		}
		
		this.thenMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.effectList.then.message"));
		
		this.effectsInput = new GuiComponentListInput<GuiComponentEffectInput>(I18n.format("gui.edit.effectList.effects.label"), this) {

			@Override
			public GuiComponentEffectInput createBlankComponent() {
				GuiComponentEffectInput input = new GuiComponentEffectInput("", GuiEditEffectList.this.addon, this.editScreen);
				return input;
			}
			
		};
		this.effectsInput.setRequired();
		if (!this.isNew) {
			this.addition.effects.forEach(toAdd->{
				GuiComponentEffectInput input = this.effectsInput.createBlankComponent();
				input.setDefaultEffect(toAdd);
				this.effectsInput.addDefaultComponent(input);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.idInput);
		this.components.add(this.whenMessage);
		this.components.add(this.causeInput);
		this.components.add(this.thenMessage);
		this.components.add(this.effectsInput);
	}
	
	@Override
	public void saveObject() {
		ResourceLocation name = this.isNew ? new ResourceLocation(AdditionsMod.MOD_ID, this.addon.id + "-" + this.idInput.getText()) : this.addition.id;
		
		if (this.idInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effectList.problem.title"), new TextComponentTranslation("gui.edit.problem.noId", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && AdditionTypeEffect.INSTANCE.hasEffectWithId(this.addon, name)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effectList.problem.title"), new TextComponentTranslation("gui.edit.effectList.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (causeInput.getEffectCause() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effectList.problem.title"), new TextComponentTranslation("gui.edit.effectList.problem.noCause.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		List<Effect> effects = this.effectsInput.getComponents().stream().filter(component -> component.getEffect() != null).map(GuiComponentEffectInput::getEffect).collect(Collectors.toList());
		
		if (effects.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effectList.problem.title"), new TextComponentTranslation("gui.edit.effectList.problem.noEffects.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.addition.cause = this.causeInput.getEffectCause();
		this.addition.effects = effects;
		
		if (this.isNew) {
			this.addition.id = name;
		}
		
		AdditionTypeEffect.INSTANCE.saveAddition(this.addon, this.addition);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.edit.effectList.success.title"), new TextComponentTranslation("gui.edit.effectList.success.message"), I18n.format("gui.buttons.continue")));
	}
	
	public void copyFrom(EffectList copyFrom) {
		this.copyFrom = copyFrom;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	public void copyFromOther() {
		this.causeInput.setDefaultEffectCause(this.copyFrom.cause);
		
		this.effectsInput.removeAllComponents();
		this.copyFrom.effects.forEach(toAdd -> {
			GuiComponentEffectInput input = this.effectsInput.createBlankComponent();
			input.setDefaultEffect(toAdd);
			this.effectsInput.addDefaultComponent(input);
		});
		
		this.copyFrom = null;
	}

}
