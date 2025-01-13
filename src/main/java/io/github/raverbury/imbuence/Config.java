package io.github.raverbury.imbuence;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue FORTRESS_MAX_HEALTH_RATIO_GROWTH;
    public static final ForgeConfigSpec.DoubleValue FORTRESS_PUNCH_SCALING;
    public static final ForgeConfigSpec.DoubleValue COMMANDER_BONUS_DAMAGE_PER_PET;
    public static final ForgeConfigSpec.DoubleValue COMMANDER_MAX_BONUS_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue PET_QUERY_RANGE;
    private static final ForgeConfigSpec.Builder CONFIG_BUILDER =
            new ForgeConfigSpec.Builder();

    static {
        FORTRESS_MAX_HEALTH_RATIO_GROWTH = CONFIG_BUILDER
                .comment("Max health ratio per level")
                .defineInRange("fortress_max_health_growth", 0.025, 0, 0.1);

        FORTRESS_PUNCH_SCALING = CONFIG_BUILDER
                .comment("Punches' bonus damage from Fortress is scaled by " +
                        "this value")
                .defineInRange("fortress_punch_scaling", 2.0, 0.0, 5.0);

        COMMANDER_BONUS_DAMAGE_PER_PET = CONFIG_BUILDER
                .comment("Each pet provides this much attack damage")
                .defineInRange("commander_bonus_damage_per_pet", 0.5, 0.0, 5.0);

        COMMANDER_MAX_BONUS_DAMAGE = CONFIG_BUILDER
                .comment("The max bonus damage provided by Commander")
                .defineInRange("commander_max_bonus_damage", 5.0, 0.0, 20.0);

        PET_QUERY_RANGE = CONFIG_BUILDER
                .comment("The range to check for nearby pets")
                .defineInRange("pet_query_range", 12.0, 1.0, 16.0);

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}
