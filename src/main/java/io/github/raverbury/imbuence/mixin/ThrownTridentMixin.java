package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
interface ProjectileAccessor {
    @Invoker("getOwner")
    public Entity imbuence$callGetOwner();
}

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin implements ProjectileAccessor {

    @WrapOperation(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")
    )
    private float imbuence$dispatchCBDFEE(ItemStack tridentIS,
                                          MobType p_44835_,
                                          Operation<Float> original,
                                          @Local(argsOnly = true) EntityHitResult entityHitResult) {
        LivingEntity owner =
                (this.imbuence$callGetOwner() instanceof LivingEntity) ?
                        (LivingEntity) this.imbuence$callGetOwner() : null;
        CalculateBonusDamageFromEnchantmentEvent event =
                new CalculateBonusDamageFromEnchantmentEvent(
                        owner,
                        tridentIS, entityHitResult.getEntity(), 1f);
        MinecraftForge.EVENT_BUS.post(event
        );
        return original.call(tridentIS, p_44835_) + event.customBonusDamage;
    }
}
