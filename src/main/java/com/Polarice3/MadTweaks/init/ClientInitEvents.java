package com.Polarice3.MadTweaks.init;

import com.Polarice3.MadTweaks.MadTweaks;
import com.Polarice3.MadTweaks.TweaksConfig;
import com.Polarice3.MadTweaks.client.render.*;
import com.Polarice3.MadTweaks.common.entities.TweaksEntityTypes;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInitEvents {

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        if (TweaksConfig.BlazeHealthGlow.get()) {
            event.registerEntityRenderer(EntityType.BLAZE, TweakedBlazeRenderer::new);
        }
        if (TweaksConfig.PhantasmicPhantoms.get()) {
            event.registerEntityRenderer(EntityType.PHANTOM, TweakedPhantomRenderer::new);
        }
        event.registerEntityRenderer(TweaksEntityTypes.TWEAKED_TRIDENT.get(), ThrownTridentRenderer::new);
        event.registerEntityRenderer(TweaksEntityTypes.MAGMA_CUBE.get(), ModMagmaCubeRenderer::new);
        event.registerEntityRenderer(TweaksEntityTypes.COBBLED_CUBE.get(), CobbledCubeRenderer::new);
        event.registerEntityRenderer(TweaksEntityTypes.SILVERFISH.get(), ModSilverfishRenderer::new);
    }
}
