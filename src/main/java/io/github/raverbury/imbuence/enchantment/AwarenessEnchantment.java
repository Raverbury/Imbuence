package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.UniqueChestplateEnchantment;
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

    private static final float BASE_COOLDOWN = 25F;
    private static final float COOLDOWN_DECREASE = 3F;
    private static final float MINIMUM_COOLDOWN = 4F;

    private static final String NBT_KEY = Imbuence.MODID + "." + ID + "." + "on_cd_till";

    private static final int MAX_MODDED_LEVEL = 10;

    public AwarenessEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity()
                .level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        int awarenessLevel = EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.AWARENESS_ENCHANTMENT.get(), entity);
        if (awarenessLevel <= 0) {
            return;
        }
        ItemStack chestplateItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        CompoundTag nbt = chestplateItem.getOrCreateTag();
        long cdFinishTick = nbt.contains(NBT_KEY) ? nbt.getLong(NBT_KEY) : 0;
        long currentTick = entity.level().getGameTime();
        if (currentTick < cdFinishTick) {
            return;
        }
        event.setCanceled(true);
        long nextCdFinishTick = currentTick + getTicksOnCd(awarenessLevel);
        nbt.putLong(NBT_KEY, nextCdFinishTick);
        entity.level()
                .playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.SHIELD_BLOCK, entity.getSoundSource(), 0.2f,
                        1F);
        //        Imbuence.LOGGER.debug("Attack blocked, " + currentTick + " -> " + nextCdFinishTick);
    }

    public static long getTicksOnCd(int level) {
        return (long) (Math.max((BASE_COOLDOWN - COOLDOWN_DECREASE * level),
                MINIMUM_COOLDOWN) * 20);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getLevelOneCost() {
        return 12;
    }

    @Override
    public int getMaxModdedLevel() {
        return 7;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 22;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
