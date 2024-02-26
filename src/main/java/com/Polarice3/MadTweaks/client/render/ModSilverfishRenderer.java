package com.Polarice3.MadTweaks.client.render;

import com.Polarice3.MadTweaks.common.entities.ModSilverfish;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class ModSilverfishRenderer extends MobRenderer<ModSilverfish, SilverfishModel<ModSilverfish>> {
   private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation("textures/entity/silverfish.png");

   public ModSilverfishRenderer(EntityRendererProvider.Context p_174378_) {
      super(p_174378_, new SilverfishModel<>(p_174378_.bakeLayer(ModelLayers.SILVERFISH)), 0.3F);
   }

   protected float getFlipDegrees(ModSilverfish p_115927_) {
      return 180.0F;
   }

   public ResourceLocation getTextureLocation(ModSilverfish p_115929_) {
      return SILVERFISH_LOCATION;
   }
}