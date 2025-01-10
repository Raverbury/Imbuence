package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.ModRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class AfterburnerEnchantment extends Enchantment {

    public static final String ID = "afterburner";

    public AfterburnerEnchantment() {
        super(Rarity.VERY_RARE, ModRegistries.ELYTRA_CATEGORY,
                new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ElytraItem);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }
}
