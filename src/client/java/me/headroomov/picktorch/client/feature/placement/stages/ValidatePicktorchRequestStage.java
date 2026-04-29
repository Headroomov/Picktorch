package me.headroomov.picktorch.client.feature.placement.stages;

import me.headroomov.picktorch.client.feature.placement.abstracts.IPicktorchPlacementStage;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;
import me.headroomov.picktorch.client.feature.placement.functions.PicktorchPlacementItems;

public final class ValidatePicktorchRequestStage implements IPicktorchPlacementStage {
    @Override
    public PicktorchStageResult execute(PicktorchPlacementContext context) {
        if (context.getClient().currentScreen != null)
            return PicktorchStageResult.PASS;

        if (context.getPlayer().isSpectator() == true)
            return PicktorchStageResult.PASS;

        if (context.getPlayer().currentScreenHandler.getCursorStack().isEmpty() == false)
            return PicktorchStageResult.PASS;

        if (PicktorchPlacementItems.isPickaxe(context.getMainHandItemBefore()) == false)
            return PicktorchStageResult.PASS;

        return PicktorchStageResult.CONTINUE;
    }
}