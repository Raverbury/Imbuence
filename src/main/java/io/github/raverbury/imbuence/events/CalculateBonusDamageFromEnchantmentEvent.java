package io.github.raverbury.imbuence.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class CalculateBonusDamageFromEnchantmentEvent extends Event {

    public LivingEntity attacker;
    public ItemStack handItem;
    public Entity target;
    public float customBonusDamage;
    public float attackStrengthScale;

    public CalculateBonusDamageFromEnchantmentEvent(LivingEntity attacker,
                                                    ItemStack handItem,
                                                    Entity target,
                                                    float attackStrengthScale) {
        this.attacker = attacker;
        this.handItem = handItem;
        this.target = target;
        this.attackStrengthScale = attackStrengthScale;
        this.customBonusDamage = 0f;
    }
}
