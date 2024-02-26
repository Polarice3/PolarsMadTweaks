package com.Polarice3.MadTweaks.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;

import javax.annotation.Nullable;
import java.util.List;

public class CreepGoal extends Goal {
   private final Creeper creeper;
   @Nullable
   private LivingEntity target;
   private final double speedModifier;
   private int timeToRecalcPath;

   public CreepGoal(Creeper p_25319_, double p_25320_) {
      this.creeper = p_25319_;
      this.speedModifier = p_25320_;
   }

   public boolean canUse() {
      List<? extends LivingEntity> list = this.creeper.level().getEntitiesOfClass(LivingEntity.class, this.creeper.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
      LivingEntity target = null;
      double d0 = Double.MAX_VALUE;

      for(LivingEntity target1 : list) {
         if (target1.isBaby()) {
            double d1 = this.creeper.distanceToSqr(target1);
            if (!(d1 > d0)) {
               d0 = d1;
               target = target1;
            }
         }
      }

      if (target == null) {
         return false;
      } else if (d0 < 9.0D) {
         return false;
      } else {
         this.target = target;
         return true;
      }
   }

   public boolean canContinueToUse() {
      if (this.target == null) {
         return false;
      } else if (!this.target.isAlive()) {
         return false;
      } else {
         double d0 = this.creeper.distanceToSqr(this.target);
         return !(d0 < 9.0D) && !(d0 > 256.0D);
      }
   }

   public void start() {
      this.timeToRecalcPath = 0;
   }

   public void stop() {
      this.target = null;
   }

   public void tick() {
      if (this.target != null) {
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.creeper.getNavigation().moveTo(this.target, this.speedModifier);
         }
      }
   }
}