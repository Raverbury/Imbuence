package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.UniqueChestplateEnchantment;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class FortressEnchantment extends UniqueChestplateEnchantment {

    public FortressEnchantment() {
        super(Rarity.VERY_RARE);
    }

    @SubscribeEvent
    public static void onCBDFEE(CalculateBonusDamageFromEnchantmentEvent event) {
        if (event.isCanceled() || event.attacker == null || !event.handItem.isEmpty()) {
            return;
        }
        int fortressLevel = EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.FORTRESS_ENCHANTMENT.get(), event.attacker);
        if (fortressLevel <= 0) {
            return;
        }
        double maxHealthRatio =
                getMaxHealthRatio(fortressLevel);
        double bonusMaxHealthDamage =
                event.attacker.getMaxHealth() * maxHealthRatio;
        event.customBonusDamage += (float) bonusMaxHealthDamage * event.attackStrengthScale;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
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
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return;
        }
        int fortressLevel = EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.FORTRESS_ENCHANTMENT.get(), livingAttacker);
        if (fortressLevel <= 0) {
            return;
        }
        double maxHealthRatio =
                getMaxHealthRatio(fortressLevel);
        double bonusMaxHealthDamage = 2;
        if (Config.FORTRESS_SCALES_WORSE_AS_MAX_HEALTH_INCREASE.get()) {
            bonusMaxHealthDamage =
                    (livingAttacker.getMaxHealth() + 20) / 1.3 * maxHealthRatio;
        } else {
            bonusMaxHealthDamage =
                    livingAttacker.getMaxHealth() * maxHealthRatio;
        }
        event.setAmount(event.getAmount() + (float) bonusMaxHealthDamage);
    }

    public static double getMaxHealthRatio(int level) {
        return level * Config.FORTRESS_MAX_HEALTH_RATIO_GROWTH.get();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getLevelOneCost() {
        return 25;
    }

    @Override
    public int getMaxModdedLevel() {
        return 5;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 16 + 8 * level;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
