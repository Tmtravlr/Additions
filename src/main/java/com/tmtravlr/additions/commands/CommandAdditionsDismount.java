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
 * Dismount an entity if it is riding something.
 * @author Rebeca (Tmtravlr)
 * @date May 2020
 */
public class CommandAdditionsDismount extends CommandBase {

	@Override
	public String getName() {
		return "additions.dismount";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.dismount.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Entity rider = sender.getCommandSenderEntity();

		if (rider != null) {
			if (rider.isRiding()) {
				rider.dismountRidingEntity();
				notifyCommandListener(sender, this, "commands.additions.dismount.success", rider.getName());
			} else {
				throw new CommandException("commands.additions.dismount.failure.notRiding", rider.getName());
			}
		} else {
			throw new CommandException("commands.additions.dismount.failure.noRider");
		}
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
	
}
