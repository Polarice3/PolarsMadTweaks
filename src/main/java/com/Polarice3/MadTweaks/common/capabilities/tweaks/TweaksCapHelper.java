package com.Polarice3.MadTweaks.common.capabilities.tweaks;

import com.Polarice3.MadTweaks.common.network.ModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TweaksCapHelper {
    public static ITweaks getCapability(LivingEntity livingEntity) {
        return livingEntity.getCapability(TweaksProvider.CAPABILITY).orElse(new TweaksImp());
    }

    public static boolean init(LivingEntity livingEntity){
        return getCapability(livingEntity).init();
    }

    public static void setInit(LivingEntity livingEntity, boolean init){
        getCapability(livingEntity).setInit(init);
    }

    public static int arrowCount(LivingEntity livingEntity){
        return getCapability(livingEntity).arrowCount();
    }

    public static void setArrowCount(LivingEntity livingEntity, int arrows){
        getCapability(livingEntity).setArrowCount(arrows);
        if (!livingEntity.level().isClientSide){
            sendTweaksUpdatePacket(livingEntity);
        }
    }

    public static void decreaseArrow(LivingEntity living){
        if (arrowCount(living) > 0) {
            setArrowCount(living, arrowCount(living) - 1);
        }
    }

    public static CompoundTag save(CompoundTag tag, ITweaks misc) {
        tag.putBoolean("init", misc.init());
        tag.putInt("arrowCount", misc.arrowCount());
        return tag;
    }

    public static ITweaks load(CompoundTag tag, ITweaks misc) {
        misc.setInit(tag.getBoolean("init"));
        misc.setArrowCount(tag.getInt("arrowCount"));
        return misc;
    }

    public static void sendTweaksUpdatePacket(Player player, LivingEntity livingEntity) {
        ModNetwork.sendTo(player, new TweaksUpdatePacket(livingEntity));
    }

    public static void sendTweaksUpdatePacket(LivingEntity livingEntity) {
        ModNetwork.sendToALL(new TweaksUpdatePacket(livingEntity));
    }
}
