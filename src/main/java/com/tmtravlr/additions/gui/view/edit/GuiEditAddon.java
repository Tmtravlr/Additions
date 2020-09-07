package com.tmtravlr.additions.gui.view.edit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.functions.FunctionAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.type.AdditionTypeFunction;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * Page for adding a new addon or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public class GuiEditAddon extends GuiEdit {
	
	/** These filenames are known to be restricted on one or more OS's. */
    public static final String[] DISALLOWED_FILENAMES = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    private boolean isNew;
    private Addon addon;
    
	private GuiComponentStringInput addonIdInput;
	private GuiComponentStringInput addonNameInput;
	private GuiComponentStringInput addonAuthorInput;
	private GuiComponentItemStackInput addonItemIcon;
	private GuiComponentDisplayText loadOrderInfo;
	private GuiComponentListInput<GuiComponentDropdownInput<String>> dependencies;
	private GuiComponentListInput<GuiComponentDropdownInput<String>> dependents;
	private GuiComponentDisplayText requiredInfo;
	private GuiComponentDisplayText requiredWarning;
	private GuiComponentListInput<GuiComponentSuggestionInput> requiredMods;
	private GuiComponentListInput<GuiComponentSuggestionInput> requiredAddons;
	private GuiComponentSuggestionInput addonLoopFunction;

	public GuiEditAddon(GuiScreen parentScreen, String title) {
		this(parentScreen, title, null);
	}
	
	public GuiEditAddon(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title);
		this.isNew = addon == null;
		
		if (this.isNew) {
			this.addon = new Addon();
		} else {
			this.addon = addon;
		}
	}
	
	@Override
	public void initComponents() {
		final String addonId = this.addon.id;
		
		this.addonIdInput = new GuiComponentStringInput(I18n.format("gui.edit.addon.addonId.label"), this);
		if (this.isNew) {
			this.addonIdInput.setRequired();
			this.addonIdInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.addonIdInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.addonIdInput.setEnabled(false);
			this.addonIdInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
		}
		this.addonIdInput.setMaxStringLength(32);
		this.addonIdInput.setDefaultText(this.addon.id);
		
		
		this.addonNameInput = new GuiComponentStringInput(I18n.format("gui.edit.addon.addonName.label"), this);
		this.addonNameInput.setRequired();
		this.addonNameInput.setMaxStringLength(64);
		this.addonNameInput.setHasColorSelect();
		this.addonNameInput.setDefaultText(this.addon.name);
		
		if (!this.isNew) {
			this.addonAuthorInput = new GuiComponentStringInput(I18n.format("gui.edit.addon.addonAuthor.label"), this);
			this.addonAuthorInput.setMaxStringLength(32);
			this.addonAuthorInput.setDefaultText(this.addon.author);
		}
		
		this.addonItemIcon = new GuiComponentItemStackInput(I18n.format("gui.edit.addon.addonIcon.label"), this);
		this.addonItemIcon.disableCount();
		this.addonItemIcon.disableMetadata();
		this.addonItemIcon.disableTag();
		if (!this.addon.logoItem.isEmpty()) {
			this.addonItemIcon.setDefaultItemStack(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(this.addon.logoItem))));
		}
		
		this.loadOrderInfo = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.addon.loadOrder.info"));

		this.dependencies = new GuiComponentListInput<GuiComponentDropdownInput<String>>(I18n.format("gui.edit.addon.dependencies.label"), this) {

			@Override
			public GuiComponentDropdownInput<String> createBlankComponent() {
				GuiComponentDropdownInput<String> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				for (Addon addon : AddonLoader.addonsLoaded) {
					if (!addon.id.equals(addonId)) {
						dropdown.addSelection(addon.id);
					}
				}
				dropdown.addSelection("*");
				return dropdown;
			}
			
		};
		this.addon.dependencies.forEach(selected->{
			GuiComponentDropdownInput<String> input = this.dependencies.createBlankComponent();
			input.setDefaultSelected(selected);
			this.dependencies.addDefaultComponent(input);
		});

		this.dependents = new GuiComponentListInput<GuiComponentDropdownInput<String>>(I18n.format("gui.edit.addon.dependents.label"), this) {

			@Override
			public GuiComponentDropdownInput<String> createBlankComponent() {
				GuiComponentDropdownInput<String> dropdown = new GuiComponentDropdownInput<>("", this.editScreen);
				for (Addon addon : AddonLoader.addonsLoaded) {
					if (!addon.id.equals(addonId)) {
						dropdown.addSelection(addon.id);
					}
				}
				dropdown.addSelection("*");
				return dropdown;
			}
			
		};
		this.addon.dependents.forEach(selected->{
			GuiComponentDropdownInput<String> input = this.dependents.createBlankComponent();
			input.setDefaultSelected(selected);
			this.dependents.addDefaultComponent(input);
		});
		
		this.requiredInfo = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.addon.required.info"));
		this.requiredWarning = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.addon.required.warning").setStyle(new Style().setColor(TextFormatting.RED)));

		this.requiredMods = new GuiComponentListInput<GuiComponentSuggestionInput>(I18n.format("gui.edit.addon.requiredMods.label"), this) {

			@Override
			public GuiComponentSuggestionInput createBlankComponent() {
				GuiComponentSuggestionInput input = new GuiComponentSuggestionInput("", this.editScreen);
				for (ModContainer mod : Loader.instance().getActiveModList()) {
					input.addSuggestion(mod.getModId());
				}
				return input;
			}
			
		};
		this.addon.requiredMods.forEach(selected->{
			GuiComponentSuggestionInput input = this.requiredMods.createBlankComponent();
			input.setDefaultText(selected);
			this.requiredMods.addDefaultComponent(input);
		});

		this.requiredAddons = new GuiComponentListInput<GuiComponentSuggestionInput>(I18n.format("gui.edit.addon.requiredAddons.label"), this) {

			@Override
			public GuiComponentSuggestionInput createBlankComponent() {
				GuiComponentSuggestionInput input = new GuiComponentSuggestionInput("", this.editScreen);
				for (Addon addon : AddonLoader.addonsLoaded) {
					if (!addon.id.equals(addonId)) {
						input.addSuggestion(addon.id);
					}
				}
				return input;
			}
			
		};
		this.addon.requiredAddons.forEach(selected->{
			GuiComponentSuggestionInput input = this.requiredAddons.createBlankComponent();
			input.setDefaultText(selected);
			this.requiredAddons.addDefaultComponent(input);
		});
		
		this.addonLoopFunction = new GuiComponentSuggestionInput(I18n.format("gui.edit.addon.loopFunction.label"), this);
		this.addonLoopFunction.setInfo(new TextComponentTranslation("gui.edit.addon.loopFunction.info"));
		this.addonLoopFunction.setSuggestions(AdditionTypeFunction.INSTANCE.getAllAdditions(this.addon).stream().map(functionAdded -> functionAdded.id.toString()).collect(Collectors.toList()));
		this.addonLoopFunction.setDefaultText(this.addon.loopFunction == null ? "" : this.addon.loopFunction.toString());
		
		this.components.add(this.addonIdInput);
		this.components.add(this.addonNameInput);
		if (!this.isNew) {
			this.components.add(this.addonAuthorInput);
		}
		this.components.add(this.addonItemIcon);
		
		this.advancedComponents.add(this.addonLoopFunction);
		this.advancedComponents.add(this.loadOrderInfo);
		this.advancedComponents.add(this.dependencies);
		this.advancedComponents.add(this.dependents);
		this.advancedComponents.add(this.requiredInfo);
		this.advancedComponents.add(this.requiredWarning);
		this.advancedComponents.add(this.requiredMods);
		this.advancedComponents.add(this.requiredAddons);
	}
	
	@Override
	public void refreshView() {
		this.addonLoopFunction.setSuggestions(AdditionTypeFunction.INSTANCE.getAllAdditions(this.addon).stream().map(functionAdded -> functionAdded.id.toString()).collect(Collectors.toList()));
	}
	
	@Override
	public void saveObject() {
		
		String addonId = this.addonIdInput.getText();
		String addonName = this.addonNameInput.getText();
		
		if (addonId.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.noAddonId", addonId), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (addonName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.noAddonName", addonId), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.duplicate", addonId), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew) {
			this.addon.setId(addonId);
		}
		
		this.addon.setName(addonName);
		
		if (this.isNew) {
			this.addon.setAuthor(this.mc.getSession().getUsername());
		} else {
			this.addon.setAuthor(this.addonAuthorInput.getText());
		}
		
		this.addon.setLogoItem(Item.REGISTRY.getNameForObject(this.addonItemIcon.getItemStack().getItem()).toString());
		
		boolean beforeAll = false;
		List<String> dependenciesList = new ArrayList<String>();
		Set<String> dependenciesSet = new TreeSet<String>();
		for (GuiComponentDropdownInput<String> dropdown : this.dependencies.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				if (selected.equals("*")) {
					beforeAll = true;
				}
				
				dependenciesSet.add(selected);
			}
		}
		dependenciesList.addAll(dependenciesSet);
		this.addon.setDependencies(dependenciesList);
		
		List<String> dependentsList = new ArrayList<String>();
		Set<String> dependentsSet = new TreeSet<String>();
		for (GuiComponentDropdownInput<String> dropdown : this.dependents.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				if (beforeAll) {
					if (selected.equals("*")) {
						this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.allBeforeAfter"), I18n.format("gui.buttons.back")));
						return;
					}
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.beforeAll", selected), I18n.format("gui.buttons.back")));
					return;
				}
				
				if (dependenciesList.contains(selected)) {
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.bothBeforeAfter", selected), I18n.format("gui.buttons.back")));
					return;
				}
				
				if (selected.equals("*") && !dependenciesList.isEmpty()) {
					this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.addon.problem.title"), new TextComponentTranslation("gui.edit.addon.problem.afterAll", dependenciesList.get(0)), I18n.format("gui.buttons.back")));
					return;
				}
				
				dependentsSet.add(selected);
			}
		}
		dependentsList.addAll(dependentsSet);
		this.addon.setDependents(dependentsList);
		
		List<String> requiredModsList = new ArrayList<String>();
		Set<String> requiredModsSet = new TreeSet<String>();
		for (GuiComponentSuggestionInput input : this.requiredMods.getComponents()) {
			String selected = input.getText();
			if (selected != null) {
				requiredModsSet.add(selected);
			}
		}
		requiredModsList.addAll(requiredModsSet);
		this.addon.setRequiredMods(requiredModsList);
		
		List<String> requiredAddonsList = new ArrayList<String>();
		Set<String> requiredAddonsSet = new TreeSet<String>();
		for (GuiComponentSuggestionInput input : this.requiredAddons.getComponents()) {
			String selected = input.getText();
			if (selected != null) {
				requiredAddonsSet.add(selected);
			}
		}
		requiredAddonsList.addAll(requiredAddonsSet);
		this.addon.setRequiredAddons(requiredAddonsList);
		
		if (this.isNew) {
			File addonFile = new File(AddonLoader.additionsFolder, getAddonDirName(addonName));
			addonFile.mkdirs();
			this.addon.setAddonFolder(addonFile);
		}
		
		if (!this.addonLoopFunction.getText().isEmpty()) {
			this.addon.setLoopFunction(new ResourceLocation(this.addonLoopFunction.getText()));
		}
		
		AddonLoader.saveAddon(this.addon);
		if (this.isNew) {
			AddonLoader.setupNewAddon(this.addon);
		}
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		this.mc.displayGuiScreen(this.parentScreen);
	}

    private String getAddonDirName(String name) {
    	
    	name = TextFormatting.getTextWithoutFormattingCodes(name);

        for (char illegal : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS)
        {
        	name = name.replace(illegal, '_');
        }

        return this.getUncollidingSaveDirName(name);
    }
    
    private String getUncollidingSaveDirName(String name) {
        name = name.replaceAll("[\\./\"]", "_");

        for (String s : DISALLOWED_FILENAMES)
        {
            if (name.equalsIgnoreCase(s))
            {
                name = "_" + name + "_";
            }
        }

        List<String> addonDirectories = Arrays.asList(AddonLoader.additionsFolder.list());
        
        while(addonDirectories.contains(name)) {
            name = name + "-";
        }

        return name;
    }
}
