package dev.nwalk.phasingeater.item;

import dev.nwalk.phasingeater.PhasingEater;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {
    public static final Item LIGHTNING_WAND = registerItem("lightning_wand", new LightningWand(
            new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PhasingEater.MOD_ID, name), item);
    }

    public static void registerItems() {
        PhasingEater.LOGGER.debug("Registering mod items for " + PhasingEater.MOD_ID);
    }
}
