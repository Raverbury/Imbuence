package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.AfterburnerEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @WrapOperation(
            method = "use",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;")
    )
    private FireworkRocketEntity imbuence$raiseMinFlightDuration(Level p_37058_,
                                                                 ItemStack p_37059_, LivingEntity p_37060_,
                                                                 Operation<FireworkRocketEntity> original) {
        ItemStack tmp = p_37059_.copy();
        int afterburnerLevel = EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.AFTERBURNER_ENCHANTMENT.get(), p_37060_);
        int minFlightDuration =
                AfterburnerEnchantment.getMininumFlightDuration(afterburnerLevel);
        int initialFlightDuration =
                p_37059_.getOrCreateTagElement("Fireworks").getByte("Flight");
        FireworkRocketItem.setDuration(tmp,
                (byte) Math.max(initialFlightDuration,
                        minFlightDuration));
        return original.call(p_37058_, tmp, p_37060_);
    }
}
