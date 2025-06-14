package com.endilcrafter.candle_blaze.event;

import com.endilcrafter.candle_blaze.CandleBlaze;
import com.endilcrafter.candle_blaze.client.model.CandleBlazeModel;
import com.endilcrafter.candle_blaze.client.model.geom.ModModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CandleBlaze.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CANDLE_BLAZE, CandleBlazeModel::createBodyLayer);
    }
}