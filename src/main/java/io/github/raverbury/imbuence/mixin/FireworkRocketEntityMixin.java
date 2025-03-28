package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.enchantment.AfterburnerEnchantment;
import io.github.raverbury.imbuence.enchantment.RocketSpecialistEnchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin implements EntityAccessor {

    @Shadow @Final private static EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM;

    /**
     * This is the ctor used to attach rockets to boost elytra, we increase
     * flight duration here
     */
    @ModifyArg(
            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/world/item/ItemStack;)V"
            ),
            index = 5
    )
    private static ItemStack imbuence$setAfterburnerMinFlightDuration(
            ItemStack itemStack,
            @Local(argsOnly = true) LivingEntity livingEntity
            )
    {
        int afterburnerLevel =
                AfterburnerEnchantment.getEnchantLevel(livingEntity);
        int minFlightDuration =
                AfterburnerEnchantment.getMininumFlightDuration(afterburnerLevel);
        int initialFlightDuration =
                itemStack.getOrCreateTagElement("Fireworks").getByte("Flight");
        ItemStack fakeItemStack = itemStack.copy();
        FireworkRocketItem.setDuration(fakeItemStack,
                (byte) Math.max(initialFlightDuration,
                        minFlightDuration));
        return fakeItemStack;
    }

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
                (float) (ev * (1 + rocketSpecialistLevel * Config.ROCKET_SPECIALIST_BONUS_DAMAGE_PER_LEVEL.get())));
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
                Math.min(RocketSpecialistEnchantment.MAX_RADIUS,
                        radius * (1 + rocketSpecialistLevel * Config.ROCKET_SPECIALIST_BONUS_RADIUS_PER_LEVEL.get())));
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

        return Math.min(RocketSpecialistEnchantment.MAX_RADIUS,
                radius * (1 + rocketSpecialistLevel * Config.ROCKET_SPECIALIST_BONUS_RADIUS_PER_LEVEL.get()));
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

    @WrapOperation(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;explode()V")
    )
    private void imbuence$stunMainTargetOnHit(FireworkRocketEntity instance,
                                              Operation<Void> original,
                                              @Local(argsOnly = true) EntityHitResult entityHitResult) {
        ItemStack fireworkRocket =
                this.imbuence$callGetEntityData().get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag tag = fireworkRocket.isEmpty()? null :
                fireworkRocket.getTagElement("Fireworks");
        int level =
                RocketSpecialistEnchantment.getRocketSpecialistLevelFromTag(tag);
        if (level > 0) {
            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(
                        new MobEffectInstance(
                                MobEffects.MOVEMENT_SLOWDOWN,
                                15,
                                4
                        )
                );
            }
        }
        original.call(instance);
    }
}
