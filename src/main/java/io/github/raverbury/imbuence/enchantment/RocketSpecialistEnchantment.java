package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.ModdedAwareEnchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class RocketSpecialistEnchantment extends ModdedAwareEnchantment {

    public static final String ID = "rocket_specialist";

    public static final String NBT_TAG_KEY = "RocketSpecialist";

    public static final float MAX_RADIUS = 12f;

    public RocketSpecialistEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.CROSSBOW,
                new EquipmentSlot[]{
                        EquipmentSlot.MAINHAND
                });
    }

    public static byte getRocketSpecialistLevelFromTag(CompoundTag compoundTag) {
        if (compoundTag == null) {
            return 0;
        }

        return compoundTag.getByte(
                "RocketSpecialist");
    }

    public static void saveRocketSpecialistLevelInTag(ItemStack itemStack,
                                                      int level) {
        if (!itemStack.is(Items.FIREWORK_ROCKET)) {
            return;
        }
        itemStack.getOrCreateTagElement("Fireworks").putByte(NBT_TAG_KEY,
                (byte) level);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment);
    }

    @Override
    public int getLevelOneCost() {
        return 19;
    }

    @Override
    public int getMaxModdedLevel() {
        return 5;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 38;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof CrossbowItem);
    }

    /**
     * This is to refuse arrows
     * @param e
     */
    @SubscribeEvent
    public static void livingGetProjectileHandler(LivingGetProjectileEvent e) {
        if (!(e.getProjectileWeaponItemStack()
                .getItem() instanceof CrossbowItem)) {
            return;
        }
        if (EnchantmentHelper.getEnchantmentLevel(
                ModRegistries.ROCKET_SPECIALIST_ENCHANTMENT.get(),
                e.getEntity()) == 0) {
            return;
        }
        if (e.getProjectileItemStack().getItem() instanceof ArrowItem) {
            e.setProjectileItemStack(ItemStack.EMPTY);
        }
    }
}
