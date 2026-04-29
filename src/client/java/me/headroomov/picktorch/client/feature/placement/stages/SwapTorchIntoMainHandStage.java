package me.headroomov.picktorch.client.feature.placement.stages;

import me.headroomov.picktorch.client.PicktorchClient;
import me.headroomov.picktorch.client.feature.placement.abstracts.IPicktorchPlacementStage;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;
import me.headroomov.picktorch.client.feature.placement.functions.PicktorchPlacementItems;
import minecraft.inventory.entities.HotbarIndex;
import minecraft.inventory.entities.ScreenHandlerSlotId;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SwapTorchIntoMainHandStage implements IPicktorchPlacementStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PicktorchClient.MOD_ID);

    @Override
    public PicktorchStageResult execute(PicktorchPlacementContext context) {
        boolean isSwapped = swapSlotWithSelectedHotbarSlot(context,
                                                           context.getTorchScreenSlot(),
                                                           context.getSelectedHotbarIndex());

        if (isSwapped == false)
            return PicktorchStageResult.HANDLED;

        context.setTorchSwappedIntoMainHand(true);

        if (context.getPlayer().currentScreenHandler.getCursorStack().isEmpty() == false) {
            LOGGER.warn("Aborting Picktorch use: cursor stack became non-empty after swap-in.");
            return PicktorchStageResult.HANDLED;
        }

        if (PicktorchPlacementItems.isSupportedTorch(context.getPlayer()
                                                            .getStackInHand(Hand.MAIN_HAND)) == false) {
            LOGGER.warn("Aborting Picktorch use: main hand does not contain a supported torch after swap-in.");
            return PicktorchStageResult.HANDLED;
        }

        return PicktorchStageResult.CONTINUE;
    }

    static boolean swapSlotWithSelectedHotbarSlot(PicktorchPlacementContext context,
                                                  ScreenHandlerSlotId screenSlot,
                                                  HotbarIndex selectedHotbarIndex) {
        if (context.getClient().interactionManager == null)
            return false;

        if (screenSlot == null) {
            LOGGER.warn("Cannot swap Picktorch slot: screen slot is null.");
            return false;
        }

        if (selectedHotbarIndex == null) {
            LOGGER.warn("Cannot swap Picktorch slot: selected hotbar index is null.");
            return false;
        }

        if (screenSlot.value() >= context.getPlayer()
                                         .currentScreenHandler
                                         .slots.size()) {
            LOGGER.warn("Invalid screen handler slot for Picktorch swap: {}", screenSlot.value());
            return false;
        }

        context.getClient().interactionManager.clickSlot(context.getPlayer().currentScreenHandler.syncId,
                                                         screenSlot.value(),
                                                         selectedHotbarIndex.value(),
                                                         SlotActionType.SWAP,
                                                         context.getPlayer());

        return true;
    }
}