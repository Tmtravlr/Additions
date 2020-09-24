package com.tmtravlr.additions.commands;

import com.tmtravlr.additions.type.AdditionTypeStructure;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Generates a structure
 * @author Rebeca (Tmtravlr)
 * @date June 2020
 */
public class CommandAdditionsStructure extends CommandBase {

	@Override
	public String getName() {
		return "additions.structure";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.structure.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getEntityWorld() instanceof WorldServer) {
			if (args.length < 1 || (args.length > 1 && args.length < 4)) {
				throw new WrongUsageException("commands.additions.structure.usage");
			} else {
				ResourceLocation structureName = new ResourceLocation(args[0]);
				BlockPos pos = args.length > 3 ? CommandBase.parseBlockPos(sender, args, 1, false) : sender.getPosition();
				
				boolean ignoreEntities = args.length > 4 ? CommandBase.parseBoolean(args[4]) : false;
				int rotationType = args.length > 5 ? CommandBase.parseInt(args[5], 0, 270) : 0;
				String mirrorType = args.length > 6 ? args[6] : "none";
				float integrity = args.length > 7 ? (float) CommandBase.parseDouble(args[7], 0, 1) : 1;
				long seed = args.length > 8 ? CommandBase.parseLong(args[8]) : 0;
				WorldServer world = (WorldServer) sender.getEntityWorld();
				
				Mirror mirror = Mirror.NONE;

				if (mirrorType != null && !mirrorType.isEmpty()) {
					if (mirrorType.equals("left-right")) {
						mirror = Mirror.LEFT_RIGHT;
						
					} else if (mirrorType.equals("front-back")) {
						mirror = Mirror.FRONT_BACK;
						
					} else if (!mirrorType.equals("none")) {
						throw new CommandException("commands.additions.structure.failure.invalidMirror");
					}
				}

				Rotation rotation = Rotation.NONE;

				if (rotationType == 90) {
					rotation = Rotation.CLOCKWISE_90;
					
				} else if (rotationType == 180) {
					rotation = Rotation.CLOCKWISE_180;
					
				} else if (rotationType == 270) {
					rotation = Rotation.COUNTERCLOCKWISE_90;
					
				} else if (rotationType != 0) {
					throw new CommandException("commands.additions.structure.failure.invalidRotation");
				}

				Template template = world.getStructureTemplateManager().get(server, structureName);

				if (template != null)  {
					PlacementSettings settings = (new PlacementSettings())
							.setMirror(mirror)
							.setRotation(rotation)
							.setIgnoreEntities(ignoreEntities)
							.setIntegrity(integrity)
							.setSeed(seed)
							.setChunk(null)
							.setReplacedBlock(null)
							.setIgnoreStructureBlock(false);

					template.addBlocksToWorldChunk(world, pos, settings);
					notifyCommandListener(sender, this, "commands.additions.structure.success", structureName, pos);
				} else {
					throw new CommandException("commands.additions.structure.failure.noStructure", structureName);
				}
			}
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) {		
			return getListOfStringsMatchingLastWord(args, AdditionTypeStructure.INSTANCE.getAllStructuresAdded());
        } else if (args.length <= 4) {
    		return getTabCompletionCoordinate(args, 0, targetPos);
        } else if (args.length == 5) {
        	return getListOfStringsMatchingLastWord(args, new String[]{"true", "false"});
        } else if (args.length == 6) {
        	return getListOfStringsMatchingLastWord(args, new String[]{"0", "90", "180", "270"});
        } else if (args.length == 7) {
        	return getListOfStringsMatchingLastWord(args, new String[]{"left-right", "front-back", "none"});
        } else {
        	return Collections.EMPTY_LIST;
        }
    }
	
}
