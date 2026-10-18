package net.terraimmerse.client.render.world.material;

import net.terraimmerse.world.level.Level;

import java.util.ArrayList;
import java.util.Map;

public class CrossMaterialRenderer extends SimpleCubeMaterialRenderer{
    public CrossMaterialRenderer(){}
    public static float sin45(){
        return (float)Math.sin(Math.toRadians(45));
    }
    public static float sin45div2(){
        return sin45() * (0.5F+sin45());
    }
    public static float oneMinusSin45div2(){
        return 1 - sin45div2();
    }
    @Override
    public float[] getTop(int x, int y, int z, float w, float h, float topTextureTx, float topTextureTy, Level level){
        return new float[]{};
    }
    @Override
    public float[] getBottom(int x, int y, int z, float w, float h, float bottomTextureTx, float bottomTextureTy, Level level){
        return new float[]{};
    }
    @Override
    public float[] getFront(int x, int y, int z, float w, float h, float frontTextureTx, float frontTextureTy, Level level){
        return new float[]{
                x+ oneMinusSin45div2(), y, z+ oneMinusSin45div2(), frontTextureTx, frontTextureTy+h, -sin45(), 0, sin45(),
                x+ sin45div2(), y, z+ sin45div2(), frontTextureTx+w, frontTextureTy+h, -sin45(), 0, sin45(),
                x+ oneMinusSin45div2(), y+1, z+ oneMinusSin45div2(), frontTextureTx, frontTextureTy, -sin45(), 0, sin45(),
                x+ sin45div2(), y, z+ sin45div2(), frontTextureTx+w, frontTextureTy+h, -sin45(), 0, sin45(),
                x+ sin45div2(), y+1, z+ sin45div2(), frontTextureTx+w, frontTextureTy, -sin45(), 0, sin45(),
                x+ oneMinusSin45div2(), y+1, z+ oneMinusSin45div2(), frontTextureTx, frontTextureTy, -sin45(), 0, sin45()
        };
    }
    @Override
    public float[] getBack(int x, int y, int z, float w, float h, float backTextureTx, float backTextureTy, Level level){
        return new float[]{
                x+ sin45div2(), y, z+ sin45div2(), backTextureTx+w, backTextureTy+h, sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y, z+ oneMinusSin45div2(), backTextureTx, backTextureTy+h, sin45(), 0, -sin45(),
                x+ sin45div2(), y+1, z+ sin45div2(), backTextureTx+w, backTextureTy, sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y, z+ oneMinusSin45div2(), backTextureTx, backTextureTy+h, sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y+1, z+ oneMinusSin45div2(), backTextureTx, backTextureTy, sin45(), 0, -sin45(),
                x+ sin45div2(), y+1, z+ sin45div2(), backTextureTx+w, backTextureTy, sin45(), 0, -sin45()
        };
    }
    @Override
    public float[] getLeft(int x, int y, int z, float w, float h, float leftTextureTx, float leftTextureTy, Level level){
        return new float[]{
                x+ sin45div2(), y, z+ oneMinusSin45div2(), leftTextureTx+w, leftTextureTy+h, -sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y, z+ sin45div2(), leftTextureTx, leftTextureTy+h, -sin45(), 0, -sin45(),
                x+ sin45div2(), y+1, z+ oneMinusSin45div2(), leftTextureTx+w, leftTextureTy, -sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y, z+ sin45div2(), leftTextureTx, leftTextureTy+h, -sin45(), 0, -sin45(),
                x+ oneMinusSin45div2(), y+1, z+ sin45div2(), leftTextureTx, leftTextureTy, -sin45(), 0, -sin45(),
                x+ sin45div2(), y+1, z+ oneMinusSin45div2(), leftTextureTx+w, leftTextureTy, -sin45(), 0, -sin45()
        };
    }
    @Override
    public float[] getRight(int x, int y, int z, float w, float h, float rightTextureTx, float rightTextureTy, Level level){
        return new float[]{
                x+ oneMinusSin45div2(), y, z+ sin45div2(), rightTextureTx, rightTextureTy+h, sin45(), 0, sin45(),
                x+ sin45div2(), y, z+ oneMinusSin45div2(), rightTextureTx+w, rightTextureTy+h, sin45(), 0, sin45(),
                x+ oneMinusSin45div2(), y+1, z+ sin45div2(), rightTextureTx, rightTextureTy, sin45(), 0, sin45(),
                x+ sin45div2(), y, z+ oneMinusSin45div2(), rightTextureTx+w, rightTextureTy+h, sin45(), 0, sin45(),
                x+ sin45div2(), y+1, z+ oneMinusSin45div2(), rightTextureTx+w, rightTextureTy, sin45(), 0, sin45(),
                x+ oneMinusSin45div2(), y+1, z+ sin45div2(), rightTextureTx, rightTextureTy, sin45(), 0, sin45()
        };
    }
    @Override
    public float[] getVertices(int x, int y, int z, String material, Level level, Map<String, int[]> atlasData, int atlasW, int atlasH, Map<String, String[]> textureData) {
        float[][] faces=getFaces(x, y, z, atlasData, atlasW, atlasH, textureData, material, level);
        ArrayList<Float> vertices = new ArrayList<>();
        for (int i = 0; i < faces.length; i++) {
            for (float v : faces[i]) {
                vertices.add(v);
            }
        }
        float[] result = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            result[i] = vertices.get(i);
        }
        return result;
    }
}
