package com.github.raverbury;

import com.github.raverbury.effect.JudgementEffect;
import com.github.raverbury.effect.ForbiddenSyzygyEffect;
import com.github.raverbury.effect.MarePotentiaEffect;
import com.github.raverbury.effect.SanctaPotentiaEffect;
import com.github.raverbury.enchantment.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistries {
    public static final EnchantmentCategory SHIELD_CATEGORY = EnchantmentCategory.create("shield", item -> item instanceof ShieldItem);
    public static final EnchantmentCategory MELEE_WEAPON_CATEGORY = EnchantmentCategory.create("melee_weapon", item -> item instanceof SwordItem || item instanceof AxeItem || item instanceof TridentItem);
    public static final EnchantmentCategory RANGED_WEAPON_CATEGORY = EnchantmentCategory.create("ranged_weapon", item -> item instanceof BowItem || item instanceof CrossbowItem);

    private static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Imbuence.MODID);
    public static final RegistryObject<Enchantment> SHADOW_WALKER_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("shadow_walker", ShadowWalkerEnchantment::new);
    public static final RegistryObject<Enchantment> COMBAT_ENGINEER_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("combat_engineer", CombatEngineerEnchantment::new);
    public static final RegistryObject<Enchantment> CRIME_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("crime", CrimeEnchantment::new);
    public static final RegistryObject<Enchantment> PUNISHMENT_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("punishment", PunishmentEnchantment::new);
    public static final RegistryObject<Enchantment> SACRED_CORONA_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("sacred_corona", SacredCoronaEnchantment::new);
    public static final RegistryObject<Enchantment> SHINING_MARIA_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("shining_maria", ShiningMariaEnchantment::new);
    public static final RegistryObject<Enchantment> FORTRESS_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("fortress", FortressEnchantment::new);
    public static final RegistryObject<Enchantment> MULTIPLATE_ENCHANTMENT = ENCHANTMENT_REGISTRY.register("multiplate", MultiplateEnchantment::new);
    public static final RegistryObject<Enchantment> AWARENESS_ENCHANTMENT = ENCHANTMENT_REGISTRY.register(AwarenessEnchantment.ID, AwarenessEnchantment::new);
    public static final RegistryObject<Enchantment> PREPAREDNESS = ENCHANTMENT_REGISTRY.register(PreparednessEnchantment.ID, PreparednessEnchantment::new);

    private static final DeferredRegister<MobEffect> MOB_EFFECT_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Imbuence.MODID);
    public static final RegistryObject<MobEffect> JUDGEMENT_EFFECT = MOB_EFFECT_REGISTRY.register("judgement", JudgementEffect::new);
    public static final RegistryObject<MobEffect> SANCTA_POTENTIA_EFFECT = MOB_EFFECT_REGISTRY.register("sancta_potentia", SanctaPotentiaEffect::new);
    public static final RegistryObject<MobEffect> MARE_POTENTIA_EFFECT = MOB_EFFECT_REGISTRY.register("mare_potentia", MarePotentiaEffect::new);
    public static final RegistryObject<MobEffect> FORBIDDEN_SYZYGY_EFFECT = MOB_EFFECT_REGISTRY.register("forbidden_syzygy", ForbiddenSyzygyEffect::new);

    public static final int TICKS_IN_DAY = 24000;

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENCHANTMENT_REGISTRY.register(modEventBus);
        MOB_EFFECT_REGISTRY.register(modEventBus);
    }
}
