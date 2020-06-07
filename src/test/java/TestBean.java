import cn.weiyinfu.gs.Gs;
import cn.weiyinfu.simplejson.Json;
import cn.weiyinfu.simplejson.JsonDumpsError;
import cn.weiyinfu.simplejson.JsonParseError;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;
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

User user;

@Override
protected void setUp() throws Exception {
    super.setUp();
    var haha = new User();
    haha.name = "weiyinfu";
    haha.age = 13;
    this.user = haha;
}

public void testDumpBean() throws JsonDumpsError, JsonParseError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    var s = Json.prettyDumps(this.user, 2);
    Map<String, Object> map = Gs.bean2Map(this.user, false);
    System.out.println(s);
    System.out.println(Json.prettyDumps(map, 2));
    var res = Json.parseObject(s, User.class);
    System.out.println(res.getName() + " " + res.getAge());
}

public void testDumpList() throws JsonDumpsError {
    var list = Arrays.asList("one", "two", "three");
    System.out.println(Json.prettyDumps(list, 2));
}

public void testDumpListBean() throws JsonDumpsError {
    var list = Arrays.asList(this.user, this.user, this.user);
    System.out.println(Json.prettyDumps(list, 2));
}

public void testDumpArray() throws JsonDumpsError {
    var x = new String[]{"one", "two", "three"};
    System.out.println(Json.dumps(x));
}

public void testDumpArrayBean() throws JsonDumpsError {
    var x = new User[]{this.user, this.user, this.user};
    System.out.println(Json.dumps(x));
}

public void testDumpMap() throws JsonDumpsError {
    Map<String, Integer> a = new TreeMap<>();
    a.put("one", 1);
    a.put("two", 2);
    System.out.println(Json.prettyDumps(a, 2));
}

public void testDumpSimple() throws JsonDumpsError {
    String s = "haha";
    int x = 3;
    System.out.println(Json.dumps(x));
    System.out.println(Json.dumps(s));
}

public void testLoadSimple() throws JsonDumpsError, JsonParseError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    String s = "haha";
    int x = 3;
    String ss = Json.parseObject(Json.dumps(s), String.class);
    System.out.println(ss);
    int xx = Json.parseObject(Json.dumps(x), Integer.class);
    System.out.println(xx);
}

public void testLoadArray() throws JsonParseError, JsonDumpsError {
    var s = "[{\"name\":\"weiyinfu\",\"age\":13},{\"name\":\"weiyinfu\",\"age\":13},{\"name\":\"weiyinfu\",\"age\":13}]";
    var ans = Json.parseArray(s, User.class);
    System.out.println(Json.dumps(ans));
}

public void testLoadBean() throws JsonParseError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    var s = "{\"name\":\"weiyinfu\",\"age\":13}";
    var user = Json.parseObject(s, User.class);
    System.out.println(user.getName());
}
}
