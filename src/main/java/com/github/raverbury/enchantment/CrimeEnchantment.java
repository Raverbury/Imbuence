package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class CrimeEnchantment extends Enchantment {

    private static final int BASE_DURATION = 3;
    private static final int DURATION_GROWTH = 1;
    private static final int BASE_EFFECT_AMPLIFIER = -1;
    private static final int EFFECT_AMPLIFIER_GROWTH = 1;

    public CrimeEnchantment() {
        super(Rarity.UNCOMMON, ModRegistries.SHIELD_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        int crimeLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.CRIME_ENCHANTMENT.get(), event.getEntity());
        if (crimeLevel <= 0) {
            return;
        }
        DamageSource damageSource = event.getDamageSource();
        if (damageSource == null) {
            return;
        }
        Entity attacker = damageSource.getEntity();
        if (attacker == null) {
            return;
        }
        if (!(attacker instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingAttacker = (LivingEntity) attacker;
        int judgementDuration = (BASE_DURATION + DURATION_GROWTH * crimeLevel) * 20;
        int judgementAmplifier = BASE_EFFECT_AMPLIFIER + EFFECT_AMPLIFIER_GROWTH * crimeLevel;
        livingAttacker.addEffect(new MobEffectInstance(ModRegistries.JUDGEMENT_EFFECT.get(), judgementDuration, judgementAmplifier));
//        Imbuence.LOGGER.debug("applied judgement at amp " + judgementAmplifier);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment);
    }

    @Override
    public int getMinCost(int level) {
        return 11 + level * 4;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 12;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ShieldItem);
    }
}
