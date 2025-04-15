package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.events.EntityElementalDOTHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @WrapOperation(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean imbuence$dispatchEEDOTHE(Entity entity,
                                             DamageSource damageSource,
                                             float damage,
                                             Operation<Boolean> original) {
        if (entity instanceof LivingEntity livingEntity && damageSource.equals(
                entity.damageSources().onFire())) {
            EntityElementalDOTHurtEvent event =
                    new EntityElementalDOTHurtEvent(livingEntity, damageSource,
                            damage);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.damageAmount > 0f) {
                return original.call(entity, damageSource, event.damageAmount);
            }
        }
        return original.call(entity, damageSource, damage);
    }
}
