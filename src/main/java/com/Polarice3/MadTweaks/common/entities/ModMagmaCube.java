package com.Polarice3.MadTweaks.common.entities;

import com.Polarice3.MadTweaks.init.ModSounds;
import com.Polarice3.MadTweaks.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.Nullable;

public class ModMagmaCube extends MagmaCube {
    public ModMagmaCube(EntityType<? extends MagmaCube> p_32968_, Level p_32969_) {
        super(p_32968_, p_32969_);
    }

    protected Component getTypeName() {
        return EntityType.MAGMA_CUBE.getDescription();
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return EntityType.MAGMA_CUBE.getDefaultLootTable();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()){
            CobbledCube newMob = this.convertTo(TweaksEntityTypes.COBBLED_CUBE.get(), true);
            if (newMob != null) {
                newMob.setXRot(this.getXRot());
                newMob.setYRot(this.getYRot());
                newMob.setSize(this.getSize(), true);
                newMob.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, this.getVoicePitch());
            }
        }
        if (this.level.random.nextInt(4096) == 0 && this.getSize() > 1) {
            int i = this.getRandom().nextInt(3);
            BlockPos blockpos = this.blockPosition();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
                if (i > 0) {
                    for (int j = 0; j < i; ++j) {
                        blockpos = blockpos.offset(this.getRandom().nextInt(3) - 1, 1, this.getRandom().nextInt(3) - 1);
                        if (this.level.isLoaded(blockpos)) {
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            if (blockstate.isAir()) {
                                if (BlockUtils.hasFlammableNeighbours(this.level, blockpos)) {
                                    this.level.setBlockAndUpdate(blockpos, Blocks.FIRE.defaultBlockState());
                                }
                            }
                        }
                    }
                } else {
                    for (int k = 0; k < 3; ++k) {
                        BlockPos blockpos1 = this.blockPosition().offset(this.getRandom().nextInt(3) - 1, 0, this.getRandom().nextInt(3) - 1);
                        if (this.level.isLoaded(blockpos1)) {
                            if (this.level.isEmptyBlock(blockpos1.above()) && BlockUtils.isFlammable(this.level, blockpos1, Direction.UP)) {
                                this.level.setBlockAndUpdate(blockpos1.above(), Blocks.FIRE.defaultBlockState());
                            }
                        }
                    }
                }
            }
            if (this.level instanceof ServerLevel serverLevel) {
                for (int k = 0; k < this.getSize(); ++k) {
                    BlockPos blockpos1 = this.blockPosition().offset(this.getRandom().nextInt(3) - 1, 0, this.getRandom().nextInt(3) - 1);
                    if (this.level.isLoaded(blockpos1)) {
                        BlockState blockstate = this.level.getBlockState(blockpos1);
                        if (blockstate.is(Blocks.SNOW)){
                            Block.dropResources(blockstate, serverLevel, blockpos1);
                            serverLevel.removeBlock(blockpos1, false);
                        }
                        if (blockstate.is(Blocks.ICE)){
                            if (serverLevel.dimensionType().ultraWarm()) {
                                serverLevel.removeBlock(blockpos1, false);
                            } else {
                                serverLevel.setBlockAndUpdate(blockpos1, Blocks.WATER.defaultBlockState());
                                serverLevel.neighborChanged(blockpos1, Blocks.WATER.defaultBlockState().getBlock(), blockpos1);
                            }
                        }
                    }
                }
            }
        }
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

    protected int decreaseAirSupply(int p_28882_) {
        return p_28882_;
    }

    public void push(Entity entity) {
        super.push(entity);
        if (this.isDealsDamage()) {
            if (this.getTarget() != null && entity == this.getTarget()) {
                this.dealDamage(this.getTarget());
            }
        }
    }

    public boolean canBeCollidedWith() {
        return !this.isTiny();
    }

    protected void dealDamage(LivingEntity target) {
        if (this.isAlive()) {
            int i = this.getSize();
            if (this.distanceToSqr(target) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(target) && target.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(ModSounds.MAGMA_CUBE_LAND.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, target);
                if (!target.fireImmune()){
                    target.setSecondsOnFire(1 + this.getSize());
                }
            }
        }

    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MAGMA_CUBE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_32992_) {
        return ModSounds.MAGMA_CUBE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.MAGMA_CUBE_DEATH.get();
    }

    protected SoundEvent getSquishSound() {
        return ModSounds.MAGMA_CUBE_LAND.get();
    }

    protected SoundEvent getJumpSound() {
        return ModSounds.MAGMA_CUBE_JUMP.get();
    }
}
