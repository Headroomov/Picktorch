package me.headroomov.picktorch.client.feature.placement;

import me.headroomov.picktorch.client.PicktorchClient;
import me.headroomov.picktorch.client.feature.placement.abstracts.IPicktorchPlacementStage;
import me.headroomov.picktorch.client.feature.placement.contexts.PicktorchPlacementContext;
import me.headroomov.picktorch.client.feature.placement.enums.PicktorchStageResult;
import me.headroomov.picktorch.client.feature.placement.scopes.PicktorchSyntheticBlockUsingScope;
import me.headroomov.picktorch.client.feature.placement.stages.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PicktorchMainHandStager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PicktorchClient.MOD_ID);

    private static final IPicktorchPlacementStage VALIDATE_REQUEST_STAGE = new ValidatePicktorchRequestStage();
    private static final IPicktorchPlacementStage RESOLVE_TORCH_SLOT_STAGE = new ResolveTorchSlotStage();
    private static final IPicktorchPlacementStage SWAP_TORCH_INTO_MAIN_HAND_STAGE = new SwapTorchIntoMainHandStage();
    private static final IPicktorchPlacementStage USE_STAGED_TORCH_STAGE = new UseStagedTorchStage();

    private static final RestoreMainHandStage RESTORE_MAIN_HAND_STAGE = new RestoreMainHandStage();

    private PicktorchMainHandStager() {
    }

    public static boolean isSyntheticUseInProgress() {
        return PicktorchSyntheticBlockUsingScope.isActive();
    }

    public static boolean tryUseTorchFromInventory(MinecraftClient client, BlockHitResult hitResult) {
        ClientPlayerEntity player = client.player;

        if (player == null || client.interactionManager == null)
            return false;

        PicktorchPlacementContext context = new PicktorchPlacementContext(
                client,
                player,
                hitResult
        );

        try {
            PicktorchStageResult validationResult = VALIDATE_REQUEST_STAGE.execute(context);

            if (shouldFinishOperation(validationResult) == true)
                return validationResult.shouldCancelOriginalBlockUsing();

            PicktorchStageResult resolvingResult = RESOLVE_TORCH_SLOT_STAGE.execute(context);

            if (shouldFinishOperation(resolvingResult) == true)
                return resolvingResult.shouldCancelOriginalBlockUsing();

            PicktorchStageResult swappingResult = SWAP_TORCH_INTO_MAIN_HAND_STAGE.execute(context);

            if (shouldFinishOperation(swappingResult) == true)
                return swappingResult.shouldCancelOriginalBlockUsing();

            PicktorchStageResult usingResult = USE_STAGED_TORCH_STAGE.execute(context);

            if (shouldFinishOperation(usingResult) == true)
                return usingResult.shouldCancelOriginalBlockUsing();

            return true;
        } catch (Exception exception) {
            LOGGER.error("Picktorch placement operation failed.", exception);

            return true;
        } finally {
            tryRestoreMainHand(context);
        }
    }

    private static boolean shouldFinishOperation(PicktorchStageResult result) {
        return result != PicktorchStageResult.CONTINUE;
    }

    private static void tryRestoreMainHand(PicktorchPlacementContext context) {
        if (context.isTorchSwappedIntoMainHand() == false)
            return;

        try {
            RESTORE_MAIN_HAND_STAGE.execute(context);
        } catch (Exception exception) {
            LOGGER.error("Picktorch restore operation failed.", exception);
        }
    }
}