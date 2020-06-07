import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFile {
public static void main(String[] args) throws IOException {
    Files.list(Paths.get("simplejson")).forEach(x -> {
        System.out.println(x.toAbsolutePath());
    });
}
}
