import cn.weiyinfu.gs.Gs;
import cn.weiyinfu.simplejson.Json;
import cn.weiyinfu.simplejson.JsonDumpsError;
import cn.weiyinfu.simplejson.JsonParseError;
import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class TestBean extends TestCase {
public static class User {
    String name;
    int age;

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

public void testBean() throws JsonDumpsError, JsonParseError {
    var haha = new User();
    haha.name = "weiyinfu";
    haha.age = 13;
    var s = Json.prettyDumps(haha, 2);

    Map<String, Object> map = Gs.bean2Map(haha, false);
    System.out.println(s);
    System.out.println(Json.prettyDumps(map, 2));

//    var res = Json.parseBean(s, User.class);
//    System.out.println(res.getName() + " " + res.getAge());
    var json = Json.loads(s);
    var x = Json.fromJsonObject(json, User.class);
    System.out.println(x.getName() + " " + x.getAge());
}

public void testList() throws JsonDumpsError {
    System.out.println(Json.prettyDumps(Arrays.asList("one", "two", "three"), 2));
}

public void testList2() throws JsonDumpsError {
    var x = new String[]{"one", "two", "three"};
    System.out.println(x.getClass().isArray());
    var obj = Json.toJsonObject(x);
    System.out.println(obj);
    System.out.println(obj.asArray());
    System.out.println(Json.prettyDumps(x, 2));
    System.out.println(Json.dumps(x));
}

public void testMap() throws JsonDumpsError {
    Map<String, Integer> a = new TreeMap<>();
    a.put("one", 1);
    a.put("two", 2);
    System.out.println(Json.prettyDumps(a, 2));
}

public void testClass() {
    var x = new ArrayList<Integer>();
    System.out.println(x.getClass());
    System.out.println(x.getClass().arrayType());
    System.out.println(x.getClass().componentType());
    System.out.println(x.getClass().getComponentType());
    System.out.println(x.getClass().isArray());
    System.out.println(x.getClass().getGenericSuperclass());
    Type type = ((ParameterizedType) x.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    System.out.println(type);
}
}
