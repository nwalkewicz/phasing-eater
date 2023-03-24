package dev.nwalk.phasingeater;

import dev.nwalk.phasingeater.client.gui.hud.WailaHud;
import dev.nwalk.phasingeater.item.LightningWand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class PhasingEaterClient implements ClientModInitializer {
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new WailaHud());
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack itemStack = player.getMainHandStack();

            if (player.getMainHandStack().getItem() instanceof LightningWand) {
                if (client.cameraEntity != null && client.world != null && client.targetedEntity != null) {
                    HitResult hitResult = client.cameraEntity.raycast(
                            -1, client.getTickDelta(), false);

                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        // Summon lightning bolt at raycast target
                        Vec3d position = hitResult.getPos();
                        LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
                        lightningBolt.setPosition(position);
                        client.world.spawnEntity(lightningBolt);
                    }

                    if (hitResult.getType() == HitResult.Type.ENTITY) {
                        // Summon lightning bolt at center of targeted entity
                        Entity targetedEntity = client.targetedEntity;
                        Vec3d position = targetedEntity.getPos();
                        LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
                        lightningBolt.setPosition(position);
                        client.world.spawnEntity(lightningBolt);
                    }
                }
            }

            return TypedActionResult.pass(itemStack);
        }));
    }
}
