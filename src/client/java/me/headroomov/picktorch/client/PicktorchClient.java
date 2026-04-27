package me.headroomov.picktorch.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public final class PicktorchClient implements ClientModInitializer {
    public static final String MOD_ID = "Picktorch";

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient())
                return ActionResult.PASS;

            if (hand != Hand.MAIN_HAND)
                return ActionResult.PASS;

            if (PicktorchMainHandStager.isSyntheticUseInProgress())
                return ActionResult.PASS;

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null || client.interactionManager == null)
                return ActionResult.PASS;

            if (client.player != player)
                return ActionResult.PASS;

            boolean isHandled = PicktorchMainHandStager.tryUseTorchFromInventory(client, hitResult);

            return isHandled ? ActionResult.FAIL : ActionResult.PASS;
        });
    }
}