package com.tmtravlr.additions.type.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class AttributeTypeManager {

	public static final Map<EntityEquipmentSlot, UUID> ARMOR_UUIDS = new HashMap<>();
	static {
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.FEET, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.LEGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.CHEST, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.HEAD, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.MAINHAND, UUID.fromString("3FCB55D3-564C-F348-4A79-B5C9A33C13DF"));
		AttributeTypeManager.ARMOR_UUIDS.put(EntityEquipmentSlot.OFFHAND, UUID.fromString("AC233E1C-4180-4865-B01B-BCFE9785ACA3"));
	}
    public static UUID attackDamageUUID;
    public static UUID attackSpeedUUID;
    
    static {
    	attackDamageUUID = new Item() {
    		public UUID getAttackDamageUUID() {
    			return ATTACK_DAMAGE_MODIFIER;
    		}
    	}.getAttackDamageUUID();
    	
    	attackSpeedUUID = new Item() {
    		public UUID getAttackSpeedUUID() {
    			return ATTACK_SPEED_MODIFIER;
    		}
    	}.getAttackSpeedUUID();
    }

	public static List<IAttribute> knownAttributes = new ArrayList<>();
	
	public static void initVanillaAttributes() {
		knownAttributes.add(SharedMonsterAttributes.MAX_HEALTH);
		knownAttributes.add(SharedMonsterAttributes.FOLLOW_RANGE);
		knownAttributes.add(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		knownAttributes.add(SharedMonsterAttributes.MOVEMENT_SPEED);
		knownAttributes.add(SharedMonsterAttributes.FLYING_SPEED);
		knownAttributes.add(SharedMonsterAttributes.ATTACK_DAMAGE);
		knownAttributes.add(SharedMonsterAttributes.ATTACK_SPEED);
		knownAttributes.add(SharedMonsterAttributes.ARMOR);
		knownAttributes.add(SharedMonsterAttributes.ARMOR_TOUGHNESS);
		knownAttributes.add(SharedMonsterAttributes.LUCK);
		knownAttributes.add(new AbstractHorse(null) {
			protected void updateHorseSlots() {} //Caused crashes because it wasn't checking for a null world
			
			public IAttribute getJumpStrengthAttribute() {
				return JUMP_STRENGTH;
			}
		}.getJumpStrengthAttribute());
		knownAttributes.add(new EntityZombie(null) {
			public IAttribute getReinforcementsAttribute() {
				return SPAWN_REINFORCEMENTS_CHANCE;
			}
		}.getReinforcementsAttribute());
	}
	
	public static void addAttribute(IAttribute attribute) {
		knownAttributes.add(attribute);
	}
	
	public static UUID getUUIDfromString(String uuidString, String attributeModifierName, EntityEquipmentSlot slot) {
		UUID uuid;
		
		if (!uuidString.isEmpty()) {
			if (uuidString.equals(attackDamageUUID.toString())) {
				uuid = attackDamageUUID;
			} else if (uuidString.equals(attackSpeedUUID.toString())) {
				uuid = attackSpeedUUID;
			} else {
				try {
					uuid = UUID.fromString(uuidString);
				} catch (IllegalArgumentException e) {
					//Not a valid UUID, but attempt to generate a uuid from the string's hash code.
					int stringHash = uuidString.hashCode();
					uuid = new UUID(0L, stringHash);
				}
			}
		} else if ("generic.attackDamage".equals(attributeModifierName) && slot == EntityEquipmentSlot.MAINHAND) {
			uuid = attackDamageUUID;
		} else if ("generic.attackSpeed".equals(attributeModifierName) && slot == EntityEquipmentSlot.MAINHAND) {
			uuid = attackSpeedUUID;
		} else if (("generic.armor".equals(attributeModifierName) || "generic.armorToughness".equals(attributeModifierName)) && ARMOR_UUIDS.containsKey(slot)) {
			uuid = ARMOR_UUIDS.get(slot);
		} else {
			uuid = UUID.randomUUID();
		}
		
		return uuid;
	}
	
}
