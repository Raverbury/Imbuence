package com.github.raverbury.enchantment;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import com.github.raverbury.enchantment.base.UniqueChestplateEnchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class PreparationEnchantment extends UniqueChestplateEnchantment {

    public static final String ID = "preparation";

    private static final int BASE_COOLDOWN = 20;
    private static final int BASE_DURATION = 0;
    private static final int DURATION_GROWTH = 1;

    private static final String NBT_KEY = Imbuence.MODID + "." + ID + "." + "last_combat_event";

    public PreparationEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource == null || damageSource.getEntity() == null || !(damageSource.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity attacker = (LivingEntity) damageSource.getEntity();
        checkAndApplyPreparation(entity);
        checkAndApplyPreparation(attacker);
    }

    private static void checkAndApplyPreparation(LivingEntity entity) {
        int preparationLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistries.PREPARATION_ENCHANTMENT.get(), entity);
        if (preparationLevel <= 0) {
            return;
        }
        long currentTick = entity.level().getGameTime();
        long outOfCombatCd = BASE_COOLDOWN * 20;
        ItemStack chestplateItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        CompoundTag nbt = chestplateItem.getOrCreateTag();
        long lastCombatTick = nbt.contains(NBT_KEY)? nbt.getLong(NBT_KEY) : 0;
        long elapsedTick = currentTick - lastCombatTick;
        nbt.putLong(NBT_KEY, currentTick);
//        Imbuence.LOGGER.debug("combat action at " + currentTick + ", exit combat at " + (currentTick + outOfCombatCd));
        if (elapsedTick < outOfCombatCd) {
//            Imbuence.LOGGER.debug(elapsedTick + " elapsed | cd " + outOfCombatCd);
            return;
        }
        int duration = BASE_DURATION + DURATION_GROWTH * preparationLevel;
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration * 20, 0));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration * 20, 0));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration * 20, 0));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration * 20, 0));
//        Imbuence.LOGGER.debug("preparedness proc'd");
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
        return super.canEnchant(itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem)itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST);
    }
}
