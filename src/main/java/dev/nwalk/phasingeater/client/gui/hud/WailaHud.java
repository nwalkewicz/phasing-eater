package dev.nwalk.phasingeater.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.nwalk.phasingeater.BreakProgressTracker;
import dev.nwalk.phasingeater.PhasingEater;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WailaHud implements HudRenderCallback {
    public static final Logger LOGGER = LoggerFactory.getLogger("phasingeater");

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final int WIDTH = 200;
    private final int HEIGHT = 50;
    private final int MARGIN = 5;
    private final int PADDING = 5;

    public static final Identifier FRAME_TEXTURE = new Identifier(
            PhasingEater.MOD_ID, "textures/gui/wailaframe.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        // Guards
        if (client == null || client.player == null || client.world == null || client.crosshairTarget == null) return;
        if (client.crosshairTarget.getType() == HitResult.Type.MISS) return;

        this.drawFrame(matrixStack);
        this.drawText(matrixStack);
    }

    private void drawFrame(MatrixStack matrixStack) {
        int clientWidth = client.getWindow().getScaledWidth();

        int offsetX = (clientWidth / 2) - (WIDTH / 2);
        int offsetY = MARGIN;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, FRAME_TEXTURE);

        // Top-left
        DrawableHelper.drawTexture(matrixStack,
                offsetX, offsetY,
                8, 8,
                0, 0,
                8, 8,
                24, 24
        );

        // Top-middle
        DrawableHelper.drawTexture(matrixStack,
                offsetX + 8, offsetY,
                WIDTH - 16, 8,
                8, 0,
                8, 8,
                24, 24
        );

        // Top-right
        DrawableHelper.drawTexture(matrixStack,
                offsetX + WIDTH - 8, offsetY,
                8, 8,
                16, 0,
                8, 8,
                24, 24
        );

        // Middle-left
        DrawableHelper.drawTexture(matrixStack,
                offsetX, offsetY + 8,
                8, HEIGHT - 16,
                0, 8,
                8, 8,
                24, 24
        );

        // Middle-middle
        DrawableHelper.drawTexture(matrixStack,
                offsetX + 8, offsetY + 8,
                WIDTH - 16, HEIGHT - 16,
                8, 8,
                8, 8,
                24, 24
        );

        // Middle-right
        DrawableHelper.drawTexture(matrixStack,
                offsetX + WIDTH - 8, offsetY + 8,
                8, HEIGHT - 16,
                16, 8,
                8, 8,
                24, 24
        );

        // Bottom-left
        DrawableHelper.drawTexture(matrixStack,
                offsetX, offsetY + HEIGHT - 8,
                8, 8,
                0, 16,
                8, 8,
                24, 24
        );

        // Bottom-middle
        DrawableHelper.drawTexture(matrixStack,
                offsetX + 8, offsetY + HEIGHT - 8,
                WIDTH - 16, 8,
                8, 16,
                8, 8,
                24, 24
        );

        // Bottom-right
        DrawableHelper.drawTexture(matrixStack,
                offsetX + WIDTH - 8, offsetY + HEIGHT - 8,
                8, 8,
                16, 16,
                8, 8,
                24, 24
        );
    }

    private void drawText(MatrixStack matrices) {
        assert client.player != null;
        assert client.world != null;
        assert client.crosshairTarget != null;
        int clientWidth = client.getWindow().getScaledWidth();
        int offsetX = (clientWidth / 2) - (WIDTH / 2);
        int offsetY = MARGIN;

        Entity cameraEntity = client.getCameraEntity();
        assert cameraEntity != null;

        HitResult hitResult = cameraEntity.raycast(20f, 0f, false);

        // Blocks
        if (client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            if (hitResult.getType() != HitResult.Type.BLOCK) return;
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockState = client.world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            // Icon
            int ICON_SIZE = 20;
            client.getItemRenderer().renderGuiItemIcon(
                    new ItemStack(block.asItem()),
                    offsetX + PADDING, offsetY + PADDING
            );

            // Title
            DrawableHelper.drawTextWithShadow(matrices,
                    client.textRenderer,
                    block.getName(),
                    offsetX + PADDING + ICON_SIZE, offsetY + PADDING,
                    0xFFFFFF
            );

            // ID
            DrawableHelper.drawTextWithShadow(matrices,
                    client.textRenderer,
                    Text.of(String.valueOf(Registries.BLOCK.getId(block))),
                    offsetX + PADDING + ICON_SIZE, offsetY + PADDING + 11,
                    0xFFFFFF
            );

            // Tool info
            boolean toolIsRequired = blockState.isToolRequired();

            Item activeItem = client.player.getActiveItem().getItem();
            MutableText toolInfoText = Text.translatable("text.phasingeater.correcttool", "tool");
            if (toolIsRequired) {
                toolInfoText.append(Text.of(" "));
                toolInfoText.append(Text.translatable("text.phasingeater.toolrequired"));
            }

            DrawableHelper.drawTextWithShadow(matrices,
                    client.textRenderer,
                    toolInfoText,
                    offsetX + PADDING, offsetY + PADDING + 22,
                    0xFFFFFF
            );

            // Break progress
            BreakProgressTracker breakProgressTracker = (BreakProgressTracker) client.interactionManager;
            if (breakProgressTracker != null) {
                float breakProgress = breakProgressTracker.getBreakProgressPercent(client.getTickDelta());
                if (breakProgress > 0) {
                    DrawableHelper.drawTextWithShadow(matrices,
                            client.textRenderer,
                            Text.translatable("text.phasingeater.breakprogress", (int) Math.floor(breakProgress)),
                            offsetX + PADDING, offsetY + PADDING + 33,
                            0xFFFFFF
                    );
                }
            }
        }

        // Entities
        if (client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            if (client.targetedEntity == null) return;

            // Title
            MutableText entityTitleText = (MutableText) client.targetedEntity.getDisplayName();
            Text entityName = client.targetedEntity.getType().getName();
            if (!entityTitleText.getContent().equals(entityName.getContent())) {
                entityTitleText.append(" (");
                entityTitleText.append(entityName);
                entityTitleText.append(")");
            }
            DrawableHelper.drawTextWithShadow(matrices,
                    client.textRenderer,
                    entityTitleText,
                    offsetX + PADDING, offsetY + PADDING,
                    0xFFFFFF
            );

            // Health
            if (client.targetedEntity instanceof LivingEntity) {
                float targetCurrentHealth = ((LivingEntity) client.targetedEntity).getHealth();
                float targetMaxHealth = ((LivingEntity) client.targetedEntity).getMaxHealth();

                RenderSystem.setShaderTexture(0, TexturedButtonWidget.GUI_ICONS_TEXTURE);
                for (int i = 0; i < Math.ceil(targetMaxHealth / 2); i++) {
                    // Draw heart background
                    DrawableHelper.drawTexture(matrices,
                            offsetX + PADDING + (i * 9), offsetY + (PADDING * 2) + 8,
                            9, 9,
                            16, 0,
                            9, 9,
                            256, 256
                    );

                    if (targetCurrentHealth > (i + 1) * 2 - 1) {
                        // Draw full heart
                        DrawableHelper.drawTexture(matrices,
                                offsetX + PADDING + (i * 9), offsetY + (PADDING * 2) + 8,
                                9, 9,
                                52, 0,
                                9, 9,
                                256, 256
                        );
                    } else if (targetCurrentHealth > (i + 1) * 2 - 2) {
                        // Draw half heart
                        DrawableHelper.drawTexture(matrices,
                                offsetX + PADDING + (i * 9), offsetY + (PADDING * 2) + 8,
                                9, 9,
                                61, 0,
                                9, 9,
                                256, 256
                        );
                    }
                }
            }
        }
    }
}
