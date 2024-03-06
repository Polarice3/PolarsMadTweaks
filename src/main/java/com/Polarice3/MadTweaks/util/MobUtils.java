package com.Polarice3.MadTweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MobUtils {

    public static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public SpiderTargetGoal(Spider p_33832_, Class<T> p_33833_) {
            super(p_33832_, p_33833_, true);
        }

        public SpiderTargetGoal(Spider p_33832_, Class<T> p_33833_, Predicate<LivingEntity> p_199894_) {
            super(p_33832_, p_33833_, true, p_199894_);
        }

        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return !(f >= 0.5F) && super.canUse();
        }
    }

    public static boolean isInFire(LivingEntity livingEntity){
        AABB axisalignedbb = livingEntity.getBoundingBox();
        BlockPos blockpos = BlockPos.containing(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = BlockPos.containing(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        if (livingEntity.level().hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = livingEntity.level().getBlockState(blockpos$mutable);
                        if (blockstate.getBlock() instanceof BaseFireBlock){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static List<Entity> pushedBy(LivingEntity livingEntity){
        return livingEntity.level().getEntities(livingEntity, livingEntity.getBoundingBox(), EntitySelector.pushableBy(livingEntity));
    }

    public static boolean isPushed(LivingEntity livingEntity){
        return !pushedBy(livingEntity).isEmpty() && livingEntity.isPushable();
    }

    public static boolean isMoving(Entity entity) {
        return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public static boolean physicalAttacks(DamageSource source){
        return source.getDirectEntity() != null && source.getDirectEntity() instanceof LivingEntity
                && (source.getMsgId().equals("mob") || source.getMsgId().equals("player"));
    }

    public static boolean toolAttack(DamageSource source, Predicate<Item> item){
        if (physicalAttacks(source)) {
            if (source.getDirectEntity() instanceof LivingEntity living) {
                return item.test(living.getMainHandItem().getItem());
            }
        }
        return false;
    }

    public static boolean isHurt(LivingEntity livingEntity){
        return livingEntity.getHealth() > 0.0F && livingEntity.getHealth() < livingEntity.getMaxHealth();
    }

    public static void mobDamagedItem(ItemStack itemStack, RandomSource randomSource){
        if (itemStack.isDamageableItem()) {
            itemStack.setDamageValue(itemStack.getMaxDamage() - randomSource.nextInt(1 + randomSource.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
        }
    }

    public static List<BlockState> surroundingBlocks(LivingEntity livingEntity, Predicate<BlockState> blockPredicate){
        List<BlockState> blockStates = new ArrayList<>();
        AABB axisalignedbb = livingEntity.getBoundingBox();
        BlockPos blockpos = BlockPos.containing(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = BlockPos.containing(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        if (livingEntity.level().hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = livingEntity.level().getBlockState(blockpos$mutable);
                        if (blockPredicate.test(blockstate)){
                            blockStates.add(blockstate);
                        }
                    }
                }
            }
        }
        return blockStates;
    }

    public static boolean isInBlock(LivingEntity livingEntity, Predicate<BlockState> blockPredicate){
        return !surroundingBlocks(livingEntity, blockPredicate).isEmpty();
    }

    public static LootParams.Builder createLootContext(DamageSource pDamageSource, LivingEntity livingEntity) {
        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)livingEntity.level())).withParameter(LootContextParams.THIS_ENTITY, livingEntity).withParameter(LootContextParams.ORIGIN, livingEntity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
        if (livingEntity.getLastHurtByMob() != null && livingEntity.getLastHurtByMob() instanceof Player player) {
            lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }

        return lootparams$builder;
    }

    public static boolean isSunBurnTick(Entity entity) {
        if (entity.level().isDay() && !entity.level().isClientSide) {
            float f = entity.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean flag = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            return f > 0.5F && entity.level().random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag && entity.level().canSeeSky(blockpos);
        }
        return false;
    }

    public static void addParticlesAroundSelf(ServerLevel serverLevel, ParticleOptions particleOptions, Entity entity){
        for(int i = 0; i < 5; ++i) {
            double d0 = serverLevel.random.nextGaussian() * 0.02D;
            double d1 = serverLevel.random.nextGaussian() * 0.02D;
            double d2 = serverLevel.random.nextGaussian() * 0.02D;
            serverLevel.sendParticles(particleOptions, entity.getRandomX(1.0D), entity.getRandomY() + 1.0D, entity.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
        }
    }

    public static boolean isInBlock(Entity entity, Predicate<BlockState> blockState){
        float f = entity.getBbWidth() * 0.8F;
        AABB aabb = AABB.ofSize(entity.getEyePosition(), (double)f, 1.0E-6D, (double)f);

        return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = entity.level().getBlockState(p_201942_);
            return blockState.test(blockstate) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(entity.level(), p_201942_).move((double)p_201942_.getX(), (double)p_201942_.getY(), (double)p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }

    public static void ClimbAnyWall(LivingEntity livingEntity){
        Vec3 movement = livingEntity.getDeltaMovement();
        if (livingEntity instanceof Player player){
            if (!player.getAbilities().flying && player.horizontalCollision){
                movement = new Vec3(movement.x, 0.2D, movement.z);
            }
            player.setDeltaMovement(movement);
        } else {
            if (livingEntity.horizontalCollision){
                movement = new Vec3(movement.x, 0.2D, movement.z);
            }
            livingEntity.setDeltaMovement(movement);
        }
    }

    public static List<EntityType<?>> getEntityTypesConfig(List<? extends String> config){
        List<EntityType<?>> list = new ArrayList<>();
        if (!config.isEmpty()){
            for (String id : config){
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(id));
                if (entityType != null){
                    list.add(entityType);
                }
            }
        }
        return list;
    }

    public static boolean hasEntityTypesConfig(List<? extends String> config, EntityType<?> entityType){
        return !getEntityTypesConfig(config).isEmpty() && getEntityTypesConfig(config).contains(entityType);
    }
}
