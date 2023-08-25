package com.github.raverbury.effect;

import com.github.raverbury.Imbuence;
import com.github.raverbury.ModRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MarePotentiaEffect extends MobEffect {

    private static final float PERCENT_BONUS_DAMAGE = 70;

    public MarePotentiaEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1F8AC4);
    }

    @SubscribeEvent
    public static void onArrowHurtEvent(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        DamageSource damageSource = event.getSource();
        if (damageSource == null) {
            return;
        }
        Entity attacker = damageSource.getEntity();
        if (attacker == null) {
            return;
        }
        Entity directEntity = damageSource.getDirectEntity();
        if (directEntity == null) {
            return;
        }
        if (!(directEntity instanceof AbstractArrow) || !(attacker instanceof LivingEntity)) {
            return;
        }
        final boolean HAS_MARE_POTENTIA = ((LivingEntity) attacker).hasEffect(ModRegistries.MARE_POTENTIA_EFFECT.get());
        final boolean HAS_FORBIDDEN_SYZYGY = ((LivingEntity) attacker).hasEffect(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get());
        if (!HAS_MARE_POTENTIA && !HAS_FORBIDDEN_SYZYGY) {
            return;
        }
        if (HAS_FORBIDDEN_SYZYGY) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.GLOWING, 5 * 20, 0, false, true));
        }
//        Imbuence.LOGGER.debug("shining maria procs");
        event.setAmount(event.getAmount() * (1 + (PERCENT_BONUS_DAMAGE / 100)));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamageEvent(LivingDamageEvent event) {
        if (event.isCanceled() || event.getEntity() == null || event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        final boolean HAS_MARE_POTENTIA = entity.hasEffect(ModRegistries.MARE_POTENTIA_EFFECT.get());
        final boolean HAS_FORBIDDEN_SYZYGY = entity.hasEffect(ModRegistries.FORBIDDEN_SYZYGY_EFFECT.get());
        if (!HAS_MARE_POTENTIA && !HAS_FORBIDDEN_SYZYGY) {
            return;
        }
        int roundedX = Math.abs((int) entity.getX());
        int roundedZ = Math.abs((int) entity.getZ());
        boolean oddTile = (roundedX + roundedZ) % 2 == 1;
        if (!oddTile) {
            return;
        }
        int damage = (int) event.getAmount();
        boolean oddDamage = damage % 2 == 1;
        if (damage > 4 || !oddDamage) {
            return;
        }
//        Imbuence.LOGGER.debug("shining maria prevents " + damage + " damage at " + roundedX + ":Y:" + roundedZ);
        event.setCanceled(true);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}