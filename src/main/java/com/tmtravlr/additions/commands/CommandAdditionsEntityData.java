package com.tmtravlr.additions.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Identical to the entity data command, but works with players.
 * @author Rebeca (Tmtravlr)
 * @since May 2020
 */
public class CommandAdditionsEntityData extends CommandBase {

	@Override
	public String getName() {
		return "additions.entitydata";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.entitydata.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.additions.entitydata.usage");
        } else {
            Entity entity = getEntity(server, sender, args[0]);
            
            NBTTagCompound entityTag = new NBTTagCompound();
            entity.writeToNBT(entityTag);
            NBTTagCompound originalTag = (NBTTagCompound)entityTag.copy();
            NBTTagCompound tagToMerge;

            try
            {
                tagToMerge = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
            } catch (NBTException e) {
                throw new CommandException("commands.entitydata.tagError", e.getMessage());
            }

            tagToMerge.removeTag("UUIDMost");
            tagToMerge.removeTag("UUIDLeast");
            entityTag.merge(tagToMerge);

            if (entityTag.equals(originalTag)) {
                throw new CommandException("commands.entitydata.failed", entityTag.toString());
            }
            else
            {
                entity.readFromNBT(entityTag);
                notifyCommandListener(sender, this, "commands.entitydata.success", entityTag.toString());
            }
        }
        
    }

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    	return args.length < 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

}
