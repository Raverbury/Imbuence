package io.github.raverbury.imbuence.accessors;

import net.minecraft.world.effect.MobEffectInstance;

public interface MobEffectInstanceAccessor {
    MobEffectInstance imbuence$getHiddenMobEffectInstance();
    void imbuence$setDuration(int newDuration);
}
