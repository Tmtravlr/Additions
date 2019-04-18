package com.tmtravlr.additions.gui.view.edit;

import java.util.ArrayList;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a creative tab or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017
 */
public class GuiEditCreativeTab extends GuiEdit {
	
	private Addon addon;
	
    private boolean isNew;
    private CreativeTabAdded oldTab;
    private CreativeTabAdded copyFrom;

    private GuiComponentStringInput idInput;
	private GuiComponentStringInput nameInput;
	private GuiComponentBooleanInput hasSearchBarInput;
	private GuiComponentItemStackInput iconInput;
	private GuiComponentListInput<GuiComponentItemStackInput> itemListInput;
    
	public GuiEditCreativeTab(GuiScreen parentScreen, String title, Addon addon, CreativeTabAdded tab) {
		super(parentScreen, title);
		this.addon = addon;
		this.isNew = tab == null;
		this.oldTab = tab;
	}

	@Override
	public void initComponents() {
		
		this.idInput = new GuiComponentStringInput(I18n.format("gui.edit.creativeTab.id.label"), this);
		if (this.isNew) {
			this.idInput.setRequired();
			this.idInput.setMaxStringLength(32);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.idInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.idInput.setEnabled(false);
			this.idInput.setMaxStringLength(1024);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.idInput.setDefaultText(this.oldTab.id);
		}
		
		this.nameInput = new GuiComponentStringInput(I18n.format("gui.edit.creativeTab.name.label"), this);
		this.nameInput.setRequired();
		this.nameInput.setMaxStringLength(128);
		this.nameInput.setHasColorSelect();
		if (!this.isNew) {
			this.nameInput.setDefaultText(this.oldTab.getTabLabel());
		}
		
		this.hasSearchBarInput = new GuiComponentBooleanInput(I18n.format("gui.edit.creativeTab.hasSearchBar.label"), this);
		if (!this.isNew) {
			this.hasSearchBarInput.setDefaultBoolean(this.oldTab.hasSearchBar);
		}
		
		this.iconInput = new GuiComponentItemStackInput(I18n.format("gui.edit.creativeTab.icon.label"), this);
		this.iconInput.disableCount();
		if (!this.isNew) {
			this.iconInput.setDefaultItemStack(this.oldTab.getIconItemStack());
		}
		
		this.itemListInput = new GuiComponentListInput<GuiComponentItemStackInput>(I18n.format("gui.edit.creativeTab.items.label"), this) {

			@Override
			public GuiComponentItemStackInput createBlankComponent() {
				GuiComponentItemStackInput input = new GuiComponentItemStackInput("", this.editScreen);
				input.disableCount();
				return input;
			}
			
		};
		this.itemListInput.setRequired();
		if (!this.isNew) {
			this.oldTab.getDisplayItems().forEach(toAdd->{
				GuiComponentItemStackInput input = this.itemListInput.createBlankComponent();
				input.setDefaultItemStack(toAdd);
				this.itemListInput.addDefaultComponent(input);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.idInput);
		this.components.add(this.nameInput);
		this.components.add(this.hasSearchBarInput);
		this.components.add(this.iconInput);
		this.components.add(this.itemListInput);
	}
	
	@Override
	public void saveObject() {
		String name = this.isNew ? this.addon.id + "-" + this.idInput.getText() : this.oldTab.id;
		String displayName = this.nameInput.getText();
		
		if (this.idInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.creativeTab.problem.title"), new TextComponentTranslation("gui.edit.problem.noId", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (displayName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.creativeTab.problem.title"), new TextComponentTranslation("gui.edit.problem.noName", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.itemListInput.getComponents().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.creativeTab.problem.title"), new TextComponentTranslation("gui.edit.creativeTab.problem.noItems", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && AdditionTypeCreativeTab.INSTANCE.hasCreativeTabWithId(this.addon, name)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.creativeTab.problem.title"), new TextComponentTranslation("gui.edit.creativeTab.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		int oldIndex = -1;
		if (!this.isNew) {
			oldIndex = this.oldTab.getTabIndex();
		}
		
		CreativeTabAdded tab = oldIndex < 0 ? new CreativeTabAdded(displayName) : new CreativeTabAdded(oldIndex, displayName);
		tab.setId(name);
		
		if (this.hasSearchBarInput.getBoolean()) {
			tab.setHasSearch();
		}
		
		tab.setCustomTabItemStack(this.iconInput.getItemStack());
		
		NonNullList<ItemStack> items = NonNullList.create();
		this.itemListInput.getComponents().forEach(itemInput -> items.add(itemInput.getItemStack()));
		tab.setDisplayItems(items);

		if (!this.isNew) {
			AdditionTypeCreativeTab.INSTANCE.deleteAddition(this.addon, this.oldTab);
		}
		AdditionTypeCreativeTab.INSTANCE.saveAddition(this.addon, tab);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.edit.creativeTab.success.title"), new TextComponentTranslation("gui.edit.creativeTab.success.message"), I18n.format("gui.buttons.continue")));
	}
	
	@Override
	public void refreshView() {
		ItemStack icon = this.iconInput.getItemStack();
		this.iconInput = new GuiComponentItemStackInput("gui.edit.creativeTab.icon.label", this);
		this.iconInput.setDefaultItemStack(icon);
		
		ArrayList<ItemStack> currentItems = new ArrayList<>();
		this.itemListInput.getComponents().forEach(itemInput -> currentItems.add(itemInput.getItemStack()));
		this.itemListInput.removeAllComponents();
		currentItems.forEach(toAdd->{
			GuiComponentItemStackInput input = this.itemListInput.createBlankComponent();
			input.setDefaultItemStack(toAdd);
			this.itemListInput.addDefaultComponent(input);
		});
	}
	
	public void copyFrom(CreativeTabAdded tab) {
		this.copyFrom = tab;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	public void copyFromOther() {
		this.nameInput.setDefaultText(this.copyFrom.getTabLabel());
		this.hasSearchBarInput.setDefaultBoolean(this.copyFrom.hasSearchBar);
		this.iconInput.setDefaultItemStack(this.copyFrom.getIconItemStack());
		
		this.itemListInput.removeAllComponents();
		this.copyFrom.getDisplayItems().forEach(toAdd->{
			GuiComponentItemStackInput input = this.itemListInput.createBlankComponent();
			input.setDefaultItemStack(toAdd);
			this.itemListInput.addDefaultComponent(input);
		});
		
		this.copyFrom = null;
	}

}
