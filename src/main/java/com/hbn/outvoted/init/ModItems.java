package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.items.InfernoHelmetItem;
import com.hbn.outvoted.items.InfernoShieldItem;
import com.hbn.outvoted.items.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    public static final RegistryObject<ModdedSpawnEggItem> INFERNO_SPAWN_EGG = ITEMS.register("inferno_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.INFERNO, 16167425, 0x000000, new Item.Properties().group(Outvoted.TAB)));
    public static final RegistryObject<ModdedSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new Item.Properties().group(Outvoted.TAB)));
    /*public static final RegistryObject<ModdedSpawnEggItem> OCEANMONSTER_SPAWN_EGG = ITEMS.register("ocean_monster_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.OCEANMONSTER, 0x000000, 0x000000, new Item.Properties().group(Outvoted.TAB)));*/

    public static final RegistryObject<Item> INFERNO_HELMET = ITEMS.register("inferno_helmet", InfernoHelmetItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD = ITEMS.register("inferno_shield", InfernoShieldItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD_PART = ITEMS.register("inferno_shield_part", () -> new Item(new Item.Properties().group(Outvoted.TAB).isImmuneToFire()));
    public static final RegistryObject<Item> INFERNO_PIECE = ITEMS.register("inferno_piece", () -> new Item(new Item.Properties().group(Outvoted.TAB).isImmuneToFire()));
}
