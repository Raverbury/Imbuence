package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.enchantment.PuzzleEnchantment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {
    @WrapOperation(
            method = "lambda$clickMenuButton$1(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/player/Player;ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;onEnchantmentPerformed(Lnet/minecraft/world/item/ItemStack;I)V")
    )
    private void imbuence$reduceExpCost(Player instance, ItemStack itemStack,
                                        int i,
                                        Operation<Void> original) {
        if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(
                instance) != 1 || i == 0) {
            original.call(instance, itemStack, i);
            return;
        }
        original.call(instance, itemStack, Math.max(1,
                (int) (((float) i) * 0.5f)));
    }
}
