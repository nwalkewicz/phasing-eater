package dev.nwalk.phasingeater.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// @Mixin(MinecraftServer.class)
// public class ExampleMixin {
// 	@Inject(at = @At("HEAD"), method = "loadWorld")
// 	private void init(CallbackInfo info) {
// 		// This code is injected into the start of MinecraftServer.loadWorld()V
// 	}
// }

@Mixin(LivingEntity.class)
public class CustomLivingEntity extends Entity {
  @Inject(at = @At("HEAD"), method = "consumeItem")
  private void injected(CallbackInfo info) {
    ((TargetClass) )
  }

  @Override
  protected void initDataTracker() {}

  @Override
  public void readCustomDataFromNbt(NbtCompound var1) {}

  @Override
  public void writeCustomDataToNbt(NbtCompound var1) {}
}