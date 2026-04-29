package me.headroomov.picktorch.client.feature.placement.contexts;

import lombok.Getter;
import lombok.Setter;
import minecraft.inventory.entities.HotbarIndex;
import minecraft.inventory.entities.ScreenHandlerSlotId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Objects;

@Getter
public final class PicktorchPlacementContext {
    private final MinecraftClient client;
    private final ClientPlayerEntity player;
    private final BlockHitResult hitResult;
    private final ItemStack mainHandItemBefore;

    @Setter
    private HotbarIndex selectedHotbarIndex;

    @Setter
    private ScreenHandlerSlotId torchScreenSlot;

    @Setter
    private boolean isTorchSwappedIntoMainHand;

    public PicktorchPlacementContext(MinecraftClient client,
                                     ClientPlayerEntity player,
                                     BlockHitResult hitResult) {
        this.client = Objects.requireNonNull(client, "client");
        this.player = Objects.requireNonNull(player, "player");
        this.hitResult = Objects.requireNonNull(hitResult, "hitResult");
        this.mainHandItemBefore = player.getStackInHand(Hand.MAIN_HAND).copy();
    }
}