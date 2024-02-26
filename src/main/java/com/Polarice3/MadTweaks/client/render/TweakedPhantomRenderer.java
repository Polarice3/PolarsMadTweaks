package com.Polarice3.MadTweaks.client.render;

import com.Polarice3.MadTweaks.TweaksConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Phantom;

import javax.annotation.Nullable;

public class TweakedPhantomRenderer extends MobRenderer<Phantom, PhantomModel<Phantom>> {
    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");

    public TweakedPhantomRenderer(EntityRendererProvider.Context p_174338_) {
        super(p_174338_, new PhantomModel<>(p_174338_.bakeLayer(ModelLayers.PHANTOM)), 0.75F);
        this.addLayer(new PhantomEyesLayer<>(this));
    }

    public ResourceLocation getTextureLocation(Phantom p_115679_) {
        return PHANTOM_LOCATION;
    }

    public void render(Phantom p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(p_115308_, this, p_115310_, p_115311_, p_115312_, p_115313_))) return;
        p_115311_.pushPose();
        this.model.attackTime = this.getAttackAnim(p_115308_, p_115310_);

        boolean shouldSit = p_115308_.isPassenger() && (p_115308_.getVehicle() != null && p_115308_.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = p_115308_.isBaby();
        float f = Mth.rotLerp(p_115310_, p_115308_.yBodyRotO, p_115308_.yBodyRot);
        float f1 = Mth.rotLerp(p_115310_, p_115308_.yHeadRotO, p_115308_.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && p_115308_.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)p_115308_.getVehicle();
            f = Mth.rotLerp(p_115310_, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(p_115310_, p_115308_.xRotO, p_115308_.getXRot());
        if (isEntityUpsideDown(p_115308_)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (p_115308_.hasPose(Pose.SLEEPING)) {
            Direction direction = p_115308_.getBedOrientation();
            if (direction != null) {
                float f4 = p_115308_.getEyeHeight(Pose.STANDING) - 0.1F;
                p_115311_.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(p_115308_, p_115310_);
        this.setupRotations(p_115308_, p_115311_, f7, f, p_115310_);
        p_115311_.scale(-1.0F, -1.0F, 1.0F);
        this.scale(p_115308_, p_115311_, p_115310_);
        p_115311_.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && p_115308_.isAlive()) {
            f8 = p_115308_.walkAnimation.speed(p_115310_);
            f5 = p_115308_.walkAnimation.position(p_115310_);
            if (p_115308_.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(p_115308_, f5, f8, p_115310_);
        this.model.setupAnim(p_115308_, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(p_115308_);
        boolean flag1 = !flag && !p_115308_.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(p_115308_);
        RenderType rendertype = this.getRenderType(p_115308_, flag, flag1, flag2);
        if (rendertype != null) {
            VertexConsumer vertexconsumer = p_115312_.getBuffer(rendertype);
            int i = getOverlayCoords(p_115308_, this.getWhiteOverlayProgress(p_115308_, p_115310_));
            float initial = TweaksConfig.PhantasmicPhantoms.get() ? 0.5F : 1.0F;
            this.model.renderToBuffer(p_115311_, vertexconsumer, p_115313_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : initial);
        }

        if (!p_115308_.isSpectator()) {
            for(RenderLayer<Phantom, PhantomModel<Phantom>> renderlayer : this.layers) {
                renderlayer.render(p_115311_, p_115312_, p_115313_, p_115308_, f5, f8, p_115310_, f7, f2, f6);
            }
        }

        p_115311_.popPose();
        this.nameTagRender(p_115308_, p_115310_, p_115311_, p_115312_, p_115313_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(p_115308_, this, p_115310_, p_115311_, p_115312_, p_115313_));
    }

    public void nameTagRender(Phantom p_114485_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(p_114485_, p_114485_.getDisplayName(), this, p_114488_, p_114489_, p_114490_, p_114487_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(p_114485_))) {
            this.renderNameTag(p_114485_, renderNameTagEvent.getContent(), p_114488_, p_114489_, p_114490_);
        }
    }

    @Nullable
    protected RenderType getRenderType(Phantom p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        ResourceLocation resourcelocation = this.getTextureLocation(p_115322_);
        if (p_115324_ || TweaksConfig.PhantasmicPhantoms.get()) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : null;
        }
    }

    protected void scale(Phantom p_115681_, PoseStack p_115682_, float p_115683_) {
        int i = p_115681_.getPhantomSize();
        float f = 1.0F + 0.15F * (float)i;
        p_115682_.scale(f, f, f);
        p_115682_.translate(0.0F, 1.3125F, 0.1875F);
    }

    protected void setupRotations(Phantom p_115685_, PoseStack p_115686_, float p_115687_, float p_115688_, float p_115689_) {
        super.setupRotations(p_115685_, p_115686_, p_115687_, p_115688_, p_115689_);
        p_115686_.mulPose(Axis.XP.rotationDegrees(p_115685_.getXRot()));
    }
}