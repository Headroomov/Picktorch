package me.headroomov.picktorch.client.feature.placement.stages;

import me.headroomov.picktorch.client.PicktorchClient;
import me.headroomov.picktorch.client.feature.placement.abstracts.IPicktorchPlacementStage;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;
import me.headroomov.picktorch.client.feature.placement.scopes.PicktorchSyntheticBlockUsingScope;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UseStagedTorchStage implements IPicktorchPlacementStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PicktorchClient.MOD_ID);

    @Override
    public PicktorchStageResult execute(PicktorchPlacementContext context) {
        if (context.getClient().interactionManager == null)
            return PicktorchStageResult.HANDLED;

        ActionResult result;

        try (PicktorchSyntheticBlockUsingScope ignored = PicktorchSyntheticBlockUsingScope.enter()) {
            result = context.getClient().interactionManager.interactBlock(context.getPlayer(),
                                                                          Hand.MAIN_HAND,
                                                                          context.getHitResult());
        }

        if (result == ActionResult.FAIL)
            LOGGER.debug("Picktorch staged torch interaction returned FAIL.");

        return PicktorchStageResult.HANDLED;
    }
}