package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.events.EntityElementalDOTHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean imbuence$dispatchEEDOTHE(LivingEntity entity,
                                             DamageSource damageSource,
                                             float damage,
                                             Operation<Boolean> original) {
        if (damageSource.equals(entity.damageSources().freeze())) {
            EntityElementalDOTHurtEvent event =
                    new EntityElementalDOTHurtEvent(entity, damageSource,
                            damage);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.damageAmount > 0f) {
                return original.call(entity, damageSource, event.damageAmount);
            }
        }
        return original.call(entity, damageSource, damage);
    }

    @ModifyExpressionValue(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getTicksRequiredToFreeze()I")
    )
    private int imbuence$increaseTicksFrozenLimit(int original) {
        return original * 3;
    }
}
