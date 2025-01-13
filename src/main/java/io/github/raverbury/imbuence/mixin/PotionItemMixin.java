package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.events.PotionDrinkAndApplyEffectEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @WrapOperation(
            method = "finishUsingItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z")
    )
    private boolean imbuence$dispatchPDAAEEvent(LivingEntity instance,
                                                MobEffectInstance p_21165_,
                                                Operation<Boolean> original,
                                                @Local(argsOnly = true) ItemStack itemStack) {
        PotionDrinkAndApplyEffectEvent event =
                new PotionDrinkAndApplyEffectEvent(
                        instance, itemStack, p_21165_);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled) {
            return false;
        }
        return original.call(instance, event.mobEffectInstance);
    }
}
