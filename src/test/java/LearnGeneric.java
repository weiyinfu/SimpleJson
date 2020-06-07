import junit.framework.TestCase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LearnGeneric extends TestCase {

public void testClass() {
    //这种方式无法获取泛型参数
    var x = new ArrayList<String>();
    System.out.println(x.getClass());
    System.out.println(x.getClass().arrayType());
    System.out.println(x.getClass().componentType());
    System.out.println(x.getClass().getComponentType());
    System.out.println(x.getClass().isArray());
    System.out.println(x.getClass().getGenericSuperclass());
    Type type = ((ParameterizedType) x.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    System.out.println(x.getClass().toGenericString());
    System.out.println(type);
    var g = (ParameterizedType) x.getClass().getGenericSuperclass();
    System.out.println(g.getOwnerType());
    System.out.println(g.getRawType());
    for (var arg : g.getActualTypeArguments()) {
        System.out.println(arg.getTypeName());
        System.out.println(arg);
    }
}


public void testArray2() {
    var x = new int[]{};
    System.out.println(x.getClass());
    System.out.println(x.getClass().arrayType());
    System.out.println(x.getClass().componentType());
    System.out.println(x.getClass().getComponentType());
    System.out.println(x.getClass().isArray());
    System.out.println(x.getClass().getGenericSuperclass());
    Type type = ((ParameterizedType) x.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    System.out.println(type);
}

public void test3() {
    List<Object> a = new ArrayList<>();
    a.add(1);
    a.add("haha");
    System.out.println(a);
}
}
