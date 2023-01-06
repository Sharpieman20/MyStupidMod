package me.voidxwalker.worldpreview.mixin.client.render.chunk;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRendererRegionMixin {

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

        this.blockStates = new BlockState[this.xSize*this.ySize*this.zSize];
    }
}
