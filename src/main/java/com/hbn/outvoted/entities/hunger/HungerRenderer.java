package com.hbn.outvoted.entities.hunger;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entities.kraken.KrakenEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class HungerRenderer extends GeoEntityRenderer<HungerEntity> {

    protected static final ResourceLocation SANDTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger.png");
    protected static final ResourceLocation SWAMPTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger_swamp.png");

    public HungerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HungerModel());
    }

    @Override
    public RenderType getRenderType(HungerEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        if (entity.variant() == 0) {
            return SANDTEXTURE;
        } else {
            return SWAMPTEXTURE;
        }
    }
}
