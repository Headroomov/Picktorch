package minecraft.inventory.functions;

import minecraft.inventory.entities.HotbarIndex;
import minecraft.inventory.entities.PlayerInventorySlotId;
import minecraft.inventory.entities.ScreenHandlerSlotId;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class PlayerInventorySlots {
    private PlayerInventorySlots() {
    }

    public static HotbarIndex getSelectedHotbarIndex(PlayerInventory inventory) {
        Objects.requireNonNull(inventory, "inventory");

        return new HotbarIndex(inventory.selectedSlot);
    }

    public static PlayerInventorySlotId getSelectedPlayerInventorySlot(PlayerInventory inventory) {
        Objects.requireNonNull(inventory, "inventory");

        return new PlayerInventorySlotId(getSelectedHotbarIndex(inventory).value());
    }

    public static Optional<PlayerInventorySlotId> findSlot(PlayerInventory inventory,
                                                           Predicate<ItemStack> predicate) {
        Objects.requireNonNull(inventory, "inventory");
        Objects.requireNonNull(predicate, "predicate");

        for (int slot = 0; slot < PlayerInventorySlotId.MAIN_INVENTORY_SIZE; slot++) {
            ItemStack stack = inventory.getStack(slot);

            if (predicate.test(stack))
                return Optional.of(new PlayerInventorySlotId(slot));
        }

        return Optional.empty();
    }

    public static Optional<PlayerInventorySlotId> findSlotExcept(PlayerInventory inventory,
                                                                 Predicate<ItemStack> predicate,
                                                                 PlayerInventorySlotId excludedSlot) {
        Objects.requireNonNull(inventory, "inventory");
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(excludedSlot, "excludedSlot");

        for (int slot = 0; slot < PlayerInventorySlotId.MAIN_INVENTORY_SIZE; slot++) {
            if (slot == excludedSlot.value())
                continue;

            ItemStack stack = inventory.getStack(slot);

            if (predicate.test(stack))
                return Optional.of(new PlayerInventorySlotId(slot));
        }

        return Optional.empty();
    }

    public static Optional<ScreenHandlerSlotId> findScreenHandlerSlot(PlayerInventory inventory,
                                                                      ScreenHandler screenHandler,
                                                                      PlayerInventorySlotId inventorySlot) {
        Objects.requireNonNull(inventory, "inventory");
        Objects.requireNonNull(screenHandler, "screenHandler");
        Objects.requireNonNull(inventorySlot, "inventorySlot");

        for (int screenSlotId = 0; screenSlotId < screenHandler.slots.size(); screenSlotId++) {
            Slot screenSlot = screenHandler.slots.get(screenSlotId);

            if (screenSlot.inventory == inventory && screenSlot.getIndex() == inventorySlot.value())
                return Optional.of(new ScreenHandlerSlotId(screenSlotId));
        }

        return Optional.empty();
    }

    public static ScreenHandlerSlotId resolveScreenHandlerSlot(PlayerInventory inventory,
                                                               ScreenHandler screenHandler,
                                                               PlayerInventorySlotId inventorySlot) {
        return findScreenHandlerSlot(inventory, screenHandler, inventorySlot).orElseThrow(
                () -> new IllegalStateException("Could not resolve player inventory slot "
                                                + inventorySlot.value()
                                                + " in current screen handler"));
    }
}