import cn.weiyinfu.simplejson.Json;
import cn.weiyinfu.simplejson.JsonObject;
import cn.weiyinfu.simplejson.JsonParseError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestJson {
public static void main(String[] args) throws IOException, JsonParseError {
    Files.list(Paths.get("json")).forEach(p -> {
        try {
            System.out.println("* 正在测试文件" + p.toAbsolutePath());
            JsonObject obj = Json.load(p);
            System.out.println(Json.dumps(obj));
            var folder = Paths.get("target/out");
            if (!Files.exists(folder)) {
                Files.createDirectory(folder);
            }
            Path filepath = folder.resolve(p.getFileName());
//            Json.dump(obj,filepath );
            Json.prettyDump(obj, filepath, 2);
        } catch (IOException | JsonParseError e) {
            e.printStackTrace();
        }
    });
}
}
