package com.Polarice3.MadTweaks;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.List;

@Mod.EventBusSubscriber(modid = MadTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TweaksConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TweakedMagmaCube;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HungrySpiders;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RottenWolves;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ViolentPolarBears;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FrogMagmaCubeHurt;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SleepingHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LivingMobHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FishySilverfish;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CryingGhast;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CatSmallAttack;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PhantasmicPhantoms;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CowNoEffect;
    public static final ForgeConfigSpec.ConfigValue<Integer> MethaneCow;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GoatNoEffect;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MobAvoidsWarden;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WardenAreaAttack;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WardenDeathHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WardenSculkHeal;

    public static final ForgeConfigSpec.ConfigValue<Boolean> NoCreeperGriefing;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PlayerFocusedCreepers;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CreeperStalkBaby;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CreeperClimb;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FireCreeperDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> FireCreeperIgnite;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerZombieHate;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombieDecay;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombieSandHusk;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombieBurnHusk;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombiePlayer;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombifyHorse;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ZombieHorseDecay;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BlazeFireHeal;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BlazeMeleeFire;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BlazeHealthGlow;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BlazeNoBio;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MaterialIronGolems;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MaterialSnowGolems;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GolemNoBio;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerBadInfluence;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IllagerRaidExplode;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LimitMobArrows;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NoBowNoArrows;
    public static final ForgeConfigSpec.ConfigValue<Integer> MinMobArrowAmount;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExtraMobArrowAmount;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> LimitArrowsBlackList;

    public static final ForgeConfigSpec.ConfigValue<Boolean> HungerAffectsMobs;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BlindnessAffectsMobs;

    static {
        BUILDER.push("Mob Tweaks");
        CowNoEffect = BUILDER.comment("Cows don't gain effects that can be healed by drinking milk, Default: true")
                .define("cowNoEffect", true);
        MethaneCow = BUILDER.comment("The percentage chance of Cows exploding when taking fire damage, Set to 0 to disable, Default: 11")
                .defineInRange("methaneCow", 11, 0, 100);
        GoatNoEffect = BUILDER.comment("Goats don't gain effects that can be healed by drinking milk, Default: true")
                .define("goatNoEffect", true);
        TweakedMagmaCube = BUILDER.comment("Replaced Magma Cubes with tweaked versions, Default: true")
                .define("tweakedMagmaCube", true);
        HungrySpiders = BUILDER.comment("Spiders will attack animals and each other if one has less health than the other, Default: true")
                .define("hungrySpiders", true);
        RottenWolves = BUILDER.comment("Untamed Wolves will also attack Zombies, Default: true")
                .define("rottenWolves", true);
        ViolentPolarBears = BUILDER.comment("Adult Polar Bears are far more aggressive and target most mobs, Default: true")
                .define("violentPolarBears", true);
        FishySilverfish = BUILDER.comment("Silverfishes can swim, breath and spawn underwater, Default: true")
                .define("fishySilverfish", true);
        CryingGhast = BUILDER.comment("Ghasts occasionally drop Ghast Tears from themselves, Default: true")
                .define("cryingGhast", true);
        CatSmallAttack = BUILDER.comment("Stray cats attack entities that are smaller than it, Default: true")
                .define("catSmallAttack", true);
        PhantasmicPhantoms = BUILDER.comment("Phantoms looks translucent and can phase through walls, Default: true")
                .define("phantasmicPhantoms", true);
        FrogMagmaCubeHurt = BUILDER.comment("Frogs take damage and is set aflame after eating a Magma Cube., Default: true")
                .define("frogMagmaCubeHurt", true);
        SleepingHeal = BUILDER.comment("Sleeping entities will periodically heal from prior damage., Default: true")
                .define("sleepingHeal", true);
        LivingMobHeal = BUILDER.comment("Non-undead mobs periodically heal from prior damage when not targeting or in combat, Default: true")
                .define("livingMobHeal", true);
            BUILDER.push("Mobs Arrows");
            LimitMobArrows = BUILDER.comment("Bow/Crossbow mobs have limited arrows., Default: true")
                    .define("limitMobArrows", true);
            NoBowNoArrows = BUILDER.comment("Bow/Crossbow mobs don't drop arrows if they're not wielding either weapons, Default: true")
                    .define("noBowNoArrows", true);
            MinMobArrowAmount = BUILDER.comment("Minimum amount of arrows a mob spawns with. Only takes effect if limitMobArrows is enabled, Default: 5")
                    .defineInRange("minMobArrowAmount", 5, 0, Integer.MAX_VALUE);
            ExtraMobArrowAmount = BUILDER.comment("Extra amount of arrows a mob can potentially spawn with. Only takes effect if limitMobArrows is enabled, Default: 20")
                    .defineInRange("extraMobArrowAmount", 20, 1, Integer.MAX_VALUE);
            LimitArrowsBlackList = BUILDER.comment("""
                            Add mobs that limitMobArrows does not affect.\s
                            To do so, enter the namespace ID of the mob, like "minecraft:zombie, minecraft:skeleton".""")
                    .defineList("limitArrowsBlackList", Lists.newArrayList(),
                            (itemRaw) -> itemRaw instanceof String);
            BUILDER.pop();
            BUILDER.push("Blazes");
            BlazeFireHeal = BUILDER.comment("Blazes heal from fire damage or being in fire, Default: true")
                    .define("blazeFireHeal", true);
            BlazeMeleeFire = BUILDER.comment("Blazes melee attacks sets target on fire, Default: true")
                    .define("blazeMeleeFire", true);
            BlazeHealthGlow = BUILDER.comment("Blazes become dimmer the lower their health, requires reset to take effect, Default: true")
                    .define("blazeHealthGlow", true);
            BlazeNoBio = BUILDER.comment("Blazes can't be effected by Poison, Wither, Hunger, Saturation and Regeneration, Default: true")
                    .define("blazeNoBio", true);
            BUILDER.pop();
            BUILDER.push("Creepers");
            NoCreeperGriefing = BUILDER.comment("Creeper explosions don't break blocks, Default: true")
                    .define("noCreeperGriefing", true);
            PlayerFocusedCreepers = BUILDER.comment("Creepers will never target another mob and only target players, Default: true")
                    .define("playerFocusedCreepers", true);
            CreeperStalkBaby = BUILDER.comment("Creepers follow baby mobs, Default: true")
                    .define("creeperStalkBaby", true);
            CreeperClimb = BUILDER.comment("Creepers can climb walls because real life creeper plants climbs on trees, Default: true")
                    .define("creeperClimb", true);
            FireCreeperDamage = BUILDER.comment("Creepers take double damage from fire, Default: true")
                    .define("fireCreeperDamage", true);
            FireCreeperIgnite = BUILDER.comment("The percentage chance of Creepers igniting when taking fire damage, Set to 0 to disable, Default: 11")
                    .defineInRange("fireCreeperIgnite", 11, 0, 100);
            BUILDER.pop();
            BUILDER.push("Wardens");
            MobAvoidsWarden = BUILDER.comment("Mobs that have less than 100 max health will avoid the Warden, Default: true")
                    .define("mobAvoidsWarden", true);
            WardenAreaAttack = BUILDER.comment("Warden's attacks effects multiple entities much like Player sweep attack, Default: true")
                    .define("wardenAreaAttack", true);
            WardenDeathHeal = BUILDER.comment("Wardens heal after killing an entity, with the heal amount depending on the entity's experience drop, Default: true")
                    .define("wardenDeathHeal", true);
            WardenSculkHeal = BUILDER.comment("Wardens heal when standing on Sculk blocks, Default: true")
                    .define("wardenSculkHeal", true);
            BUILDER.pop();
            BUILDER.push("Zombies");
            IllagerZombieHate = BUILDER.comment("Illagers and Zombies are hostile to each other, Default: true")
                    .define("illagerZombieHate", true);
            ZombieDecay = BUILDER.comment("Zombies will turn into Skeletons after a long period, Default: true")
                    .define("zombieDecay", true);
            ZombieSandHusk = BUILDER.comment("Zombies will turn into Husks when suffocating in Sand blocks, Default: true")
                    .define("zombieSandHusk", true);
            ZombieBurnHusk = BUILDER.comment("Zombies will turn into Husks when burning to death on desert biomes' surface, Default: true")
                    .define("zombieBurnHusk", true);
            ZombiePlayer = BUILDER.comment("A Zombie will spawn on a player's location if the latter is killed by one, Default: true")
                    .define("ZombiePlayer", true);
            ZombifyHorse = BUILDER.comment("Horses will turn into Zombie Horses if killed by Zombie, Default: true")
                    .define("zombifyHorse", true);
            ZombieHorseDecay = BUILDER.comment("Zombie Horses will turn into Skeleton Horses after a long period of time, Default: true")
                    .define("zombieHorseDecay", true);
            BUILDER.pop();
            BUILDER.push("Illagers");
            IllagerBadInfluence = BUILDER.comment("Baby Villagers will grow up into an Illager if surrounded by Raiders, Default: true")
                    .define("illagerBadInfluence", true);
            IllagerRaidExplode = BUILDER.comment("Raiders will massively explode after celebrating a successful raid, Default: true")
                    .define("illagerRaidExplode", true);
            BUILDER.pop();
            BUILDER.push("Golems");
            MaterialIronGolems = BUILDER.comment("Iron Golems take 1/10th damage from non-armor penetrative sources, but take extra damage from pickaxes, Default: true")
                    .define("materialIronGolems", true);
            MaterialSnowGolems = BUILDER.comment("Snow Golems take extra damage from shovels, Default: true")
                    .define("materialSnowGolems", true);
            GolemNoBio = BUILDER.comment("Golems (and Shulkers) can't be effected by Poison, Wither, Hunger, Saturation and Regeneration, Default: true")
                    .define("golemNoBio", true);
            BUILDER.pop();
            BUILDER.push("Effects");
            HungerAffectsMobs = BUILDER.comment("Hunger effect causes mobs to periodically take starvation damage unless they gain Saturation, Default: true")
                    .define("hungerAffectsMobs", true);
            BlindnessAffectsMobs = BUILDER.comment("Blindness effect causes mobs to stop targeting their initial target and reduces their detection range, Default: true")
                    .define("blindnessAffectsMobs", true);
            BUILDER.pop();
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        config.setConfig(file);
    }
}
