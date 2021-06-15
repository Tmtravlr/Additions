package com.tmtravlr.additions.gui.view.edit.update;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockStateInfoInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockStateValueInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlock;
import com.tmtravlr.additions.util.BlockStateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for updating a block state info.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2021
 */
public class GuiEditBlockStateInfo extends GuiEditUpdate {

    private final GuiComponentBlockStateInfoInput stateInfoInput;
	
	private GuiComponentDropdownInputBlock blockInput;
	private GuiComponentDisplayText valueListMessage;
	private GuiComponentListInput<GuiComponentBlockStateValueInput> valueListInput;
    
	public GuiEditBlockStateInfo(GuiScreen parentScreen, String title, GuiComponentBlockStateInfoInput stateInfoInput) {
		super(parentScreen, title);
		this.stateInfoInput = stateInfoInput;
	}
	
	@Override
	public void initComponents() {
		this.blockInput = new GuiComponentDropdownInputBlock(I18n.format("gui.edit.blockStateInfo.block.label"), this) {
			
			@Override
			public void setDefaultSelected(Block selected) {
				if (selected != this.getSelected()) {
					GuiEditBlockStateInfo.this.valueListInput.removeAllComponents();
					GuiEditBlockStateInfo.this.valueListMessage.setHidden(true);
					GuiEditBlockStateInfo.this.valueListInput.setHidden(true);
					
					if (!selected.getBlockState().getProperties().isEmpty()) {
						GuiEditBlockStateInfo.this.valueListMessage.setHidden(false);
						GuiEditBlockStateInfo.this.valueListInput.setHidden(false);
					}
				}
				
				super.setDefaultSelected(selected);
			}
			
		};
		
		this.valueListMessage = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.blockStateInfo.valueList.info"));
		this.valueListMessage.setHidden(true);
		
		this.valueListInput = new GuiComponentListInput<GuiComponentBlockStateValueInput>(I18n.format("gui.edit.blockStateInfo.valueList.label"), this) {

			@Override
			public GuiComponentBlockStateValueInput createBlankComponent() {
				GuiComponentBlockStateValueInput input = new GuiComponentBlockStateValueInput("", this.editScreen, GuiEditBlockStateInfo.this.blockInput.getSelected());
				return input;
			}
			
		};
		this.valueListInput.setHidden(true);
		
		if (stateInfoInput.getBlockStateInfo() != null) {
			this.blockInput.setDefaultSelected(stateInfoInput.getBlockStateInfo().getBlock());
		}
		
		if (this.stateInfoInput.getBlockStateInfo() != null) {
			this.stateInfoInput.getBlockStateInfo().getBlockState().getProperties().entrySet().forEach(entry -> {
				if (this.stateInfoInput.getBlockStateInfo().getStateMap().containsKey(entry.getKey().getName())) {
					GuiComponentBlockStateValueInput input = this.valueListInput.createBlankComponent();
					input.setDefaultKeyAndValue(entry.getKey(), entry.getValue());
					this.valueListInput.addDefaultComponent(input);
				}
			});
		}
		
		this.components.add(this.blockInput);
		this.components.add(this.valueListMessage);
		this.components.add(this.valueListInput);
	}
	
	@Override
	public void saveObject() {
		String soundName;
		
		if (this.blockInput.getSelected() == null) {
			this.stateInfoInput.setBlockStateInfo(null);
		} else {
			if (this.valueListInput.getComponents().isEmpty()) {
				this.stateInfoInput.setBlockStateInfo(new BlockStateInfo(this.blockInput.getSelected()));
			} else {
				Set<IProperty> duplicateProperties = this.valueListInput.getComponents().stream()
						.filter(component -> component != null && component.getKey() != null && component.getValue() != null)
						.map(component -> component.getKey()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
								.filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toSet());
				
				if (!duplicateProperties.isEmpty()) {
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.blockStateInfo.problem.title"), new TextComponentTranslation("gui.edit.blockStateInfo.problem.duplicateProperty.message", duplicateProperties.iterator().next().getName()), I18n.format("gui.buttons.back")));
        			return;
				}
				
				this.stateInfoInput.setBlockStateInfo(new BlockStateInfo(this.blockInput.getSelected(), this.valueListInput.getComponents().stream()
						.filter(component -> component != null && component.getKey() != null && component.getValue() != null)
						.collect(Collectors.toMap(component -> component.getKey().getName(), component -> component.getValue().toString()))));
			}
		}
		
		this.parentScreen.mc.displayGuiScreen(parentScreen);
	}

}
