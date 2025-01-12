package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.enchantment.RocketSpecialistEnchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    @WrapOperation(
            method = "dealExplosionDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean imbuence$increaseExplosionDamage(LivingEntity instance,
                                                     DamageSource entity, float ev,
                                                     Operation<Boolean> original, @Local CompoundTag compoundTag) {
        byte rocketSpecialistLevel =
                RocketSpecialistEnchantment.getRocketSpecialistLevelFromTag(
                        compoundTag);

        return original.call(instance, entity,
                ev * (1 + rocketSpecialistLevel * RocketSpecialistEnchantment.BONUS_DAMAGE_MULTIPLIER_GROWTH));
    }

    @WrapOperation(
            method = "dealExplosionDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;")
    )
    private AABB imbuence$increaseSplashRadius1(AABB instance, double radius,
                                                Operation<AABB> original, @Local CompoundTag compoundTag) {
        byte rocketSpecialistLevel = (byte) Math.min(
                RocketSpecialistEnchantment.getRocketSpecialistLevelFromTag(
                        compoundTag), 4);

        return original.call(instance,
                radius * (1 + rocketSpecialistLevel * RocketSpecialistEnchantment.INCREASED_SPLASH_RADIUS_GROWTH));
    }

    /**
     * Seriously mojank, why hardcode radius in 2 different places when d0 is
     * there?
     *
     * @param radius
     * @param compoundTag
     * @return
     */
    @ModifyExpressionValue(
            method = "dealExplosionDamage",
            at = @At(
                    value = "CONSTANT",
                    args =
                            "doubleValue=25.0")
    )
    private double imbuence$increaseSplashRadius2(double radius,
                                                  @Local CompoundTag compoundTag) {
        byte rocketSpecialistLevel = (byte) Math.min(
                RocketSpecialistEnchantment.getRocketSpecialistLevelFromTag(
                        compoundTag), 4);

        return radius * (1 + rocketSpecialistLevel * RocketSpecialistEnchantment.INCREASED_SPLASH_RADIUS_GROWTH);
    }

    @ModifyExpressionValue(
            method = "dealExplosionDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;sqrt(D)D")
    )
    private double imbuence$negateSplashDamageDropoff(double original,
                                                      @Local CompoundTag compoundTag) {
        byte rocketSpecialistLevel =
                RocketSpecialistEnchantment.getRocketSpecialistLevelFromTag(
                        compoundTag);

        if (rocketSpecialistLevel == 0) {
            return original;
        }

        return 1;
    }
}
