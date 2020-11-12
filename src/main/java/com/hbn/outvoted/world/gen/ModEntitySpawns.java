package com.hbn.outvoted.world.gen;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.entities.inferno.InfernoEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    @SubscribeEvent
    public static void spawnEntities(BiomeLoadingEvent event) {
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (event.getCategory() == Biome.Category.NETHER) {
                event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.BLAZE, OutvotedConfig.COMMON.rateblaze.get(), 3, 6));
            }
        }
        if (OutvotedConfig.COMMON.spawnhunger.get()) {
            if (event.getCategory() == Biome.Category.DESERT) {
                event.getSpawns().withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(ModEntityTypes.HUNGER.get(), OutvotedConfig.COMMON.ratehunger.get(), 1, 2));
            } else if (event.getCategory() == Biome.Category.SWAMP) {
                event.getSpawns().withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(ModEntityTypes.HUNGER.get(), OutvotedConfig.COMMON.ratehunger.get(), 1, 2));
            }
        }
        if (OutvotedConfig.COMMON.spawnkraken.get()) {
            if (event.getName().toString().equals("minecraft:deep_ocean") || event.getName().toString().equals("minecraft:deep_warm_ocean") || event.getName().toString().equals("minecraft:deep_lukewarm_ocean") || event.getName().toString().equals("minecraft:deep_cold_ocean")) {
                event.getSpawns().withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(ModEntityTypes.KRAKEN.get(), OutvotedConfig.COMMON.ratekraken.get(), 1, 1));
            }
        }
    }

    @SubscribeEvent
    public static void changeMobs(LivingSpawnEvent.SpecialSpawn event) {
        Entity e = event.getEntity();
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (e instanceof BlazeEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    if (Math.random() > 1.0 - (OutvotedConfig.COMMON.rateinferno.get() / 100)) {
                        World world = event.getEntity().getEntityWorld();

                        InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                        inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);

                        world.addEntity(inferno);
                    }
                } else if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                    if (Math.random() > 1.0 - (OutvotedConfig.COMMON.rateinferno.get() / 200)) {
                        World world = event.getEntity().getEntityWorld();

                        InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                        inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);

                        world.addEntity(inferno);
                    }
                }
            }
        }
    }
}
