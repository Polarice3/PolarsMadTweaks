package com.Polarice3.MadTweaks.data;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEntityTypeTagsProvider extends TagsProvider<EntityType<?>> {

    public ModEntityTypeTagsProvider(DataGenerator p_126517_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126517_, Registry.ENTITY_TYPE, MadTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TweaksEntityTypes.MAGMA_CUBE.get());
        this.tag(EntityTypeTags.FROG_FOOD).add(TweaksEntityTypes.MAGMA_CUBE.get());
        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(TweaksEntityTypes.SILVERFISH.get());
    }
}
