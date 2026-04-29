package minecraft.inventory.entities;

public record HotbarIndex(int value) {
    public static final int SIZE = 9;

    public HotbarIndex {
        if (value < 0 || value >= SIZE)
            throw new IllegalArgumentException("Invalid hotbar index: " + value);
    }
}