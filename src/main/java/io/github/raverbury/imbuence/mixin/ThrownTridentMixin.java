package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.ThermoshockEnchantment;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
interface ProjectileAccessor {
    @Invoker("getOwner")
    Entity imbuence$callGetOwner();
}

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin implements ProjectileAccessor {

    @Shadow
    private ItemStack tridentItem;

    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float imbuence$dispatchCBDFEE(ItemStack tridentIS, MobType p_44835_, Operation<Float> original, @Local(argsOnly = true) EntityHitResult entityHitResult) {
        LivingEntity owner = (this.imbuence$callGetOwner() instanceof LivingEntity) ? (LivingEntity) this.imbuence$callGetOwner() : null;
        CalculateBonusDamageFromEnchantmentEvent event = new CalculateBonusDamageFromEnchantmentEvent(
                owner, tridentIS, entityHitResult.getEntity(), 1f,
                CalculateBonusDamageFromEnchantmentEvent.AttackType.THROWN_TRIDENT);
        MinecraftForge.EVENT_BUS.post(event);
        return original.call(tridentIS, p_44835_) + event.customBonusDamage;
    }

    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet" + "/minecraft/world/entity/projectile/ThrownTrident;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void clearBurnAndFreeze(ThrownTrident instance, SoundEvent soundEvent, float v1, float v2, Operation<Void> original, @Local(argsOnly = true) EntityHitResult entityHitResult) {
        if (EnchantmentHelper.getItemEnchantmentLevel(
                ModRegistries.THERMOSHOCK_ENCHANTMENT.get(),
                this.tridentItem) > 0) {
            Entity entity = entityHitResult.getEntity();
            if (entity instanceof LivingEntity target && ThermoshockEnchantment.shouldApplyBonusDamage(
                    target)) {
                instance.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5f, 1f);
                entity.clearFire();
                entity.setTicksFrozen(0);
            }
        }
        original.call(instance, soundEvent, v1, v2);
    }
}
