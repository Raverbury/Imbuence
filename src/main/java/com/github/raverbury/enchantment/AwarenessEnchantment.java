package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import com.github.raverbury.enchantment.base.UniqueChestplateEnchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
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
public class AwarenessEnchantment extends UniqueChestplateEnchantment {

    public static final String ID = "awareness";

    private static final float BASE_COOLDOWN = 30.25F;
    private static final float COOLDOWN_DECREASE = 2.5F;
    private static final float MINIMUM_COOLDOWN = 10F;

    private static final String NBT_KEY = Imbuence.MODID + "." + ID + "." + "on_cd_till";

    public AwarenessEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        int awarenessLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.AWARENESS_ENCHANTMENT.get(), entity);
        if (awarenessLevel <= 0) {
            return;
        }
        ItemStack chestplateItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        CompoundTag nbt = chestplateItem.getOrCreateTag();
        long cdFinishTick = nbt.contains(NBT_KEY) ? nbt.getLong(NBT_KEY) : 0;
        long currentTick = entity.level.getGameTime();
        if (currentTick < cdFinishTick) {
            return;
        }
        event.setCanceled(true);
        long nextCdFinishTick = currentTick + getTicksOnCd(awarenessLevel);
        nbt.putLong(NBT_KEY, nextCdFinishTick);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SHIELD_BLOCK, entity.getSoundSource(), 1F, 1F);
//        Imbuence.LOGGER.debug("Attack blocked, " + currentTick + " -> " + nextCdFinishTick);
    }

    public static long getTicksOnCd(int level) {
        return (long) (Math.max((BASE_COOLDOWN - COOLDOWN_DECREASE * level), MINIMUM_COOLDOWN) * 20);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int level) {
        return 16 + level * 3;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 22;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem)itemStack.getItem()).getSlot() == EquipmentSlot.CHEST);
    }
}
