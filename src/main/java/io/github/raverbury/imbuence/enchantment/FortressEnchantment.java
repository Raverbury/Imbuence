package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.UniqueChestplateEnchantment;
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
        super(Rarity.RARE);
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
        if (((LivingEntity) attacker).getMainHandItem().isEmpty()) {
            maxHealthRatio *= Config.FORTRESS_PUNCH_SCALING.get();
        }
        double bonusMaxHealthDamage =
                livingAttacker.getMaxHealth() * maxHealthRatio;
        event.setAmount(event.getAmount() + (float) bonusMaxHealthDamage);
        //        Imbuence.LOGGER.debug("dealt " + maxHealthRatio + "% of " + livingAttacker.getMaxHealth() + " as " + bonusMaxHealthDamage + "bonus damage");
    }

    public static double getMaxHealthRatio(int level) {
        return level * Config.FORTRESS_MAX_HEALTH_RATIO_GROWTH.get();
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
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
