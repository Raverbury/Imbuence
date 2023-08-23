package com.github.raverbury;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Imbuence.MODID)
public class Imbuence
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "imbuence";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Imbuence()
    {
        ModRegistries.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
