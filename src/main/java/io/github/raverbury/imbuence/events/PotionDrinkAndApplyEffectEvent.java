package io.github.raverbury.imbuence.events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class PotionDrinkAndApplyEffectEvent extends Event {
    public LivingEntity drinker;
    public ItemStack potion;
    public MobEffectInstance mobEffectInstance;
    public boolean isCancelled;

    public PotionDrinkAndApplyEffectEvent(LivingEntity drinker,
                                          ItemStack potion,
                                          MobEffectInstance mobEffectInstance) {
        this.drinker = drinker;
        this.potion = potion;
        this.mobEffectInstance = mobEffectInstance;
        this.isCancelled = false;
    }
}
