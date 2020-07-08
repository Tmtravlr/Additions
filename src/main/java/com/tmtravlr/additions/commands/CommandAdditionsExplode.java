package com.tmtravlr.additions.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Causes an explosion with several options at a location or entity's position
 * @author Rebeca (Tmtravlr)
 * @since May 2020
 */
public class CommandAdditionsExplode extends CommandBase {

	@Override
	public String getName() {
		return "additions.explode";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.explode.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 4 || args.length > 7) {
			throw new WrongUsageException("commands.additions.explode.usage");
		}
		else {
			float strength = 0.0f;
			boolean destroy = true;
			boolean fire = false;
			BlockPos pos = sender.getPosition();
			World world = sender.getEntityWorld();
			Entity cause = null;
			Entity entity = null;
		
			if (args.length < 6) {
				entity = getEntity(server, sender, args[0], Entity.class);
				pos = entity.getPosition();
				world = entity.getEntityWorld();
				strength = (float)CommandBase.parseDouble(args[1]);
				
				if (args.length > 2) {
					destroy = CommandBase.parseBoolean(args[2]);
				}
				
				if (args.length > 3) {
					fire = CommandBase.parseBoolean(args[3]);
				}
				
				if (args.length > 4) {
					cause = getEntity(server, sender, args[4], Entity.class);
				}
			} else {
				pos = parseBlockPos(sender, args, 0, false);
				strength = (float)CommandBase.parseDouble(args[3]);
				
				if (args.length > 4) {
					destroy = CommandBase.parseBoolean(args[4]);
				}
				
				if (args.length > 5) {
					fire = CommandBase.parseBoolean(args[5]);
				}
				
				if (args.length > 6) {
					cause = getEntity(server, sender, args[6], Entity.class);
				}
			}
			
			if (!world.isRemote) {
				world.newExplosion(cause, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, strength, fire, destroy);
			}
			
			notifyCommandListener(sender, this, "commands.additions.explode.success", strength, entity == null ? pos : entity.getName());
			
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length <= 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args[0].matches("[\\~\\.\\-0-9]+")) {
        	return args.length < 4 ? getTabCompletionCoordinate(args, 0, targetPos) : args.length < 7 ? Arrays.asList(new String[]{"true", "false"}) : args.length == 7 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
        } else {
        	return args.length < 5 ? Arrays.asList(new String[]{"true", "false"}) : args.length == 5 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
        }
    }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0 || (args.length > 5 ? index == 6 : index == 4);
	}

}
