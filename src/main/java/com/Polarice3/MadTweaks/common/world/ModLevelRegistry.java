package com.Polarice3.MadTweaks.common.world;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLevelRegistry {

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (TweaksConfig.FishySilverfish.get()) {
            if (biome.containsTag(Tags.Biomes.IS_WATER) && !biome.containsTag(Tags.Biomes.IS_MUSHROOM) && !biome.is(Biomes.DEEP_DARK)
                    && !biome.is(biomeResourceKey -> biomeResourceKey.registry().getNamespace().contains("alexscaves"))) {
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(TweaksEntityTypes.SILVERFISH.get(), 100, 4, 4));
            }
        }
    }
}
