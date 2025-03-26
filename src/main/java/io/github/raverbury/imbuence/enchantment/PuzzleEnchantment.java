package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.events.PotionDrinkAndApplyEffectEvent;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
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
    public static void livingChangeEquipmentHandler(LivingEquipmentChangeEvent e) {
        if (e.getEntity() instanceof ServerPlayer player) {
            int puzzleCount = getSlotsWithPuzzleEnchantmentCount(player);
            if (puzzleCount == 0) {
                return;
            }
            ResourceLocation advancementId =
                    new ResourceLocation(Imbuence.MODID, "main/puzzle_" +
                            puzzleCount);
            Advancement advancement =
                    player.server.getAdvancements()
                            .getAdvancement(advancementId);
            if (advancement == null) {
                Imbuence.LOGGER.warn("Cannot get {} advancement",
                        advancementId);
                return;
            }
            player.getAdvancements().award(advancement,
                    "equipments_with_puzzle");
        }
    }

    @SubscribeEvent
    public static void playerTickHandler(TickEvent.PlayerTickEvent e) {
        if (e.side.isClient() || e.phase != TickEvent.Phase.START) {
            return;
        }
        if (getSlotsWithPuzzleEnchantmentCount(e.player) == 3) {
            e.player.addEffect(
                    new MobEffectInstance(MobEffects.LUCK, 119, 2, true,
                            true));
        }
    }

    @SubscribeEvent
    public static void puzzle4Handler(LivingAttackEvent e) {
        if (e.getEntity().level().isClientSide()) {
            return;
        }
        if (getSlotsWithPuzzleEnchantmentCount(e.getEntity()) == 4) {
            if (e.getSource()
                    .getEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.distanceToSqr(e.getEntity()) >= 16) {
                    if (Config.PUZZLE_4_APPLIES_GLOWING.get()) {
                        livingEntity.addEffect(
                                new MobEffectInstance(MobEffects.GLOWING, 80,
                                        0));
                    }
                    livingEntity.addEffect(
                            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                                    80, 3));
                    livingEntity.addEffect(
                            new MobEffectInstance(
                                    ModRegistries.JUDGEMENT_EFFECT.get(), 80,
                                    3)
                    );
                }
            }
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

        e.mobEffectInstance.update(
                new MobEffectInstance(
                        e.mobEffectInstance.getEffect(),
                        (int) ((float) e.mobEffectInstance.getDuration() * 1.5f),
                        e.mobEffectInstance.getAmplifier()
                )
        );
    }

    @SubscribeEvent
    public static void puzzle5ModifyEffectDuration(MobEffectEvent.Added event) {
        MobEffectInstance newMobEffectInstance = event.getEffectInstance();
        float durationModifier =
                newMobEffectInstance.getEffect()
                        .getCategory() == MobEffectCategory.HARMFUL ?
                        0.5f : 2f;
        newMobEffectInstance.update(
                new MobEffectInstance(
                        newMobEffectInstance.getEffect(),
                        (int) ((float) newMobEffectInstance.getDuration() * durationModifier),
                        newMobEffectInstance.getAmplifier()
                )
        );
    }

    @SubscribeEvent
    public static void entityDamageEvent(LivingHurtEvent e) {
        if (e.getEntity().level().isClientSide()) {
            return;
        }
        if (getSlotsWithPuzzleEnchantmentCount(e.getEntity()) == 6) {
            if (e.getSource()
                    .getEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.getMaxHealth() > e.getEntity()
                        .getMaxHealth()) {
                    float damageReductionFactor =
                            (float) Math.min(
                                    Config.PUZZLE_6_DAMAGE_REDUCTION_CAP.get(),
                                    livingEntity.getMaxHealth() / e.getEntity()
                                            .getMaxHealth() * Config.PUZZLE_6_DAMAGE_REDUCTION.get());
                    e.setAmount(
                            e.getAmount() * (1 - damageReductionFactor));
                }
            }
        }
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
}

