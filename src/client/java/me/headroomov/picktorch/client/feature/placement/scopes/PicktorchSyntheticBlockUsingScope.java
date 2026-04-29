package me.headroomov.picktorch.client.feature.placement.scopes;

public final class PicktorchSyntheticBlockUsingScope implements AutoCloseable {
    private static int activeDepth;

    private boolean isClosed;

    private PicktorchSyntheticBlockUsingScope() {
        activeDepth++;
    }

    public static PicktorchSyntheticBlockUsingScope enter() {
        return new PicktorchSyntheticBlockUsingScope();
    }

    public static boolean isActive() {
        return activeDepth > 0;
    }

    @Override
    public void close() {
        if (isClosed == true)
            return;

        isClosed = true;

        if (activeDepth > 0)
            activeDepth--;
    }
}