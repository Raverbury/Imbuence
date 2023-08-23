package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Imbuence.MODID)
public class CombatEngineerEnchantment extends Enchantment {

    public static final int BASE_PROC_CHANCE = 10;
    public static final int PROC_CHANCE_GROWTH = 10;
    public static final int BASE_DURABILITY_RESTORED = 1;
    public static final int DURABILITY_RESTORED_GROWTH = 1;

    public CombatEngineerEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        int combatEngineerLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.COMBAT_ENGINEER_ENCHANTMENT.get(), entity);
        if (combatEngineerLevel <= 0) {
            return;
        }
        DamageSource damageSource = event.getSource();
        if (damageSource == null || damageSource.getEntity() == null) {
            return;
        }
        Imbuence.LOGGER.debug(damageSource.getEntity().toString());
        ItemStack mainHandItemStack = entity.getMainHandItem();
        if (!(mainHandItemStack.getItem() instanceof DiggerItem) || !mainHandItemStack.isDamageableItem() || !mainHandItemStack.isDamaged()) {
            return;
        }
        int roll = entity.level.getRandom().nextIntBetweenInclusive(1, 100);
        int procChance = BASE_PROC_CHANCE + PROC_CHANCE_GROWTH * combatEngineerLevel;
        if (roll <= procChance) {
            int damageHealed = BASE_DURABILITY_RESTORED + DURABILITY_RESTORED_GROWTH * combatEngineerLevel;
            Imbuence.LOGGER.debug("healed item for " + damageHealed + " durability, rolled " + roll + ", proc chance " + procChance);
            int newDamageValue = Math.max(0, mainHandItemStack.getDamageValue() - damageHealed);
            mainHandItemStack.setDamageValue(newDamageValue);
        }
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
    public int getMinCost(int level) {
        return 17 + level * 3;
    }

    @Override
    public int getMaxCost(int level) {
        return 27 + level * 4;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof DiggerItem);
    }
}
