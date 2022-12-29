package me.voidxwalker.worldpreview.mixin.client.render.chunk;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class)
public class RebuildTaskMixin {

    private static final String targetString = "net/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask.region:Lnet/minecraft/client/render/chunk/ChunkRendererRegion;";

//    @Shadow
//    @Final
//    private static Logger LOGGER;

    public RebuildTaskMixin() {

        
    }

    private void myCodeInner(ChunkRendererRegion region) {

        throw new IllegalStateException();
    }

    @Redirect(method = "run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "FIELD", target = targetString, opcode = Opcodes.PUTFIELD))
    private void injected(ChunkBuilder.BuiltChunk.RebuildTask instance, ChunkRendererRegion value) {

        myCodeInner(value);
    }

    @Redirect(method = "render(FFFLnet/minecraft/client/render/chunk/ChunkBuilder$ChunkData;Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/Set;", at = @At(value = "FIELD", target = targetString, opcode = Opcodes.PUTFIELD))
    private void injected2(ChunkBuilder.BuiltChunk.RebuildTask instance, ChunkRendererRegion value) {

        myCodeInner(value);
    }

    @Redirect(method = "cancel()V", at = @At(value = "FIELD", target = targetString, opcode = Opcodes.PUTFIELD))
    private void injected3(ChunkBuilder.BuiltChunk.RebuildTask instance, ChunkRendererRegion value) {

        myCodeInner(value);
    }
}
