package com.tmtravlr.additions.commands;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTable;

/**
 * Generates and drops a loot table
 * @author Rebeca (Tmtravlr)
 * @Date May 2020
 */
public class CommandAdditionsLoot extends CommandBase {

	@Override
	public String getName() {
		return "additions.loot";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.loot.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getEntityWorld() instanceof WorldServer) {
			if (args.length < 1) {
				throw new WrongUsageException("commands.additions.loot.usage", new Object[0]);
			} else {
				ResourceLocation lootName = new ResourceLocation(args[0]);
				Vec3d dropPos = sender.getPositionVector();
				Long seed = args.length > 4 ? CommandBase.parseLong(args[4]) : null;
				
				WorldServer world = (WorldServer) sender.getEntityWorld();
				Entity looter = sender.getCommandSenderEntity();
				
				double x = dropPos.x;
				double y = dropPos.y;
				double z = dropPos.z;
				
				if (args.length > 1) {
					x = parseCoordinate(sender.getPositionVector().x, args[1], true).getResult();
				}
				
				if (args.length > 2) {
					y = parseCoordinate(sender.getPositionVector().y, args[2], -4096, 4096, false).getResult();
				}
				
				if (args.length > 3) {
					z = parseCoordinate(sender.getPositionVector().z, args[3], true).getResult();
				}
				
				dropPos = new Vec3d(x, y, z);
				
				LootTable table = world.getLootTableManager().getLootTableFromLocation(lootName);
	            LootContextExtendedBuilder contextBuilder = new LootContextExtendedBuilder(world).withPosition(new BlockPos(dropPos));
	
	            if (looter != null) {
	            	contextBuilder.withLooter(looter);
	            	
	            	if (looter instanceof EntityPlayer) {
	            		contextBuilder.withPlayer((EntityPlayer) looter);
	            		contextBuilder.withLuck(((EntityPlayer) looter).getLuck());
	            	}
	            }
	            
	            List<ItemStack> lootItems = table.generateLootForPools(seed == null ? world.rand : new Random(seed), contextBuilder.build());
	            
	            if (lootItems.isEmpty()) {
	            	throw new CommandException("commands.additions.loot.failure.noItems", lootName);
	            } else {
		            for (ItemStack stack : lootItems) {
		            	EntityItem itemEntity = new EntityItem(world, dropPos.x, dropPos.y, dropPos.z, stack);
		                itemEntity.setDefaultPickupDelay();
		                world.spawnEntity(itemEntity);
		            }
					notifyCommandListener(sender, this, "commands.additions.loot.success", lootName, dropPos);
				}
			}
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, AdditionTypeLootTable.INSTANCE.getAllLootTablesAdded());
        } else {
        	return args.length < 5 ? getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
        }
    }
	
}
