package dev.nwalk.phasingeater.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class LightningWand extends Item {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public LightningWand(Settings settings) {
        super(settings);
    }

//    @Override
//    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
//        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
//    }
}
