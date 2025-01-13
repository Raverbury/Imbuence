package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.enchantment.PuzzleEnchantment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Animal.class)
public class AnimalMixin {
    @WrapOperation(
            method = "spawnChildFromBreeding",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Animal;" +
                            "setAge(I)V"
            )
    )
    private void imbuence$reduceBreedingCooldown(Animal instance, int i,
                                                 Operation<Void> original,
                                                 @Local(argsOnly = true) ServerLevel serverLevel) {
        int reducedCooldown = i == 6000 ? 200 : (int) (((float) i) * 0.2f);
        for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                instance.getBoundingBox().inflate(12))) {
            if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(
                    livingEntity) == 2) {
                original.call(instance, reducedCooldown);
                return;
            }
        }
        original.call(instance, i);
    }

    @WrapOperation(
            method = "finalizeSpawnChildFromBreeding",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Animal;" +
                            "setAge(I)V"
            )
    )
    private void imbuence$reduceBreedingCooldown2(Animal instance, int i,
                                                  Operation<Void> original,
                                                  @Local(argsOnly = true) ServerLevel serverLevel) {
        int reducedCooldown = i == 6000 ? 200 : (int) (((float) i) * 0.2f);
        for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                instance.getBoundingBox().inflate(12))) {
            if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(
                    livingEntity) == 2) {
                original.call(instance, reducedCooldown);
                return;
            }
        }
        original.call(instance, i);
    }
}
