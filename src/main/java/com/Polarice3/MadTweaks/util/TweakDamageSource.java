package com.Polarice3.MadTweaks.util;

import com.Polarice3.MadTweaks.MadTweaks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class TweakDamageSource extends DamageSource {
    public static ResourceKey<DamageType> SWING = create("swing");

    public TweakDamageSource(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }

    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, MadTweaks.location(name));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
        return getEntityDamageSource(level, type, null);
    }

    public static DamageSource entityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
        return getEntityDamageSource(level, type, attacker);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
        return getIndirectEntityDamageSource(level, type, attacker, attacker);
    }

    public static DamageSource indirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker){
        return getIndirectEntityDamageSource(level, type, attacker, indirectAttacker);
    }

    public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker) {
        return source(level, type, attacker, indirectAttacker);
    }

    public static DamageSource source(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker){
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }

    public static DamageSource swing(LivingEntity pMob) {
        return TweakDamageSource.entityDamageSource(pMob.level, SWING, pMob);
    }

    public static String source(String source){
        return "polars_mad_tweaks." + source;
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(SWING, new DamageType("mob", 0.1F));
    }
}
