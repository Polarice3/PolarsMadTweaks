package com.Polarice3.MadTweaks.init;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.common.capabilities.tweaks.TweaksProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InitEvents {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity){
            event.addCapability(MadTweaks.location("extra"), new TweaksProvider());
        }
    }
}
