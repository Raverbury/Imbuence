package com.github.raverbury.enchantment;

import com.github.raverbury.ModRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ArrowDamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ShiningMariaEnchantment extends Enchantment {

    public ShiningMariaEnchantment() {
        super(Rarity.RARE, ModRegistries.RANGED_WEAPON_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && !(otherEnchantment instanceof ArrowDamageEnchantment);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem);
    }
}
