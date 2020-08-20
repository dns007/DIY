/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/19 8:30
 */
public class Entry<K, V> implements BaseEntry<K, V> {
    K k;
    V v;
    Entry<K, V> next;

    public Entry(K k, V v, Entry<K, V> next) {
        this.k = k;
        this.v = v;
        this.next = next;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }
}
