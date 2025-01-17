package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.UniqueChestplateEnchantment;
import io.github.raverbury.imbuence.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber
public class WolfPackEnchantment extends UniqueChestplateEnchantment {

    public static final String ID = "wolf_pack";

    public WolfPackEnchantment() {
        super(Rarity.VERY_RARE);
    }

    @SubscribeEvent
    public static void playerTickHandler(TickEvent.PlayerTickEvent e) {
        if (e.side.isClient()) {
            return;
        }

        ItemStack chestplateIS = e.player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplateIS.isEmpty() || chestplateIS.getEnchantmentLevel(
                ModRegistries.WOLF_PACK_ENCHANTMENT.get()) == 0) {
            return;
        }

        Pair<Integer, List<LivingEntity>> nearbyOwnedEntities =
                Imbuence.getAndCountNearbyPets(e.player);

        if (nearbyOwnedEntities.getLeft() < 2) {
            return;
        }

        int effectLevel = nearbyOwnedEntities.getLeft() >= 4 ? 1 : 0;

        for (LivingEntity livingEntity : nearbyOwnedEntities.getRight()) {
            livingEntity.addEffect(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 39,
                            effectLevel, true, true));
            livingEntity.addEffect(
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 39,
                            effectLevel, true, true));
            if (nearbyOwnedEntities.getLeft() >= 4) {
                if (!livingEntity.hasEffect(MobEffects.REGENERATION)) {
                    livingEntity.addEffect(
                            new MobEffectInstance(MobEffects.REGENERATION,
                                    90,
                                    0, true, true));
                }
                livingEntity.addEffect(
                        new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
                                39,
                                0, true, true));
            }
        }
    }

    @Override
    public int getLevelOneCost() {
        return 29;
    }

    @Override
    public int getMaxModdedLevel() {
        return getMaxLevel();
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 33;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
