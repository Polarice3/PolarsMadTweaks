package com.Polarice3.MadTweaks.common.entities.ai;

import com.Polarice3.MadTweaks.TweaksConfig;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Chicken;

public class AnimalAttackGoal extends MeleeAttackGoal {
    public AnimalAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_, p_25553_, p_25554_);
    }

    public boolean canUse() {
        return super.canUse()
                && ((TweaksConfig.LivestockRetaliation.get()
                || TweaksConfig.LivestockRandomHostile.get())
                || (this.mob instanceof Chicken && TweaksConfig.ChickenJockeyAttack.get()))
                && this.mob.getAttribute(Attributes.ATTACK_DAMAGE) != null;
    }

    public boolean canContinueToUse() {
        if (this.mob.getAttribute(Attributes.ATTACK_DAMAGE) == null){
            return false;
        }
        if (this.mob.getTarget() != null && !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.mob.getTarget())){
            return false;
        }
        return super.canContinueToUse();
    }
}
