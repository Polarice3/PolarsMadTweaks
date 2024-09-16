package com.Polarice3.MadTweaks.common.blocks;

import com.Polarice3.MadTweaks.MadTweaks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TweaksBlocks {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, MadTweaks.MOD_ID);

    public static final RegistryObject<Block> SHATTERED_GLASS = BLOCK.register("shattered_glass", () -> new ShatteredGlassBlock(
            BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.HAT)
            .strength(0.3F)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn(TweaksBlocks::never)
            .isRedstoneConductor(TweaksBlocks::never)
            .isSuffocating(TweaksBlocks::never)
            .isViewBlocking(TweaksBlocks::never)));

    private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return (boolean)false;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

}
