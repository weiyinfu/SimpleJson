package cn.weiyinfu.simplejson;

import cn.weiyinfu.gs.Gs;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 一开始没有建maven项目，建成了普通项目，所以就不能使用JSON库了，那就自己写一个JSON库吧
 * <p>
 * 这是一个小练习
 */
public class Json {
private final Object obj;
private int I = -1;//解析到了第几个token

Json(String content) throws JsonParseError {
    List<String> tokens = tokenize(content);
    Map<Integer, Integer> matches = match(tokens);
    this.obj = this.parse(tokens, matches, 0);
    if (this.I != tokens.size() - 1) {
        throw new JsonParseError("有无法解析的剩余token ");
    }
}

private int nextDoubleQuote(String content, int begin) {
    //寻找字符串的结尾，也就是寻找下一个双引号
    for (int i = begin; i < content.length(); i++) {
        //如果当前字符是转义字符，那么跳过下一个字符
        if (content.charAt(i) == '\\'
                && i + 1 < content.length()
                && "\"\\".indexOf(content.charAt(i + 1)) != -1) {
            i++;
            continue;
        }
        if (content.charAt(i) == '"') {
            return i;
        }
    }
    return -1;
}

private String startsWithNumber(String content, int i) {
    boolean hasPoint = false;
    int beg = i;
    for (; i < content.length(); i++) {
        char ch = content.charAt(i);
        if (!('9' >= ch && ch >= '0')) {
            if (!hasPoint && ch == '.') {
                hasPoint = true;
            } else {
                break;
            }
        }
    }
    if (i == beg) return null;
    return content.substring(beg, i);
}

private boolean startsWith(String s, int beg, String small) {
    for (int i = 0; i < small.length(); i++) {
        if (beg + i >= s.length()) return false;
        if (s.charAt(beg + i) != small.charAt(i)) {
            return false;
        }
    }
    return true;
}

private void debugTokens(List<String> tokens) {
    for (String i : tokens) {
        System.out.print(i + "|||");
    }
    System.out.println("======");
}

private List<String> tokenize(String content) throws JsonParseError {
    //使用一个hashmap构建括号的索引，O(n)复杂度完成
    List<String> tokens = new ArrayList<>();
    for (int i = 0; i < content.length(); i++) {
        char ch = content.charAt(i);
        if (ch == ' ') continue;//跳过空格
        else if ("{[]}:,".indexOf(ch) != -1) {
            tokens.add(String.valueOf(ch));
        } else if (ch == '"') {
            //一气到尾解析右引号，发现字符串
            int end = nextDoubleQuote(content, i + 1);
            if (end == -1) {
                throw new JsonParseError("缺少匹配的引号\"");
            }
            tokens.add(content.substring(i, end + 1));
            i = end;
        } else {
            //对于其它情况，直接等待空格即可
            String s = startsWithNumber(content, i);
            if (s != null) {
                tokens.add(s);
                i += s.length() - 1;
            } else {
                //boolean
                if (startsWith(content, i, "true")) {
                    tokens.add("true");
                    i += "true".length() - 1;
                } else if (startsWith(content, i, "false")) {
                    tokens.add("false");
                    i += "false".length() - 1;
                }
            }
        }
    }
    return tokens;
}

private Map<Integer, Integer> match(List<String> tokens) throws JsonParseError {
    //匹配括号
    Map<Integer, Integer> pairs = new HashMap<>();
    Stack<Integer> sta = new Stack<>();
    String quotePairs = "{}[]";
    for (int i = 0; i < tokens.size(); i++) {
        String t = tokens.get(i);
        if (t.length() == 1) {
            int ind = quotePairs.indexOf(t);
            if (ind == -1) continue;
            if ((ind & 1) == 0) {
                //如果左半括号
                sta.push(i);
            } else {
                //遇到右半括号弹栈
                if (sta.isEmpty()) throw new JsonParseError("括号不匹配");
                int last = sta.pop();
                String lastToken = tokens.get(last);
                if (lastToken.length() != 1) {
                    throw new JsonParseError("last bracket error");
                }
                int lastIndex = quotePairs.indexOf(lastToken);
                if (lastIndex == -1) {
                    throw new JsonParseError("cannot find last bracket " + last);
                }
                if (lastIndex + 1 != ind) {
                    //如果不是左括号
                    throw new JsonParseError("lack left quote for " + t);
                }
                pairs.put(last, i);
            }
        }
    }
    return pairs;
}

private Object parse(List<String> tokens, Map<Integer, Integer> matches, int beg) throws JsonParseError {
    /**
     * 此函数根据beg吃掉一个JsonObject，更改this.I变量，表示下次从何处开始解析
     * */
    String beginString = tokens.get(beg);
    if (beginString.equals("{")) {
        //是个对象
        int end = matches.get(beg);
        JsonObject ma = new JsonObject();
        for (int i = beg + 1; i < end; i = this.I + 1) {
            if (i != beg + 1) {
                //如果不是beg+1，那么肯定需要逗号与上一个元素隔开
                if (!tokens.get(i).equals(",")) {
                    throw new JsonParseError("对象里面需要逗号隔开，但是现在却是" + tokens.get(i));
                }
                i++;
            }
            String key = tokens.get(i);
            if (!(key.startsWith("\"") && key.endsWith("\""))) {
                throw new JsonParseError("key should startswith and endswith \"");
            }
            key = key.substring(1, key.length() - 1);//去掉引号
            if (!tokens.get(i + 1).equals(":")) {
                throw new JsonParseError("need :");
            }
            Object value = parse(tokens, matches, i + 2);
            ma.put(key, value);
        }
        this.I = end;
        return ma;
    } else if (beginString.equals("[")) {
        //是个数组
        int end = matches.get(beg);
        JsonArray a = new JsonArray();
        for (int i = beg + 1; i < end; i = this.I + 1) {
            if (i != beg + 1) {
                //如果不是数组里面的第一个元素，那么第一个token应该是逗号
                if (!tokens.get(i).equals(",")) {
                    throw new JsonParseError("数组里面元素之间需要用逗号隔开");
                }
                i++;
            }
            Object item = parse(tokens, matches, i);
            a.add(item);
        }
        this.I = end;
        return a;
    } else if (beginString.startsWith("\"")) {
        //基本数据类型，string，number，boolean
        this.I = beg;
        if (!(beginString.startsWith("\"") && beginString.endsWith("\""))) {
            throw new JsonParseError("字符串应该开头和结尾都是双引号");
        }
        String s = beginString.substring(1, beginString.length() - 1);
        return s;
    } else if (beginString.equals("true") || beginString.equals("false")) {
        this.I = beg;
        return Boolean.valueOf(beginString);
    } else {
        //数字
        this.I = beg;
        if (beginString.contains(".")) {
            //浮点数
            return Float.parseFloat(beginString);
        } else {
            return Integer.valueOf(beginString);
        }
    }
}

/**
 * ========================
 * static区，提供给外部使用
 */
public static Object loads(String content) throws JsonParseError {
    return new Json(content).obj;
}

public static JsonArray parseArray(String content) throws JsonParseError {
    return (JsonArray) loads(content);
}

public static JsonObject parseObject(String content) throws JsonParseError {
    return (JsonObject) loads(content);
}

public static String dumps(Object obj) {
    if (obj.getClass().isArray()) {
        obj = Arrays.asList((Object[]) obj);
    }
    if (obj instanceof List) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Object i : (List<Object>) obj) {
            builder.append(dumps(i)).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    } else if (obj instanceof Map) {
        Map<String, Object> ma = (Map<String, Object>) obj;
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        ma.forEach((k, v) -> builder.append(String.format("\"%s\":%s,", k, dumps(v))));
        builder.deleteCharAt(builder.length() - 1);//删除掉最后一个多余的逗号
        builder.append("}");
        return builder.toString();
    } else if (obj instanceof String) {
        return String.format("\"%s\"", obj);
    } else if (obj instanceof Integer
            || obj instanceof Boolean
            || obj instanceof Float) {
        //基本类型，直接toString
        return obj.toString();
    } else {
        Map<String, Object> ma = Gs.bean2Map(obj, false);
        return dumps(ma);
    }
}

