package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.DefianceEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {
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
}
