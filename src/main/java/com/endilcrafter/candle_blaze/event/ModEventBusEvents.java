package com.endilcrafter.candle_blaze.event;

import com.endilcrafter.candle_blaze.CandleBlaze;
import com.endilcrafter.candle_blaze.world.entity.CandleBlazeEntity;
import com.endilcrafter.candle_blaze.world.entity.ModEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CandleBlaze.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.CANDLE_BLAZE.get(), CandleBlazeEntity.createAttributes().build());
    }
}
