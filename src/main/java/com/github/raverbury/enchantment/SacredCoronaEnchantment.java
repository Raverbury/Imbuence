package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.ArrowDamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class SacredCoronaEnchantment extends Enchantment {

    public SacredCoronaEnchantment() {
        super(Rarity.RARE, ModRegistries.RANGED_WEAPON_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        final boolean HAS_SACRED_CORONA = EnchantmentHelper.getEnchantmentLevel(ModRegistries.SACRED_CORONA_ENCHANTMENT.get(), entity) >= 1;
        final boolean HAS_SHINING_MARIA = EnchantmentHelper.getEnchantmentLevel(ModRegistries.SHINING_MARIA_ENCHANTMENT.get(), entity) >= 1;
        long dayTick = entity.level.getDayTime() % ModRegistries.TICKS_IN_DAY;
        long firstHalfThreshold = ModRegistries.TICKS_IN_DAY - (ModRegistries.TICKS_IN_DAY / 2);
        if (HAS_SACRED_CORONA && HAS_SHINING_MARIA) {
            entity.removeEffect(ModRegistries.SANCTA_POTENTIA_EFFECT.get());
            entity.removeEffect(ModRegistries.MARE_POTENTIA_EFFECT.get());
            entity.addEffect(new MobEffectInstance(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get(), 9999 * 20, 0, true, false, true));
        }
        else if (HAS_SACRED_CORONA && dayTick < firstHalfThreshold) {
            entity.addEffect(new MobEffectInstance(ModRegistries.SANCTA_POTENTIA_EFFECT.get(), 9999 * 20, 0, true, false, true));
            entity.removeEffect(ModRegistries.MARE_POTENTIA_EFFECT.get());
            entity.removeEffect(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get());
        }
        else if (HAS_SHINING_MARIA && dayTick >= firstHalfThreshold) {
            entity.removeEffect(ModRegistries.SANCTA_POTENTIA_EFFECT.get());
            entity.addEffect(new MobEffectInstance(ModRegistries.MARE_POTENTIA_EFFECT.get(), 9999 * 20, 0, true, false, true));
            entity.removeEffect(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get());
        }
        else {
            entity.removeEffect(ModRegistries.SANCTA_POTENTIA_EFFECT.get());
            entity.removeEffect(ModRegistries.MARE_POTENTIA_EFFECT.get());
            entity.removeEffect(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get());
        }
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && !(otherEnchantment instanceof ArrowDamageEnchantment);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}
