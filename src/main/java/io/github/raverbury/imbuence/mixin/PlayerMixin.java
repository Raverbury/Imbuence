package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.DefianceEnchantment;
import io.github.raverbury.imbuence.enchantment.PuzzleEnchantment;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow public abstract InteractionResult interactOn(Entity p_36158_, InteractionHand p_36159_);

    @WrapOperation(
            method = "disableShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemCooldowns;" +
                            "addCooldown(Lnet/minecraft/world/item/Item;I)V")
    )
    private void imbuence$reduceDisableShieldCooldown(ItemCooldowns instance,
                                                      Item p_41525_,
                                                      int cooldown,
                                                      Operation<Void> original) {
        if (EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.DEFIANCE_ENCHANTMENT.get(),
                (Player) (Object) this) < 0) {
            original.call(instance, p_41525_, cooldown);
        }
        original.call(instance, p_41525_,
                (int) ((float) cooldown * (1 - DefianceEnchantment.SHIELD_DISABLE_COOLDOWN_REDUCTION_PERCENTAGE)));
    }

    @WrapOperation(
            method = "blockUsingShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;disableShield(Z)V")
    )
    private void imbuence$applyDefianceEffect(Player instance,
                                              boolean p_36385_,
                                              Operation<Void> original,
                                              @Local(argsOnly = true) LivingEntity livingEntity) {
        original.call(instance, p_36385_);
        DefianceEnchantment.applySlowAndKnockback(livingEntity, instance);
    }

    @WrapOperation(
            method = "jumpFromGround",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;" +
                            "causeFoodExhaustion(F)V")
    )
    private void imbuence$halfFoodExhaustion1(Player instance, float p_36400_,
                       Operation<Void> original) {
        if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(instance) != 5) {
            original.call(instance, p_36400_);
            return;
        }
        if (instance.getActiveEffects().size() < 5) {
            original.call(instance, p_36400_);
            return;
        }
        original.call(instance, p_36400_ * 0.5f);
    }

    @WrapOperation(
            method = "checkMovementStatistics",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;" +
                            "causeFoodExhaustion(F)V")
    )
    private void imbuence$halfFoodExhaustion2(Player instance, float p_36400_,
                       Operation<Void> original) {
        if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(instance) != 5) {
            original.call(instance, p_36400_);
            return;
        }
        if (instance.getActiveEffects().size() < 5) {
            original.call(instance, p_36400_);
            return;
        }
        original.call(instance, p_36400_ * 0.5f);
    }
}
