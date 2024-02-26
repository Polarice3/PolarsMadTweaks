package com.Polarice3.MadTweaks.common.entities.ai;

import com.Polarice3.MadTweaks.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SeekFireGoal extends MoveToBlockGoal {
    private final Blaze blaze;

    public SeekFireGoal(Blaze p_25149_, double p_25150_) {
        super(p_25149_, p_25150_, 16);
        this.blaze = p_25149_;
    }

    @Override
    public boolean canUse() {
        return MobUtils.isHurt(this.blaze) && super.canUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader p_25153_, BlockPos p_25154_) {
        BlockState blockstate = p_25153_.getBlockState(p_25154_);
        return blockstate.getBlock() instanceof BaseFireBlock;
    }
}