private static String prettyDumps(Object obj, int indent, int count) throws JsonDumpsError {
    /*
     * indent表示每层缩进个数
     * count表示当前缩进层数
     * 实现方式：如果比较短，则直接在一行里面显示，如果比较长，则分多行显示
     * */
    String indentString = " ".repeat(indent * count);
    String parentIndent = " ".repeat(indent * Math.max(0, count - 1));
    if (obj.getClass().isArray()) {
        obj = Arrays.asList((Object[]) obj);
    }
    if (obj instanceof List) {
        String s = dumps(obj);
        if (s.length() < 120) {
            return s;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        boolean first = true;
        for (Object i : (List<Object>) obj) {
            if (first) first = false;
            else builder.append(",\n");
            builder.append(indentString).append(prettyDumps(i, indent, count + 1));
        }
        builder.append('\n').append(parentIndent).append("]");
        return builder.toString();
    } else if (obj instanceof Map) {
        Map<String, Object> ma = (Map<String, Object>) obj;
        String s = dumps(obj);
        if (s.length() < 120) {
            return s;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        boolean isFirst = true;
        for (String k : ma.keySet()) {
            Object v = ma.get(k);
            if (isFirst) isFirst = false;
            else builder.append(",\n");
            //如果是字典的value是基本数据类型，则不用进行多余缩进，否则需要进行缩进
            builder.append(indentString).append(String.format("\"%s\":%s", k, prettyDumps(v, indent, count + 1)));
        }
        builder.append("\n").append(parentIndent).append("}");
        return builder.toString();
    } else if (obj instanceof String) {
        return String.format("\"%s\"", obj);
    } else if (obj instanceof Integer
            || obj instanceof Boolean
            || obj instanceof Float) {
        //基本类型，直接toString
        return obj.toString();
    } else {
        Map<String, Object> ma = Gs.bean2Map(obj, false);
        return prettyDumps(ma, indent);
    }
}

public static <T> String prettyDumps(T obj, int indent) throws JsonDumpsError {
    return prettyDumps(obj, indent, 0);
}

public static <T> T parseObject(String json, Class<T> type) throws JsonParseError, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    if (type == String.class || type == Integer.class || type == Float.class || type == Boolean.class) {
        return (T) loads(json);
    }
    JsonObject obj = parseObject(json);
    return Gs.map2Bean(obj, type, false);
}

public static <T> List<T> parseArray(String json, Class<T> type) throws JsonParseError {
    //解析数组,type表示数组中的元素
    JsonArray a = parseArray(json);
    var ans = new ArrayList<T>();
    for (var i : a) {
        ans.add(Gs.map2Bean((JsonObject) i, type, false));
    }
    return ans;
}
}
