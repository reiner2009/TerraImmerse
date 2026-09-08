package net.terraimmerse.client.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.terraimmerse.client.render.world.ClientLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AtlasManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtlasManager.class);
    private static JsonObject atlasDataJson;
    private static JsonObject modelDataJson;
    public static HashMap<String, String[]> modelData;
    public static HashMap<String, int[]> atlasData;
    public AtlasManager(){
        LOGGER.info("Creating atlas data");
        atlasData=new HashMap<>();
        try {
            InputStream atlasDataFile = ResourceStreamManager.getStreamResource("/assets/atlas.json");
            atlasDataJson = JsonParser.parseReader(new InputStreamReader(atlasDataFile)).getAsJsonObject();
            for (String key : atlasDataJson.keySet()) {
                JsonArray array = atlasDataJson.getAsJsonArray(key);
                int t1 = array.get(0).getAsInt();
                int t2 = array.get(1).getAsInt();
                atlasData.put(key, new int[]{t1, t2});
            }
            atlasDataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelData=new HashMap<>();
        try {
            InputStream modelDataFile = ClientLevel.class.getResourceAsStream("/assets/models.json");
            if (modelDataFile == null) {
                throw new RuntimeException("Couldn't find textures.json");
            }
            modelDataJson = JsonParser.parseReader(new InputStreamReader(modelDataFile)).getAsJsonObject();
            int i = 0;
            for (String key : modelDataJson.keySet()) {
                i+=1;
                JsonArray array = modelDataJson.getAsJsonArray(key);
                String t1 = array.get(0).getAsString();
                String t2 = array.get(1).getAsString();
                String t3 = array.get(2).getAsString();
                String t4 = array.get(3).getAsString();
                String t5 = array.get(4).getAsString();
                String t6 = array.get(5).getAsString();
                modelData.put(key, new String[]{t1, t2, t3, t4, t5, t6});
                LOGGER.info("model "+i+" of "+modelDataJson.size()+" successfully created");
            }
            modelDataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HashMap<String, String[]> getTextureData(){
        return modelData;
    }
    public HashMap<String, int[]> getAtlasData() {
        return atlasData;
    }
}
