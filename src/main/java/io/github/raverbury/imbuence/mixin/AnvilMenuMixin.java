package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.enchantment.PuzzleEnchantment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @WrapOperation(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;giveExperienceLevels(I)V"
            )
    )
    private void imbuence$reduceExpCost(Player instance, int i,
                                        Operation<Void> original) {
        if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(
                instance) != 1 || i == 0) {
            original.call(instance, i);
            return;
        }
        original.call(instance, Math.max(-1, (int) (((float) i) * 0.5f)));
    }
}
