package me.voidxwalker.worldpreview.mixin.client.render.chunk;

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

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;xSize:I", opcode = Opcodes.PUTFIELD))
    private void injected(ChunkRendererRegion obj, int val) {

        tempXSize = val;
        this.xSize = 0;
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;blockStates:[Lnet/minecraft/block/BlockState;", opcode = Opcodes.PUTFIELD))
    private void injected(ChunkRendererRegion obj, BlockState[] val) {

        this.blockStates = new BlockState[this.tempXSize*this.ySize*this.ySize];
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Object;<init>()V", shift = At.Shift.AFTER), cancellable = true)
    public void mymod_myCode(World world, int chunkX, int chunkZ, WorldChunk[][] chunks, BlockPos startPos, BlockPos endPos, CallbackInfo ci) {
        ChunkRendererRegion chunkRendererRegion = getChunkRendererRegion();

        this.world = world;
        this.chunkXOffset = chunkX;
        this.chunkZOffset = chunkZ;
        this.chunks = chunks;
        this.offset = startPos;
        this.xSize = endPos.getX() - startPos.getX() + 1;
        this.ySize = endPos.getY() - startPos.getY() + 1;
        this.zSize = endPos.getZ() - startPos.getZ() + 1;
        this.blockStates = new BlockState[this.xSize * this.ySize * this.zSize];
        this.fluidStates = new FluidState[this.xSize * this.ySize * this.zSize];
//        this.blockStates = PooledObjectHolder<BlockState>.create(this.xSize * this.ySize * this.zSize);
//        this.fluidStates = PooledObjectHolder<FluidState>.create(this.xSize * this.ySize * this.zSize);
        for (BlockPos lv : BlockPos.iterate(startPos, endPos)) {
            int k = ChunkSectionPos.getSectionCoord(lv.getX()) - chunkX;
            int l = ChunkSectionPos.getSectionCoord(lv.getZ()) - chunkZ;
            WorldChunk lv2 = chunks[k][l];
            int m = this.getIndex(lv);
            this.blockStates[m] = lv2.getBlockState(lv);
            this.fluidStates[m] = lv2.getFluidState(lv);
//            chunkRendererRegion.blockStates.set(m, lv2.getBlockState(lv));
//            chunkRendererRegion.fluidStates.set(m, lv2.getFluidState(lv));
        }
        ci.cancel();
    }
}
