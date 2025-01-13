package io.github.raverbury.imbuence.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.raverbury.imbuence.enchantment.PuzzleEnchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
interface EntityAccessor {
    @Accessor("level")
    public Level imbuence$getLevel();

    @Invoker("getBoundingBox")
    public AABB imbuence$callGetBoundingBox();
}

@Mixin(ZombieVillager.class)
public abstract class ZombieVillagerMixin implements EntityAccessor {
    @Inject(
            method = "startConverting",
            at = @At(
                    value = "HEAD"
            )
    )
    private void imbuence$reduceConversionCooldown(UUID p_34384_,
                                                   int p_34385_,
                                                   CallbackInfo ci,
                                                   @Local(argsOnly = true) LocalIntRef cooldown) {
        for (LivingEntity livingEntity :
                this.imbuence$getLevel().getEntitiesOfClass(LivingEntity.class,
                        this.imbuence$callGetBoundingBox().inflate(12))) {
            if (PuzzleEnchantment.getSlotsWithPuzzleEnchantmentCount(
                    livingEntity) == 2) {
                cooldown.set(p_34385_ > 200 ? 200 :
                        (int) (((float) p_34385_) * 0.2f));
                return;
            }
        }
    }
}
