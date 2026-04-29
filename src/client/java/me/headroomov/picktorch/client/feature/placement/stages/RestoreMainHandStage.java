package me.headroomov.picktorch.client.feature.placement.stages;

import me.headroomov.picktorch.client.PicktorchClient;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RestoreMainHandStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PicktorchClient.MOD_ID);

    public void execute(PicktorchPlacementContext context) {
        if (context.isTorchSwappedIntoMainHand() == false)
            return;

        if (context.getPlayer().currentScreenHandler.getCursorStack().isEmpty() == false) {
            LOGGER.error("Picktorch could not restore main hand safely because cursor stack is not empty.");
            return;
        }

        boolean isRestored = SwapTorchIntoMainHandStage.swapSlotWithSelectedHotbarSlot(context,
                                                                                       context.getTorchScreenSlot(),
                                                                                       context.getSelectedHotbarIndex());

        if (isRestored == false) {
            LOGGER.error("Picktorch failed to restore main hand slot.");
            return;
        }

        ItemStack mainHandItemAfterRestore = context.getPlayer()
                                                    .getStackInHand(Hand.MAIN_HAND);

        if (ItemStack.areItemsEqual(context.getMainHandItemBefore(), mainHandItemAfterRestore) == false) {
            LOGGER.warn("Picktorch restore check: main hand item differs after restore. Before={}, After={}",
                        context.getMainHandItemBefore(),
                        mainHandItemAfterRestore
            );
        }
    }
}