package io.github.raverbury.imbuence;

import com.mojang.logging.LogUtils;
import io.github.raverbury.imbuence.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Imbuence.MODID)
public class Imbuence {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "imbuence";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Imbuence() {
        ModLoadingContext.get()
                .registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModRegistries.register();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Config.Cache::onConfigReload);
    }

    public static Pair<Integer, List<LivingEntity>> getAndCountNearbyPets(Player player) {
        List<LivingEntity> nearbyEntitiesOwnedByPlayer = new ArrayList<>();
        int count = 0;
        for (LivingEntity livingEntity : player.level()
                .getEntitiesOfClass(LivingEntity.class,
                        player.getBoundingBox()
                                .inflate(Config.PET_QUERY_RANGE.get()))) {
            if (livingEntity instanceof OwnableEntity ownableEntity) {
                if (Objects.equals(ownableEntity.getOwnerUUID(),
                        player.getUUID())) {
                    count += 1;
                    if (ownableEntity.getClass() == Wolf.class) {
                        count += 1;
                    }
                    nearbyEntitiesOwnedByPlayer.add(livingEntity);
                }
            }
        }
        return new Pair<>(count, nearbyEntitiesOwnedByPlayer);
    }

    public static final class Util {
        public static boolean isArrowNotTrident(Entity entity) {
            return entity instanceof AbstractArrow && !(entity instanceof ThrownTrident);
        }
    }
}
