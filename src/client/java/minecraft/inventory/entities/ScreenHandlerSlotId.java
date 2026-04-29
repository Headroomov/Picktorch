package minecraft.inventory.entities;

public record ScreenHandlerSlotId(int value) {
    public ScreenHandlerSlotId {
        if (value < 0)
            throw new IllegalArgumentException("Invalid screen handler slot id: " + value);
    }
}