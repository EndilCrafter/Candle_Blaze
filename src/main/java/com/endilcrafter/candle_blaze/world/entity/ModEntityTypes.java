package com.endilcrafter.candle_blaze.world.entity;

import com.endilcrafter.candle_blaze.CandleBlaze;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CandleBlaze.MODID);

    public static final RegistryObject<EntityType<CandleBlazeEntity>> CANDLE_BLAZE =
            ENTITY_TYPES.register("candle_blaze", () -> EntityType.Builder.of(CandleBlazeEntity::new, MobCategory.CREATURE)
                    .fireImmune().sized(0.4F, 0.9F).clientTrackingRange(8).build("candle_blaze"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
