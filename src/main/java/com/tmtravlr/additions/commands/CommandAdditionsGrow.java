package com.tmtravlr.additions.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Grows a block (like if bonemeal was applied to it)
 * @author Rebeca (Tmtravlr)
 * @Date June 2020
 */
public class CommandAdditionsGrow extends CommandBase {
	
	private static final int NETHER_WART_MAX_AGE = 3;

	@Override
	public String getName() {
		return "additions.grow";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.grow.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if (args.length < 3) {
			throw new WrongUsageException("commands.additions.grow.usage");
		} else {
			BlockPos pos = CommandBase.parseBlockPos(sender, args, 0, false);
			IBlockState state = world.getBlockState(pos);
			boolean growFully = args.length > 3 && CommandBase.parseBoolean(args[3]);
			
			if (state.getBlock() instanceof BlockNetherWart && state.getValue(BlockNetherWart.AGE) < NETHER_WART_MAX_AGE) {
				BlockNetherWart netherWart = ((BlockNetherWart)state.getBlock());

                if (ForgeHooks.onCropsGrowPre(world, pos, state, true)) {
                	IBlockState newState = state.withProperty(BlockNetherWart.AGE, growFully ? NETHER_WART_MAX_AGE : state.getValue(BlockNetherWart.AGE) + 1);
                	world.setBlockState(pos, newState, 2);
                    ForgeHooks.onCropsGrowPost(world, pos, state, newState);
                }
			} else if (state.getBlock() instanceof IGrowable && ((IGrowable)state.getBlock()).canGrow(world, pos, state, false)) {
				IGrowable growable = ((IGrowable)state.getBlock());
					
				if (growFully && (growable instanceof BlockCrops || growable instanceof BlockSapling || growable instanceof BlockMushroom)) {
					if (growable instanceof BlockCrops) {
						int maxAge = ((BlockCrops)growable).getMaxAge();
						PropertyInteger ageProperty = BlockCrops.AGE;
						
						try {
							Method getAgePropertyMethod = ReflectionHelper.findMethod(BlockCrops.class, "getAgeProperty", "func_185524_e");
							getAgePropertyMethod.setAccessible(true);
							ageProperty = (PropertyInteger) getAgePropertyMethod.invoke(growable);
						} catch (InvocationTargetException | IllegalAccessException e) {
							AdditionsMod.logger.warn("Unable to get age property for crop block " + state.getBlock().getRegistryName() + ", defaulting to BlockCrops.AGE, which may or may not work.", e);
						}

		                if (ForgeHooks.onCropsGrowPre(world, pos, state, true)) {
		                	IBlockState newState = state.withProperty(ageProperty, maxAge);
		                	world.setBlockState(pos, newState, 2);
		                    ForgeHooks.onCropsGrowPost(world, pos, state, newState);
		                }
					} else if (growable instanceof BlockSapling) {
						((BlockSapling)growable).generateTree(world, pos, state, world.rand);
					} else if (growable instanceof BlockMushroom) {
						((BlockMushroom)growable).generateBigMushroom(world, pos, state, world.rand);
					}
				} else {
					growable.grow(world, world.rand, pos, state);
				}
			} else {
				throw new CommandException("commands.additions.grow.failure", pos);
			}
			
			notifyCommandListener(sender, this, "commands.additions.grow.success", pos);
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length <= 3) {
    		return getTabCompletionCoordinate(args, 0, targetPos);
        } else if (args.length <= 4) {
        	return getListOfStringsMatchingLastWord(args, new String[]{"true", "false"});
        } else {
        	return Collections.EMPTY_LIST;
        }
    }
	
}
