package io.github.raverbury.imbuence.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EntityElementalDOTHurtEvent extends LivingEvent {

    private final DamageSource damageSource;
    public float damageAmount = 0;

    public EntityElementalDOTHurtEvent(LivingEntity livingEntity,
                                       DamageSource damageSource,
                                       float damageAmount) {
        super(livingEntity);
        this.damageSource = damageSource;
        this.damageAmount = damageAmount;
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }
}
