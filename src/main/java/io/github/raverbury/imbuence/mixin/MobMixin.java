package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.DefianceEnchantment;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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

    @WrapOperation(
            method = "doHurtTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")
    )
    private float imbuence$dispatchCBDFEE(ItemStack handItem, MobType p_44835_
            , Operation<Float> original, @Local(argsOnly = true) Entity target) {
        CalculateBonusDamageFromEnchantmentEvent event =
                new CalculateBonusDamageFromEnchantmentEvent(
                        (LivingEntity) (Object) this,
                        handItem, target, 1f);
        MinecraftForge.EVENT_BUS.post(event
        );
        return original.call(handItem, p_44835_) + event.customBonusDamage;
    }
}
