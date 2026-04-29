package me.headroomov.picktorch.client.feature.placement.functions;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

public final class PicktorchPlacementItems {
    private PicktorchPlacementItems() {
    }

    public static boolean isPickaxe(ItemStack stack) {
        return stack.isEmpty() == false &&
                stack.isIn(ItemTags.PICKAXES) == true;
    }

    public static boolean isSupportedTorch(ItemStack stack) {
        if (stack.isEmpty() == true)
            return false;

        return stack.isOf(Items.TORCH) == true ||
                stack.isOf(Items.SOUL_TORCH) == true;
    }
}