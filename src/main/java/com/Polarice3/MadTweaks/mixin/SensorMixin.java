package com.Polarice3.MadTweaks.mixin;

import com.Polarice3.MadTweaks.TweaksConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Sensor.class)
public class SensorMixin<T extends LivingEntity> {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensor;doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER), method = "tick")
    private void mixinDoTick(ServerLevel serverLevel, T entity, CallbackInfo callbackInfo){
        if (entity instanceof AbstractPiglin piglin){
            Brain<?> brain = piglin.getBrain();

            Optional<Mob> mob = Optional.empty();

            NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

            for(LivingEntity livingEntity : nearestvisiblelivingentities.findAll((le) -> true)){
                if (TweaksConfig.PiglinHateIllagers.get()) {
                    if (livingEntity instanceof AbstractIllager) {
                        mob = Optional.of((Mob) livingEntity);
                    }
                }
                if (TweaksConfig.PiglinHateVillagers.get()) {
                    if (livingEntity instanceof AbstractVillager) {
                        mob = Optional.of((Mob) livingEntity);
                    }
                }
            }

            if (mob.isPresent()){
                brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, mob);
            }
        }
    }
}
