package dev.nwalk.phasingeater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.nwalk.phasingeater.item.ItemRegistry;
import dev.nwalk.phasingeater.item.LightningWand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class PhasingEater implements ModInitializer {
  public static final String MOD_ID = "phasingeater";
  public static final Logger LOGGER = LoggerFactory.getLogger("phasingeater");

  private final MinecraftClient client = MinecraftClient.getInstance();

  @Override
  public void onInitialize() {
    ItemRegistry.registerItems();
    UseItemCallback.EVENT.register(((player, world, hand) -> {
      ItemStack itemStack = player.getStackInHand(hand);
      if (player instanceof ClientPlayerEntity) return TypedActionResult.pass(itemStack);
      if (client.cameraEntity == null) return TypedActionResult.pass(itemStack);
      if (itemStack.getItem() instanceof LightningWand) {
        HitResult hitResult = client.cameraEntity.raycast(256, client.getTickDelta(), false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
          Vec3d position = hitResult.getPos();
          LOGGER.info(String.format("Hit block at (%f, %f, %f)", position.x, position.y, position.z));
          LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
          lightningBolt.setPosition(position);
          world.spawnEntity(lightningBolt);
        }

        LOGGER.info("No target");
      }

      return TypedActionResult.pass(itemStack);
    }));
  }
}
