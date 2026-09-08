package net.terraimmerse.client.manager;

import java.io.InputStream;

public class ResourceStreamManager {
    public static InputStream getStreamResource(String path) {
        InputStream stream=ResourceStreamManager.class.getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Falied to load file");
        }
        return stream;
    }
}
