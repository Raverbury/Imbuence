package io.github.raverbury.imbuence.enchantment.base;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public abstract class UniqueHelmetEnchantment extends Enchantment {
    protected UniqueHelmetEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_HEAD,
                new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment otherEnchantment) {
        return super.checkCompatibility(
                otherEnchantment) && !(otherEnchantment instanceof UniqueHelmetEnchantment);
    }
}
