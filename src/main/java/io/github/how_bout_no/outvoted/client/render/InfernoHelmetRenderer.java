package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.InfernoHelmetModel;
import io.github.how_bout_no.outvoted.item.InfernoHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class InfernoHelmetRenderer extends GeoArmorRenderer<InfernoHelmetItem> {
    public InfernoHelmetRenderer() {
        super(new InfernoHelmetModel());

        this.headBone = "helmet";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
