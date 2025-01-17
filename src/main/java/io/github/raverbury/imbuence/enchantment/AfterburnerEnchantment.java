package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.ModdedAwareEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AfterburnerEnchantment extends ModdedAwareEnchantment {

    public static final String ID = "afterburner";

    public AfterburnerEnchantment() {
        super(Rarity.VERY_RARE, ModRegistries.ELYTRA_CATEGORY,
                new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    public static int getMininumFlightDuration(int level) {
        return Math.min(5, 1 + level);
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ElytraItem);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public int getLevelOneCost() {
        return 22;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxModdedLevel() {
        return 4;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + level * 32;
    }
}
