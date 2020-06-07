import cn.weiyinfu.gs.Gs;
import cn.weiyinfu.simplejson.Json;
import cn.weiyinfu.simplejson.JsonDumpsError;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class TestBean extends TestCase {
public static class User {
    String name;
    int age;

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

public void testBean() throws JsonDumpsError {
    var haha = new User();
    haha.name = "weiyinfu";
    haha.age = 13;
    var s = Json.prettyDumps(haha, 2);

    Map<String, Object> map = Gs.bean2Map(haha, false);
    System.out.println(s);
    System.out.println(Json.prettyDumps(map, 2));
}

public void testList() throws JsonDumpsError {
    System.out.println(Json.prettyDumps(Arrays.asList("one", "two", "three"), 2));
}

public void testMap() throws JsonDumpsError {
    Map<String, Integer> a = new TreeMap<>();
    a.put("one", 1);
    a.put("two", 2);
    System.out.println(Json.prettyDumps(a, 2));
}
}
