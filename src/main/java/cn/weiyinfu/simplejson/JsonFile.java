package cn.weiyinfu.simplejson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static cn.weiyinfu.simplejson.Json.*;

public class JsonFile {

public static Object load(Path p) throws IOException, JsonParseError {
    return loads(Files.readString(p));
}

public static JsonObject parseObject(Path filepath) throws IOException, JsonParseError {
    return (JsonObject) load(filepath);
}


public static void dump(Object obj, Path p) throws IOException {
    Files.writeString(p, dumps(obj), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
}

public static void prettyDump(Object obj, Path p, int indent) throws IOException, JsonDumpsError {
    Files.writeString(p, prettyDumps(obj, indent), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
}

public static JsonArray parseArray(Path filepath) throws IOException, JsonParseError {
    return (JsonArray) load(filepath);
}

}
