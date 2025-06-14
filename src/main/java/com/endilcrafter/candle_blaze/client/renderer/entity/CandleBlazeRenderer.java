package com.endilcrafter.candle_blaze.client.renderer.entity;

import com.endilcrafter.candle_blaze.CandleBlaze;
import com.endilcrafter.candle_blaze.client.model.CandleBlazeModel;
import com.endilcrafter.candle_blaze.client.model.geom.ModModelLayers;
import com.endilcrafter.candle_blaze.world.entity.CandleBlazeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CandleBlazeRenderer extends MobRenderer<CandleBlazeEntity, CandleBlazeModel<CandleBlazeEntity>> {
    private static final ResourceLocation CANDLE_BLAZE_LOCATION = ResourceLocation.fromNamespaceAndPath(CandleBlaze.MODID, "textures/entity/candle_blaze.png");

    public CandleBlazeRenderer(EntityRendererProvider.Context p_173933_) {
        super(p_173933_, new CandleBlazeModel<>(p_173933_.bakeLayer(ModModelLayers.CANDLE_BLAZE)), 0.3F);
    }

    protected int getBlockLightLevel(CandleBlazeEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(CandleBlazeEntity pEntity) {
        return CANDLE_BLAZE_LOCATION;
    }
}
