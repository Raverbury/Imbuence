package io.github.raverbury.imbuence;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.IntValue OVERCAP_MAX_COST;
    public static final ForgeConfigSpec.DoubleValue FORTRESS_MAX_HEALTH_RATIO_GROWTH;
    public static final ForgeConfigSpec.BooleanValue FORTRESS_SCALES_WORSE_AS_MAX_HEALTH_INCREASE;
    public static final ForgeConfigSpec.DoubleValue COMMANDER_BONUS_DAMAGE_PER_PET;
    public static final ForgeConfigSpec.DoubleValue COMMANDER_MAX_BONUS_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue CRIME_PUNISHMENT_BONUS_DAMAGE_PER_LEVEL;
    public static final ForgeConfigSpec.DoubleValue PET_QUERY_RANGE;
    public static final ForgeConfigSpec.BooleanValue FORBIDDEN_SYZYGY_APPLIES_GLOWING;
    public static final ForgeConfigSpec.BooleanValue PUZZLE_4_APPLIES_GLOWING;
    public static final ForgeConfigSpec.DoubleValue PUZZLE_6_DAMAGE_REDUCTION;
    public static final ForgeConfigSpec.DoubleValue PUZZLE_6_DAMAGE_REDUCTION_CAP;
    public static final ForgeConfigSpec.DoubleValue ROCKET_SPECIALIST_BONUS_DAMAGE_PER_LEVEL;
    public static final ForgeConfigSpec.DoubleValue ROCKET_SPECIALIST_BONUS_RADIUS_PER_LEVEL;
    public static final ForgeConfigSpec.DoubleValue SANCTA_POTENTIA_BONUS_FLAT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue MARE_POTENTIA_BONUS_PERCENT_DAMAGE;

    private static final ForgeConfigSpec.Builder CONFIG_BUILDER =
            new ForgeConfigSpec.Builder();

    static {
        OVERCAP_MAX_COST = CONFIG_BUILDER
                .comment("When enchantment levels are uncapped by other mods," +
                        " what is the maximum enchantment cost that should be" +
                        " used to scale this mod's overcapped levels, an" +
                        " example is Apotheosis with their default max cost" +
                        " of 200. This is to allow controlled overcap as" +
                        " opposed to uncontrolled.")
                .defineInRange("overcap_max_cost", 199, 31, 1000);

        FORTRESS_MAX_HEALTH_RATIO_GROWTH = CONFIG_BUILDER
                .comment("Fortress's max health ratio per level in decimal " +
                        "form.")
                .defineInRange("fortress_max_health_ratio_per_level", 0.01, 0,
                        0.1);

        FORTRESS_SCALES_WORSE_AS_MAX_HEALTH_INCREASE = CONFIG_BUILDER
                .comment("""
                        Control whether Fortress scales less effectively as \
                        max health increases. Remember that Fortress applies \
                        to every instance of damage dealt, not just melee, and it's not a lot worse. \
                        When off, it is just maxHP * fortress_max_health_ratio_growth * level.
                        When on, it is (maxHP + 20)/1.3 * fortress_max_health_ratio_growth * level, \
                        or better at base vanilla health but scales a bit slower (still linear and infinite).\
                        """)
                .define("fortress_scales_worse", true);

        COMMANDER_BONUS_DAMAGE_PER_PET = CONFIG_BUILDER
                .comment("Each pet provides this much attack damage with " +
                        "Commander enchantment.")
                .defineInRange("commander_bonus_damage_per_pet", 0.5, 0.0,
                        Double.MAX_VALUE);

        COMMANDER_MAX_BONUS_DAMAGE = CONFIG_BUILDER
                .comment("The max bonus damage provided by Commander, to " +
                        "discourage having too many pets causing entity lag.")
                .defineInRange("commander_max_bonus_damage", 5.0, 0.0, Double.MAX_VALUE);

        CRIME_PUNISHMENT_BONUS_DAMAGE_PER_LEVEL = CONFIG_BUILDER
                .comment("Crime and Punishment deals this much damage per " +
                        "level. Half of this is given based on their combined" +
                        "levels, and half is given based on their min level " +
                        "to retain some of the original \"balance\" theme.")
                .defineInRange("crime_punishment_bonus_damage_per_level", 2,
                        0.0, Double.MAX_VALUE);

        PET_QUERY_RANGE = CONFIG_BUILDER
                .comment("The range to check for nearby pets.")
                .defineInRange("pet_query_range", 12.0, 1.0, 64.0);

        FORBIDDEN_SYZYGY_APPLIES_GLOWING = CONFIG_BUILDER
                .comment("Controls whether Forbiden Syzygy applies Glowing to" +
                        " entities hit by arrows.")
                .define("forbidden_syzygy_applies_glowing", true);

        PUZZLE_4_APPLIES_GLOWING = CONFIG_BUILDER
                .comment("Controls whether Puzzle 4 effect applies Glowing to" +
                        " attacker.")
                .define("puzzle_4_applies_glowing", true);

        PUZZLE_6_DAMAGE_REDUCTION = CONFIG_BUILDER
                .comment("Puzzle 6 reduces damage taken by this much, " +
                        "multiplied by attacker's max health over victim's " +
                        "max health, in decimal form.")
                .defineInRange("puzzle_6_damage_reduction", 0.01d, 0d, 1d);

        PUZZLE_6_DAMAGE_REDUCTION_CAP = CONFIG_BUILDER
                .comment("Puzzle 6 damage reduction reaches a cap of " +
                        "this value, in decimal form.")
                .defineInRange("puzzle_6_damage_reduction_cap", 0.5d, 0d, 1d);

        ROCKET_SPECIALIST_BONUS_DAMAGE_PER_LEVEL = CONFIG_BUILDER
                .comment("Firework rockets shot with Rocket Specialist deal " +
                        "percentage increased damage, in decimal form.")
                .defineInRange("rocket_specialist_bonus_damage_per_level",
                        0.25d, 0d,
                        Double.MAX_VALUE);

        ROCKET_SPECIALIST_BONUS_RADIUS_PER_LEVEL = CONFIG_BUILDER
                .comment("Firework rockets shot with Rocket Specialist have " +
                        "percentage increased explosion radius, in decimal " +
                        "form.")
                .defineInRange("rocket_specialist_bonus_radius_per_level",
                        0.15d, 0d,
                        0.5d);

        SANCTA_POTENTIA_BONUS_FLAT_DAMAGE = CONFIG_BUILDER
                .comment("Sancta Potentia causes arrows to deal this much " +
                        "extra flat damage.")
                .defineInRange("sancta_potentia_bonus_flat_damage",
                        6d, 0d, Double.MAX_VALUE);

        MARE_POTENTIA_BONUS_PERCENT_DAMAGE = CONFIG_BUILDER
                .comment("Mare Potentia causes arrows to deal this much " +
                        "extra percentage damage, in decimal form.")
                .defineInRange("mare_potentia_bonus_percent_damage",
                        0.7d, 0d, Double.MAX_VALUE);

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}
