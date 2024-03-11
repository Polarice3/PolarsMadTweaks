package com.Polarice3.MadTweaks;

import com.Polarice3.MadTweaks.client.ClientProxy;
import com.Polarice3.MadTweaks.common.CommonProxy;
import com.Polarice3.MadTweaks.common.entities.ModMagmaCube;
import com.Polarice3.MadTweaks.common.entities.ModSilverfish;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import com.Polarice3.MadTweaks.common.network.ModNetwork;
import com.Polarice3.MadTweaks.common.world.ModMobSpawnBiomeModifier;
import com.Polarice3.MadTweaks.init.ModProxy;
import com.Polarice3.MadTweaks.init.ModSounds;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(MadTweaks.MOD_ID)
public class MadTweaks {
    public static final String MOD_ID = "polars_mad_tweaks";
    public static ModProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public MadTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        TweaksEntityTypes.ENTITY_TYPE.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupEntityAttributeCreation);
        modEventBus.addListener(this::setupEntityAttributeModify);
        modEventBus.addListener(this::SpawnPlacementEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TweaksConfig.SPEC, "mad-tweaks.toml");
        TweaksConfig.loadConfig(TweaksConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("mad-tweaks.toml").toString());

        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MadTweaks.MOD_ID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("mob_spawns", ModMobSpawnBiomeModifier::makeCodec);

        MinecraftForge.EVENT_BUS.register(this);
        ModSounds.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.init();
    }

    private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(TweaksEntityTypes.MAGMA_CUBE.get(), ModMagmaCube.createAttributes().build());
        event.put(TweaksEntityTypes.COBBLED_CUBE.get(), ModMagmaCube.createAttributes().build());
        event.put(TweaksEntityTypes.SILVERFISH.get(), ModSilverfish.createAttributes().build());
    }

    private void setupEntityAttributeModify(final EntityAttributeModificationEvent event){
        event.getTypes().forEach(entityType -> {
            if (!event.has(entityType, Attributes.ATTACK_DAMAGE)){
                event.add(entityType, Attributes.ATTACK_DAMAGE);
            }
        });
    }

    private void SpawnPlacementEvent(SpawnPlacementRegisterEvent event){
        event.register(TweaksEntityTypes.SILVERFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSilverfish::checkSilverfishSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}
