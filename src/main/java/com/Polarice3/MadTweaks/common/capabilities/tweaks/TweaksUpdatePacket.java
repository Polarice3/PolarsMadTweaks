package com.Polarice3.MadTweaks.common.capabilities.tweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TweaksUpdatePacket {
    private final int entityID;
    private CompoundTag tag;

    public TweaksUpdatePacket(int id, CompoundTag tag) {
        this.entityID = id;
        this.tag = tag;
    }

    public TweaksUpdatePacket(LivingEntity living) {
        this.entityID = living.getId();
        living.getCapability(TweaksProvider.CAPABILITY, null).ifPresent((misc) -> {
            this.tag = (CompoundTag) TweaksCapHelper.save(new CompoundTag(), misc);
        });
    }

    public static void encode(TweaksUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityID);
        buffer.writeNbt(packet.tag);
    }

    public static TweaksUpdatePacket decode(FriendlyByteBuf buffer) {
        return new TweaksUpdatePacket(buffer.readInt(), buffer.readNbt());
    }

    public static void consume(TweaksUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            ClientLevel clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null){
                Entity entity = clientLevel.getEntity(packet.entityID);
                if (entity != null) {
                    entity.getCapability(TweaksProvider.CAPABILITY).ifPresent((misc) -> {
                        TweaksCapHelper.load(packet.tag, misc);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
