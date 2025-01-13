package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.accessors.MobEffectInstanceAccessor;
import io.github.raverbury.imbuence.events.PotionDrinkAndApplyEffectEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class CurseOfTheDragonPalaceEnchantment extends Enchantment {

    public static final String ID = "curse_of_the_dragon_palace";

    public CurseOfTheDragonPalaceEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_HEAD,
                new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    @SubscribeEvent
    public static void playerTickHandler(TickEvent.PlayerTickEvent e) {
        if (e.side.isClient()) {
            return;
        }
        ItemStack helmetIS = e.player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetIS.isEmpty()) {
            return;
        }
        if (e.player.isUnderWater()) {
            if (helmetIS.getEnchantmentLevel(
                    ModRegistries.CURSE_OF_THE_DRAGON_PALACE_ENCHANTMENT.get()) > 0) {
                e.player.awardStat(Stats.TIME_SINCE_REST);
                e.player.awardStat(Stats.TIME_SINCE_REST);
            }
            if (helmetIS.is(Items.TURTLE_HELMET)) {
                helmetIS.setDamageValue(helmetIS.getDamageValue() - 1);
                e.player.addEffect(new MobEffectInstance(
                        MobEffects.WATER_BREATHING, 219, 0, true, true
                ));
            }
        }
    }

    @SubscribeEvent
    public static void potionDrinkApplyEffectHandler(PotionDrinkAndApplyEffectEvent e) {
        ItemStack helmetIS = e.drinker.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetIS.isEmpty()) {
            return;
        }

        CompoundTag tag = e.potion.getTag();
        if (tag != null) {
            if (tag.getString("Potion").contains("turtle_master")) {
                if (helmetIS.is(
                        Items.TURTLE_HELMET) && helmetIS.getEnchantmentLevel(
                        ModRegistries.CURSE_OF_THE_DRAGON_PALACE_ENCHANTMENT.get()) > 0) {
                    if (e.mobEffectInstance.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
                        e.isCancelled = true;
                    }
                    if (e.mobEffectInstance.getEffect() == MobEffects.DAMAGE_RESISTANCE) {
                        e.mobEffectInstance = new MobEffectInstance(
                                e.mobEffectInstance.getEffect(),
                                e.mobEffectInstance.getDuration() * 24,
                                e.mobEffectInstance.getAmplifier(),
                                e.mobEffectInstance.isAmbient(),
                                e.mobEffectInstance.isVisible(),
                                e.mobEffectInstance.showIcon(),
                                ((MobEffectInstanceAccessor) e.mobEffectInstance).imbuence$getHiddenMobEffectInstance(),
                                e.mobEffectInstance.getFactorData());
                    }
                }
            }
        }
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(
                otherEnchantment);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.HEAD);
    }
}
