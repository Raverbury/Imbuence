package io.github.raverbury.imbuence.mixin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("level")
    Level imbuence$getLevel();

    @Invoker("getBoundingBox")
    AABB imbuence$callGetBoundingBox();

    @Invoker("getEntityData")
    SynchedEntityData imbuence$callGetEntityData();
}
