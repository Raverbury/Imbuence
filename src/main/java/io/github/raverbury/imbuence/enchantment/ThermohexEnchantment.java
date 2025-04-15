package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.ModdedAwareEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThermohexEnchantment extends ModdedAwareEnchantment {
    public ThermohexEnchantment() {
        super(Rarity.RARE, ModRegistries.RANGED_WEAPON_CATEGORY,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @SubscribeEvent
    public static void inflitctHeatFragility(LivingAttackEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity()
                .level().isClientSide()) {
            return;
        }
        DamageSource damageSource = event.getSource();
        if (damageSource == null) {
            return;
        }
        Entity attacker = damageSource.getEntity();
        if (attacker == null) {
            return;
        }
        Entity directEntity = damageSource.getDirectEntity();
        if (directEntity == null) {
            return;
        }
        if (!(directEntity instanceof AbstractArrow) || !(attacker instanceof LivingEntity)) {
            return;
        }
        int thermoHexLevel =
                EnchantmentHelper.getEnchantmentLevel(
                        ModRegistries.THERMOHEX_ENCHANTMENT.get(),
                        (LivingEntity) attacker);
        event.getEntity().addEffect(new MobEffectInstance(
                ModRegistries.HEAT_FRAGILITY_EFFECT.get(), 200,
                thermoHexLevel - 1, true, false, true));
    }

    @Override
    public int getLevelOneCost() {
        return 14;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxModdedLevel() {
        return 5;
    }
}
