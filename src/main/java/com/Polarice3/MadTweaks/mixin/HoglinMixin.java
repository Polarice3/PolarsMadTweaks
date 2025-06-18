package com.Polarice3.MadTweaks.mixin;

import com.Polarice3.MadTweaks.TweaksConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Animal implements Enemy, HoglinBase {

    public HoglinMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Inject(
            method = {"isConverting()Z"},
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void isConverting(CallbackInfoReturnable<Boolean> cir){
        if (TweaksConfig.HoglinNightImmunity.get()) {
            if (this.level.isNight()) {
                cir.setReturnValue(false);
            }
        }
    }
}
