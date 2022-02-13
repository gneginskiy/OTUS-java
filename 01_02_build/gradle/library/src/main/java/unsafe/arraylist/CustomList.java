package unsafe.arraylist;

import java.util.Collection;

public interface CustomList<T>  {
    public void add(T a);
    public boolean contains(T a);
    public T get(int index);
    public void remove(int index);
}