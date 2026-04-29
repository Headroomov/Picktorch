package minecraft.inventory.entities;

public record PlayerInventorySlotId(int value) {
    public static final int HOTBAR_SIZE = 9;
    public static final int MAIN_INVENTORY_SIZE = 36;

    public PlayerInventorySlotId {
        if (value < 0 || value >= MAIN_INVENTORY_SIZE)
            throw new IllegalArgumentException("Invalid player inventory slot id: " + value);
    }

    public boolean isHotbar() {
        return value < HOTBAR_SIZE;
    }

    public boolean isMainInventory() {
        return isHotbar() == false;
    }
}