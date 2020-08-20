/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/19 8:27
 */
public interface BaseMap<K,V> {
    //存
    public V put(K k, V v) ;
    //取
    public V get(K k);
}