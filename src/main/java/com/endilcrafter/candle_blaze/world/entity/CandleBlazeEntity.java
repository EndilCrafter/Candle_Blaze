package com.endilcrafter.candle_blaze.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class CandleBlazeEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> ROD_COUNT = SynchedEntityData.defineId(CandleBlazeEntity.class, EntityDataSerializers.INT);
    private float allowedHeightOffset = 0.5F;
    private int nextHeightOffsetChangeTick;
    public int rodTime = this.random.nextInt(600) + 1200;

    public CandleBlazeEntity(EntityType<? extends CandleBlazeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.0, Ingredient.of(ItemTags.COALS), false));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new MoveToBlockGoal(this, 1.0, 16) {

            @Override
            protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
                BlockState blockstate = pLevel.getBlockState(pPos);
                return (blockstate.is(BlockTags.CANDLES) && !blockstate.getValue(CandleBlock.LIT)) || (blockstate.is(BlockTags.CANDLE_CAKES) && !blockstate.getValue(CandleCakeBlock.LIT));
            }

            @Override
            public void tick() {
                super.tick();
                if (this.isReachedTarget()) {
                    Level world = this.mob.level();
                    BlockPos pos = this.blockPos;
                    BlockState blockstate = world.getBlockState(pos);

                    if (blockstate.is(BlockTags.CANDLES) && !blockstate.getValue(CandleBlock.LIT)) {
                        world.playLocalSound(this.mob.getX() + 0.5D, this.mob.getY() + 0.5D, this.mob.getZ() + 0.5D, SoundEvents.FLINTANDSTEEL_USE, this.mob.getSoundSource(), 1.0F, 1.0F, false);
                        world.setBlock(pos, blockstate.setValue(CandleBlock.LIT, true), 3);
                    } else if (blockstate.is(BlockTags.CANDLE_CAKES) && !blockstate.getValue(CandleCakeBlock.LIT)) {
                        world.playLocalSound(this.mob.getX() + 0.5D, this.mob.getY() + 0.5D, this.mob.getZ() + 0.5D, SoundEvents.FLINTANDSTEEL_USE, this.mob.getSoundSource(), 1.0F, 1.0F, false);
                        world.setBlock(pos, blockstate.setValue(CandleCakeBlock.LIT, true), 3);
                    }
                }
            }

        });
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("RodTime")) {
            this.rodTime = pCompound.getInt("RodTime");
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("RodTime", this.rodTime);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        if (--this.rodTime > 0) {
            return InteractionResult.PASS;
        } else if (!itemStack.is(ItemTags.COALS)) {
            return InteractionResult.PASS;
        } else {
            float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
            this.playSound(SoundEvents.LAVA_POP, 1.0F, f1);
            this.spawnAtLocation(Items.BLAZE_ROD);
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.rodTime = this.random.nextInt(600) + 1200;
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);

        }

    }

    @Override
    public void tick() {
        super.tick();
        --this.rodTime;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.BLAZE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        return 1.25F;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void aiStep() {
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.level().isClientSide) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level().playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }
                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
        }

        super.aiStep();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    protected void customServerAiStep() {
        --this.nextHeightOffsetChangeTick;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = (float) this.random.triangle(0.5D, 6.891D);
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double) this.allowedHeightOffset && this.canAttack(livingentity)) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, ((double) 0.3F - vec3.y) * (double) 0.3F, 0.0D));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }
}
