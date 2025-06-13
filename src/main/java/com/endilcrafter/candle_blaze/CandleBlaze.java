package com.endilcrafter.candle_blaze;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CandleBlaze.MODID)
public class CandleBlaze {
    public static final String MODID = "candle_blaze";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CandleBlaze(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
