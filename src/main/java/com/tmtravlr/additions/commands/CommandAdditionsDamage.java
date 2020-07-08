package com.tmtravlr.additions.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.tmtravlr.additions.util.EntityDamageSourceBash;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class CommandAdditionsDamage extends CommandBase {
	
	private static final String[] DAMAGE_TYPES = new String[]{"anvil", "bash", "cactus", "cramming", "dragon_breath", "drown", "explosion", "fall", "falling_block", "fireworks", "fly_into_wall", "generic", "hot_floor", "in_fire", "in_wall", "lava", "lightning_bolt", "magic", "mob", "on_fire", "out_of_world", "player", "starve", "thorns", "wither", "custom"};
	private static final List<String> CUSTOM_OPTIONS = Arrays.asList(new String[]{"projectile", "magic", "fire", "explosion", "difficulty_scaled", "absolute", "unblockable", "hurts_creative", "knockback"});

	@Override
	public String getName() {
		return "additions.damage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.additions.damage.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 3) {
			throw new WrongUsageException("commands.additions.damage.usage");
		} else {
			Entity entity = getEntity(server, sender, args[0], Entity.class);
			String type = args[1];
			float amount = (float) parseDouble(args[2], 0);
			
			if (type.equals("anvil")) {
				entity.attackEntityFrom(DamageSource.ANVIL, amount);
			}
			else if (type.equals("bash")) {
				EntityLivingBase source = null;
				
				if (sender.getCommandSenderEntity() instanceof EntityLivingBase) {
					source = (EntityLivingBase) sender.getCommandSenderEntity();
				}
				
				entity.attackEntityFrom(new EntityDamageSourceBash(source), amount);
			}
			else if (type.equals("cactus")) {
				entity.attackEntityFrom(DamageSource.CACTUS, amount);
			}
			else if (type.equals("cramming")) {
				entity.attackEntityFrom(DamageSource.CRAMMING, amount);
			}
			else if (type.equals("dragon_breath")) {
				entity.attackEntityFrom(DamageSource.DRAGON_BREATH, amount);
			}
			else if (type.equals("drown")) {
				entity.attackEntityFrom(DamageSource.DROWN, amount);
			}
			else if (type.equals("explosion")) {
				EntityLivingBase source = null;
				
				if (sender.getCommandSenderEntity() instanceof EntityLivingBase) {
					source = (EntityLivingBase) sender.getCommandSenderEntity();
				}
				
				entity.attackEntityFrom(DamageSource.causeExplosionDamage(source), amount);
			}
			else if (type.equals("fall")) {
				entity.attackEntityFrom(DamageSource.FALL, amount);
			}
			else if (type.equals("falling_block")) {
				entity.attackEntityFrom(DamageSource.FALLING_BLOCK, amount);
			}
			else if (type.equals("fireworks")) {
				entity.attackEntityFrom(DamageSource.FIREWORKS, amount);
			}
			else if (type.equals("fly_into_wall")) {
				entity.attackEntityFrom(DamageSource.FLY_INTO_WALL, amount);
			}
			else if (type.equals("generic")) {
				entity.attackEntityFrom(DamageSource.GENERIC, amount);
			}
			else if (type.equals("hot_floor")) {
				entity.attackEntityFrom(DamageSource.HOT_FLOOR, amount);
			}
			else if (type.equals("in_fire")) {
				entity.attackEntityFrom(DamageSource.IN_FIRE, amount);
			}
			else if (type.equals("in_wall")) {
				entity.attackEntityFrom(DamageSource.IN_WALL, amount);
			}
			else if (type.equals("lava")) {
				entity.attackEntityFrom(DamageSource.LAVA, amount);
			}
			else if (type.equals("lightning_bolt")) {
				entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
			}
			else if (type.equals("magic")) {
				entity.attackEntityFrom(DamageSource.MAGIC, amount);
			}
			else if (type.equals("mob")) {
				EntityLivingBase source = null;
				
				if (sender.getCommandSenderEntity() instanceof EntityLivingBase) {
					source = (EntityLivingBase) sender.getCommandSenderEntity();
				}
				
				entity.attackEntityFrom(DamageSource.causeMobDamage(source), amount);
			}
			else if (type.equals("on_fire")) {
				entity.attackEntityFrom(DamageSource.ON_FIRE, amount);
			}
			else if (type.equals("out_of_world")) {
				entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, amount);
			}
			else if (type.equals("player")) {
				EntityPlayer source = null;
				
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
					source = (EntityPlayer) sender.getCommandSenderEntity();
				}
				
				entity.attackEntityFrom(DamageSource.causePlayerDamage(source), amount);
			}
			else if (type.equals("starve")) {
				entity.attackEntityFrom(DamageSource.STARVE, amount);
			}
			else if (type.equals("thorns")) {
				entity.attackEntityFrom(DamageSource.causeThornsDamage(sender.getCommandSenderEntity()), amount);
			}
			else if (type.equals("wither")) {
				entity.attackEntityFrom(DamageSource.WITHER, amount);
			}
			else if (type.equals("custom")) {
				List<String> options = new ArrayList();
				int deathMessageStart = 3;
				
				if (args.length > 3) {
					String[] optionsSplit = args[3].split(",");
					boolean optionMatched = false;
					
					for (String option : optionsSplit) {
						if (CUSTOM_OPTIONS.contains(option)) {
							options.add(option);
							optionMatched = true;
						}
					}
					
					if (optionMatched) {
						deathMessageStart = 4;
					}
				}

				String deathMessage = args.length > deathMessageStart ? String.join(" ", Arrays.copyOfRange(args, deathMessageStart, args.length)) : "death.attack.generic";
				Entity source = sender.getCommandSenderEntity();
				
				DamageSource customDamage = new EntityDamageSource("additions.custom", source) {
					
					@Override
					public ITextComponent getDeathMessage(EntityLivingBase entityDying) {
						String dyingName = entityDying.getDisplayName().getFormattedText();
						String killingName = "Nobody";
						String itemName = "Nothing";
						String fullDeathMessage = deathMessage;
						
						if (this.getTrueSource() != null) {
							killingName = this.getTrueSource().getDisplayName().getFormattedText();
						}
						
						if (this.getTrueSource() instanceof EntityLivingBase && !((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand().isEmpty()) {
							itemName = ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand().getDisplayName();
						}
						
						if (I18n.canTranslate(fullDeathMessage)) {
							return new TextComponentTranslation(fullDeathMessage, dyingName, killingName, itemName);
						} else {
							if (fullDeathMessage.contains("%s")) {
								fullDeathMessage = fullDeathMessage.replace("%s", dyingName);
							}
							
							if (fullDeathMessage.contains("%1$s")) {
								fullDeathMessage = fullDeathMessage.replace("%1$s", dyingName);
							}
							
							if (fullDeathMessage.contains("%2$s")) {
								fullDeathMessage = fullDeathMessage.replace("%2$s", killingName);
							}
							
							if (fullDeathMessage.contains("%3$s")) {
								fullDeathMessage = fullDeathMessage.replace("%3$s", itemName);
							}
							
							return new TextComponentString(fullDeathMessage);
						}
					}
					
				};
				
				for (String option : options) {
					if (option.equals("projectile")) {
						customDamage.setProjectile();
					} 
					else if (option.equals("magic")) {
						customDamage.setMagicDamage();
					} 
					else if (option.equals("fire")) {
						customDamage.setFireDamage();
					} 
					else if (option.equals("explosion")) {
						customDamage.setExplosion();
					} 
					else if (option.equals("difficulty_scaled")) {
						customDamage.setDifficultyScaled();
					} 
					else if (option.equals("absolute")) {
						customDamage.setDamageIsAbsolute();
					} 
					else if (option.equals("unblockable")) {
						customDamage.setDamageBypassesArmor();
					} 
					else if (option.equals("hurts_creative")) {
						customDamage.setDamageAllowedInCreativeMode();
					}
				}
				
				entity.attackEntityFrom(customDamage, amount);
			} else {
				throw new CommandException("commands.additions.damage.notvaliddamage", type);
			}
			
			notifyCommandListener(sender, this, "commands.additions.damage.success", amount, type, entity.getName());
			
		}
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, DAMAGE_TYPES);
        } else {
            return (args.length == 4 && "custom".equals(args[1])) ? getListOfStringsMatchingLastWord(args, CUSTOM_OPTIONS.toArray(new String[CUSTOM_OPTIONS.size()])) : Collections.emptyList();
        }
    }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}

}
