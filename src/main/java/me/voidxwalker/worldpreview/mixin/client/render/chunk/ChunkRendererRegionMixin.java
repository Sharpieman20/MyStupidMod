package me.voidxwalker.worldpreview.mixin.client.render.chunk;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.voidxwalker.worldpreview.BlockStateArrayHolder;
import me.voidxwalker.worldpreview.FluidStateArrayHolder;
import me.voidxwalker.worldpreview.Releaseable;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRendererRegionMixin implements Releaseable {

    @Mutable @Shadow public int chunkXOffset;
    @Shadow @Mutable @Final public int chunkZOffset;
    @Shadow @Mutable @Final public BlockPos offset;
    @Shadow @Mutable public int xSize;
    @Mutable @Shadow public int ySize;
    @Mutable @Shadow public int zSize;
    @Mutable @Shadow public WorldChunk[][] chunks;
    @Mutable @Shadow public BlockState[] blockStates;
    @Mutable @Shadow public FluidState[] fluidStates;
    @Mutable @Shadow public World world;
    @Shadow public abstract int getIndex(BlockPos pos);

    private int tempXSize = 0;
    private BlockStateArrayHolder blockStateHolder;
    private FluidStateArrayHolder fluidStateHolder;

    private ChunkRendererRegion getChunkRendererRegion() {

        return ((ChunkRendererRegion) ((Object)this));
    }

//    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;blockStates:[Lnet/minecraft/block/BlockState;", opcode = Opcodes.PUTFIELD, ordinal = 0))
//    private void injected2(ChunkRendererRegion obj, BlockState[] val) {
//
//        this.blockStates = new BlockState[16*this.ySize*this.ySize];
//    }

    @WrapWithCondition(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;blockStates:[Lnet/minecraft/block/BlockState;", ordinal = 0))
    private boolean mymode_myskip(ChunkRendererRegion obj, BlockState[] val) {

        return false;
    }

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;fluidStates:[Lnet/minecraft/fluid/FluidState;", shift=At.Shift.BEFORE, ordinal = 0))
    public void mymod_myCode(World world, int chunkX, int chunkZ, WorldChunk[][] chunks, BlockPos startPos, BlockPos endPos, CallbackInfo ci) {

//        this.blockStates = new BlockState[this.xSize*this.ySize*this.zSize];
        blockStateHolder = BlockStateArrayHolder.create(this.xSize*this.ySize*this.zSize);
        this.blockStates = blockStateHolder.array;
    }

    @WrapWithCondition(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;fluidStates:[Lnet/minecraft/fluid/FluidState;", ordinal = 0))
    private boolean mymode_myskip2(ChunkRendererRegion obj, FluidState[] val) {

        return false;
    }

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;fluidStates:[Lnet/minecraft/fluid/FluidState;", shift=At.Shift.AFTER, by=2, ordinal = 0))
    public void mymod_myCode2(World world, int chunkX, int chunkZ, WorldChunk[][] chunks, BlockPos startPos, BlockPos endPos, CallbackInfo ci) {

//        this.blockStates = new BlockState[this.xSize*this.ySize*this.zSize];
        fluidStateHolder = FluidStateArrayHolder.create(this.xSize*this.ySize*this.zSize);
        this.fluidStates = fluidStateHolder.array;
    }

    public void myNewmethod_release() {

        if (blockStateHolder != null) {

            blockStateHolder.release();
            blockStateHolder = null;
        }

        if (fluidStateHolder != null) {

            fluidStateHolder.release();
            fluidStateHolder = null;
        }
    }
}
