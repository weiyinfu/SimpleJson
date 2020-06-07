import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
String startsWithNumber(String content, int i) {
    Pattern p = Pattern.compile("^\\d+(\\.\\d+)?");
    Matcher matcher = p.matcher(content);
    boolean isNumber = matcher.find(i);
    if (!isNumber) return null;
    return matcher.group();
}

public static void main(String[] args) {
    String res = new TestRegex().startsWithNumber("34.234234", 0);
    System.out.println(res);
    res = new TestRegex().startsWithNumber("34", 0);
    System.out.println(res);
}
}
