package dev.nwalk.phasingeater.mixin;

import java.util.Timer;
import java.util.TimerTask;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  public LivingEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Inject(method = "consumeItem", at = @At("HEAD"))
  private void onConsumeItem(CallbackInfo info) {
    LivingEntity player = ((LivingEntity)(Object)this);

    // Set player's GameMode to spectator
    if (player instanceof ServerPlayerEntity) ((ServerPlayerEntity) player).changeGameMode(GameMode.SPECTATOR);

    // Start 10-second timer
    int delay = 1000;
    int period = 1000;
    Timer timer = new Timer();
    final int[] secondsRemaining = {10};
    final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    updateOverlay(minecraftClient, secondsRemaining[0]);

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        // Runs after each second
        secondsRemaining[0]--;
        if (secondsRemaining[0] == 0) {
          // Set player's GameMode back to survival
          if (player instanceof ServerPlayerEntity) ((ServerPlayerEntity) player).changeGameMode(GameMode.SURVIVAL);
          timer.cancel();

          // Remove overlay
          minecraftClient.inGameHud.setOverlayMessage(Text.empty(), false);
        } else {
          // Show/update overlay
          updateOverlay(minecraftClient, secondsRemaining[0]);
        }
      }
    }, delay, period);
  }

  void updateOverlay(MinecraftClient minecraftClient, int secondsRemaining) {
    if (secondsRemaining != 0) {
      String template = "Spectator ends in ";
      template += String.valueOf(secondsRemaining);
      template += "s";

      Text message = Text.of(template);
      minecraftClient.inGameHud.setOverlayMessage(message, false);
    } else minecraftClient.inGameHud.setOverlayMessage(Text.empty(), false);
  }
}
