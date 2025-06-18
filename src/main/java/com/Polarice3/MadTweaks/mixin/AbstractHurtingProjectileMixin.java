package com.Polarice3.MadTweaks.mixin;

import com.Polarice3.MadTweaks.TweaksConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHurtingProjectile.class)
public abstract class AbstractHurtingProjectileMixin extends Projectile {

    public AbstractHurtingProjectileMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Inject(
            method = {"canHitEntity(Lnet/minecraft/world/entity/Entity;)Z"},
            at = @At(value = "HEAD"),
            cancellable = true
    )
    protected void canHitEntity(Entity pEntity, CallbackInfoReturnable<Boolean> callback) {
        if (pEntity.noPhysics) {
            if (TweaksConfig.PhantasmicPhantoms.get()) {
                if (pEntity instanceof Phantom) {
                    callback.setReturnValue(true);
                }
            }
            if (TweaksConfig.GhostlyGhast.get()) {
                if (pEntity instanceof Ghast) {
                    callback.setReturnValue(true);
                }
            }
        }
    }
}
