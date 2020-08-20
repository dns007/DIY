/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/19 8:50
 */
public class TestMap {
    public static void main(String[] args) {
        MyHashMap<String, String> testHashMap = new MyHashMap<String, String>();
        testHashMap.put("1号", "1号");
        testHashMap.put("2号", "1号");
        testHashMap.put("3号", "1号");
        testHashMap.put("4号", "1号");
        testHashMap.put("6号", "1号");
        testHashMap.put("7号", "1号");
        testHashMap.put("8号", "1号");
        testHashMap.put("9号", "1号");
        testHashMap.put("10号", "1号");
        testHashMap.put("11号", "1号");
        testHashMap.put("12号", "1号");
        testHashMap.put("13号", "1号");
        testHashMap.put("14号", "1号");
        testHashMap.put("15号", "1号");
        testHashMap.put("16号", "1号");
        testHashMap.put("17号", "1号");
        testHashMap.put("18号", "1号");
        testHashMap.put("19号", "1号");
        testHashMap.put("20号", "1号");
        testHashMap.put("21号", "1号");
        testHashMap.put("22号", "1号");
        testHashMap.put("23号", "1号");
        testHashMap.put("24号", "1号");
        testHashMap.put("25号", "1号");
        testHashMap.put("26号", "1号");
        testHashMap.put("27号", "1号");
        testHashMap.put("28号", "1号");
        testHashMap.put("29号", "1号");
        testHashMap.put("30号", "1号");
        testHashMap.put("31号", "1号");
        System.out.println("扩容后数据....数据的地址发生了变更");
        //注意扩容之后 数据的地址会变更（重新hash table.length变了 ）
        testHashMap.print();
        testHashMap.put("32号", "1号");
        testHashMap.put("33号", "1号");
        testHashMap.put("34号", "1号");
        testHashMap.put("35号", "1号");
        testHashMap.put("36号", "1号");
        testHashMap.put("37号", "1号");
        System.out.println("最后数据....");
        testHashMap.print();


    }
}
