package com.tmtravlr.additions.commands;

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
 * Mount or stack an entity onto another.
 * @author Rebeca (Tmtravlr)
 * @date June 2015
 */
public class CommandAdditionsMount extends CommandBase {

	@Override
	public String getName() {
		return "additions.mount";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.mount.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if (args.length < 1) {
			throw new WrongUsageException("commands.additions.mount.usage");
		} else {
			Entity rider = sender.getCommandSenderEntity();
			Entity mount = getEntity(server, sender, args[0]);
			
			//Make sure the rider exists
			if (rider == null) {
				throw new CommandException("commands.additions.mount.failure.noRider");
			}
			
			//Make sure we aren't trying to stack something onto itself
			if (rider == mount) {
				throw new CommandException("commands.additions.mount.failure.self", rider.getName());
			}
			
			World world = mount.world;
			
			double x = mount.posX;
			double y = mount.posY;
			double z = mount.posZ;
			
			float pitch = mount.rotationPitch;
			float yaw = mount.rotationYaw;
			
			double motionX = mount.motionX;
			double motionY = mount.motionY;
			double motionZ = mount.motionZ;
						
			//First teleport the rider to the mount
			
			rider.startRiding(mount, true);

			notifyCommandListener(sender, this, "commands.additions.mount.success", rider.getName(), mount.getName());
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length < 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }


	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index < 2;
	}
	
}
