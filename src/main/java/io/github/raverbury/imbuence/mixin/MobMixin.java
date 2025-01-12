package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.DefianceEnchantment;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin {
    @WrapOperation(
            method = "maybeDisableShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V")
    )
    private void imbuence$shieldBlockInteractWithDefiance(ItemCooldowns instance,
                                                          Item p_41525_, int cooldown,
                                                          Operation<Void> original,
                                                          @Local(argsOnly = true) Player player,
                                                          @Local(ordinal = 1, argsOnly = true) ItemStack shieldItemStack) {
        int defianceLevel =
                shieldItemStack.getEnchantmentLevel(
                        ModRegistries.DEFIANCE_ENCHANTMENT.get());
        if (defianceLevel == 0) {
            original.call(instance, p_41525_, cooldown);
            return;
        }
        original.call(instance, p_41525_,
                (int) ((float) cooldown * (1 - DefianceEnchantment.SHIELD_DISABLE_COOLDOWN_REDUCTION_PERCENTAGE)));
        DefianceEnchantment.applySlowAndKnockback((Mob) (Object) this, player);
    }
}
