//package me.voidxwalker.worldpreview;
//
//import java.lang.reflect.Array;
//
//public class PooledObjectArrayHolder<T> {
//
//    private static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<PooledObjectHolder<T>>> freeHoldersBySize;
//
//    private ConcurrentLinkedQueue<PooledObjectHolder<T>> freeHolders;
//
//    private T[] objects;
//
//    public PooledObjectArrayHolder(Class<T> clazz, int size) {
//        objects = (T[]) Array.newInstance(clazz, size);
//    }
//
//    private static ConcurrentLinkedQueue<PooledObjectHolder<T>> getQueue(int size) {
//        if (!freeHoldersBySize.contains(size)) {
//            freeHoldersBySize.putIfAbsent(size, new ConcurrentLinkedQueue<PooledObjectHolder<T>>());
//        }
//        return freeHoldersBySize.get(size);
//    }
//
//    public static BlockStateHolder<T> create(int size) {
//        ConcurrentLinkedQueue<PooledObjectHolder<T>> freeHolders = getQueue(size);
//        PooledObjectHolder<T> myHolder = freeHolders.poll();
//        if (myHolder == null) {
//            myHolder = new PooledObjectHolder<T>(size);
//        }
//        return myHolder;
//    }
//
//    public void set(int index, T value) {
//        objects[index] = value;
//    }
//
//    private void release() {
//        for (int i = 0; i < objects.length; i++) {
//            objects[i] = null;
//        }
//        getQueue(objects.length).add(this);
//    }
//}
