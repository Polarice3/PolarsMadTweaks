package com.Polarice3.MadTweaks.common.entities.ai;

import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class TweakEnderManGoals {

    public static class EndermanFreezeWhenLookedAt extends Goal {
        private final EnderMan enderman;
        @Nullable
        private LivingEntity target;

        public EndermanFreezeWhenLookedAt(EnderMan p_32550_) {
            this.enderman = p_32550_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!TweaksConfig.EndermanEquality.get()){
                return false;
            }
            if (this.target == null){
                return false;
            }
            double d0 = this.target.distanceToSqr(this.enderman);
            return !(d0 > 256.0D) && MobUtils.isLookingAtEnderMan(this.enderman, this.target);
        }

        public void start() {
            this.enderman.getNavigation().stop();
        }

        public void tick() {
            if (this.target == null){
                return;
            }
            this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }

    public static class EndermanLookForTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        private final EnderMan enderman;
        @Nullable
        private LivingEntity pendingTarget;
        private int aggroTime;
        private int teleportTime;
        private final TargetingConditions startAggroTargetConditions;
        private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

        public EndermanLookForTargetGoal(EnderMan p_32573_, @Nullable Predicate<LivingEntity> p_32574_) {
            super(p_32573_, LivingEntity.class, 10, false, false, p_32574_);
            this.enderman = p_32573_;
            this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector((p_32578_) -> {
                return MobUtils.isLookingAtEnderMan(p_32573_, p_32578_) && !(p_32578_ instanceof EnderMan) && !(p_32578_ instanceof SnowGolem snowGolem && snowGolem.hasPumpkin());
            });
        }

        public boolean canUse() {
            if (this.enderman.level instanceof ServerLevel serverLevel){
                List<LivingEntity> entities = new ArrayList<>();
                for (Entity entity : serverLevel.getAllEntities()){
                    if (entity instanceof LivingEntity living) {
                        entities.add(living);
                    }
                }
                this.pendingTarget = serverLevel.getNearestEntity(entities, this.startAggroTargetConditions, this.enderman, this.enderman.getX(), this.enderman.getY(), this.enderman.getZ());
            }
            if (!TweaksConfig.EndermanEquality.get()){
                return false;
            }
            return this.pendingTarget != null;
        }

        public void start() {
            this.aggroTime = this.adjustedTickDelay(5);
            this.teleportTime = 0;
            this.enderman.setBeingStaredAt();
        }

        public void stop() {
            this.pendingTarget = null;
            super.stop();
        }

        public boolean canContinueToUse() {
            if (!TweaksConfig.EndermanEquality.get()){
                return false;
            }
            if (this.pendingTarget != null) {
                if (!MobUtils.isLookingAtEnderMan(this.enderman, this.pendingTarget)) {
                    return false;
                } else {
                    this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.target != null && this.continueAggroTargetConditions.test(this.enderman, this.target) || super.canContinueToUse();
            }
        }

        public void tick() {
            if (this.enderman.getTarget() == null) {
                super.setTarget((LivingEntity)null);
            }

            if (this.pendingTarget != null) {
                if (--this.aggroTime <= 0) {
                    this.target = this.pendingTarget;
                    this.pendingTarget = null;
                    super.start();
                }
            } else {
                if (this.target != null && !this.enderman.isPassenger()) {
                    if (MobUtils.isLookingAtEnderMan(this.enderman, this.target)) {
                        if (this.target.distanceToSqr(this.enderman) < 16.0D) {
                            this.teleport();
                        }

                        this.teleportTime = 0;
                    } else if (this.target.distanceToSqr(this.enderman) > 256.0D && this.teleportTime++ >= this.adjustedTickDelay(30) && this.teleportTowards(this.target)) {
                        this.teleportTime = 0;
                    }
                }

                super.tick();
            }

        }

        protected boolean teleport() {
            if (!this.enderman.level.isClientSide() && this.enderman.isAlive()) {
                double d0 = this.enderman.getX() + (this.enderman.getRandom().nextDouble() - 0.5D) * 64.0D;
                double d1 = this.enderman.getY() + (double)(this.enderman.getRandom().nextInt(64) - 32);
                double d2 = this.enderman.getZ() + (this.enderman.getRandom().nextDouble() - 0.5D) * 64.0D;
                return this.teleport(d0, d1, d2);
            } else {
                return false;
            }
        }

        protected boolean teleportTowards(Entity p_32501_) {
            Vec3 vec3 = new Vec3(this.enderman.getX() - p_32501_.getX(), this.enderman.getY(0.5D) - p_32501_.getEyeY(), this.enderman.getZ() - p_32501_.getZ());
            vec3 = vec3.normalize();
            double d0 = 16.0D;
            double d1 = this.enderman.getX() + (this.enderman.getRandom().nextDouble() - 0.5D) * 8.0D - vec3.x * 16.0D;
            double d2 = this.enderman.getY() + (double)(this.enderman.getRandom().nextInt(16) - 8) - vec3.y * 16.0D;
            double d3 = this.enderman.getZ() + (this.enderman.getRandom().nextDouble() - 0.5D) * 8.0D - vec3.z * 16.0D;
            return this.teleport(d1, d2, d3);
        }

        protected boolean teleport(double p_32544_, double p_32545_, double p_32546_) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_, p_32545_, p_32546_);

            while(blockpos$mutableblockpos.getY() > this.enderman.level.getMinBuildHeight() && !this.enderman.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
                blockpos$mutableblockpos.move(Direction.DOWN);
            }

            BlockState blockstate = this.enderman.level.getBlockState(blockpos$mutableblockpos);
            boolean flag = blockstate.getMaterial().blocksMotion();
            boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
            if (flag && !flag1) {
                net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this.enderman, p_32544_, p_32545_, p_32546_);
                if (event.isCanceled()) return false;
                Vec3 vec3 = this.enderman.position();
                boolean flag2 = this.enderman.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2) {
                    this.enderman.level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this.enderman));
                    if (!this.enderman.isSilent()) {
                        this.enderman.level.playSound((Player)null, this.enderman.xo, this.enderman.yo, this.enderman.zo, SoundEvents.ENDERMAN_TELEPORT, this.enderman.getSoundSource(), 1.0F, 1.0F);
                        this.enderman.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }

                return flag2;
            } else {
                return false;
            }
        }
    }
}
