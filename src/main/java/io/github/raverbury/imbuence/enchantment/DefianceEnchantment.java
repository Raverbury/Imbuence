package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class DefianceEnchantment extends Enchantment {
    public static final String ID = "defiance";

    public static final float SHIELD_DISABLE_COOLDOWN_REDUCTION_PERCENTAGE =
            0.6f;

    public DefianceEnchantment() {
        super(Rarity.RARE, ModRegistries.SHIELD_CATEGORY,
                new EquipmentSlot[]{
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND,
                });
    }

    public static void applySlowAndKnockback(LivingEntity shieldDisabler,
                                             LivingEntity shieldHolder) {
        shieldDisabler.knockback(1,
                Mth.sin(
                        shieldHolder.getYRot() * ((float) Math.PI / 180F)),
                -Mth.cos(
                        shieldHolder.getYRot() * ((float) Math.PI / 180F)));
        shieldDisabler.addEffect(
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2));
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(
                otherEnchantment) && !(otherEnchantment instanceof CrimeEnchantment);
    }

    @Override
    public int getMinCost(int level) {
        return 20 + level * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 14;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ShieldItem);
    }
}

