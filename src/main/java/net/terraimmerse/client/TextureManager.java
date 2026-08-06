package net.terraimmerse.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.terraimmerse.client.blaze3d.world.ClientChunk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TextureManager {
    private static JsonObject atlasDataJson;
    private static JsonObject textureDataJson;
    public static HashMap<String, String[]> textureData;
    public static HashMap<String, int[]> atlasData;
    public TextureManager(){
        atlasData=new HashMap<>();
        try {
            InputStream atlasDataFile = ClientChunk.class.getResourceAsStream("/assets/atlas.json");
            if (atlasDataFile == null) {
                throw new RuntimeException("Couldn't find atlas.json");
            }
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
        textureData=new HashMap<>();
        try {
            InputStream textureDataFile = ClientChunk.class.getResourceAsStream("/assets/textures.json");
            if (textureDataFile == null) {
                throw new RuntimeException("Couldn't find textures.json");
            }
            textureDataJson = JsonParser.parseReader(new InputStreamReader(textureDataFile)).getAsJsonObject();
            for (String key : textureDataJson.keySet()) {
                JsonArray array = textureDataJson.getAsJsonArray(key);
                String t1 = array.get(0).getAsString();
                String t2 = array.get(1).getAsString();
                String t3 = array.get(2).getAsString();
                String t4 = array.get(3).getAsString();
                String t5 = array.get(4).getAsString();
                String t6 = array.get(5).getAsString();
                textureData.put(key, new String[]{t1, t2, t3, t4, t5, t6});
            }
            textureDataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HashMap<String, String[]> getTextureData(){
        return textureData;
    }
    public HashMap<String, int[]> getAtlasData() {
        return atlasData;
    }
}
