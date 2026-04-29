package me.headroomov.picktorch.client.feature.placement.enums;

public enum PicktorchStageResult {
    CONTINUE(false),
    PASS(false),
    HANDLED(true);

    private final boolean shouldCancelOriginalBlockUsing;

    PicktorchStageResult(boolean shouldCancelOriginalBlockUsing) {
        this.shouldCancelOriginalBlockUsing = shouldCancelOriginalBlockUsing;
    }

    public boolean shouldCancelOriginalBlockUsing() {
        return shouldCancelOriginalBlockUsing;
    }
}