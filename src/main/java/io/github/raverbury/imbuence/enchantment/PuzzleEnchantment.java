package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.accessors.MobEffectInstanceAccessor;
import io.github.raverbury.imbuence.events.PotionDrinkAndApplyEffectEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class PuzzleEnchantment extends Enchantment {
    public static final String ID = "puzzle";

    public PuzzleEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE,
                new EquipmentSlot[]{
                        EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
                        EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                        EquipmentSlot.LEGS, EquipmentSlot.FEET
                });
    }

    @SubscribeEvent
    public static void playerTickHandler(TickEvent.PlayerTickEvent e) {
        if (e.player.level().isClientSide()) {
            return;
        }
        if (getSlotsWithPuzzleEnchantmentCount(e.player) == 3) {
            e.player.addEffect(new MobEffectInstance(MobEffects.LUCK, 2, 2));
        }
    }

    @SubscribeEvent
    public static void potionDrinkApplyEffectHandler(PotionDrinkAndApplyEffectEvent e) {
        int puzzleCount = getSlotsWithPuzzleEnchantmentCount(e.drinker);
        if (puzzleCount != 5) {
            return;
        }

        if (e.mobEffectInstance.getEffect()
                .getCategory() == MobEffectCategory.HARMFUL) {
            return;
        }

        if (e.potion.getItem() instanceof PotionItem potionItem) {
            CompoundTag tag = e.potion.getTag();
            if (tag != null) {
                if (tag.getString("Potion").contains("turtle_master")) {
                    Imbuence.LOGGER.info("Woo");
                }
            }
        }

        e.mobEffectInstance =
                new MobEffectInstance(e.mobEffectInstance.getEffect(),
                        e.mobEffectInstance.getDuration() * 2,
                        e.mobEffectInstance.getAmplifier(),
                        e.mobEffectInstance.isAmbient(),
                        e.mobEffectInstance.isVisible(),
                        e.mobEffectInstance.showIcon(),
                        ((MobEffectInstanceAccessor) e.mobEffectInstance).imbuence$getHiddenMobEffectInstance(),
                        e.mobEffectInstance.getFactorData());
    }

    public static int getSlotsWithPuzzleEnchantmentCount(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return 0;
        }
        int count = 0;
        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            if (armorPiece == null || armorPiece.isEmpty()) {
                continue;
            }
            count += armorPiece.getEnchantmentLevel(
                    ModRegistries.PUZZLE_ENCHANTMENT.get());
        }
        ItemStack mainhandItemStack = livingEntity.getMainHandItem();
        if (!mainhandItemStack.isEmpty()) {
            count += mainhandItemStack.getEnchantmentLevel(
                    ModRegistries.PUZZLE_ENCHANTMENT.get());
        }
        ItemStack offhandItemStack = livingEntity.getOffhandItem();
        if (!offhandItemStack.isEmpty()) {
            count += offhandItemStack.getEnchantmentLevel(
                    ModRegistries.PUZZLE_ENCHANTMENT.get());
        }
        return count;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && itemStack.isEnchantable();
    }
}

