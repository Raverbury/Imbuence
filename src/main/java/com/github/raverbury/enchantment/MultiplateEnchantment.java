package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import com.github.raverbury.enchantment.base.UniqueChestplateEnchantment;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class MultiplateEnchantment extends UniqueChestplateEnchantment {

    private static final float BASE_MAX_HEALTH_THRESHOLD = 70F;
    private static final float MAX_HEALTH_THRESHOLD_NONLINEAR_REDUCTION = 0.85F;
    private static final int BASE_DURABILITY_PENALTY = 3;

    public MultiplateEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        int multiplateLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.MULTIPLATE_ENCHANTMENT.get(), entity);
        if (multiplateLevel <= 0) {
            return;
        }
        DamageSource damageSource = event.getSource();
        if (damageSource == null || damageSource.getEntity() == null) {
            return;
        }
        double maxHealthThreshold = getMaxHealthThreshold(multiplateLevel);
        float damageCap = (float) (entity.getMaxHealth() * maxHealthThreshold);
        float finalDamage = event.getAmount();
        if (finalDamage <= damageCap) {
            return;
        }
        finalDamage = damageCap;
        ItemStack chestplate = entity.getItemBySlot(EquipmentSlot.CHEST);
//        Imbuence.LOGGER.debug("multiplate proc'd");
        if ((chestplate.getItem() instanceof ArmorItem) && chestplate.isDamageableItem()) {
            float absorbedDamage = event.getAmount() - damageCap;
            int durabilityPenalty = BASE_DURABILITY_PENALTY + (int) absorbedDamage;
            int durabilityDamage = chestplate.getDamageValue() + durabilityPenalty;
//            Imbuence.LOGGER.debug("absorbed " + absorbedDamage + " damage, durability penalty of " + durabilityPenalty);
            int finalDurabilityDamage = Math.min(chestplate.getMaxDamage(), durabilityDamage);
            chestplate.setDamageValue(finalDurabilityDamage);
        }
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SHIELD_BLOCK, entity.getSoundSource(), 1F, 1F);
        event.setAmount(finalDamage);
    }

    public static double getMaxHealthThreshold(int level) {
        return (BASE_MAX_HEALTH_THRESHOLD * Math.pow(MAX_HEALTH_THRESHOLD_NONLINEAR_REDUCTION, level)) / 100;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int level) {
        return 21 + level * 2;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 17;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem)itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
