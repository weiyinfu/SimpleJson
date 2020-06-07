import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TestGeneric {
private List<String> list;
private Map<String, Object> map;
private String string;

public static void test(String name) throws NoSuchFieldException, SecurityException {
    Type t = TestGeneric.class.getDeclaredField(name).getGenericType();
    System.out.println(t.getTypeName());
    if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
        for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
            System.out.print(t1 + ",");
        }
        System.out.println();
    }
}

public static void main(String args[]) throws Exception {
    System.out.println(">>>>>>>>>>>testList>>>>>>>>>>>");
    test("list");
    System.out.println("<<<<<<<<<<<testList<<<<<<<<<<<\n");
    System.out.println(">>>>>>>>>>>testMap>>>>>>>>>>>");
    test("map");
    System.out.println("<<<<<<<<<<<testMap<<<<<<<<<<<\n");
    test("string");
}
}
