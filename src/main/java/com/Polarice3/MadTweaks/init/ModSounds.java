package com.Polarice3.MadTweaks.init;

import com.Polarice3.MadTweaks.MadTweaks;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MadTweaks.MOD_ID);

    public static void init(){
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> MAGMA_CUBE_AMBIENT = create("magma_cube_ambient");
    public static final RegistryObject<SoundEvent> MAGMA_CUBE_HURT = create("magma_cube_hurt");
    public static final RegistryObject<SoundEvent> MAGMA_CUBE_JUMP = create("magma_cube_jump");
    public static final RegistryObject<SoundEvent> MAGMA_CUBE_LAND = create("magma_cube_land");
    public static final RegistryObject<SoundEvent> MAGMA_CUBE_DEATH = create("magma_cube_death");

    static RegistryObject<SoundEvent> create(String name) {
        SoundEvent event = SoundEvent.createVariableRangeEvent(MadTweaks.location(name));
        return SOUNDS.register(name, () -> event);
    }
}
