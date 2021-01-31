package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;

import java.util.ArrayList;
import java.util.Collection;

/* I don't know if splitting it up into a bunch of inner classes is better or worse, I just don't like
 * having a clutter of files and idk any smoother implementation :v */
public class ModBlockItems {
    public static class ModBlockItem extends BlockItem {
        public ModBlockItem(Block blockIn, Properties properties) {
            super(blockIn, properties.group(Outvoted.TAB_BLOCKS));
        }

        @Override
        public Collection<ItemGroup> getCreativeTabs() {
            Collection<ItemGroup> groups = new ArrayList<>();
            groups.add(Outvoted.TAB_BLOCKS);
            groups.add(ItemGroup.SEARCH);
            return groups;
        }
    }

    public static class ModDecoBlockItem extends ModBlockItem {
        public ModDecoBlockItem(Block blockIn, Properties properties) {
            super(blockIn, properties.group(Outvoted.TAB_DECO));
        }

        @Override
        public Collection<ItemGroup> getCreativeTabs() {
            Collection<ItemGroup> groups = new ArrayList<>();
            groups.add(Outvoted.TAB_DECO);
            groups.add(ItemGroup.SEARCH);
            return groups;
        }
    }

    public static class ModTallBlockItem extends TallBlockItem {
        public ModTallBlockItem(Block blockIn, Properties properties) {
            super(blockIn, properties.group(Outvoted.TAB_DECO));
        }

        @Override
        public Collection<ItemGroup> getCreativeTabs() {
            Collection<ItemGroup> groups = new ArrayList<>();
            groups.add(Outvoted.TAB_DECO);
            groups.add(ItemGroup.SEARCH);
            return groups;
        }
    }

    public static class ModSignItem extends SignItem {
        public ModSignItem(Properties properties, Block floorBlockIn, Block wallBlockIn) {
            super(properties.maxStackSize(16).group(Outvoted.TAB_DECO), floorBlockIn, wallBlockIn);
        }

        @Override
        public Collection<ItemGroup> getCreativeTabs() {
            Collection<ItemGroup> groups = new ArrayList<>();
            groups.add(Outvoted.TAB_DECO);
            groups.add(ItemGroup.SEARCH);
            return groups;
        }
    }
}
