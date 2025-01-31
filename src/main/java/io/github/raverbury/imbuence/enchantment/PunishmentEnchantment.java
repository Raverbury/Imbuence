package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.ModdedAwareEnchantment;
import io.github.raverbury.imbuence.events.CalculateBonusDamageFromEnchantmentEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PunishmentEnchantment extends ModdedAwareEnchantment {

    public PunishmentEnchantment() {
        super(Rarity.UNCOMMON, ModRegistries.MELEE_WEAPON_CATEGORY,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @SubscribeEvent
    public static void onEnchantBonusDamage(CalculateBonusDamageFromEnchantmentEvent event) {
        if (event.isCanceled() || event.handItem.isEmpty()) {
            return;
        }
        int punishmentLevel =
                event.handItem.getEnchantmentLevel(
                        ModRegistries.PUNISHMENT_ENCHANTMENT.get());
        if (punishmentLevel <= 0) {
            return;
        }
        if (!(event.target instanceof LivingEntity livingEntity)) {
            return;
        }
        if (!livingEntity.hasEffect(ModRegistries.JUDGEMENT_EFFECT.get())) {
            return;
        }
        int judgementLevel = Objects.requireNonNull(
                        livingEntity.getEffect(ModRegistries.JUDGEMENT_EFFECT.get()))
                .getAmplifier() + 1;
        int minSyncLevel = Math.min(judgementLevel, punishmentLevel);
        double halfBonusDamagePerLevel =
                Config.CRIME_PUNISHMENT_BONUS_DAMAGE_PER_LEVEL.get() * 0.5;
        float bonusDamage =
                (float) (halfBonusDamagePerLevel * ((judgementLevel + punishmentLevel) * 0.5f + minSyncLevel)) * event.attackStrengthScale;
        event.customBonusDamage += bonusDamage;
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
    public int getLevelOneCost() {
        return 14;
    }

    @Override
    public int getMaxModdedLevel() {
        return 6;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 28;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem || itemStack.getItem() instanceof TridentItem);
    }
}
