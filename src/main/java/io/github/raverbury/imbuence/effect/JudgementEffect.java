package io.github.raverbury.imbuence.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class JudgementEffect extends MobEffect {
    public JudgementEffect() {
        super(MobEffectCategory.HARMFUL, 0x808080);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}
