package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAddedHat extends ItemArmor implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "hat");

	public static final ItemArmor.ArmorMaterial HAT_MATERIAL = EnumHelper.addArmorMaterial("ADDITIONS_HAT", "", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();

	public boolean hasScreenOverlay;
	public int enchantability = 0;
	public IngredientOreNBT repairStacks = IngredientOreNBT.EMPTY;
	
	public ItemAddedHat() {
		super(HAT_MATERIAL, 0, EntityEquipmentSlot.HEAD);
	}
	
	@Override
	public void setTooltip(List<String> infoToAdd) {
		this.extraTooltip = infoToAdd;
	}
	
	@Override
	public void setOreDict(List<String> oreDict) {
		this.oreDictEntries = oreDict;
	}
	
	@Override
	public void setAlwaysShines(boolean alwaysShines) {
		this.shines = alwaysShines;
	}
	
	@Override
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifierList) {
		this.attributeModifiers = attributeModifierList;
	}

	@Override
	public List<String> getTooltip() {
		return this.extraTooltip;
	}

	@Override
	public List<String> getOreDict() {
		return this.oreDictEntries;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public boolean getAlwaysShines() {
		return this.shines;
	}
	
	@Override
	public int getBurnTime() {
		return this.burnTime;
	}

	@Override
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}

    @Override
    public int getItemEnchantability() {
        return enchantability;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return shines ? true : super.hasEffect(stack);
    }
	
	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.burnTime;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
    	for (String line : extraTooltip) {
    		if(I18n.canTranslate(line)) {
    			line = I18n.translateToLocal(line);
    		}
    		tooltip.add(line);
    	}
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(displayName)) {
			return (I18n.translateToLocal(displayName)).trim();
		} else {
			return displayName;
		}
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modifiersForSlot = HashMultimap.create();
		
		if (this.attributeModifiers.containsKey(slot)) {
			for (AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return EntityEquipmentSlot.MAINHAND;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, net.minecraft.client.gui.ScaledResolution resolution, float partialTicks) {
		if (this.hasScreenOverlay) {
			GlStateManager.disableDepth();
	        GlStateManager.depthMask(false);
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.disableAlpha();
	        Minecraft.getMinecraft().getTextureManager().bindTexture(getOverlayResourceLocation());
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuffer();
	        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	        bufferbuilder.pos(0.0D, (double)resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
	        bufferbuilder.pos((double)resolution.getScaledWidth(), (double)resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
	        bufferbuilder.pos((double)resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
	        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
	        tessellator.draw();
	        GlStateManager.depthMask(true);
	        GlStateManager.enableDepth();
	        GlStateManager.enableAlpha();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
	public ResourceLocation getOverlayResourceLocation() {
		return new ResourceLocation(AdditionsMod.MOD_ID, "textures/overlays/" + this.getId() + ".png");
	}
	
	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack materialStack) {
		return this.repairStacks.itemMatches(materialStack, false, true);
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedHat> {
		
		public Serializer() {
			super(TYPE, ItemAddedHat.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedHat itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("durability", itemAdded.getMaxDamage());
			json.addProperty("has_overlay", itemAdded.hasScreenOverlay);
			json.addProperty("enchantability", itemAdded.enchantability);
			json.add("repair_stacks", IngredientOreNBT.Serializer.serialize(itemAdded.repairStacks));
			
			return json;
		}
		
		@Override
		public ItemAddedHat deserialize(JsonObject json, JsonDeserializationContext context) {
			
			ItemAddedHat itemAdded = new ItemAddedHat();
			
			itemAdded.setMaxDamage(JsonUtils.getInt(json, "durability"));
			itemAdded.hasScreenOverlay = JsonUtils.getBoolean(json, "has_overlay");
			itemAdded.enchantability = JsonUtils.getInt(json, "enchantability");
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedHat itemAdded) {
			
			itemAdded.repairStacks = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "repair_stacks"));
			
			postDeserializeDefaults(json, itemAdded);
		}
    }
}
