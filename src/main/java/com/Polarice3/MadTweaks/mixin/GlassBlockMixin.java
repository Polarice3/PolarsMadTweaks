package com.Polarice3.MadTweaks.mixin;

import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.common.blocks.TweaksBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(GlassBlock.class)
public abstract class GlassBlockMixin extends AbstractGlassBlock {
    protected GlassBlockMixin(Properties p_48729_) {
        super(p_48729_);
    }

    @Override
    public void playerDestroy(Level p_54157_, Player p_54158_, BlockPos p_54159_, BlockState p_54160_, @Nullable BlockEntity p_54161_, ItemStack p_54162_) {
        super.playerDestroy(p_54157_, p_54158_, p_54159_, p_54160_, p_54161_, p_54162_);
        if (TweaksConfig.ShatteredGlass.get()) {
            if (!EnchantmentHelper.hasSilkTouch(p_54162_)) {
                BlockState blockstate = TweaksBlocks.SHATTERED_GLASS.get().defaultBlockState();
                if (blockstate.canSurvive(p_54157_, p_54159_)) {
                    p_54157_.setBlockAndUpdate(p_54159_, blockstate);
                }
            }
        }

    }
}
