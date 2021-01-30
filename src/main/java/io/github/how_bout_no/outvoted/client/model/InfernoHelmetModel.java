package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.InfernoHelmetItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class InfernoHelmetModel extends AnimatedGeoModel<InfernoHelmetItem> {
    @Override
    public ResourceLocation getAnimationFileLocation(InfernoHelmetItem item) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(InfernoHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/infernohelmet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(InfernoHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno/inferno.png");
    }
}
