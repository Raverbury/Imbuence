package io.github.raverbury.imbuence;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Imbuence.MODID)
public class Imbuence {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "imbuence";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Imbuence() {
        ModRegistries.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static int getNearbyPetCounts(Player player) {
        int count = 0;
        for (TamableAnimal animal :
                player.level().getEntitiesOfClass(TamableAnimal.class,
                        player.getBoundingBox().inflate(12))) {
            if (animal.getOwnerUUID() == player.getUUID()) {
                if (animal.getClass() == Wolf.class) {
                    count += 1;
                }
                count += 1;
            }
        }
        return count;
    }
}
