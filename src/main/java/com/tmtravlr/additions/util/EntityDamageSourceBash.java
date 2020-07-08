package com.tmtravlr.additions.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSourceBash extends EntityDamageSource {

	public EntityDamageSourceBash(Entity attacker) {
		super("additions.bash", attacker);
	}
	
	@Override
    public ITextComponent getDeathMessage(EntityLivingBase killed) {
        EntityLivingBase attacker = this.getTrueSource() instanceof EntityLivingBase ? (EntityLivingBase) this.getTrueSource() : killed.getAttackingEntity();
        String deathMessage = "death.attack." + this.damageType;
        String deathMessagePlayer = deathMessage + ".player";
        return attacker != null ? new TextComponentTranslation(deathMessagePlayer, killed.getDisplayName(), attacker.getDisplayName()) : new TextComponentTranslation(deathMessage, killed.getDisplayName());
    }

}
