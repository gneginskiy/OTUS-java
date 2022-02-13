package unsafe.arraylist;

import net.bramp.unsafe.UnsafeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnsafeCustomList<T> implements CustomList<T> {
    public static final int SLEEP = 1000;
    public static final int SAMPLE_SIZE = 50_000_000;
    private long[] storage = new long[2];
    private int size;

    public static void main(String[] args) {
        measure(() -> {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                list.add("objasdasdfasdfsadfsafsdafj2" + i);
            }
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                list.get(i);
            }
        },"safe");
        tryToGc();

        measure(() -> {
            UnsafeCustomList<Object> list = new UnsafeCustomList<>();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                list.add("objasdasdfasdfsadfsafsdafja" + i);
            }
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                list.get(i);
            }
        },"Unsafe");
        tryToGc();
    }

    private static void tryToGc() {
        System.gc();
        sleep(SLEEP);
        System.out.println("freeMemory = " + Runtime.getRuntime().freeMemory());
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void measure(Runnable runnable, String name) {
        System.out.println("Measuring "+name);
        long start = System.nanoTime();
        runnable.run();
        long nanos = (System.nanoTime() - start)  ;
        System.out.printf("%s took %f seconds%n", name, nanos/1000000000.);
        System.out.printf("approximately %d nanos per operation %n",  nanos/SAMPLE_SIZE);
    }

    @Override
    public void add(T object) {
        ensureSize();
        storage[size++] = UnsafeHelper.toAddress(object);
    }

    private void ensureSize() {
        if (size < storage.length - 1) return;
        long[] newStorage = new long[(int) (storage.length * 1.7)];
        System.arraycopy(storage, 0, newStorage, 0, storage.length);
        storage = newStorage;
    }

    @Override
    public boolean contains(T a) {
        for (int i = 0; i < storage.length; i++) {
            long address = storage[i];
            if (address == 0) {
                continue;
            }
            if (Objects.equals(a, UnsafeHelper.fromAddress(address))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int index) {
        return index > size || storage[index] == 0 ? null : (T) UnsafeHelper.fromAddress(storage[index]);
    }

    @Override
    public void remove(int index) {
        UnsafeHelper.getUnsafe().freeMemory(storage[index]);
        storage[index] = 0;
        size--;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(i == 0 ? "" : ",").append(get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
