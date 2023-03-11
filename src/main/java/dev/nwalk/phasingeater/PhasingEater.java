package dev.nwalk.phasingeater;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhasingEater implements ModInitializer {
	public static final String MOD_ID = "phasingeater";
	public static final Logger LOGGER = LoggerFactory.getLogger("phasingeater");

	@Override
	public void onInitialize() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack itemStack = player.getStackInHand(hand);

			if (!(player instanceof ServerPlayerEntity)) return TypedActionResult.pass(itemStack);

			if (itemStack.getItem().isFood() && player.getItemUseTimeLeft() == 0) {
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
				GameMode spectatorMode = GameMode.SPECTATOR;
				serverPlayer.changeGameMode(spectatorMode);
			}
			
			return TypedActionResult.pass(itemStack);
		});
	}
}