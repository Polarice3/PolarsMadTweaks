package com.Polarice3.MadTweaks.data;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public ModEntityTypeTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, Registries.ENTITY_TYPE, p_256572_, (p_256665_) -> p_256665_.builtInRegistryHolder().key(), MadTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(TweaksEntityTypes.MAGMA_CUBE.get());
        this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TweaksEntityTypes.MAGMA_CUBE.get());
        this.tag(EntityTypeTags.FROG_FOOD).add(TweaksEntityTypes.MAGMA_CUBE.get());
        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(TweaksEntityTypes.SILVERFISH.get());
    }
}
