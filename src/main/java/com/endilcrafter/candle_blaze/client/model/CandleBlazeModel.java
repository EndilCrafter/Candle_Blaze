package com.endilcrafter.candle_blaze.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class CandleBlazeModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart[] upperBodyParts;
    private final ModelPart head;

    public CandleBlazeModel(ModelPart pRoot) {
        this.root = pRoot;
        this.head = pRoot.getChild("head");
        this.upperBodyParts = new ModelPart[8];
        Arrays.setAll(this.upperBodyParts, (sticks) -> {
            return pRoot.getChild(getPartName(sticks));
        });
    }

    private static String getPartName(int pIndex) {
        return "stick" + pIndex;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 11.5F, 0.0F));
        float f = 0.0F;
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F);

        for (int i = 0; i < 4; ++i) {
            float f1 = Mth.cos(f) * 6.0F;
            float f2 = -2.0F + Mth.cos((float) (i * 2) * 0.25F);
            float f3 = Mth.sin(f) * 6.0F;
            partdefinition.addOrReplaceChild(getPartName(i), cubelistbuilder, PartPose.offset(f1, 13.0F, f3));
            ++f;
        }

        f = ((float) Math.PI / 4F);

        for (int j = 4; j < 8; ++j) {
            float f4 = Mth.cos(f) * 4.0F;
            float f6 = 2.0F + Mth.cos((float) (j * 2) * 0.25F);
            float f8 = Mth.sin(f) * 4.0F;
            partdefinition.addOrReplaceChild(getPartName(j), cubelistbuilder, PartPose.offset(f4, 17.0F, f8));
            ++f;
        }

        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f = pAgeInTicks * (float) Math.PI * -0.1F;

        for (int i = 0; i < 4; ++i) {
            this.upperBodyParts[i].y = 13.0F + Mth.cos(((float) (i * 2) + pAgeInTicks) * 0.125F);
            this.upperBodyParts[i].x = Mth.cos(f) * 6.0F;
            this.upperBodyParts[i].z = Mth.sin(f) * 6.0F;
            ++f;
        }

        f = ((float) Math.PI / 4F) + pAgeInTicks * (float) Math.PI * 0.03F;

        for (int j = 4; j < 8; ++j) {
            this.upperBodyParts[j].y = 17.0F + Mth.cos(((float) (j * 2) + pAgeInTicks) * 0.125F);
            this.upperBodyParts[j].x = Mth.cos(f) * 4.0F;
            this.upperBodyParts[j].z = Mth.sin(f) * 4.0F;
            ++f;
        }

        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
    }
}
