package com.Polarice3.MadTweaks.common.events;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.common.capabilities.tweaks.TweaksCapHelper;
import com.Polarice3.MadTweaks.common.entities.ModMagmaCube;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import com.Polarice3.MadTweaks.common.entities.ai.CreepGoal;
import com.Polarice3.MadTweaks.common.entities.ai.SeekFireGoal;
import com.Polarice3.MadTweaks.util.MathHelper;
import com.Polarice3.MadTweaks.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MadTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TweakEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level world = event.getLevel();
        if (!world.isClientSide()){
            if (entity instanceof LivingEntity livingEntity) {
                if (!TweaksCapHelper.init(livingEntity)){
                    if (livingEntity instanceof RangedAttackMob){
                        TweaksCapHelper.setArrowCount(livingEntity, world.random.nextInt(TweaksConfig.ExtraMobArrowAmount.get() + 1) + TweaksConfig.MinMobArrowAmount.get());
                    }
                    TweaksCapHelper.setInit(livingEntity, true);
                }
                if (livingEntity instanceof Mob mob) {
                    if (TweaksConfig.HungrySpiders.get()) {
                        if (mob instanceof Spider spider) {
                            spider.targetSelector.addGoal(1, new MobUtils.SpiderTargetGoal<>(spider, Animal.class));
                            spider.targetSelector.addGoal(1, new MobUtils.SpiderTargetGoal<>(spider, Spider.class, target -> target.getHealth() < spider.getHealth()));
                        }
                    }
                    if (TweaksConfig.RottenWolves.get()) {
                        if (mob instanceof Wolf wolf) {
                            wolf.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(wolf, Zombie.class, false, null));
                        }
                    }
                    if (TweaksConfig.ViolentPolarBears.get()) {
                        if (mob instanceof PolarBear polarBear) {
                            polarBear.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(polarBear, LivingEntity.class, 0, false, false, (livingEntity1 -> livingEntity1.isAttackable() && !(livingEntity1 instanceof Creeper) && !(livingEntity1 instanceof PolarBear))));
                        }
                    }
                    if (TweaksConfig.BlazeFireHeal.get()){
                        if (mob instanceof Blaze blaze){
                            blaze.goalSelector.addGoal(0, new SeekFireGoal(blaze, 1.0D));
                        }
                    }
                    if (TweaksConfig.TweakedMagmaCube.get()) {
                        if (mob instanceof MagmaCube magmaCube) {
                            if (mob.getType() == EntityType.MAGMA_CUBE) {
                                ModMagmaCube newMob = mob.convertTo(TweaksEntityTypes.MAGMA_CUBE.get(), true);
                                if (newMob != null) {
                                    newMob.setSize(magmaCube.getSize(), true);
                                }
                            }
                        }
                    }
                    if (TweaksConfig.IllagerZombieHate.get()) {
                        if (mob instanceof Zombie zombie) {
                            zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, AbstractIllager.class, false));
                        }
                        if (mob instanceof AbstractIllager illager) {
                            illager.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(illager, Zombie.class, true));
                        }
                    }
                    if (TweaksConfig.FishySilverfish.get()) {
                        if (mob.getType() == EntityType.SILVERFISH) {
                            mob.convertTo(TweaksEntityTypes.SILVERFISH.get(), true);
                        }
                    }
                    if (TweaksConfig.CreeperStalkBaby.get()) {
                        if (mob instanceof Creeper creeper) {
                            creeper.goalSelector.addGoal(5, new CreepGoal(creeper, 1.0D));
                        }
                    }
                    if (TweaksConfig.CatSmallAttack.get()) {
                        if (mob instanceof Cat cat) {
                            cat.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(cat, LivingEntity.class, false, livingEntity1 -> livingEntity1.getBoundingBox().getSize() < cat.getBoundingBox().getSize() && !(livingEntity1 instanceof Cat) && !livingEntity1.isBaby()));
                        }
                    }
                    if (TweaksConfig.MobAvoidsWarden.get()) {
                        if (mob instanceof PathfinderMob pathfinderMob) {
                            if (pathfinderMob.getAttribute(Attributes.MAX_HEALTH) != null) {
                                if (pathfinderMob.getMaxHealth() <= 100) {
                                    pathfinderMob.goalSelector.addGoal(0, new AvoidEntityGoal<>(pathfinderMob, Warden.class, 16.0F, 1.0D, 1.2D));
                                }
                            }
                        }
                    }
                }
            }
            if (TweaksConfig.LimitMobArrows.get()) {
                if (entity instanceof Projectile projectile) {
                    if (projectile.getOwner() instanceof Mob mob) {
                        TweaksCapHelper.decreaseArrow(mob);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingEvents(LivingEvent.LivingTickEvent event){
        LivingEntity livingEntity = event.getEntity();
        if ((livingEntity instanceof AbstractGolem && TweaksConfig.GolemNoBio.get())
                || (livingEntity instanceof Blaze && TweaksConfig.BlazeNoBio.get())) {
            livingEntity.getActiveEffects().removeIf(effectInstance -> {
                MobEffect effect = effectInstance.getEffect();
                return effect == MobEffects.POISON || effect == MobEffects.WITHER
                        || effect == MobEffects.REGENERATION || effect == MobEffects.SATURATION
                        || effect == MobEffects.HUNGER;
            });
        }
        if (TweaksConfig.SleepingHeal.get()) {
            if (livingEntity.isSleeping() && MobUtils.isHurt(livingEntity)) {
                if (livingEntity.tickCount % 100 == 0) {
                    livingEntity.heal(1.0F);
                }
            }
        }
        if (livingEntity instanceof Mob mob) {
            if (TweaksConfig.CowNoEffect.get()) {
                if (mob instanceof Cow) {
                    mob.getActiveEffects().removeIf(effectInstance -> effectInstance.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET)));
                }
            }
            if (TweaksConfig.GoatNoEffect.get()) {
                if (mob instanceof Goat) {
                    mob.getActiveEffects().removeIf(effectInstance -> effectInstance.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET)));
                }
            }
            if (TweaksConfig.LivingMobHeal.get()){
                if (!mob.level().isClientSide) {
                    if (mob.getTarget() == null && MobUtils.isHurt(mob)
                            && mob.canBeAffected(new MobEffectInstance(MobEffects.REGENERATION))){
                        if (mob.tickCount % 100 == 0) {
                            mob.heal(1.0F);
                        }
                    }
                }
            }
            if (TweaksConfig.BlazeFireHeal.get()) {
                if (mob instanceof Blaze blaze) {
                    if (!mob.level().isClientSide) {
                        if (MobUtils.isInFire(blaze)) {
                            if (MobUtils.isHurt(blaze)) {
                                if (blaze.tickCount % 50 == 0) {
                                    blaze.heal(1.0F);
                                }
                            }
                        }
                    }
                }
            }
            if (TweaksConfig.TweakedMagmaCube.get()) {
                if (mob instanceof MagmaCube magmaCube) {
                    if (mob.getType() == EntityType.MAGMA_CUBE) {
                        ModMagmaCube newMob = mob.convertTo(TweaksEntityTypes.MAGMA_CUBE.get(), true);
                        if (newMob != null) {
                            newMob.setSize(magmaCube.getSize(), true);
                        }
                    }
                }
            } else {
                if (mob instanceof ModMagmaCube magmaCube) {
                    if (mob.getType() == TweaksEntityTypes.MAGMA_CUBE.get()) {
                        MagmaCube newMob = mob.convertTo(EntityType.MAGMA_CUBE, true);
                        if (newMob != null) {
                            newMob.setSize(magmaCube.getSize(), true);
                        }
                    }
                }
            }
            if (TweaksConfig.HungerAffectsMobs.get()) {
                if (mob.hasEffect(MobEffects.SATURATION)) {
                    mob.getActiveEffects().removeIf(effectInstance -> {
                        MobEffect effect = effectInstance.getEffect();
                        return effect == MobEffects.HUNGER;
                    });
                }
                if (mob.hasEffect(MobEffects.HUNGER)) {
                    mob.setSprinting(false);
                    MobEffectInstance mobEffectInstance = mob.getEffect(MobEffects.HUNGER);
                    if (mobEffectInstance != null) {
                        int a = mobEffectInstance.getAmplifier();
                        int j = 80 >> a;
                        if (mob.getLastHurtMob() == null) {
                            if (j > 0) {
                                if (mob.tickCount % j == 0) {
                                    mob.hurt(mob.damageSources().starve(), 1.0F);
                                }
                            }
                        }
                    }
                }
            }
            if (TweaksConfig.FishySilverfish.get()) {
                if (mob.getType() == EntityType.SILVERFISH) {
                    mob.convertTo(TweaksEntityTypes.SILVERFISH.get(), true);
                }
            } else {
                if (mob.getType() == TweaksEntityTypes.SILVERFISH.get()) {
                    mob.convertTo(EntityType.SILVERFISH, true);
                }
            }
            if (TweaksConfig.ZombieDecay.get()) {
                if (mob instanceof Zombie zombie) {
                    if (zombie.tickCount >= MathHelper.minutesToTicks(20)) {
                        zombie.convertTo(EntityType.SKELETON, true);
                    }
                }
            }
            if (TweaksConfig.ZombieHorseDecay.get()) {
                if (mob instanceof ZombieHorse zombie) {
                    if (zombie.tickCount >= MathHelper.minutesToTicks(20)) {
                        zombie.convertTo(EntityType.SKELETON_HORSE, true);
                    }
                }
            }
            if (TweaksConfig.LimitMobArrows.get()) {
                if (!mob.level().isClientSide) {
                    if (TweaksCapHelper.init(mob) && TweaksCapHelper.arrowCount(mob) <= 0 && mob.tickCount >= 20) {
                        if (mob.getMainHandItem().is(itemHolder -> itemHolder.get() instanceof ProjectileWeaponItem)) {
                            mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        } else if (mob.getOffhandItem().is(itemHolder -> itemHolder.get() instanceof ProjectileWeaponItem)) {
                            mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                        }
                    }
                }
            }
            if (TweaksConfig.CryingGhast.get()) {
                if (mob instanceof Ghast ghast) {
                    if (ghast.getRandom().nextInt(6000) == 0) {
                        ghast.spawnAtLocation(Items.GHAST_TEAR);
                        ghast.gameEvent(GameEvent.ENTITY_PLACE);
                    }
                }
            }
            if (TweaksConfig.WardenSculkHeal.get()){
                if (mob instanceof Warden warden){
                    BlockState blockState = warden.level().getBlockState(warden.blockPosition().below());
                    if (blockState.is(Blocks.SCULK)){
                        if (warden.tickCount % 20 == 0){
                            warden.heal(2.0F);
                        }
                    }
                }
            }
            if (TweaksConfig.PhantasmicPhantoms.get()) {
                if (mob instanceof Phantom phantom) {
                    phantom.noPhysics = true;
                    if (phantom.level().isDay() && !phantom.level().isThundering()){
                        phantom.spawnAnim();
                        phantom.discard();
                    }
                }
            }
            if (TweaksConfig.IllagerBadInfluence.get()) {
                if (mob.level() instanceof ServerLevel serverLevel) {
                    if (mob instanceof AbstractVillager abstractVillager) {
                        if (abstractVillager.isBaby()) {
                            double range = abstractVillager.getAttribute(Attributes.FOLLOW_RANGE) != null ? abstractVillager.getAttributeValue(Attributes.FOLLOW_RANGE) : 16.0D;
                            List<Raider> raiders = abstractVillager.level().getEntitiesOfClass(Raider.class, abstractVillager.getBoundingBox().inflate(range), abstractVillager::hasLineOfSight);
                            if (raiders.size() >= 4) {
                                if (abstractVillager.getAge() >= -50) {
                                    int chance = Math.min(16, raiders.size());
                                    abstractVillager.handleEntityEvent((byte) 13);
                                    AbstractIllager illager;
                                    if (raiders.stream().anyMatch(raider -> raider instanceof SpellcasterIllager)
                                            && abstractVillager.level().random.nextInt(32 - chance) == 0) {
                                        abstractVillager.playSound(SoundEvents.EVOKER_AMBIENT);
                                        illager = abstractVillager.convertTo(EntityType.EVOKER, true);
                                    } else if (raiders.size() >= 8) {
                                        abstractVillager.playSound(SoundEvents.VINDICATOR_AMBIENT);
                                        illager = abstractVillager.convertTo(EntityType.VINDICATOR, true);
                                    } else {
                                        abstractVillager.playSound(SoundEvents.PILLAGER_AMBIENT);
                                        illager = abstractVillager.convertTo(EntityType.PILLAGER, true);
                                    }
                                    if (illager != null) {
                                        illager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.CONVERSION, null, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingAttack(LivingAttackEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (TweaksConfig.WardenAreaAttack.get()) {
            if (entity instanceof Warden warden) {
                if (!event.getSource().is(DamageTypes.THORNS)) {
                    float f = (float) warden.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    if (f > 0) {
                        float f3 = (1.0F + EnchantmentHelper.getSweepingDamageRatio(warden)) * f;
                        for (LivingEntity livingentity : warden.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0F, 0.25D, 1.0F))) {
                            if (livingentity != warden && livingentity != target && !warden.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker()) && warden.distanceToSqr(livingentity) < 16.0D && livingentity != warden.getVehicle()) {
                                livingentity.knockback(0.4F, Mth.sin(warden.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(warden.getYRot() * ((float) Math.PI / 180F)));
                                if (livingentity.hurt(warden.damageSources().thorns(warden), f3)) {
                                    EnchantmentHelper.doPostHurtEffects(livingentity, warden);
                                    EnchantmentHelper.doPostDamageEffects(warden, livingentity);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (TweaksConfig.BlazeFireHeal.get()) {
            if (target instanceof Blaze blaze) {
                if (event.getSource().is(DamageTypeTags.IS_FIRE) && entity instanceof LivingEntity && !(entity instanceof Blaze)) {
                    blaze.heal(event.getAmount());
                }
            }
        }
        if (TweaksConfig.BlazeMeleeFire.get()){
            if (entity instanceof Blaze){
                if (!target.fireImmune()){
                    target.setSecondsOnFire(5);
                }
            }
        }
        if (TweaksConfig.FrogMagmaCubeHurt.get()) {
            if (entity instanceof Frog frog) {
                if (target instanceof MagmaCube) {
                    frog.lavaHurt();
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingHurt(LivingHurtEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            if (target instanceof Creeper creeper) {
                if (TweaksConfig.FireCreeperIgnite.get() > 0) {
                    float chance = TweaksConfig.FireCreeperIgnite.get() / 100.0F;
                    if (creeper.getRandom().nextFloat() <= chance) {
                        creeper.ignite();
                    }
                }
                if (TweaksConfig.FireCreeperDamage.get()) {
                    event.setAmount(event.getAmount() * 2);
                }
            }
            if (target instanceof Cow cow){
                if (TweaksConfig.MethaneCow.get() > 0) {
                    float chance = TweaksConfig.MethaneCow.get() / 100.0F;
                    if (cow.getRandom().nextFloat() <= chance) {
                        if (!cow.level().isClientSide) {
                            cow.level().explode(cow, cow.getX(), cow.getY(), cow.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
                            cow.discard();
                        }
                    }
                }
            }
        }
        if (TweaksConfig.MaterialIronGolems.get()) {
            if (target instanceof IronGolem) {
                float amount = event.getAmount() / 10.0F;
                if (event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
                    amount = event.getAmount();
                }
                if (entity instanceof LivingEntity livingEntity) {
                    ItemStack itemStack = livingEntity.getMainHandItem();
                    if (MobUtils.toolAttack(event.getSource(), item -> item instanceof PickaxeItem pickaxeItem
                            && pickaxeItem.isCorrectToolForDrops(new ItemStack(item), Blocks.IRON_BLOCK.defaultBlockState()))) {
                        amount = event.getAmount() * itemStack.getDestroySpeed(Blocks.IRON_BLOCK.defaultBlockState());
                    }
                }
                event.setAmount(amount);
            }
        }
        if (TweaksConfig.MaterialSnowGolems.get()) {
            if (target instanceof SnowGolem) {
                if (entity instanceof LivingEntity livingEntity) {
                    ItemStack itemStack = livingEntity.getMainHandItem();
                    if (MobUtils.toolAttack(event.getSource(), item -> item instanceof ShovelItem shovelItem
                            && shovelItem.isCorrectToolForDrops(new ItemStack(item), Blocks.SNOW_BLOCK.defaultBlockState()))) {
                        event.setAmount(event.getAmount() * itemStack.getDestroySpeed(Blocks.SNOW_BLOCK.defaultBlockState()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingTarget(LivingChangeTargetEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Creeper){
            if (TweaksConfig.PlayerFocusedCreepers.get()) {
                if (!(event.getNewTarget() instanceof Player)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingProjectile(LivingGetProjectileEvent event){
        if (TweaksConfig.LimitMobArrows.get()) {
            if (event.getEntity() instanceof Mob mob){
                if (TweaksCapHelper.arrowCount(mob) <= 0){
                    event.setProjectileItemStack(ItemStack.EMPTY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingDeath(LivingDeathEvent event){
        Entity source = event.getSource().getEntity();
        LivingEntity victim = event.getEntity();
        Level world = victim.getCommandSenderWorld();
        if (world instanceof ServerLevel serverLevel) {
            if (TweaksConfig.ZombifyHorse.get()) {
                if (victim instanceof AbstractHorse horse) {
                    if (horse.getMobType() != MobType.UNDEAD) {
                        if (source instanceof Zombie) {
                            ZombieHorse zombieHorse = horse.convertTo(EntityType.ZOMBIE_HORSE, false);
                            if (zombieHorse != null) {
                                zombieHorse.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(zombieHorse.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), (CompoundTag) null);
                                zombieHorse.setOwnerUUID(horse.getOwnerUUID());
                                zombieHorse.setTamed(horse.isTamed());
                                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(horse, zombieHorse);
                                if (!zombieHorse.isSilent()) {
                                    serverLevel.levelEvent((Player) null, 1026, zombieHorse.blockPosition(), 0);
                                }
                            }
                        }
                    }
                }
            }
            if (TweaksConfig.WardenDeathHeal.get()) {
                if (source instanceof Warden warden) {
                    if (victim.shouldDropExperience() && victim.getExperienceReward() > 0) {
                        victim.skipDropExperience();
                        warden.heal(victim.getExperienceReward());
                        MobUtils.addParticlesAroundSelf(serverLevel, ParticleTypes.SCULK_SOUL, warden);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingDrops(LivingDropsEvent event){
        LivingEntity victim = event.getEntity();
        if (victim instanceof RangedAttackMob){
            if (TweaksCapHelper.arrowCount(victim) <= 0
                    || (!victim.isHolding(item -> item.getItem() instanceof ProjectileWeaponItem)
                    && TweaksConfig.NoBowNoArrows.get())){
                event.getDrops().removeIf(itemEntity -> itemEntity.getItem().getItem() instanceof ArrowItem);
            }
        }
    }

    @SubscribeEvent
    public static void ExplosionEvents(ExplosionEvent event){
        Explosion explosion = event.getExplosion();
        if (TweaksConfig.NoCreeperGriefing.get()) {
            if (explosion.getExploder() instanceof Creeper || explosion.getIndirectSourceEntity() instanceof Creeper) {
                explosion.clearToBlow();
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(MobEffectEvent.Applicable event){
        LivingEntity livingEntity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect mobEffect = instance.getEffect();
        if ((livingEntity instanceof AbstractGolem && TweaksConfig.GolemNoBio.get())
                || (livingEntity instanceof Blaze && TweaksConfig.BlazeNoBio.get())){
            if (mobEffect == MobEffects.POISON || mobEffect == MobEffects.WITHER
            || mobEffect == MobEffects.REGENERATION || mobEffect == MobEffects.SATURATION
            || mobEffect == MobEffects.HUNGER){
                event.setResult(Event.Result.DENY);
            }
        }
        if (TweaksConfig.CowNoEffect.get()) {
            if (livingEntity instanceof Cow) {
                if (mobEffect.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET))) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
        if (TweaksConfig.GoatNoEffect.get()) {
            if (livingEntity instanceof Goat) {
                if (mobEffect.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET))) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void EffectVisibilityEvents(LivingEvent.LivingVisibilityEvent event){
        if (event.getLookingEntity() instanceof LivingEntity living){
            if (TweaksConfig.BlindnessAffectsMobs.get()) {
                if (living.hasEffect(MobEffects.BLINDNESS)) {
                    MobEffectInstance mobEffectInstance = living.getEffect(MobEffects.BLINDNESS);
                    if (mobEffectInstance != null) {
                        int a = mobEffectInstance.getAmplifier();
                        event.modifyVisibility(0.5D - (a / 10.0D));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionAddedEvents(MobEffectEvent.Added event){
        LivingEntity effected = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();

        if (TweaksConfig.BlindnessAffectsMobs.get()) {
            if (effect == MobEffects.BLINDNESS) {
                if (effected instanceof Mob mob) {
                    mob.setTarget(null);
                }
            }
        }
    }
}
