package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.items.InfernoHelmetItem;
import com.hbn.outvoted.items.InfernoShieldItem;
import com.hbn.outvoted.items.ModItem;
import com.hbn.outvoted.items.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    // Spawn Eggs
    public static final RegistryObject<ModdedSpawnEggItem> SOUL_BLAZE_SPAWN_EGG = ITEMS.register("soul_blaze_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.SOUL_BLAZE, 0x04E2FB, 0xFFF87E, new ModItem.Properties().group(Outvoted.TAB_MISC)));
    public static final RegistryObject<ModdedSpawnEggItem> INFERNO_SPAWN_EGG = ITEMS.register("inferno_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.INFERNO, 0xF6B201, 0x000000, new ModItem.Properties().group(Outvoted.TAB_MISC)));
    public static final RegistryObject<ModdedSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new ModItem.Properties().group(Outvoted.TAB_MISC)));
    public static final RegistryObject<ModdedSpawnEggItem> KRAKEN_SPAWN_EGG = ITEMS.register("kraken_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.KRAKEN, 0x5B872E, 0x000000, new ModItem.Properties().group(Outvoted.TAB_MISC)));

    // Items
    public static final RegistryObject<Item> INFERNO_HELMET = ITEMS.register("inferno_helmet", InfernoHelmetItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD = ITEMS.register("inferno_shield", InfernoShieldItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD_PART = ITEMS.register("inferno_shield_part", () -> new ModItem(new Item.Properties().group(Outvoted.TAB_MISC).isImmuneToFire()));
    public static final RegistryObject<Item> INFERNO_PIECE = ITEMS.register("inferno_piece", () -> new ModItem(new Item.Properties().group(Outvoted.TAB_MISC).isImmuneToFire()));
    public static final RegistryObject<Item> VOID_HEART = ITEMS.register("void_heart", () -> new ModItem(new Item.Properties().group(Outvoted.TAB_MISC)));
    public static final RegistryObject<Item> KRAKEN_TOOTH = ITEMS.register("kraken_tooth", () -> new ModItem(new Item.Properties().group(Outvoted.TAB_MISC)));
    public static final RegistryObject<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new ModItem(new Item.Properties().group(Outvoted.TAB_MISC)));
}
