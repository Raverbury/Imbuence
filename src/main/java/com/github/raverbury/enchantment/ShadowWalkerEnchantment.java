package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Imbuence.MODID)
public class ShadowWalkerEnchantment extends Enchantment {

    private static final int MIN_INTERNAL_LIGHT_LEVEL_THRESHOLD = 0;
    private static final int MAX_INTERNAL_LIGHT_LEVEL_THRESHOLD = 6;
    private static final int INTERNAL_LIGHT_LEVEL_THRESHOLD_GROWTH = 2;

    public ShadowWalkerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        int shadowWalkerLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.SHADOW_WALKER_ENCHANTMENT.get(), entity);
        if (shadowWalkerLevel <= 0)
        {
            return;
        }
        Level level = entity.level;
        BlockPos blockPos = entity.blockPosition();
        int internalLightLevel = level.getRawBrightness(blockPos, level.getSkyDarken());
        // To prevent mods that raise level limit of enchantments from making this permanently active
        int internalLightLevelThreshold = Math.min(MIN_INTERNAL_LIGHT_LEVEL_THRESHOLD + INTERNAL_LIGHT_LEVEL_THRESHOLD_GROWTH * shadowWalkerLevel, MAX_INTERNAL_LIGHT_LEVEL_THRESHOLD);
        int strengthLevel = shadowWalkerLevel > 1? 1 : 0;
        if (internalLightLevel <= internalLightLevelThreshold && !entity.hasEffect(MobEffects.DAMAGE_BOOST)) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 4 * 20, strengthLevel, true, false, true));
            if (shadowWalkerLevel > 1) {
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 4 * 20, 0, true, false, true));
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && !(otherEnchantment instanceof FrostWalkerEnchantment);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem)itemStack.getItem()).getSlot() == EquipmentSlot.FEET);
    }
}
