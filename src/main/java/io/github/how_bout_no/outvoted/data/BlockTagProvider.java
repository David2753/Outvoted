package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ModTags.HUNGER_CAN_BURROW).addTag(BlockTags.SAND)
                .add(Blocks.GRAVEL, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MYCELIUM);

        getOrCreateBuilder(ModTags.PALM_LOGS)
                .add(ModBlocks.PALM_LOG.get(), ModBlocks.STRIPPED_PALM_LOG.get());
        getOrCreateBuilder(BlockTags.LOGS)
                .addTag(ModTags.PALM_LOGS);
        getOrCreateBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(ModTags.PALM_LOGS);
        getOrCreateBuilder(BlockTags.LEAVES)
                .add(ModBlocks.PALM_LEAVES.get());
        getOrCreateBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.PALM_SAPLING.get());
        getOrCreateBuilder(BlockTags.PLANKS)
                .add(ModBlocks.PALM_PLANKS.get());
        getOrCreateBuilder(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.PALM_FENCE.get());
        getOrCreateBuilder(BlockTags.makeWrapperTag("forge:fences/wooden"))
                .add(ModBlocks.PALM_FENCE.get());
        getOrCreateBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.PALM_FENCE_GATE.get());
        getOrCreateBuilder(BlockTags.makeWrapperTag("forge:fence_gates/wooden"))
                .add(ModBlocks.PALM_FENCE_GATE.get());
    }
}
