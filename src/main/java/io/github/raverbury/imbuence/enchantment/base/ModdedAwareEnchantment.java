package io.github.raverbury.imbuence.enchantment.base;

import io.github.raverbury.imbuence.util.MathUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class ModdedAwareEnchantment extends Enchantment {
    protected ModdedAwareEnchantment(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot[] p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }

    @Override
    public int getMinCost(int level) {
        return MathUtil.getModdedAwareMinCost(getLevelOneCost(), level,
                getMaxLevel(),
                getMaxModdedLevel());
    }

    public abstract int getLevelOneCost();

    public abstract int getMaxModdedLevel();
}
