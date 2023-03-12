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
	public void onInitialize() {}
}