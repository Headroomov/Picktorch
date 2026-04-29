package me.headroomov.picktorch.client.feature.placement.stages;

import me.headroomov.picktorch.client.PicktorchClient;
import me.headroomov.picktorch.client.feature.placement.abstracts.IPicktorchPlacementStage;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;
import me.headroomov.picktorch.client.feature.placement.functions.PicktorchPlacementItems;
import minecraft.inventory.entities.HotbarIndex;
import minecraft.inventory.entities.PlayerInventorySlotId;
import minecraft.inventory.entities.ScreenHandlerSlotId;
import minecraft.inventory.functions.PlayerInventorySlots;
import net.minecraft.entity.player.PlayerInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class ResolveTorchSlotStage implements IPicktorchPlacementStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PicktorchClient.MOD_ID);

    @Override
    public PicktorchStageResult execute(PicktorchPlacementContext context) {
        PlayerInventory inventory = context.getPlayer().getInventory();

        HotbarIndex selectedHotbarIndex = PlayerInventorySlots.getSelectedHotbarIndex(inventory);

        PlayerInventorySlotId selectedInventorySlot = PlayerInventorySlots.getSelectedPlayerInventorySlot(inventory);

        Optional<PlayerInventorySlotId> torchInventorySlot = PlayerInventorySlots.findSlotExcept(inventory,
                                                                                                 PicktorchPlacementItems::isSupportedTorch,
                                                                                                 selectedInventorySlot);

        if (torchInventorySlot.isEmpty() == true)
            return PicktorchStageResult.PASS;

        Optional<ScreenHandlerSlotId> torchScreenSlot = PlayerInventorySlots.findScreenHandlerSlot(inventory,
                                                                                                   context.getPlayer().currentScreenHandler,
                                                                                                   torchInventorySlot.get());

        if (torchScreenSlot.isEmpty() == true) {
            LOGGER.warn("Could not resolve torch inventory slot {} in current screen handler.",
                        torchInventorySlot.get().value());

            return PicktorchStageResult.PASS;
        }

        context.setSelectedHotbarIndex(selectedHotbarIndex);
        context.setTorchScreenSlot(torchScreenSlot.get());

        return PicktorchStageResult.CONTINUE;
    }
}