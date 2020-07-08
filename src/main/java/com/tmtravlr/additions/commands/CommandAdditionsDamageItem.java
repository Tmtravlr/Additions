package com.tmtravlr.additions.commands;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CommandAdditionsDamageItem extends CommandBase {
	
    private static final Map<String, Integer> SHORTCUTS = Maps.<String, Integer>newHashMap();

    static {
        for (int i = 0; i < 54; ++i) {
            SHORTCUTS.put("slot.container." + i, Integer.valueOf(i));
        }

        for (int i = 0; i < 9; ++i) {
            SHORTCUTS.put("slot.hotbar." + i, Integer.valueOf(i));
        }

        for (int i = 0; i < 27; ++i) {
            SHORTCUTS.put("slot.inventory." + i, Integer.valueOf(9 + i));
        }

        for (int i = 0; i < 27; ++i) {
            SHORTCUTS.put("slot.enderchest." + i, Integer.valueOf(200 + i));
        }

        for (int i = 0; i < 8; ++i) {
            SHORTCUTS.put("slot.villager." + i, Integer.valueOf(300 + i));
        }

        for (int i = 0; i < 15; ++i) {
            SHORTCUTS.put("slot.horse." + i, Integer.valueOf(500 + i));
        }

        SHORTCUTS.put("slot.weapon", Integer.valueOf(98));
        SHORTCUTS.put("slot.weapon.mainhand", Integer.valueOf(98));
        SHORTCUTS.put("slot.weapon.offhand", Integer.valueOf(99));
        SHORTCUTS.put("slot.armor.head", Integer.valueOf(100 + EntityEquipmentSlot.HEAD.getIndex()));
        SHORTCUTS.put("slot.armor.chest", Integer.valueOf(100 + EntityEquipmentSlot.CHEST.getIndex()));
        SHORTCUTS.put("slot.armor.legs", Integer.valueOf(100 + EntityEquipmentSlot.LEGS.getIndex()));
        SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100 + EntityEquipmentSlot.FEET.getIndex()));
        SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
        SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
        SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
    }

	@Override
	public String getName() {
		return "additions.damageitem";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.damageitem.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
            throw new WrongUsageException("commands.additions.damageitem.usage");
        } else {
            boolean forBlock = true;

            if ("entity".equals(args[0])) {
                forBlock = false;
            } else {
                if (!"block".equals(args[0])) {
                    throw new WrongUsageException("commands.additions.damageitem.usage");
                }
        	}

            int slotArgIndex;

            if (forBlock) {
                if (args.length < 6) {
                    throw new WrongUsageException("commands.additions.damageitem.block.usage");
                }

                slotArgIndex = 4;
            } else {
                if (args.length < 4) {
                    throw new WrongUsageException("commands.additions.damageitem.entity.usage");
                }

                slotArgIndex = 2;
            }

            String slotName = args[slotArgIndex];
            int slotInventoryIndex = this.getSlotForShortcut(slotName);
            int amount = parseInt(args[slotArgIndex + 1], 0);

            if (forBlock) {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos blockpos = parseBlockPos(sender, args, 1, false);
                World world = sender.getEntityWorld();
                TileEntity tileentity = world.getTileEntity(blockpos);

                if (tileentity == null || !(tileentity instanceof IInventory)) {
                    throw new CommandException("commands.replaceitem.noContainer", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }

                IInventory iinventory = (IInventory)tileentity;

                if (slotInventoryIndex >= 0 && slotInventoryIndex < iinventory.getSizeInventory()) {
                    ItemStack stack = iinventory.getStackInSlot(slotInventoryIndex);
                    stack.damageItem(amount, null);
                    iinventory.setInventorySlotContents(slotInventoryIndex, stack);
                }
            } else {
                Entity entity = getEntity(server, sender, args[1]);
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
                }
                
                damageItemStackInInventory(slotInventoryIndex, entity, amount);

                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
                }
            }

            notifyCommandListener(sender, this, "commands.additions.damageitem.success", slotName, amount);
        }
    }

    private int getSlotForShortcut(String shortcut) throws CommandException {
        if (!SHORTCUTS.containsKey(shortcut)) {
            throw new CommandException("commands.generic.parameter.invalid", shortcut);
        } else {
            return ((Integer)SHORTCUTS.get(shortcut)).intValue();
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, new String[] {"entity", "block"});
        }
        else if (args.length == 2 && "entity".equals(args[0])) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if (args.length >= 2 && args.length <= 4 && "block".equals(args[0])) {
            return getTabCompletionCoordinate(args, 1, targetPos);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 0 && "entity".equals(args[0]) && index == 1;
    }
    
    private void damageItemStackInInventory(int slotInventoryIndex, Entity entity, int amount) {
    	if (entity instanceof EntityLivingBase) {
    		EntityLivingBase entityLiving = ((EntityLivingBase) entity);
    		EntityEquipmentSlot entityEquipmentSlot = null;

            if (slotInventoryIndex == 98) {
                entityEquipmentSlot = EntityEquipmentSlot.MAINHAND;
            } else if (slotInventoryIndex == 99) {
                entityEquipmentSlot = EntityEquipmentSlot.OFFHAND;
            } else if (slotInventoryIndex == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
                entityEquipmentSlot = EntityEquipmentSlot.HEAD;
            } else if (slotInventoryIndex == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
                entityEquipmentSlot = EntityEquipmentSlot.CHEST;
            } else if (slotInventoryIndex == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
                entityEquipmentSlot = EntityEquipmentSlot.LEGS;
            } else  if (slotInventoryIndex != 100 + EntityEquipmentSlot.FEET.getIndex()) {
                entityEquipmentSlot = EntityEquipmentSlot.FEET;
            }

            if (entityEquipmentSlot != null) {
            	ItemStack itemStack = entityLiving.getItemStackFromSlot(entityEquipmentSlot);
            	itemStack.damageItem(amount, entityLiving);
            	entityLiving.setItemStackToSlot(entityEquipmentSlot, itemStack);
            	
            } else if (entityLiving instanceof AbstractHorse) {
            	ContainerHorseChest horseChest = ObfuscationReflectionHelper.getPrivateValue(AbstractHorse.class, (AbstractHorse) entityLiving, "field_110296_bG", "horseChest");
            	int horseChestIndex = slotInventoryIndex - 400;

                if (horseChestIndex >= 0 && horseChestIndex < 2 && horseChestIndex < horseChest.getSizeInventory()) {
                	
                	ItemStack itemStack = horseChest.getStackInSlot(horseChestIndex);
                	itemStack.damageItem(amount, entityLiving);
                	horseChest.setInventorySlotContents(horseChestIndex, itemStack);
                } else {
                	horseChestIndex = slotInventoryIndex - 500 + 2;

                    if (horseChestIndex >= 2 && horseChestIndex < horseChest.getSizeInventory()) {
                    	ItemStack itemStack = horseChest.getStackInSlot(horseChestIndex);
                    	itemStack.damageItem(amount, entityLiving);
                    	horseChest.setInventorySlotContents(horseChestIndex, itemStack);
                    }
                }
            } else if (entityLiving instanceof EntityVillager) {
            	InventoryBasic villagerInventory = ObfuscationReflectionHelper.getPrivateValue(EntityVillager.class, (EntityVillager) entityLiving, "field_175560_bz", "villagerInventory");
            	int villagerInventoryIndex = slotInventoryIndex - 300;

                if (villagerInventoryIndex >= 0 && villagerInventoryIndex < villagerInventory.getSizeInventory()) {
                	ItemStack itemStack = villagerInventory.getStackInSlot(villagerInventoryIndex);
                	itemStack.damageItem(amount, entityLiving);
                    villagerInventory.setInventorySlotContents(villagerInventoryIndex, itemStack);
                }
            } else if (entityLiving instanceof EntityPlayer) {
            	InventoryPlayer inventory = ((EntityPlayer) entityLiving).inventory;
            	
            	if (slotInventoryIndex >= 0 && slotInventoryIndex < inventory.mainInventory.size()) {
                	ItemStack itemStack = inventory.getStackInSlot(slotInventoryIndex);
                	itemStack.damageItem(amount, entityLiving);
            		inventory.setInventorySlotContents(slotInventoryIndex, itemStack);
                } else {
                	InventoryEnderChest enderChest = ((EntityPlayer) entityLiving).getInventoryEnderChest();
	            	int enderChestIndex = slotInventoryIndex - 200;
	
	                if (enderChestIndex >= 0 && enderChestIndex < enderChest.getSizeInventory()) {
	                	ItemStack itemStack = enderChest.getStackInSlot(enderChestIndex);
	                	itemStack.damageItem(amount, entityLiving);
	                	enderChest.setInventorySlotContents(enderChestIndex, itemStack);
	                }
                }
            }
    	}
    }
}
