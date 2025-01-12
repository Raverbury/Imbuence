package io.github.raverbury.imbuence.mixin;

import io.github.raverbury.imbuence.accessors.MobEffectInstanceAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceAccessor {

    @Shadow
    private MobEffectInstance hiddenEffect;

    @Override
    public MobEffectInstance imbuence$getHiddenMobEffectInstance() {
        return this.hiddenEffect;
    }
}
