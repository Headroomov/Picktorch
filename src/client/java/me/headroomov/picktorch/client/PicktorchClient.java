package me.headroomov.picktorch.client;

import me.headroomov.picktorch.client.feature.placement.PicktorchMainHandStager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public final class PicktorchClient implements ClientModInitializer {
    public static final String MOD_ID = "Picktorch";

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register(PicktorchClient::OnBlockUsed);
    }

    private static ActionResult OnBlockUsed(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
        if (world.isClient() == false)
            return ActionResult.PASS;

        if (hand != Hand.MAIN_HAND)
            return ActionResult.PASS;

        if (PicktorchMainHandStager.isSyntheticUseInProgress() == true)
            return ActionResult.PASS;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.interactionManager == null)
            return ActionResult.PASS;

        if (client.player != player)
            return ActionResult.PASS;

        boolean shouldCancelOriginalBlockUsing = PicktorchMainHandStager.tryUseTorchFromInventory(client, hitResult);

        return shouldCancelOriginalBlockUsing == true ? ActionResult.FAIL : ActionResult.PASS;
    }
}