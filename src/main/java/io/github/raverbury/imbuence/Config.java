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
                        "form")
                .defineInRange("fortress_max_health_ratio_per_level", 0.01, 0,
                        0.1);

        FORTRESS_SCALES_WORSE_AS_MAX_HEALTH_INCREASE = CONFIG_BUILDER
                .comment("""
                        Control whether Fortress scales less effectively as \
                        max health increases. Remember that Fortress applies \
                        to every instance of damage dealt, not just melee, and it's not a lot worse. \
                        When off, it is just maxHP * fortress_max_health_ratio_growth * level.
                        When on, it is (maxHP + 20)/1.3 * fortress_max_health_ratio_growth * level, \
                        or better at base vanilla health but scales a bit slower (still linear and infinite)\
                        """)
                .define("fortress_scales_worse", true);

        COMMANDER_BONUS_DAMAGE_PER_PET = CONFIG_BUILDER
                .comment("Each pet provides this much attack damage with " +
                        "Commander enchantment")
                .defineInRange("commander_bonus_damage_per_pet", 0.5, 0.0, 5.0);

        COMMANDER_MAX_BONUS_DAMAGE = CONFIG_BUILDER
                .comment("The max bonus damage provided by Commander, to " +
                        "discourage having too many pets causing entity lag")
                .defineInRange("commander_max_bonus_damage", 5.0, 0.0, 20.0);

        CRIME_PUNISHMENT_BONUS_DAMAGE_PER_LEVEL = CONFIG_BUILDER
                .comment("Crime and Punishment deals this much damage per " +
                        "level. Half of this is given based on their combined" +
                        "levels, and half is given based on their min level " +
                        "to retain some of the original \"balance\" theme")
                .defineInRange("crime_punishment_bonus_damage_per_level", 1,
                        0.1, 10.0);

        PET_QUERY_RANGE = CONFIG_BUILDER
                .comment("The range to check for nearby pets")
                .defineInRange("pet_query_range", 12.0, 1.0, 16.0);

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}
