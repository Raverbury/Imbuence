package com.github.raverbury.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ForbiddenSyzygyEffect extends MobEffect {
    public ForbiddenSyzygyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF2400);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}