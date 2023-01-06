package me.voidxwalker.worldpreview;

import net.minecraft.block.BlockState;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockStateArrayHolder {



    private static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<BlockStateArrayHolder>> freeHoldersBySize;

    private static ConcurrentLinkedQueue<BlockStateArrayHolder> freeHolders;

    static {
        freeHoldersBySize = new ConcurrentHashMap<>();
        freeHolders = new ConcurrentLinkedQueue<>();
    }

    public BlockState[] array;

    public BlockStateArrayHolder(int size) {
        array = new BlockState[size];
    }

    private static ConcurrentLinkedQueue<BlockStateArrayHolder> getQueue(int size) {
        if (!freeHoldersBySize.contains(size)) {
            freeHoldersBySize.putIfAbsent(size, new ConcurrentLinkedQueue<BlockStateArrayHolder>());
        }
        return freeHoldersBySize.get(size);
    }

    public static BlockStateArrayHolder create(int size) {
        ConcurrentLinkedQueue<BlockStateArrayHolder> freeHolders = getQueue(size);
        BlockStateArrayHolder myHolder = freeHolders.poll();
        if (myHolder == null) {
            myHolder = new BlockStateArrayHolder(size);
        }
        return myHolder;
    }

    public void release() {
//        for (int i = 0; i < array.length; i++) {
//            array[i] = null;
//        }
        getQueue(array.length).add(this);
    }
}
