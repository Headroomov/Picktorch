package me.headroomov.picktorch.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PicktorchMainHandStager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Picktorch");

//     PlayerInventory:
//     0..8   = hotbar
//     9..35  = main inventory
//
//     PlayerScreenHandler slot ids:
//     9..35  = main inventory
//     36..44 = hotbar

    private static final int MAIN_INVENTORY_SIZE = 36;
    private static final int HOTBAR_SIZE = 9;
    private static final int HOTBAR_SCREEN_SLOT_OFFSET = 36;

    private static boolean syntheticUseInProgress = false;

    private PicktorchMainHandStager() {
    }

    public static boolean isSyntheticUseInProgress() {
        return syntheticUseInProgress;
    }

    public static boolean tryUseTorchFromInventory(MinecraftClient client, BlockHitResult hitResult) {
        ClientPlayerEntity player = client.player;

        if (player == null || client.interactionManager == null)
            return false;

        if (client.currentScreen != null)
            return false;

        if (player.isSpectator())
            return false;

        if (!player.currentScreenHandler.getCursorStack().isEmpty())
            return false;

        ItemStack mainHandItemBefore = player.getStackInHand(Hand.MAIN_HAND);

        if (!isPickaxe(mainHandItemBefore))
            return false;

        PlayerInventory inventory = player.getInventory();

        int selectedHotbarIndex = inventory.selectedSlot;
        int selectedScreenSlotId = HOTBAR_SCREEN_SLOT_OFFSET + selectedHotbarIndex;

        int torchInventorySlotId = findTorchInMainInventory(inventory, selectedHotbarIndex);

        if (torchInventorySlotId < 0)
            return false;

        int torchScreenSlotId = toPlayerScreenHandlerSlot(torchInventorySlotId);

        if (torchScreenSlotId < 0)
            return false;

        if (torchScreenSlotId == selectedScreenSlotId)
            return interactWithMainHand(client, player, hitResult);

        boolean isSwappedIntoMainHand = false;

        try {
            isSwappedIntoMainHand = swapSlotWithSelectedHotbarSlot(client,
                                                                   player,
                                                                   torchScreenSlotId,
                                                                   selectedHotbarIndex
            );

            if (!isSwappedIntoMainHand)
                return true;

            if (!player.currentScreenHandler.getCursorStack().isEmpty()) {
                LOGGER.warn("Aborting Picktorch use: cursor stack became non-empty after swap-in.");
                return true;
            }

            if (!isSupportedTorch(player.getStackInHand(Hand.MAIN_HAND))) {
                LOGGER.warn("Aborting Picktorch use: main hand does not contain a supported torch after swap-in.");
                return true;
            }

            interactWithMainHand(client, player, hitResult);

            return true;
        } finally {
            if (isSwappedIntoMainHand) {
                if (player.currentScreenHandler.getCursorStack().isEmpty()) {
                    swapSlotWithSelectedHotbarSlot(client,
                                                   player,
                                                   torchScreenSlotId,
                                                   selectedHotbarIndex
                    );

                    ItemStack mainHandItemAfterRestore = player.getStackInHand(Hand.MAIN_HAND);

                    if (!ItemStack.areItemsEqual(mainHandItemBefore, mainHandItemAfterRestore)) {
                        LOGGER.warn("Picktorch restore check: main hand item differs after restore. Before={}, After={}",
                                    mainHandItemBefore,
                                    mainHandItemAfterRestore
                        );
                    }
                } else {
                    LOGGER.error("Picktorch could not restore main hand safely because cursor stack is not empty.");
                }
            }
        }
    }

    private static boolean interactWithMainHand(MinecraftClient client,
                                                ClientPlayerEntity player,
                                                BlockHitResult hitResult) {
        if (client.interactionManager == null)
            return false;

        syntheticUseInProgress = true;

        try {
            ActionResult result = client.interactionManager.interactBlock(player,
                                                                          Hand.MAIN_HAND,
                                                                          hitResult
            );

            return result != ActionResult.FAIL;
        } finally {
            syntheticUseInProgress = false;
        }
    }

    private static boolean swapSlotWithSelectedHotbarSlot(MinecraftClient client,
                                                          ClientPlayerEntity player,
                                                          int screenSlot,
                                                          int selectedHotbarIndex) {
        if (client.interactionManager == null)
            return false;

        if (selectedHotbarIndex < 0 || selectedHotbarIndex >= HOTBAR_SIZE) {
            LOGGER.warn("Invalid selected hotbar index for Picktorch swap: {}", selectedHotbarIndex);
            return false;
        }

        if (screenSlot < 0 || screenSlot >= player.currentScreenHandler.slots.size()) {
            LOGGER.warn("Invalid screen slot for Picktorch main-hand swap: {}", screenSlot);
            return false;
        }

        client.interactionManager.clickSlot(player.currentScreenHandler.syncId,
                                            screenSlot,
                                            selectedHotbarIndex,
                                            SlotActionType.SWAP,
                                            player
        );

        return true;
    }

    private static int findTorchInMainInventory(PlayerInventory inventory, int selectedHotbarIndex) {
        for (int slot = 0; slot < MAIN_INVENTORY_SIZE; slot++) {
            if (slot == selectedHotbarIndex)
                continue;

            ItemStack stack = inventory.getStack(slot);

            if (isSupportedTorch(stack))
                return slot;
        }

        return -1;
    }

    private static int toPlayerScreenHandlerSlot(int inventorySlot) {
        if (inventorySlot >= 0 && inventorySlot < HOTBAR_SIZE)
            return HOTBAR_SCREEN_SLOT_OFFSET + inventorySlot;

        if (inventorySlot >= HOTBAR_SIZE && inventorySlot < MAIN_INVENTORY_SIZE)
            return inventorySlot;

        return -1;
    }

    private static boolean isPickaxe(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ItemTags.PICKAXES);
    }

    private static boolean isSupportedTorch(ItemStack stack) {
        if (stack.isEmpty())
            return false;

        return stack.isOf(Items.TORCH) || stack.isOf(Items.SOUL_TORCH);
    }
}