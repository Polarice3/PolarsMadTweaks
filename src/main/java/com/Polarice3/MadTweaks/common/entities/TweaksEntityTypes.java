package com.Polarice3.MadTweaks.common.entities;

import com.Polarice3.MadTweaks.MadTweaks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TweaksEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MadTweaks.MOD_ID);

    public static final RegistryObject<EntityType<TweakedTrident>> TWEAKED_TRIDENT = register("trident",
            EntityType.Builder.<TweakedTrident>of(TweakedTrident::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20));

    public static final RegistryObject<EntityType<ModMagmaCube>> MAGMA_CUBE = register("magma_cube",
            EntityType.Builder.of(ModMagmaCube::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(2.04F, 2.04F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<CobbledCube>> COBBLED_CUBE = register("cobbled_cube",
            EntityType.Builder.of(CobbledCube::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(2.04F, 2.04F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<ModSilverfish>> SILVERFISH = register("silverfish",
            EntityType.Builder.of(ModSilverfish::new, MobCategory.MONSTER)
                    .sized(0.4F, 0.3F)
                    .clientTrackingRange(8));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return ENTITY_TYPE.register(p_20635_, () -> p_20636_.build(MadTweaks.location(p_20635_).toString()));
    }
}
