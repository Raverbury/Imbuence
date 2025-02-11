package io.github.raverbury.imbuence.enchantment;

import io.github.raverbury.imbuence.Config;
import io.github.raverbury.imbuence.Imbuence;
import io.github.raverbury.imbuence.ModRegistries;
import io.github.raverbury.imbuence.enchantment.base.UniqueHelmetEnchantment;
import io.github.raverbury.imbuence.util.Pair;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommanderEnchantment extends UniqueHelmetEnchantment {

    public static final String ID = "commander";
    private static final UUID ATTACK_DAMAGE_ATTRIB_MODIFIER_UUID =
            UUID.fromString("3b341e48-1264-4ede-a69d-53b64122334a");

    public CommanderEnchantment() {
        super(Rarity.RARE);
    }

    @SubscribeEvent
    public static void playerTickHandler(TickEvent.PlayerTickEvent e) {
        if (e.side.isClient() || e.phase != TickEvent.Phase.START || e.player.level().getGameTime() % 20 != 0) {
            return;
        }

        AttributeInstance attackDamageAttribute =
                e.player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttribute == null) {
            return;
        }
        attackDamageAttribute.removeModifier(
                ATTACK_DAMAGE_ATTRIB_MODIFIER_UUID);

        ItemStack helmetItem = e.player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetItem.isEmpty() || helmetItem.getEnchantmentLevel(
                ModRegistries.COMMANDER_ENCHANTMENT.get()) == 0) {
            return;
        }

        Pair<Integer, List<LivingEntity>> nearbyOwnedEntities =
                Imbuence.getAndCountNearbyPets(e.player);

        if (nearbyOwnedEntities.getLeft() > 0) {
            AttributeModifier attributeModifier = new AttributeModifier(
                    ATTACK_DAMAGE_ATTRIB_MODIFIER_UUID,
                    "Commander bonus attack damage",
                    Math.min(
                            nearbyOwnedEntities.getLeft() * Config.COMMANDER_BONUS_DAMAGE_PER_PET.get(),
                            Config.COMMANDER_MAX_BONUS_DAMAGE.get()),
                    AttributeModifier.Operation.ADDITION
            );
            attackDamageAttribute.addPermanentModifier(
                    attributeModifier);
        }
    }

    @Override
    public int getMinCost(int level) {
        return 25 + level * 4;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 33;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemStack) {
        return super.canEnchant(
                itemStack) && (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getEquipmentSlot() == EquipmentSlot.HEAD);
    }
}
