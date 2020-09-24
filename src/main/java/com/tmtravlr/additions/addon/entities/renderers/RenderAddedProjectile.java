package com.tmtravlr.additions.addon.entities.renderers;

import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderAddedProjectile extends RenderSnowball<EntityAddedProjectile> {
	
    private final RenderItem itemRenderer;
    private final Random random = new Random();

	public RenderAddedProjectile(RenderManager renderManager, RenderItem itemRenderer) {
		super(renderManager, Items.AIR, itemRenderer);
		this.itemRenderer = itemRenderer;
	}
	
	@Override
    public void doRender(EntityAddedProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getRenders3D()) {
        	ItemStack stack = this.getStackToRender(entity);
        	
        	this.bindEntityTexture(entity);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.translate((float) x, (float) y, (float) z);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 45F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.2F, -0.2F, 0.0F);
            GlStateManager.scale(1.5F, 1.5F, 1.5F);
            
            float shake = (float) entity.arrowShake - partialTicks;

            if (shake > 0.0F) {
                float shakeRotation = -MathHelper.sin(shake * 3.0F) * shake;
                GlStateManager.rotate(shakeRotation, 0.0F, 0.0F, 2.0F);
            }

            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }

            this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

            if (this.renderOutlines)  {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            
            if (!this.renderOutlines) {
                this.renderName(entity, x, y, z);
            }
        } else {
        	super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
	
	@Override
    @Nonnull
	public ItemStack getStackToRender(@Nonnull EntityAddedProjectile entity) {
        return entity.getArrowStack();
    }

}
