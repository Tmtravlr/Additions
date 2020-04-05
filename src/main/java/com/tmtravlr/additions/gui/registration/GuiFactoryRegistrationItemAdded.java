package com.tmtravlr.additions.gui.registration;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.addon.items.ItemAddedArrow;
import com.tmtravlr.additions.addon.items.ItemAddedAxe;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.addon.items.ItemAddedClub;
import com.tmtravlr.additions.addon.items.ItemAddedFirestarter;
import com.tmtravlr.additions.addon.items.ItemAddedFood;
import com.tmtravlr.additions.addon.items.ItemAddedGun;
import com.tmtravlr.additions.addon.items.ItemAddedHat;
import com.tmtravlr.additions.addon.items.ItemAddedHoe;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.items.ItemAddedMultiTool;
import com.tmtravlr.additions.addon.items.ItemAddedPickaxe;
import com.tmtravlr.additions.addon.items.ItemAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedRecord;
import com.tmtravlr.additions.addon.items.ItemAddedShears;
import com.tmtravlr.additions.addon.items.ItemAddedShield;
import com.tmtravlr.additions.addon.items.ItemAddedShovel;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.addon.items.ItemAddedSword;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;
import com.tmtravlr.additions.api.gui.IGuiItemAddedFactory;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemArmor;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemArrow;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemAxe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemBow;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemClub;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemFirestarter;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemFood;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemGun;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemHat;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemHoe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemMultiTool;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemPickaxe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemProjectile;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemRecord;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShears;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShield;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShovel;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSimple;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSword;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemThrowable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFactoryRegistrationItemAdded {

	public static void registerGuiFactories() {
		ItemAddedManager.registerGuiFactory(ItemAddedSimple.TYPE, new IGuiItemAddedFactory<ItemAddedSimple>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.simple.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.simple.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedSimple item) {
				return new GuiEditItemSimple(parent, item == null ? I18n.format("gui.edit.item.simple.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedSimple item) {
				GuiEditItemSimple editScreen = new GuiEditItemSimple(parent, I18n.format("gui.edit.item.simple.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedFood.TYPE, new IGuiItemAddedFactory<ItemAddedFood>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.food.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.food.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedFood item) {
				return new GuiEditItemFood(parent, item == null ? I18n.format("gui.edit.item.food.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedFood item) {
				GuiEditItemFood editScreen = new GuiEditItemFood(parent, I18n.format("gui.edit.item.food.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedRecord.TYPE, new IGuiItemAddedFactory<ItemAddedRecord>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.record.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item..description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedRecord item) {
				return new GuiEditItemRecord(parent, item == null ? I18n.format("gui.edit.item.record.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedRecord item) {
				GuiEditItemRecord editScreen = new GuiEditItemRecord(parent, I18n.format("gui.edit.item.record.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedArmor.TYPE, new IGuiItemAddedFactory<ItemAddedArmor>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.armor.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.armor.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedArmor item) {
				return new GuiEditItemArmor(parent, item == null ? I18n.format("gui.edit.item.armor.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedArmor item) {
				GuiEditItemArmor editScreen = new GuiEditItemArmor(parent, I18n.format("gui.edit.item.armor.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedHat.TYPE, new IGuiItemAddedFactory<ItemAddedHat>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.hat.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.hat.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedHat item) {
				return new GuiEditItemHat(parent, item == null ? I18n.format("gui.edit.item.hat.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedHat item) {
				GuiEditItemHat editScreen = new GuiEditItemHat(parent, I18n.format("gui.edit.item.hat.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedSword.TYPE, new IGuiItemAddedFactory<ItemAddedSword>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.sword.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.sword.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedSword item) {
				return new GuiEditItemSword(parent, item == null ? I18n.format("gui.edit.item.sword.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedSword item) {
				GuiEditItemSword editScreen = new GuiEditItemSword(parent, I18n.format("gui.edit.item.sword.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedClub.TYPE, new IGuiItemAddedFactory<ItemAddedClub>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.club.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.club.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedClub item) {
				return new GuiEditItemClub(parent, item == null ? I18n.format("gui.edit.item.club.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedClub item) {
				GuiEditItemClub editScreen = new GuiEditItemClub(parent, I18n.format("gui.edit.item.club.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShears.TYPE, new IGuiItemAddedFactory<ItemAddedShears>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shears.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shears.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShears item) {
				return new GuiEditItemShears(parent, item == null ? I18n.format("gui.edit.item.shears.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShears item) {
				GuiEditItemShears editScreen = new GuiEditItemShears(parent, I18n.format("gui.edit.item.shears.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedFirestarter.TYPE, new IGuiItemAddedFactory<ItemAddedFirestarter>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.firestarter.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.firestarter.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedFirestarter item) {
				return new GuiEditItemFirestarter(parent, item == null ? I18n.format("gui.edit.item.firestarter.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedFirestarter item) {
				GuiEditItemFirestarter editScreen = new GuiEditItemFirestarter(parent, I18n.format("gui.edit.item.firestarter.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedPickaxe.TYPE, new IGuiItemAddedFactory<ItemAddedPickaxe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.pickaxe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.pickaxe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedPickaxe item) {
				return new GuiEditItemPickaxe(parent, item == null ? I18n.format("gui.edit.item.pickaxe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedPickaxe item) {
				GuiEditItemPickaxe editScreen = new GuiEditItemPickaxe(parent, I18n.format("gui.edit.item.pickaxe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedAxe.TYPE, new IGuiItemAddedFactory<ItemAddedAxe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.axe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.axe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedAxe item) {
				return new GuiEditItemAxe(parent, item == null ? I18n.format("gui.edit.item.axe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedAxe item) {
				GuiEditItemAxe editScreen = new GuiEditItemAxe(parent, I18n.format("gui.edit.item.axe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShovel.TYPE, new IGuiItemAddedFactory<ItemAddedShovel>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shovel.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shovel.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShovel item) {
				return new GuiEditItemShovel(parent, item == null ? I18n.format("gui.edit.item.shovel.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShovel item) {
				GuiEditItemShovel editScreen = new GuiEditItemShovel(parent, I18n.format("gui.edit.item.shovel.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedHoe.TYPE, new IGuiItemAddedFactory<ItemAddedHoe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.hoe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.hoe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedHoe item) {
				return new GuiEditItemHoe(parent, item == null ? I18n.format("gui.edit.item.hoe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedHoe item) {
				GuiEditItemHoe editScreen = new GuiEditItemHoe(parent, I18n.format("gui.edit.item.hoe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedMultiTool.TYPE, new IGuiItemAddedFactory<ItemAddedMultiTool>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.multitool.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.multitool.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedMultiTool item) {
				return new GuiEditItemMultiTool(parent, item == null ? I18n.format("gui.edit.item.multitool.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedMultiTool item) {
				GuiEditItemMultiTool editScreen = new GuiEditItemMultiTool(parent, I18n.format("gui.edit.item.multitool.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShield.TYPE, new IGuiItemAddedFactory<ItemAddedShield>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shield.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shield.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShield item) {
				return new GuiEditItemShield(parent, item == null ? I18n.format("gui.edit.item.shield.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShield item) {
				GuiEditItemShield editScreen = new GuiEditItemShield(parent, I18n.format("gui.edit.item.shield.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedBow.TYPE, new IGuiItemAddedFactory<ItemAddedBow>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.bow.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.bow.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedBow item) {
				return new GuiEditItemBow(parent, item == null ? I18n.format("gui.edit.item.bow.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedBow item) {
				GuiEditItemBow editScreen = new GuiEditItemBow(parent, I18n.format("gui.edit.item.bow.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedGun.TYPE, new IGuiItemAddedFactory<ItemAddedGun>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.gun.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.gun.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedGun item) {
				return new GuiEditItemGun(parent, item == null ? I18n.format("gui.edit.item.gun.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedGun item) {
				GuiEditItemGun editScreen = new GuiEditItemGun(parent, I18n.format("gui.edit.item.gun.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedThrowable.TYPE, new IGuiItemAddedFactory<ItemAddedThrowable>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.throwable.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.throwable.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedThrowable item) {
				return new GuiEditItemThrowable(parent, item == null ? I18n.format("gui.edit.item.throwable.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedThrowable item) {
				GuiEditItemThrowable editScreen = new GuiEditItemThrowable(parent, I18n.format("gui.edit.item.throwable.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedArrow.TYPE, new IGuiItemAddedFactory<ItemAddedArrow>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.arrow.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.arrow.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedArrow item) {
				return new GuiEditItemArrow(parent, item == null ? I18n.format("gui.edit.item.arrow.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedArrow item) {
				GuiEditItemArrow editScreen = new GuiEditItemArrow(parent, I18n.format("gui.edit.item.arrow.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedProjectile.TYPE, new IGuiItemAddedFactory<ItemAddedProjectile>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.projectile.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.projectile.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedProjectile item) {
				return new GuiEditItemProjectile(parent, item == null ? I18n.format("gui.edit.item.projectile.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedProjectile item) {
				GuiEditItemProjectile editScreen = new GuiEditItemProjectile(parent, I18n.format("gui.edit.item.projectile.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
	}
	
}
