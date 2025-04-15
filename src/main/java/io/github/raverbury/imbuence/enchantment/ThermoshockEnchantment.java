package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.ModdedAwareEnchantment;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThermoshockEnchantment extends ModdedAwareEnchantment {
    public ThermoshockEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.TRIDENT,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @SubscribeEvent
    public static void applyThermoshockDamage(CalculateBonusDamageFromEnchantmentEvent event) {
        if (event.getAttackType() != CalculateBonusDamageFromEnchantmentEvent.AttackType.THROWN_TRIDENT) {
            return;
        }
        if (!(event.target instanceof LivingEntity target)) {
            return;
        }
        if (shouldApplyBonusDamage(target)) {
            event.customBonusDamage += (float) (Config.THERMOSHOCK_BONUS_DAMAGE_PER_LEVEL.get() * event.handItem.getEnchantmentLevel(
                    ModRegistries.THERMOSHOCK_ENCHANTMENT.get()));
        }
    }

    public static boolean shouldApplyBonusDamage(LivingEntity target) {
        return (target.getRemainingFireTicks() > 0 && !target.fireImmune()) || (target.getTicksFrozen() > 0 && target.canFreeze());
    }

    @Override
    public int getLevelOneCost() {
        return 17;
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
