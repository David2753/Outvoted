package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.item.InfernoHelmetItem;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib3.item.GeoArmorItem;

/**
 * For some reason getArmorTexture is final in the GeoArmorItem... no problem!
 */
@Mixin(GeoArmorItem.class)
public abstract class MixinGeoArmorItem {
    private static final String HELMET_TEXTURE_SOUL = "outvoted:textures/entity/inferno/inferno_soul.png";

    @Inject(method = "getArmorTexture", at = @At("RETURN"), cancellable = true, remap = false)
    public void armorTextures(ItemStack stack, Entity entity, EquipmentSlotType slot, String type, CallbackInfoReturnable<String> cir) {
        if (!(stack.getItem() instanceof InfernoHelmetItem)) return;
        if (OutvotedConfig.COMMON.infernovariant.get()) {
            if (stack.getTag() != null && stack.getTag().getFloat("CustomModelData") == 1.0F) {
                cir.setReturnValue(HELMET_TEXTURE_SOUL);
            }
        }
    }
}
