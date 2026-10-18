package net.terraimmerse.client.render.world.material;

import net.terraimmerse.client.Client;
import net.terraimmerse.client.render.RenderLayerMap;
import net.terraimmerse.world.level.Level;

import java.util.ArrayList;
import java.util.Map;

public class SimpleCubeMaterialRenderer {
    public SimpleCubeMaterialRenderer(){}
    public float[] getTop(int x, int y, int z, float w, float h, float topTextureTx, float topTextureTy, Level level){
        return new float[]{
                x, y+1, z+1, topTextureTx, topTextureTy+h, 0, 1, 0,
                x+1, y+1, z, topTextureTx+w, topTextureTy, 0, 1, 0,
                x, y+1, z, topTextureTx, topTextureTy, 0, 1, 0,
                x+1, y+1, z+1, topTextureTx+w, topTextureTy+h, 0, 1, 0,
                x+1, y+1, z, topTextureTx+w, topTextureTy, 0, 1, 0,
                x, y+1, z+1, topTextureTx, topTextureTy+h, 0, 1, 0
        };
    }
    public float[] getBottom(int x, int y, int z, float w, float h, float bottomTextureTx, float bottomTextureTy, Level level){
        return new float[]{
                x, y, z, bottomTextureTx, bottomTextureTy, 0, -1, 0,
                x+1, y, z, bottomTextureTx+w, bottomTextureTy, 0, -1, 0,
                x, y, z+1, bottomTextureTx, bottomTextureTy+h, 0, -1, 0,
                x+1, y, z, bottomTextureTx+w, bottomTextureTy, 0, -1, 0,
                x+1, y, z+1, bottomTextureTx+w, bottomTextureTy+h, 0, -1, 0,
                x, y, z+1, bottomTextureTx, bottomTextureTy+h, 0, -1, 0
        };
    }
    public float[] getFront(int x, int y, int z, float w, float h, float frontTextureTx, float frontTextureTy, Level level){
        return new float[]{
                x, y, z+1, frontTextureTx, frontTextureTy+h, 0, 0, 1,
                x+1, y, z+1, frontTextureTx+w, frontTextureTy+h, 0, 0, 1,
                x, y+1, z+1, frontTextureTx, frontTextureTy, 0, 0, 1,
                x+1, y, z+1, frontTextureTx+w, frontTextureTy+h, 0, 0, 1,
                x+1, y+1, z+1, frontTextureTx+w, frontTextureTy, 0, 0, 1,
                x, y+1, z+1, frontTextureTx, frontTextureTy, 0, 0, 1
        };
    }
    public float[] getBack(int x, int y, int z, float w, float h, float backTextureTx, float backTextureTy, Level level){
        return new float[]{
                x+1, y, z, backTextureTx, backTextureTy+h, 0, 0, -1,
                x, y, z, backTextureTx+w, backTextureTy+h, 0, 0, -1,
                x+1, y+1, z, backTextureTx, backTextureTy, 0, 0, -1,
                x, y, z, backTextureTx+w, backTextureTy+h, 0, 0, -1,
                x, y+1, z, backTextureTx+w, backTextureTy, 0, 0, -1,
                x+1, y+1, z, backTextureTx, backTextureTy, 0, 0, -1
        };
    }
    public float[] getLeft(int x, int y, int z, float w, float h, float leftTextureTx, float leftTextureTy, Level level){
        return new float[]{
                x, y, z, leftTextureTx, leftTextureTy+h, -1, 0, 0,
                x, y, z+1, leftTextureTx+w, leftTextureTy+h, -1, 0, 0,
                x, y+1, z, leftTextureTx, leftTextureTy, -1, 0, 0,
                x, y, z+1, leftTextureTx+w, leftTextureTy+h, -1, 0, 0,
                x, y+1, z+1, leftTextureTx+w, leftTextureTy, -1, 0, 0,
                x, y+1, z, leftTextureTx, leftTextureTy, -1, 0, 0
        };
    }
    public float[] getRight(int x, int y, int z, float w, float h, float rightTextureTx, float rightTextureTy, Level level){
        return new float[]{
                x+1, y, z+1, rightTextureTx, rightTextureTy+h, 1, 0, 0,
                x+1, y, z, rightTextureTx+w, rightTextureTy+h, 1, 0, 0,
                x+1, y+1, z+1, rightTextureTx, rightTextureTy, 1, 0, 0,
                x+1, y, z, rightTextureTx+w, rightTextureTy+h, 1, 0, 0,
                x+1, y+1, z, rightTextureTx+w, rightTextureTy, 1, 0, 0,
                x+1, y+1, z+1, rightTextureTx, rightTextureTy, 1, 0, 0
        };
    }
    public float[][] getFaces(int x, int y, int z, Map<String, int[]> atlasData, int atlasW, int atlasH, Map<String, String[]> textureData, String material, Level level){
        String[] textures = textureData.get(material);
        float w = 1.0f / atlasW;
        float h = 1.0f / atlasH;
        int[] top = atlasData.get(textures[0]);
        int[] bottom = atlasData.get(textures[1]);
        int[] front = atlasData.get(textures[2]);
        int[] back = atlasData.get(textures[3]);
        int[] left = atlasData.get(textures[4]);
        int[] right = atlasData.get(textures[5]);
        float topTextureTx = top[0] / (float) atlasW;
        float topTextureTy = top[1] / (float) atlasH;
        float bottomTextureTx = bottom[0] / (float) atlasW;
        float bottomTextureTy = bottom[1] / (float) atlasH;
        float frontTextureTx = front[0] / (float) atlasW;
        float frontTextureTy = front[1] / (float) atlasH;
        float backTextureTx = back[0] / (float) atlasW;
        float backTextureTy = back[1] / (float) atlasH;
        float leftTextureTx = left[0] / (float) atlasW;
        float leftTextureTy = left[1] / (float) atlasH;
        float rightTextureTx = right[0] / (float) atlasW;
        float rightTextureTy = right[1] / (float) atlasH;
        float[] TOP = getTop(x, y, z, w, h, topTextureTx, topTextureTy, level);
        float[] BOTTOM = getBottom(x, y, z, w, h, bottomTextureTx, bottomTextureTy, level);
        float[] FRONT = getFront(x, y, z, w, h, frontTextureTx, frontTextureTy, level);
        float[] BACK = getBack(x, y, z, w, h, backTextureTx, backTextureTy, level);
        float[] LEFT = getLeft(x, y, z, w, h, leftTextureTx, leftTextureTy, level);
        float[] RIGHT = getRight(x, y, z, w, h, rightTextureTx, rightTextureTy, level);
        return new  float[][]{
                TOP,
                BOTTOM,
                FRONT,
                BACK,
                LEFT,
                RIGHT
        };
    }
    public float[] getVertices(int x, int y, int z, String material, Level level, Map<String, int[]> atlasData, int atlasW, int atlasH, Map<String, String[]> textureData) {
        float[][] faces=getFaces(x, y, z, atlasData, atlasW, atlasH, textureData, material, level);
        int[][] neighbors = {
                {0,1,0},
                {0,-1,0},
                {0,0,1},
                {0,0,-1},
                {-1,0,0},
                {1,0,0}
        };
        ArrayList<Float> vertices = new ArrayList<>();
        for (int i = 0; i < faces.length; i++) {
            String neighborMaterial = level.getMaterial(
                    x + neighbors[i][0],
                    y + neighbors[i][1],
                    z + neighbors[i][2]
            );

            if (("air".equals(neighborMaterial)) || (RenderLayerMap.CUTOUT.equals(Client.materialRenderLayerMapRegistry.get(neighborMaterial)) || (RenderLayerMap.TRANSLUCENT.equals(Client.materialRenderLayerMapRegistry.get(neighborMaterial)))) && (!RenderLayerMap.TRANSLUCENT.equals(Client.materialRenderLayerMapRegistry.get(material)))) {
                for (float v : faces[i]) {
                    vertices.add(v);
                }
            }
        }
        float[] result = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            result[i] = vertices.get(i);
        }
        return result;
    }
}