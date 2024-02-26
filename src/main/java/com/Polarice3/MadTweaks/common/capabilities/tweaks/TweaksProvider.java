package com.Polarice3.MadTweaks.common.capabilities.tweaks;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TweaksProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<ITweaks> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    ITweaks instance = new TweaksImp();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? LazyOptional.of(() -> (T) instance) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return TweaksCapHelper.save(new CompoundTag(), instance);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        TweaksCapHelper.load(nbt, instance);
    }
}
