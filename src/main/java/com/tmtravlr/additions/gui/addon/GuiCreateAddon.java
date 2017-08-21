package com.tmtravlr.additions.gui.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.addon.AddonInfo;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.components.GuiBooleanInput;
import com.tmtravlr.additions.gui.edit.components.GuiComponentList;
import com.tmtravlr.additions.gui.edit.components.GuiDisplayText;
import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInput;
import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInputBlock;
import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInputItem;
import com.tmtravlr.additions.gui.edit.components.GuiFloatInput;
import com.tmtravlr.additions.gui.edit.components.GuiIntInput;
import com.tmtravlr.additions.gui.edit.components.GuiNBTInput;
import com.tmtravlr.additions.gui.edit.components.GuiStringInput;

/**
 * Page for adding a new addon.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class GuiCreateAddon extends GuiEdit {
	
	/** These filenames are known to be restricted on one or more OS's. */
    public static final String[] DISALLOWED_FILENAMES = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

	private GuiStringInput addonIdInput;
	private GuiStringInput addonNameInput;
	private GuiDropdownListInputItem addonItemIcon;
	private GuiDisplayText loadOrderInfo;
	private GuiComponentList<GuiDropdownListInput<String>> dependencies;
	private GuiComponentList<GuiDropdownListInput<String>> dependents;
	private GuiDisplayText requiredInfo;
	private GuiDisplayText requiredWarning;
	private GuiComponentList<GuiDropdownListInput<String>> requiredMods;
	private GuiComponentList<GuiDropdownListInput<String>> requiredAddons;

	public GuiCreateAddon(GuiScreen parentScreen, String title) {
		super(parentScreen, title);
	}
	
	@Override
	public void saveObject() {
		
		String addonId = addonIdInput.getText();
		String addonName = addonNameInput.getText();
		
		if (addonId.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.noAddonId", addonId), "gui.buttons.back"));
			return;
		}
		
		if (addonName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.noAddonName", addonId), "gui.buttons.back"));
			return;
		}
		
		if (AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.duplicate", addonId), "gui.buttons.back"));
			return;
		}
		
		File addonFile = new File(AddonLoader.addonFolder, getAddonDirName(addonName));
		
		AddonInfo addonInfo = new AddonInfo();
		addonInfo.setId(addonIdInput.getText());
		addonInfo.setName(addonNameInput.getText());
		addonInfo.setAuthor(this.mc.getSession().getUsername());
		addonInfo.setLogoItem(addonItemIcon.selectedText.getText());
		
		boolean beforeAll = false;
		List<String> dependenciesList = new ArrayList<String>();
		Set<String> dependenciesSet = new TreeSet<String>();
		for (GuiDropdownListInput<String> dropdown : dependencies.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				if (selected.equals("*")) {
					beforeAll = true;
				}
				
				dependenciesSet.add(selected);
			}
		}
		dependenciesList.addAll(dependenciesSet);
		addonInfo.setDependencies(dependenciesList);
		
		List<String> dependentsList = new ArrayList<String>();
		Set<String> dependentsSet = new TreeSet<String>();
		for (GuiDropdownListInput<String> dropdown : dependents.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				if (beforeAll) {
					if (selected.equals("*")) {
						this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.allBeforeAfter"), "gui.buttons.back"));
						return;
					}
					this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.beforeAll", selected), "gui.buttons.back"));
					return;
				}
				
				if (dependenciesList.contains(selected)) {
					this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.bothBeforeAfter", selected), "gui.buttons.back"));
					return;
				}
				
				if (selected.equals("*") && !dependenciesList.isEmpty()) {
					this.mc.displayGuiScreen(new GuiMessagePopup(this, "gui.createAddon.problem.title", new TextComponentTranslation("gui.createAddon.problem.afterAll", dependenciesList.get(0)), "gui.buttons.back"));
					return;
				}
				
				dependentsSet.add(selected);
			}
		}
		dependentsList.addAll(dependentsSet);
		addonInfo.setDependents(dependentsList);
		
		List<String> requiredModsList = new ArrayList<String>();
		Set<String> requiredModsSet = new TreeSet<String>();
		for (GuiDropdownListInput<String> dropdown : requiredMods.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				requiredModsSet.add(selected);
			}
		}
		requiredModsList.addAll(requiredModsSet);
		addonInfo.setRequiredMods(requiredModsList);
		
		List<String> requiredAddonsList = new ArrayList<String>();
		Set<String> requiredAddonsSet = new TreeSet<String>();
		for (GuiDropdownListInput<String> dropdown : requiredAddons.getComponents()) {
			String selected = dropdown.getSelected();
			if (selected != null) {
				requiredAddonsSet.add(selected);
			}
		}
		requiredAddonsList.addAll(requiredAddonsSet);
		addonInfo.setRequiredAddons(requiredAddonsList);
		
		addonInfo.setAddonFile(addonFile);
		
		addonFile.mkdirs();
		AddonLoader.saveAddonInfo(addonInfo);
		AddonLoader.addonsLoaded.add(addonInfo);
		AddonLoader.ADDONS_NAMED.put(addonInfo.id, addonInfo);
		
		this.mc.displayGuiScreen(parentScreen);
	}
	
	@Override
	public void initComponents() {
		this.addonIdInput = new GuiStringInput("gui.createAddon.addonId.label", this);
		this.addonIdInput.setRequired();
		this.addonIdInput.setInfo(new TextComponentTranslation("gui.createAddon.addonId.info"));
		this.addonIdInput.setMaxStringLength(32);
		this.addonIdInput.setValidator(new Predicate<String>() {

			@Override
			public boolean apply(String input) {
				return input.matches("[a-z0-9\\_]*");
			}
			
		});
		
		this.addonNameInput = new GuiStringInput("gui.createAddon.addonName.label", this);
		this.addonNameInput.setRequired();
		this.addonNameInput.setMaxStringLength(64);
		this.addonNameInput.setHasColorSelect();
		
		this.addonItemIcon = new GuiDropdownListInputItem("gui.createAddon.addonIcon.label", this);
		ArrayList<Item> registeredItems = new ArrayList<>();
		Item.REGISTRY.forEach(item->registeredItems.add(item));
		this.addonItemIcon.setSelections(registeredItems);
		
		this.loadOrderInfo = new GuiDisplayText(this, new TextComponentTranslation("gui.createAddon.loadOrder.info"));

		this.dependencies = new GuiComponentList<GuiDropdownListInput<String>>("gui.createAddon.dependencies.label", this) {

			@Override
			public GuiDropdownListInput<String> createBlankComponent() {
				GuiDropdownListInput<String> dropdown = new GuiDropdownListInput<>("", this.parent);
				for (AddonInfo addon : AddonLoader.addonsLoaded) {
					dropdown.addSelection(addon.id);
				}
				dropdown.addSelection("*");
				return dropdown;
			}
			
		};

		this.dependents = new GuiComponentList<GuiDropdownListInput<String>>("gui.createAddon.dependents.label", this) {

			@Override
			public GuiDropdownListInput<String> createBlankComponent() {
				GuiDropdownListInput<String> dropdown = new GuiDropdownListInput<>("", this.parent);
				for (AddonInfo addon : AddonLoader.addonsLoaded) {
					dropdown.addSelection(addon.id);
				}
				dropdown.addSelection("*");
				return dropdown;
			}
			
		};
		
		this.requiredInfo = new GuiDisplayText(this, new TextComponentTranslation("gui.createAddon.required.info"));
		this.requiredWarning = new GuiDisplayText(this, new TextComponentTranslation("gui.createAddon.required.warning"));

		this.requiredMods = new GuiComponentList<GuiDropdownListInput<String>>("gui.createAddon.requiredMods.label", this) {

			@Override
			public GuiDropdownListInput createBlankComponent() {
				GuiDropdownListInput<String> dropdown = new GuiDropdownListInput<>("", this.parent);
				for (ModContainer mod : Loader.instance().getActiveModList()) {
					String modId = mod.getModId();
					if (!(modId.equals("minecraft")
							|| modId.equals("mcp")
							|| modId.equals("FML")
							|| modId.equals("forge")
							|| modId.equals("additions"))) {
						dropdown.addSelection(modId);
					}
				}
				return dropdown;
			}
			
		};

		this.requiredAddons = new GuiComponentList<GuiDropdownListInput<String>>("gui.createAddon.requiredAddons.label", this) {

			@Override
			public GuiDropdownListInput<String> createBlankComponent() {
				GuiDropdownListInput<String> dropdown = new GuiDropdownListInput<>("", this.parent);
				for (AddonInfo addon : AddonLoader.addonsLoaded) {
					dropdown.addSelection(addon.id);
				}
				return dropdown;
			}
			
		};
		
		this.components.add(this.addonIdInput);
		this.components.add(this.addonNameInput);
		this.components.add(this.addonItemIcon);
		this.advancedComponents.add(this.loadOrderInfo);
		this.advancedComponents.add(this.dependencies);
		this.advancedComponents.add(this.dependents);
		this.advancedComponents.add(this.requiredInfo);
		this.advancedComponents.add(this.requiredWarning);
		this.advancedComponents.add(this.requiredMods);
		this.advancedComponents.add(this.requiredAddons);
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

        List<String> addonDirectories = Arrays.asList(AddonLoader.addonFolder.list());
        
        while(addonDirectories.contains(name)) {
            name = name + "-";
        }

        return name;
    }
}
