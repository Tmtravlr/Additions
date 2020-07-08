package com.tmtravlr.additions.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Moves an entity, with several different modes.
 * 
 * @author Rebeca (Tmtravlr)
 * @Date June 2020
 */
public class CommandAdditionsMove extends CommandBase {
	private static final Random RAND = new Random();
	private static final double INACCURACY_FACTOR = 0.007499999832361937D;
	private static final float DEGREES_TO_RADIANS = (float) Math.PI / 180F;
	private static final double RADIANS_TO_DEGREES = 180D / Math.PI;

	@Override
	public String getName() {
		return "additions.move";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.move.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
            throw new WrongUsageException("commands.additions.move.usage");
        } else {
			Entity entity = sender.getCommandSenderEntity();
			if (entity == null) {
	            throw new WrongUsageException("commands.additions.move.noEntity");
			}
			
			String movementType = args[0];
			
			if ("global".equals(movementType)) {
				if (args.length < 4) {
		            throw new WrongUsageException("commands.additions.move.global.usage");
		        }
				
				entity.motionX = parseCoordinate(entity.motionX, args[1], true).getResult();
				entity.motionY = parseCoordinate(entity.motionY, args[2], true).getResult();
				entity.motionZ = parseCoordinate(entity.motionZ, args[3], true).getResult();
				
			} else if ("relative".equals(movementType) || "facing".equals(movementType)) {
				if (args.length < 2) {
		            throw new WrongUsageException("commands.additions.move.facing.usage");
		        }
				
				Vec3d vecForward = entity.getLookVec();
				
				if ("relative".equals(movementType)) {
					vecForward = (vecForward.x == 0 && vecForward.z == 0) ? new Vec3d(1, 0, 0) : new Vec3d(vecForward.x, 0, vecForward.z).normalize();
				}
				
				Vec3d vecRight = (vecForward.x == 0 && vecForward.z == 0) ? new Vec3d(0, 0, 1) : new Vec3d(-vecForward.z, 0, vecForward.x).normalize();
				Vec3d vecUp = vecRight.crossProduct(vecForward);
				
				double motionForward = new Vec3d(vecForward.x * entity.motionX, vecForward.y * entity.motionY, vecForward.z * entity.motionZ).lengthVector();
				double motionRight = new Vec3d(vecRight.x * entity.motionX, vecRight.y * entity.motionY, vecRight.z * entity.motionZ).lengthVector();
				double motionUp = new Vec3d(vecUp.x * entity.motionX, vecUp.y * entity.motionY, vecUp.z * entity.motionZ).lengthVector();
				
				motionForward = parseCoordinate(motionForward, args[1], true).getResult();
				
				if (args.length > 2) {
					motionUp = parseCoordinate(motionUp, args[2], true).getResult();
				}
				
				if (args.length > 3) {
					motionRight = parseCoordinate(motionRight, args[3], true).getResult();
				}
				
				vecForward = new Vec3d(vecForward.x * motionForward, vecForward.y * motionForward, vecForward.z * motionForward);
				vecRight = new Vec3d(vecRight.x * motionRight, vecRight.y * motionRight, vecRight.z * motionRight);
				vecUp = new Vec3d(vecUp.x * motionUp, vecUp.y * motionUp, vecUp.z * motionUp);
				
				entity.motionX = vecForward.x + vecRight.x + vecUp.x;
				entity.motionY = vecForward.y + vecRight.y + vecUp.y;
				entity.motionZ = vecForward.z + vecRight.z + vecUp.z;
			} else if ("toward".equals(movementType)) {
				if (args.length < 3) {
		            throw new WrongUsageException("commands.additions.move.toward.usage");
		        }
				
				Entity entityToward = getEntity(server, sender, args[1]);

				Vec3d entityPos = entity.getPositionVector();
				Vec3d towardVec = entityToward.getPositionVector().addVector(-entityPos.x, -entityPos.y, -entityPos.z).normalize();
				
	            double speed = parseDouble(args[2]);
				
				entity.motionX += towardVec.x * speed;
				entity.motionY += towardVec.y * speed;
				entity.motionZ += towardVec.z * speed;
			} else {
				throw new WrongUsageException("commands.additions.move.unknownType", movementType);
			}

            entity.velocityChanged = true;
            notifyCommandListener(sender, this, "commands.additions.move.success", entity.getDisplayName().getFormattedText(), "[" + entity.motionX + ", " + entity.motionY + ", " + entity.motionZ + "]");
        }
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) {
			return Arrays.asList("global", "relative", "facing", "toward");
		} else {
			String movementType = args[0];
			
			if ("global".equals(movementType)) {
				if (args.length < 5) {
					return getTabCompletionCoordinate(args, 1, targetPos);
				}
			} else if ("relative".equals(movementType) || "facing".equals(movementType)) {
				if (args.length < 5) {
					return getTabCompletionCoordinate(args, 1, targetPos);
				}
			} else if ("toward".equals(movementType)) {
				if (args.length < 3) { 
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				}
			}
		}
		
		return Collections.emptyList();
	}

}
