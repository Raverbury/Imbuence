package io.github.raverbury.imbuence.compat.curios;

import io.github.raverbury.imbuence.ModRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

public class CuriosCompat {
    public static int getAfterburneronCurios(Player player) {

        String[] slotsToCheck = new String[] {"back", "body", "elytra"};

        LazyOptional<ICuriosItemHandler> optionalCurios =
                CuriosApi.getCuriosInventory(player);
        if (!optionalCurios.isPresent()) {
            return 0;
        }
        ICuriosItemHandler curiosInventory =
                optionalCurios.orElseThrow(NullPointerException::new);
        final int[] maxLevel = {0};
        for (String slot: slotsToCheck) {
            Optional<ICurioStacksHandler> stacksHandler =
                    curiosInventory.getStacksHandler(slot);
            if (stacksHandler.isPresent()) {
                stacksHandler.ifPresent(c -> {
                    for (int i = 0; i < c.getSlots(); i++) {
                        ItemStack itemStack = c.getStacks().getStackInSlot(i);
                        int level =
                                itemStack.getEnchantmentLevel(ModRegistries.AFTERBURNER_ENCHANTMENT.get());
                        if (level > maxLevel[0]) {
                            maxLevel[0] = level;
                        }
                    }
                });
            }
        }
        return maxLevel[0];
    }
}
