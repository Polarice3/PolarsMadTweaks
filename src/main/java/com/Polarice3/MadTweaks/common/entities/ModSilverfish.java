package com.Polarice3.MadTweaks.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ModSilverfish extends Silverfish {
    @Nullable
    private ModSilverfish.SilverfishWakeUpFriendsGoal friendsGoal;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;

    public ModSilverfish(EntityType<? extends Silverfish> p_33523_, Level p_33524_) {
        super(p_33523_, p_33524_);
        this.moveControl = new FishMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, p_33524_);
        this.groundNavigation = new GroundPathNavigation(this, p_33524_);
    }

    protected void registerGoals() {
        this.friendsGoal = new ModSilverfish.SilverfishWakeUpFriendsGoal(this);
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(3, this.friendsGoal);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new ModSilverfish.SilverfishMergeWithStoneGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected Component getTypeName() {
        return EntityType.SILVERFISH.getDescription();
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return EntityType.SILVERFISH.getDefaultLootTable();
    }

    public boolean checkSpawnObstruction(LevelReader p_32829_) {
        return p_32829_.isUnobstructed(this);
    }

    public static boolean checkSilverfishSpawnRules(EntityType<ModSilverfish> p_218956_, ServerLevelAccessor p_218957_, MobSpawnType p_218958_, BlockPos p_218959_, RandomSource p_218960_) {
        if (!p_218957_.getFluidState(p_218959_.below()).is(FluidTags.WATER)) {
            return false;
        } else {
            boolean flag = p_218957_.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(p_218957_, p_218959_, p_218960_) && (p_218958_ == MobSpawnType.SPAWNER || p_218957_.getFluidState(p_218959_).is(FluidTags.WATER));
            return p_218960_.nextInt(15) == 0 && flag;
        }
    }

    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public boolean hurt(DamageSource p_33527_, float p_33528_) {
        if (this.isInvulnerableTo(p_33527_)) {
            return false;
        } else {
            if ((p_33527_.getEntity() != null || p_33527_.is(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)) && this.friendsGoal != null) {
                this.friendsGoal.notifyHurt();
            }

            return super.hurt(p_33527_, p_33528_);
        }
    }

    public void travel(Vec3 p_32394_) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(0.01F, p_32394_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(p_32394_);
        }

    }

    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.setSwimming(false);
            }
        }

    }

    static class FishMoveControl extends MoveControl {
        private final ModSilverfish fish;

        FishMoveControl(ModSilverfish p_27501_) {
            super(p_27501_);
            this.fish = p_27501_;
        }

        public void tick() {
            LivingEntity livingentity = this.fish.getTarget();
            if (this.fish.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.fish.getY()) {
                    this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
                    this.fish.setSpeed(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.fish.getX();
                double d1 = this.wantedY - this.fish.getY();
                double d2 = this.wantedZ - this.fish.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.fish.setYRot(this.rotlerp(this.fish.getYRot(), f, 90.0F));
                this.fish.yBodyRot = this.fish.getYRot();
                float f1 = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.fish.getSpeed(), f1);
                this.fish.setSpeed(f2);
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                if (!this.fish.onGround()) {
                    this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }

                super.tick();
            }
        }
    }

    static class SilverfishMergeWithStoneGoal extends RandomStrollGoal {
        @Nullable
        private Direction selectedDirection;
        private boolean doMerge;

        public SilverfishMergeWithStoneGoal(Silverfish p_33558_) {
            super(p_33558_, 1.0D, 10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.getNavigation().isDone()) {
                return false;
            } else if (!this.mob.isInWater()){
                return false;
            } else {
                RandomSource randomsource = this.mob.getRandom();
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.mob.level(), this.mob) && randomsource.nextInt(reducedTickDelay(10)) == 0) {
                    this.selectedDirection = Direction.getRandom(randomsource);
                    BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
                    BlockState blockstate = this.mob.level().getBlockState(blockpos);
                    if (InfestedBlock.isCompatibleHostBlock(blockstate)) {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.canUse();
            }
        }

        public boolean canContinueToUse() {
            return this.doMerge ? false : super.canContinueToUse();
        }

        public void start() {
            if (!this.doMerge) {
                super.start();
            } else {
                LevelAccessor levelaccessor = this.mob.level();
                BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
                BlockState blockstate = levelaccessor.getBlockState(blockpos);
                if (InfestedBlock.isCompatibleHostBlock(blockstate)) {
                    levelaccessor.setBlock(blockpos, InfestedBlock.infestedStateByHost(blockstate), 3);
                    this.mob.spawnAnim();
                    this.mob.discard();
                }

            }
        }
    }

    static class SilverfishWakeUpFriendsGoal extends Goal {
        private final Silverfish silverfish;
        private int lookForFriends;

        public SilverfishWakeUpFriendsGoal(Silverfish p_33565_) {
            this.silverfish = p_33565_;
        }

        public void notifyHurt() {
            if (this.lookForFriends == 0) {
                this.lookForFriends = this.adjustedTickDelay(20);
            }

        }

        public boolean canUse() {
            return this.lookForFriends > 0;
        }

        public void tick() {
            --this.lookForFriends;
            if (this.lookForFriends <= 0) {
                Level level = this.silverfish.level();
                RandomSource randomsource = this.silverfish.getRandom();
                BlockPos blockpos = this.silverfish.blockPosition();

                for(int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i) {
                    for(int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j) {
                        for(int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k) {
                            BlockPos blockpos1 = blockpos.offset(j, i, k);
                            BlockState blockstate = level.getBlockState(blockpos1);
                            Block block = blockstate.getBlock();
                            if (block instanceof InfestedBlock) {
                                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level, this.silverfish)) {
                                    level.destroyBlock(blockpos1, true, this.silverfish);
                                } else {
                                    level.setBlock(blockpos1, ((InfestedBlock)block).hostStateByInfested(level.getBlockState(blockpos1)), 3);
                                }

                                if (randomsource.nextBoolean()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
