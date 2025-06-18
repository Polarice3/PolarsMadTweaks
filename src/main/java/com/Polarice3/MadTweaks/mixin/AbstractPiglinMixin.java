package com.Polarice3.MadTweaks.mixin;

import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.util.MathHelper;
import com.Polarice3.MadTweaks.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster {

    public AbstractPiglinMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(
            method = {"isConverting()Z"},
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void isConverting(CallbackInfoReturnable<Boolean> cir){
        if (TweaksConfig.PiglinNightImmunity.get()) {
            if (MobUtils.isNight(this.level)) {
                cir.setReturnValue(false);
            }
        }
    }
}
