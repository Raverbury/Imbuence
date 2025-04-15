package io.github.raverbury.imbuence.effect;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.events.EntityElementalDOTHurtEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HeatFragilityEffect extends MobEffect {
    public HeatFragilityEffect() {
        super(MobEffectCategory.HARMFUL, 0xA45EB5);
    }

    @SubscribeEvent
    public static void increaseBurnOrFreezeDamage(EntityElementalDOTHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        MobEffectInstance mobEffectInstance = livingEntity.getEffect(
                ModRegistries.HEAT_FRAGILITY_EFFECT.get());
        if (mobEffectInstance == null) {
            return;
        }
        int effectLevel = mobEffectInstance.getAmplifier() + 1;
        float percentMaxHealthDamage =
                (float) (Config.THERMOHEX_PERCENT_MAX_HEALTH_DOT_BASE.get() + effectLevel * Config.THERMOHEX_PERCENT_MAX_HEALTH_DOT_PER_LEVEL.get());
        float bonusDamage = Math.max(0.5f * effectLevel,
                livingEntity.getMaxHealth() * percentMaxHealthDamage);
        float damageMultiplier = 0f;
        if (event.getDamageSource()
                .equals(livingEntity.damageSources().onFire())) {
            damageMultiplier = 1f + Math.min(
                    livingEntity.getRemainingFireTicks() / 140f * 0.5f, 0.5f);
        } else if (event.getDamageSource()
                .equals(livingEntity.damageSources().freeze())) {
            damageMultiplier = (1f + Math.min(
                    livingEntity.getTicksFrozen() / 140f * 0.5f, 0.5f)) * 2f;
        }
        event.damageAmount += bonusDamage * damageMultiplier;
    }
}
