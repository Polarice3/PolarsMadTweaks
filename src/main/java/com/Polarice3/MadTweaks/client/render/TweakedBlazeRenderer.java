package com.Polarice3.MadTweaks.client.render;

import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Blaze;

public class TweakedBlazeRenderer extends MobRenderer<Blaze, BlazeModel<Blaze>> {
    private static final ResourceLocation BLAZE_LOCATION = new ResourceLocation("textures/entity/blaze.png");

    public TweakedBlazeRenderer(EntityRendererProvider.Context p_173933_) {
        super(p_173933_, new BlazeModel<>(p_173933_.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    }

    protected int getBlockLightLevel(Blaze blaze, BlockPos blockPos) {
        return (int) (blaze.getHealth() / blaze.getMaxHealth() * 15);
    }

    public ResourceLocation getTextureLocation(Blaze p_113908_) {
        return BLAZE_LOCATION;
    }
}
