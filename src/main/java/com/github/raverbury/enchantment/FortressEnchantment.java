package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import com.github.raverbury.enchantment.base.UniqueChestplateEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class FortressEnchantment extends UniqueChestplateEnchantment {

    private static final float BASE_MAX_HEALTH_RATIO = 5F;
    private static final float MAX_HEALTH_RATIO_GROWTH = 5F;

    public FortressEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
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
        if (!(attacker instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingAttacker = (LivingEntity) attacker;
        int fortressLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.FORTRESS_ENCHANTMENT.get(), livingAttacker);
        if (fortressLevel <= 0) {
            return;
        }
        float maxHealthRatio = getMaxHealthRatio(fortressLevel);
        float bonusMaxHealthDamage = livingAttacker.getMaxHealth() * (maxHealthRatio / 100F);
        event.setAmount(event.getAmount() + bonusMaxHealthDamage);
//        Imbuence.LOGGER.debug("dealt " + maxHealthRatio + "% of " + livingAttacker.getMaxHealth() + " as " + bonusMaxHealthDamage + "bonus damage");
    }

    public static float getMaxHealthRatio(int level) {
        return BASE_MAX_HEALTH_RATIO + MAX_HEALTH_RATIO_GROWTH * level;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 12 + level * 3;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 16;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem)itemStack.getItem()).getSlot() == EquipmentSlot.CHEST);
    }
}
