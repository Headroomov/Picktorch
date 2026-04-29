package me.headroomov.picktorch.client.feature.placement.abstracts;

import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;

public interface IPicktorchPlacementStage {
    PicktorchStageResult execute(PicktorchPlacementContext context);
}