package cn.weiyinfu.simplejson;

import java.util.List;
import java.util.Map;

/**
 * JsonObject应该尽量简单，不要把逻辑放在JsonObject里面，JsonObject只是一个简单的结构体
 */
public class JsonObject {
final JsonType type;
final Object obj;

public JsonObject(Object obj, JsonType type) {
    this.obj = obj;
    this.type = type;
}

public JsonType type() {
    return this.type;
}

public String asString() {
    return (String) obj;
}

public Integer asInteger() {
    return (int) obj;
}

public Float asFloat() {
    return (float) obj;
}

public Boolean asBoolean() {
    return (boolean) obj;
}

public List<JsonObject> asArray() {
    return (List<JsonObject>) obj;
}

public Map<String, JsonObject> asMap() {
    return (Map<String, JsonObject>) obj;
}

/**
 * 为了API的简洁，添加getArray和getMap两个函数
 */
public List<JsonObject> getArray(int ind) {
    return this.asArray().get(ind).asArray();
}

public Map<String, JsonObject> getMap(int ind) {
    return this.asArray().get(ind).asMap();
}

public List<JsonObject> getArray(String ind) {
    return this.asMap().get(ind).asArray();
}

public Map<String, JsonObject> getMap(String ind) {
    return this.asMap().get(ind).asMap();
}
}
