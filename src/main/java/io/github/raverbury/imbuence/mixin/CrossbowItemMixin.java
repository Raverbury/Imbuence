package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.RocketSpecialistEnchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @WrapOperation(
            method = "shootProjectile",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;DDDZ)Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;")
    )
    private static FireworkRocketEntity imbuence$saveRocketSpecialistLevelInItemStackTag(Level level,
                                                                                         ItemStack itemStack,
                                                                                         Entity entity, double p_37053_,
                                                                                         double p_37054_, double p_37055_, boolean p_37056_, Operation<FireworkRocketEntity> original) {
        if (!(entity instanceof LivingEntity)) {
            return original.call(level, itemStack, entity, p_37053_, p_37054_,
                    p_37055_, p_37056_);
        }
        ItemStack fakeNewFireworkItemStack = itemStack.copy();
        int rocketSpecialistLevel = EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.ROCKET_SPECIALIST_ENCHANTMENT.get(),
                (LivingEntity) entity);
        RocketSpecialistEnchantment.saveRocketSpecialistLevelInTag(
                fakeNewFireworkItemStack, rocketSpecialistLevel);
        return original.call(level, fakeNewFireworkItemStack, entity, p_37053_,
                p_37054_,
                p_37055_, p_37056_);
    }
}
