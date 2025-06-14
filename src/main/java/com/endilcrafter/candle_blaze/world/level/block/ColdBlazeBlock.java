package com.endilcrafter.candle_blaze.world.level.block;

import com.endilcrafter.candle_blaze.world.entity.CandleBlazeEntity;
import com.endilcrafter.candle_blaze.world.entity.ModEntityTypes;
import com.endilcrafter.candle_blaze.world.level.state.properties.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ColdBlazeBlock extends HorizontalDirectionalBlock {
    public static final int MAX_HEAT_LEVEL = 3;
    public static final IntegerProperty HEAT_LEVEL = ModBlockStateProperties.HEAT_LEVEL;
    public static final int HEAT_TICK_DELAY = 60;
    private static final VoxelShape SHAPE = Block.box(5.5, 0.0, 5.5, 10.5, 5.0, 10.5);

    protected ColdBlazeBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.getStateDefinition()
                        .any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(HEAT_LEVEL, 0)
        );
    }

    public static float getYRot(Direction direction) {
        return switch (direction) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case WEST -> 90.0F;
            case EAST -> -90.0F;
            default -> throw new IllegalStateException("No y-Rot for vertical axis: " + direction);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HEAT_LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public int getHeatLevel(BlockState state) {
        return state.getValue(HEAT_LEVEL);
    }

    private boolean onMagmaBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK);
    }

    private boolean isReadyToSpawn(BlockState state) {
        return this.getHeatLevel(state) == MAX_HEAT_LEVEL;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK)) {
            this.tickBurning(state, level, pos, random);
        }
    }

    private void tickBurning(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.isReadyToSpawn(state)) {
            level.setBlock(pos, state.setValue(HEAT_LEVEL, this.getHeatLevel(state) + 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        } else {
            this.spawnCandleBlaze(level, pos, state);
        }
    }

    private void spawnCandleBlaze(ServerLevel level, BlockPos pos, BlockState state) {
        level.removeBlock(pos, false);
        CandleBlazeEntity blaze = ModEntityTypes.CANDLE_BLAZE.get().create(level);
        if (blaze != null) {
            Vec3 center = Vec3.atBottomCenterOf(pos);
            blaze.setBaby(true);
            float yRot = getYRot(state.getValue(FACING));
            blaze.setYHeadRot(yRot);
            blaze.setPosRaw(center.x(), center.y(), center.z());
            blaze.setYRot(yRot);
            blaze.setXRot(0.0F);
            blaze.setOldPosAndRot();
            blaze.setPos(blaze.position().x(), blaze.position().y(), blaze.position().z());
            level.addFreshEntity(blaze);
            level.playSound(null, blaze, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK) && random.nextInt(6) == 0) {
            if (state.getValue(HEAT_LEVEL) > 2) {
                level.addParticle(ParticleTypes.FLAME, x + (random.nextFloat() * 2.0F - 1.0F) / 3.0F, y + 0.4, z + (random.nextFloat() * 2.0F - 1.0F) / 3.0F,
                        0.0, 0.0, 0.0);
            }
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.01, 0.0);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if ((onMagmaBlock(level, pos) || state.getValue(HEAT_LEVEL) > 0) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.scheduleTick(pos, this, HEAT_TICK_DELAY);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
