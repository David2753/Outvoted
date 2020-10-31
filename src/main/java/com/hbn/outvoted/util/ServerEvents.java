package com.hbn.outvoted.util;

import com.hbn.outvoted.items.InfernoShieldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null) {
            Entity attacker = event.getSource().getTrueSource();
            LivingEntity player = event.getEntityLiving();
            Item shield = player.getActiveItemStack().getItem();
            if (shield instanceof InfernoShieldItem) {
                if (player.isActiveItemStackBlocking() && !event.getSource().isProjectile()) {
                    attacker.setFire(5);
                } else if (event.getSource().isProjectile()) {
                    event.getSource().getImmediateSource().setFire(5);
                }
            }
        }
    }
}
