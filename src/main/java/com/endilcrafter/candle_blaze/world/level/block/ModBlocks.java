package com.endilcrafter.candle_blaze.world.level.block;

import com.endilcrafter.candle_blaze.CandleBlaze;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CandleBlaze.MODID);

    public static final RegistryObject<ColdBlazeBlock> COLD_BLAZE = BLOCKS.register("cold_blaze", () ->
            new ColdBlazeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instabreak().sound(SoundType.STONE).noOcclusion().randomTicks()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
