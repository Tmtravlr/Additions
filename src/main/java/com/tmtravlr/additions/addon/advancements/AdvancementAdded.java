package com.tmtravlr.additions.addon.advancements;

import java.util.Map;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Holds info necessary to add/edit an advancement
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class AdvancementAdded {

	public ResourceLocation id;
	public ResourceLocation parentId;
	public DisplayInfo display;
	public AdvancementRewards rewards;
	public Map<String, Criterion> criteria;
	public String[][] requirements;

	public Advancement.Builder advancementBuilder;

	public AdvancementAdded(ResourceLocation id, Advancement.Builder advancementBuilder)  {
		this.id = id;
		this.parentId = ObfuscationReflectionHelper.getPrivateValue(Advancement.Builder.class, advancementBuilder, "field_192061_a", "parentId");
		this.display = ObfuscationReflectionHelper.getPrivateValue(Advancement.Builder.class, advancementBuilder, "field_192063_c", "display");
		this.rewards = ObfuscationReflectionHelper.getPrivateValue(Advancement.Builder.class, advancementBuilder, "field_192064_d", "rewards");
		this.criteria = ObfuscationReflectionHelper.getPrivateValue(Advancement.Builder.class, advancementBuilder, "field_192065_e", "criteria");
		this.requirements = ObfuscationReflectionHelper.getPrivateValue(Advancement.Builder.class, advancementBuilder, "field_192066_f", "requirements");
		this.advancementBuilder = advancementBuilder;
	}
}
