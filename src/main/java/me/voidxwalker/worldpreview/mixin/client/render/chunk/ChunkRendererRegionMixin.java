package me.voidxwalker.worldpreview.mixin.client.render.chunk;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin {

    private ChunkRendererRegion getChunkRendererRegion() {

        return ((ChunkRendererRegion) ((Object)this));
    }

    @Inject(method = "<init>", at = @At(value = "HEAD"))
    public void mymod_myCode(World world, int chunkX, int chunkZ, WorldChunk[][] chunks, BlockPos startPos, BlockPos endPos, CallbackInfo ci) {
        ChunkRendererRegion chunkRendererRegion = getChunkRendererRegion();

        chunkRendererRegion.world = world;
        chunkRendererRegion.chunkXOffset = chunkX;
        chunkRendererRegion.chunkZOffset = chunkZ;
        chunkRendererRegion.chunks = chunks;
        chunkRendererRegion.offset = startPos;
        chunkRendererRegion.xSize = endPos.getX() - startPos.getX() + 1;
        chunkRendererRegion.ySize = endPos.getY() - startPos.getY() + 1;
        chunkRendererRegion.zSize = endPos.getZ() - startPos.getZ() + 1;
        chunkRendererRegion.blockStates = new BlockState[chunkRendererRegion.xSize * chunkRendererRegion.ySize * chunkRendererRegion.zSize];
        chunkRendererRegion.fluidStates = new FluidState[chunkRendererRegion.xSize * chunkRendererRegion.ySize * chunkRendererRegion.zSize];
//        this.blockStates = PooledObjectHolder<BlockState>.create(this.xSize * this.ySize * this.zSize);
//        this.fluidStates = PooledObjectHolder<FluidState>.create(this.xSize * this.ySize * this.zSize);
        for (BlockPos lv : BlockPos.iterate(startPos, endPos)) {
            int k = ChunkSectionPos.getSectionCoord(lv.getX()) - chunkX;
            int l = ChunkSectionPos.getSectionCoord(lv.getZ()) - chunkZ;
            WorldChunk lv2 = chunks[k][l];
            int m = chunkRendererRegion.getIndex(lv);
            chunkRendererRegion.blockStates.set(m, lv2.getBlockState(lv));
            chunkRendererRegion.fluidStates.set(m, lv2.getFluidState(lv));
        }
    }
}
