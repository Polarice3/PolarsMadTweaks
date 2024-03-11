package com.Polarice3.MadTweaks.common.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class TweakedTrident extends ThrownTrident {
    public TweakedTrident(EntityType<? extends ThrownTrident> p_37561_, Level p_37562_) {
        super(p_37561_, p_37562_);
    }

    public TweakedTrident(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(p_37569_, p_37570_, p_37571_);
    }

    @Override
    public EntityType<?> getType() {
        return TweaksEntityTypes.TWEAKED_TRIDENT.get();
    }

    @Override
    public Component getTypeName() {
        return EntityType.TRIDENT.getDescription();
    }

    public void playerTouch(Player p_37580_) {
        if (this.canPickUp()) {
            if (this.tryPickup(p_37580_)) {
                p_37580_.take(this, 1);
                this.discard();
            }
        }
    }

    public boolean canPickUp(){
        return !this.level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0;
    }

    public ItemStack getItem(){
        return this.getPickupItem();
    }

    protected boolean tryPickup(Player p_150121_) {
        return switch (this.pickup) {
            case ALLOWED -> p_150121_.getInventory().add(this.getPickupItem());
            case CREATIVE_ONLY -> p_150121_.getAbilities().instabuild;
            default -> false;
        };
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
