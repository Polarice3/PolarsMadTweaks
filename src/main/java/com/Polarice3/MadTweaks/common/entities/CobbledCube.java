package com.Polarice3.MadTweaks.common.entities;

import com.Polarice3.MadTweaks.init.ModSounds;
import com.Polarice3.MadTweaks.util.MobUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class CobbledCube extends Slime {
    public CobbledCube(EntityType<? extends Slime> p_33588_, Level p_33589_) {
        super(p_33588_, p_33589_);
    }

    protected Component getTypeName() {
        return EntityType.MAGMA_CUBE.getDescription();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()){
            this.onInsideBubbleColumn(true);
            if (this.fallDistance > 1 && this.verticalCollisionBelow){
                this.die(this.damageSources().drown());
            }
        }
    }

    public float getFluidFallDistanceModifier(FluidType type) {
        return 1.0F;
    }

    public void resetFallDistance() {
        if (!this.isInWater()){
            super.resetFallDistance();
        }
    }

    protected ParticleOptions getParticleType() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.COBBLESTONE.defaultBlockState());
    }

    public void remove(Entity.RemovalReason p_149847_) {
        this.setRemoved(p_149847_);
        this.invalidateCaps();
    }

    @Override
    public void die(DamageSource p_21014_) {
        if (!this.isRemoved() && !this.dead) {
            if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                int l = this.getSize();
                l = Mth.floor(Math.tan(0.3D * l) * Math.PI * 1.5D);
                for (int i = 0; i < l; ++i) {
                    this.spawnAtLocation(Items.COBBLESTONE);
                }
            }
            Entity entity = p_21014_.getEntity();
            LivingEntity livingentity = this.getKillCredit();
            if (this.deathScore >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.deathScore, p_21014_);
            }

            if (this.isSleeping()) {
                this.stopSleeping();
            }

            this.dead = true;
            this.getCombatTracker().recheckStatus();
            Level level = this.level();
            if (level instanceof ServerLevel serverlevel) {
                if (entity == null || entity.killedEntity(serverlevel, this)) {
                    this.gameEvent(GameEvent.ENTITY_DIE);
                    this.dropAllDeathLoot(p_21014_);
                }

                this.level().broadcastEntityEvent(this, (byte)3);
                this.level().broadcastEntityEvent(this, (byte)60);
            }
            this.playSound(SoundEvents.STONE_BREAK, 1.0F, this.getVoicePitch());
            this.discard();
        }
    }

    protected int decreaseAirSupply(int p_28882_) {
        return p_28882_;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean hasLineOfSight(Entity p_147185_) {
        return false;
    }

    protected void dealDamage(LivingEntity target) {
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (MobUtils.toolAttack(damageSource, item -> item instanceof PickaxeItem)){
            amount *= 2.0F;
        } else if (!damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)){
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    @Override
    public void knockback(double p_147241_, double p_147242_, double p_147243_) {
    }

    @Nullable
    public ItemStack getPickResult() {
        SpawnEggItem spawneggitem = ForgeSpawnEggItem.fromEntityType(EntityType.MAGMA_CUBE);
        return spawneggitem == null ? null : new ItemStack(spawneggitem);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance instance) {
        if (instance.getEffect() == MobEffects.POISON || instance.getEffect() == MobEffects.WITHER
                || instance.getEffect() == MobEffects.REGENERATION || instance.getEffect() == MobEffects.HUNGER
                || instance.getEffect() == MobEffects.SATURATION) {
            MobEffectEvent.Applicable event = new MobEffectEvent.Applicable(this, instance);
            MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(instance);
    }

    protected SoundEvent getHurtSound(DamageSource p_32992_) {
        return SoundEvents.STONE_BREAK;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STONE_BREAK;
    }

    protected SoundEvent getSquishSound() {
        return ModSounds.MAGMA_CUBE_LAND.get();
    }

    protected SoundEvent getJumpSound() {
        return ModSounds.MAGMA_CUBE_JUMP.get();
    }
}
