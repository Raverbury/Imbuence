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

    private AttackType attackType = AttackType.OTHER;

    public CalculateBonusDamageFromEnchantmentEvent(LivingEntity attacker, ItemStack handItem, Entity target, float attackStrengthScale, AttackType attackType) {
        this.attacker = attacker;
        this.handItem = handItem;
        this.target = target;
        this.attackStrengthScale = attackStrengthScale;
        this.customBonusDamage = 0f;
        this.attackType = attackType;
    }

    public AttackType getAttackType() {
        return this.attackType;
    }

    public enum AttackType {
        PLAYER_MELEE, MOB_MELEE, THROWN_TRIDENT, OTHER,
    }
}
