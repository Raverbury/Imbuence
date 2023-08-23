package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PunishmentEnchantment extends Enchantment {

    private static final float BASE_BONUS_DAMAGE = 1F;
    private static final float BONUS_DAMAGE_GROWTH = 1.5F;

    public PunishmentEnchantment() {
        super(Rarity.UNCOMMON, ModRegistries.MELEE_WEAPON_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
        int punishmentLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.PUNISHMENT_ENCHANTMENT.get(), livingAttacker);
        if (punishmentLevel <= 0) {
            return;
        }
        LivingEntity target = event.getEntity();
        if (!target.hasEffect(ModRegistries.JUDGEMENT_EFFECT.get())) {
            return;
        }
        int judgementLevel = Objects.requireNonNull(target.getEffect(ModRegistries.JUDGEMENT_EFFECT.get())).getAmplifier() + 1;
        int minSyncLevel = Math.min(judgementLevel, punishmentLevel);
        float bonusDamage = BASE_BONUS_DAMAGE + BONUS_DAMAGE_GROWTH * minSyncLevel;
        Imbuence.LOGGER.debug("min sync level of " + minSyncLevel + ", dealt " + bonusDamage + " damage");
        event.setAmount(event.getAmount() + bonusDamage);
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
        return 14 + level * 4;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 17;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem || itemStack.getItem() instanceof TridentItem);
    }
}
