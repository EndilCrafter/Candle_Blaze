package com.endilcrafter.candle_blaze.world.item;

import com.endilcrafter.candle_blaze.CandleBlaze;
import com.endilcrafter.candle_blaze.world.entity.ModEntityTypes;
import com.endilcrafter.candle_blaze.world.level.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CandleBlaze.MODID);

    public static final RegistryObject<Item> COLD_BLAZE = ITEMS.register("cold_blaze", () ->
            new BlockItem(ModBlocks.COLD_BLAZE.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CANDLE_BLAZE_SPAWN_EGG = ITEMS.register("candle_blaze_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityTypes.CANDLE_BLAZE, 16167425, 16775294, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
