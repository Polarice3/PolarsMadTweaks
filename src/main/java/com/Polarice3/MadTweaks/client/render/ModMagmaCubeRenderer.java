package com.Polarice3.MadTweaks.client.render;

import com.Polarice3.MadTweaks.common.entities.ModMagmaCube;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModMagmaCubeRenderer extends MobRenderer<ModMagmaCube, LavaSlimeModel<ModMagmaCube>> {
   private static final ResourceLocation MAGMACUBE_LOCATION = new ResourceLocation("textures/entity/slime/magmacube.png");

   public ModMagmaCubeRenderer(EntityRendererProvider.Context p_174298_) {
      super(p_174298_, new LavaSlimeModel<>(p_174298_.bakeLayer(ModelLayers.MAGMA_CUBE)), 0.25F);
   }

   protected int getBlockLightLevel(ModMagmaCube p_115399_, BlockPos p_115400_) {
      return 15;
   }

   public ResourceLocation getTextureLocation(ModMagmaCube p_115393_) {
      return MAGMACUBE_LOCATION;
   }

   public void render(ModMagmaCube p_265315_, float p_265620_, float p_265669_, PoseStack p_265647_, MultiBufferSource p_265147_, int p_265465_) {
      this.shadowRadius = 0.25F * (float)p_265315_.getSize();
      super.render(p_265315_, p_265620_, p_265669_, p_265647_, p_265147_, p_265465_);
   }

   protected void scale(ModMagmaCube p_115395_, PoseStack p_115396_, float p_115397_) {
      int i = p_115395_.getSize();
      float f = Mth.lerp(p_115397_, p_115395_.oSquish, p_115395_.squish) / ((float)i * 0.5F + 1.0F);
      float f1 = 1.0F / (f + 1.0F);
      p_115396_.scale(f1 * (float)i, 1.0F / f1 * (float)i, f1 * (float)i);
   }
}