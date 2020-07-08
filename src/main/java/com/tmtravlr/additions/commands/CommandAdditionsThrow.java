package com.tmtravlr.additions.commands;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

/**
 * Makes an entity throw another entity!
 * Basically the same as the summon command, but adds motion to
 * the summoned mob as if thrown by whoever ran the command.
 * @author Rebeca (Tmtravlr)
 * @Date May 2020
 */
public class CommandAdditionsThrow extends CommandBase {
	private static final Random RAND = new Random();
	private static final double INACCURACY_FACTOR = 0.007499999832361937D;
	private static final float DEGREES_TO_RADIANS = (float) Math.PI / 180F;
	private static final double RADIANS_TO_DEGREES = 180D / Math.PI;

	@Override
	public String getName() {
		return "additions.throw";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.throw.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
            throw new WrongUsageException("commands.additions.throw.usage");
        } else {
			Entity thrower = sender.getCommandSenderEntity();
			if (thrower == null) {
	            throw new WrongUsageException("commands.additions.throw.noThrower");
			}
			
            String entityName = args[0];
            Vec3d lookVec = thrower.getLookVec();
            double speed = args.length > 1 ? parseDouble(args[1]) : 3;
            double inaccuracy = args.length > 2 ? parseDouble(args[2], 0) : 0;
            

            double xMotion = -MathHelper.sin(thrower.rotationYaw * DEGREES_TO_RADIANS) * MathHelper.cos(thrower.rotationPitch * DEGREES_TO_RADIANS);
            double yMotion = -MathHelper.sin(thrower.rotationPitch * DEGREES_TO_RADIANS);
            double zMotion = MathHelper.cos(thrower.rotationYaw * DEGREES_TO_RADIANS) * MathHelper.cos(thrower.rotationPitch * DEGREES_TO_RADIANS);
            
            double totalMotion = MathHelper.sqrt(xMotion*xMotion + yMotion*yMotion + zMotion*zMotion);
            double horizontalMotion = MathHelper.sqrt(xMotion*xMotion + zMotion*zMotion);
            xMotion /= totalMotion;
            yMotion /= totalMotion;
            zMotion /= totalMotion;
            xMotion += RAND.nextGaussian() * INACCURACY_FACTOR * inaccuracy;
            yMotion += RAND.nextGaussian() * INACCURACY_FACTOR * inaccuracy;
            zMotion += RAND.nextGaussian() * INACCURACY_FACTOR * inaccuracy;
            xMotion *= speed;
            yMotion *= speed;
            zMotion *= speed;
            
            BlockPos blockPos = sender.getPosition();

            World world = sender.getEntityWorld();

            if (!world.isBlockLoaded(blockPos)) {
                throw new CommandException("commands.summon.outOfWorld");
            } else if (EntityList.LIGHTNING_BOLT.equals(new ResourceLocation(entityName))) {
                throw new CommandException("commands.additions.throw.noLightning"); // Too bad you can't throw lightning
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                boolean hasCustomTag = false;

                if (args.length > 3) {
                    String tagString = buildString(args, 3);

                    try {
                        tag = JsonToNBT.getTagFromJson(tagString);
                        hasCustomTag = true;
                    } catch (NBTException nbtexception) {
                        throw new CommandException("commands.summon.tagError", new Object[] {nbtexception.getMessage()});
                    }
                }
                
                tag.setString("id", entityName);
                NBTTagList motionList = new NBTTagList();
                motionList.appendTag(new NBTTagDouble(xMotion + thrower.motionX));
                motionList.appendTag(new NBTTagDouble(yMotion + thrower.motionY));
                motionList.appendTag(new NBTTagDouble(zMotion + thrower.motionZ));
                tag.setTag("Motion", motionList);
                
                Entity entity = AnvilChunkLoader.readWorldEntityPos(tag, world, thrower.posX, thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ, true);

                if (entity == null) {
                    throw new CommandException("commands.additions.throw.failure", thrower.getDisplayName().getFormattedText(), entityName);
                } else {
                	entity.setLocationAndAngles(thrower.posX, thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ, (float)(MathHelper.atan2(xMotion, zMotion) * RADIANS_TO_DEGREES), (float)(MathHelper.atan2(yMotion, horizontalMotion) * RADIANS_TO_DEGREES));
                	entity.prevRotationYaw = entity.rotationYaw;
                	entity.prevRotationPitch = entity.rotationPitch;
                	
                	if (entity instanceof EntityArrow) {
                		((EntityArrow)entity).shootingEntity = thrower;
                	}

                    if (!hasCustomTag && entity instanceof EntityLiving) {
                        ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                    }

                    notifyCommandListener(sender, this, "commands.additions.throw.success", thrower.getDisplayName().getFormattedText(), entity.getDisplayName().getFormattedText());
                }
            }
        }
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList()) : Collections.emptyList();
    }

}
