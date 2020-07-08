package com.tmtravlr.additions.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Ejects all the passengers that are riding something.
 * @author Rebeca (Tmtravlr)
 * @Date May 2020
 */
public class CommandAdditionsEjectPassengers extends CommandBase {

	@Override
	public String getName() {
		return "additions.ejectpassengers";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.ejectpassengers.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Entity mount = sender.getCommandSenderEntity();
		
		if (mount != null) {
			if (!mount.getPassengers().isEmpty()) {
				mount.removePassengers();
				notifyCommandListener(sender, this, "commands.additions.ejectpassengers.success", mount.getName());
			} else {
				throw new CommandException("commands.additions.ejectpassengers.failure.noPassengers", mount.getName());
			}
		} else {
			throw new CommandException("commands.additions.ejectpassengers.failure.noMount");
		}
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
	
}
