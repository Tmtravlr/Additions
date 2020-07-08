package com.tmtravlr.additions.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

/**
 * Runs a command, and conditionally runs another command if it passes (or fails)
 * @author Rebeca (Tmtravlr)
 * @since May 2020
 */
public class CommandAdditionsCondition extends CommandBase {

	@Override
	public String getName() {
		return "additions.condition";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.condition.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getEntityWorld() instanceof WorldServer) {
			if (args.length < 1) {
	            throw new WrongUsageException("commands.additions.condition.usage");
	        }
			
			final List<String> conditionCommands = new ArrayList<>();
			String thenCommand = "";
			String elseCommand = "";
			
			try {
				NBTTagCompound tag = JsonToNBT.getTagFromJson(buildString(args, 0));
				
				if (tag.hasKey("if", 8)) {
					conditionCommands.add(tag.getString("if"));
				} else if (tag.hasKey("if", 9)) {
					NBTTagList commandList = tag.getTagList("if", 8);
					commandList.forEach(command -> conditionCommands.add(((NBTTagString)command).getString()));
				}
				
				if (tag.hasKey("then", 8)) {
					thenCommand = tag.getString("then");
				}
				
				if (tag.hasKey("else", 8)) {
					elseCommand = tag.getString("else");
				}
            } catch (NBTException e) {
                throw new CommandException("commands.additions.condition.failure.parse", e.getMessage());
            }
			
			if (conditionCommands.isEmpty()) {
                throw new CommandException("commands.additions.condition.failure.noConditions");
			}
			
			boolean success = true;
			
			try {
				for (String conditionCommand : conditionCommands) {
					if (server.getCommandManager().executeCommand(sender, conditionCommand) == 0) {
						success = false;
						break;
					}
				}
			} catch (Exception e) {
				success = false;
			}
			
			if (success) {
				if (!thenCommand.isEmpty()) {
					server.getCommandManager().executeCommand(sender, thenCommand);
					notifyCommandListener(sender, this, "commands.additions.condition.success.then");
				} else {
					notifyCommandListener(sender, this, "commands.additions.condition.success.noThen");
				}
			} else {
				if (!elseCommand.isEmpty()) {
					server.getCommandManager().executeCommand(sender, elseCommand);
					notifyCommandListener(sender, this, "commands.additions.condition.success.else");
				} else {
					notifyCommandListener(sender, this, "commands.additions.condition.success.noElse");
				}
			}
		}
	}

}
