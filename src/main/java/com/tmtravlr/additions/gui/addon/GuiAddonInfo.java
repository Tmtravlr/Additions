package com.tmtravlr.additions.gui.addon;

import java.util.ArrayList;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.addon.AddonInfo;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.components.GuiComponentList;
import com.tmtravlr.additions.gui.edit.components.GuiDisplayText;
import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInput;
import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInputItem;
import com.tmtravlr.additions.gui.edit.components.GuiStringInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class GuiAddonInfo extends GuiEdit {
	
	private GuiStringInput addonNameInput;
	private GuiStringInput addonAuthorInput;
	private GuiDropdownListInputItem addonItemIcon;
	private GuiDisplayText loadOrderInfo;
	private GuiComponentList<GuiDropdownListInput<String>> dependencies;
	private GuiComponentList<GuiDropdownListInput<String>> dependents;
	private GuiDisplayText requiredInfo;
	private GuiDisplayText requiredWarning;
	private GuiComponentList<GuiDropdownListInput<String>> requiredMods;
	private GuiComponentList<GuiDropdownListInput<String>> requiredAddons;
	
	private final AddonInfo addon;

	public GuiAddonInfo(GuiScreen parentScreen, String title, AddonInfo addon) {
		super(parentScreen, title);
		this.addon = addon;
	}

	@Override
	public void saveObject() {
		
	}

	@Override
	public void initComponents() {
		GuiStringInput addonIdInput = new GuiStringInput("gui.createAddon.addonId.label", this);
		addonIdInput.setEnabled(false);
		addonIdInput.setRequired();
		addonIdInput.setText(this.addon.id);
		
		this.addonNameInput = new GuiStringInput("gui.createAddon.addonName.label", this);
		this.addonNameInput.setRequired();
		this.addonNameInput.setMaxStringLength(64);
		this.addonNameInput.setHasColorSelect();
		
		this.addonAuthorInput = new GuiStringInput("gui.createAddon.addonName.label", this);
		this.addonAuthorInput.setMaxStringLength(64);
		this.addonAuthorInput.setText(this.addon.author);
		
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
		
		this.components.add(addonIdInput);
		this.components.add(this.addonNameInput);
		this.components.add(this.addonAuthorInput);
		this.components.add(this.addonItemIcon);
		this.advancedComponents.add(this.loadOrderInfo);
		this.advancedComponents.add(this.dependencies);
		this.advancedComponents.add(this.dependents);
		this.advancedComponents.add(this.requiredInfo);
		this.advancedComponents.add(this.requiredWarning);
		this.advancedComponents.add(this.requiredMods);
		this.advancedComponents.add(this.requiredAddons);
	}

}
