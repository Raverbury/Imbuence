package com.github.raverbury.enchantment.base;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class UniqueChestplateEnchantment extends Enchantment {
    protected UniqueChestplateEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && !(otherEnchantment instanceof UniqueChestplateEnchantment);
    }
}
