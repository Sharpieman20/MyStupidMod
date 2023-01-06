package me.voidxwalker.worldpreview;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FluidStateArrayHolder {

    private static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<FluidStateArrayHolder>> freeHoldersBySize;

    private static ConcurrentLinkedQueue<FluidStateArrayHolder> freeHolders;

    static {
        freeHoldersBySize = new ConcurrentHashMap<>();
        freeHolders = new ConcurrentLinkedQueue<>();
    }

    public FluidState[] array;

    public FluidStateArrayHolder(int size) {
        array = new FluidState[size];
    }

    private static ConcurrentLinkedQueue<FluidStateArrayHolder> getQueue(int size) {
        if (!freeHoldersBySize.contains(size)) {
            freeHoldersBySize.putIfAbsent(size, new ConcurrentLinkedQueue<FluidStateArrayHolder>());
        }
        return freeHoldersBySize.get(size);
    }

    public static FluidStateArrayHolder create(int size) {
        ConcurrentLinkedQueue<FluidStateArrayHolder> freeHolders = getQueue(size);
        FluidStateArrayHolder myHolder = freeHolders.poll();
        if (myHolder == null) {
            myHolder = new FluidStateArrayHolder(size);
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
