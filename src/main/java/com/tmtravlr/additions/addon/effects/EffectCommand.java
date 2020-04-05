package com.tmtravlr.additions.addon.effects;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.lootoverhaul.commands.CommandSenderGeneric;

import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Info about a command to run.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectCommand extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "command");

	public String command = "";
	public String commandSenderName = "@";
	public boolean hideFeedback = true;
	public float chance = 1;

	@Override
	public void affectEntity(@Nullable Entity cause, Entity entity) {
		if (entity.world.getMinecraftServer() != null && entity.world.rand.nextFloat() <= this.chance) {
			entity.world.getMinecraftServer().commandManager.executeCommand(new CommandSenderWrapper(entity, entity.getPositionVector(), entity.getPosition(), 2, entity, !this.hideFeedback) {
				
				@Override
				public boolean canUseCommand(int permLevel, String commandName) {
					return true;
				}
				
			}, this.command);
		}
	}

	@Override
	public void affectBlock(@Nullable Entity cause, World world, BlockPos pos) {
		if (world.getMinecraftServer() != null && world.rand.nextFloat() <= this.chance) {
			world.getMinecraftServer().commandManager.executeCommand(new CommandSenderGeneric(this.commandSenderName, world, pos), this.command);
		}
	}

	public static class Serializer extends Effect.Serializer<EffectCommand> {
		
		public Serializer() {
			super(TYPE, EffectCommand.class);
		}
		
		@Override
		public JsonObject serialize(EffectCommand effect, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (StringUtils.isNullOrEmpty(effect.command)) {
				throw new IllegalArgumentException("Trying to save command effect without a command");
			}
			
			json.addProperty("command", effect.command);
			
			if (!"@".equals(effect.commandSenderName)) {
				json.addProperty("command_sender_name", effect.commandSenderName);
			}
			
			if (!effect.hideFeedback) {
				json.addProperty("hide_feedback", false);
			}
			
			if (effect.chance < 1) {
				json.addProperty("chance", effect.chance);
			}
			
			return json;
		}
		
		@Override
		public EffectCommand deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCommand effect = new EffectCommand();
			
			effect.command = JsonUtils.getString(json, "command", "");
			effect.commandSenderName = JsonUtils.getString(json, "command_sender_name", "@");
			effect.hideFeedback = JsonUtils.getBoolean(json, "hide_feedback", true);
			effect.chance = JsonUtils.getFloat(json, "chance", 1);
			
			return effect;
		}
    }
}
