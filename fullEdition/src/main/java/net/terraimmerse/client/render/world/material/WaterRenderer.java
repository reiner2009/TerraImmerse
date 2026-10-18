package net.terraimmerse.client.render.world.material;

import net.terraimmerse.world.level.Level;

public class WaterRenderer extends SimpleCubeMaterialRenderer {
    public WaterRenderer(){}
    @Override
    public float[] getTop(int x, int y, int z, float w, float h, float topTextureTx, float topTextureTy, Level level){
        return new float[]{
                x, y+0.9F, z+1, topTextureTx, topTextureTy+h, 0, 1, 0,
                x+1, y+0.9F, z, topTextureTx+w, topTextureTy, 0, 1, 0,
                x, y+0.9F, z, topTextureTx, topTextureTy, 0, 1, 0,
                x+1, y+0.9F, z+1, topTextureTx+w, topTextureTy+h, 0, 1, 0,
                x+1, y+0.9F, z, topTextureTx+w, topTextureTy, 0, 1, 0,
                x, y+0.9F, z+1, topTextureTx, topTextureTy+h, 0, 1, 0
        };
    }
    @Override
    public float[] getFront(int x, int y, int z, float w, float h, float frontTextureTx, float frontTextureTy, Level level){
        if(level.getMaterial(x, y+1, z).equals("air")) {
            return new float[]{
                    x, y, z + 1, frontTextureTx, frontTextureTy + h, 0, 0, 1,
                    x + 1, y, z + 1, frontTextureTx + w, frontTextureTy + h, 0, 0, 1,
                    x, y + 0.9F, z + 1, frontTextureTx, frontTextureTy, 0, 0, 1,
                    x + 1, y, z + 1, frontTextureTx + w, frontTextureTy + h, 0, 0, 1,
                    x + 1, y + 0.9F, z + 1, frontTextureTx + w, frontTextureTy, 0, 0, 1,
                    x, y + 0.9F, z + 1, frontTextureTx, frontTextureTy, 0, 0, 1
            };
        }
        return super.getFront(x, y, z, w, h, frontTextureTx, frontTextureTy, level);
    }
    @Override
    public float[] getBack(int x, int y, int z, float w, float h, float backTextureTx, float backTextureTy, Level level){
        if(level.getMaterial(x, y+1, z).equals("air")) {
            return new float[]{
                    x + 1, y, z, backTextureTx, backTextureTy + h, 0, 0, -1,
                    x, y, z, backTextureTx + w, backTextureTy + h, 0, 0, -1,
                    x + 1, y + 0.9F, z, backTextureTx, backTextureTy, 0, 0, -1,
                    x, y, z, backTextureTx + w, backTextureTy + h, 0, 0, -1,
                    x, y + 0.9F, z, backTextureTx + w, backTextureTy, 0, 0, -1,
                    x + 1, y + 0.9F, z, backTextureTx, backTextureTy, 0, 0, -1
            };
        }
        return super.getBack(x, y, z, w, h, backTextureTx, backTextureTy, level);
    }
    @Override
    public float[] getLeft(int x, int y, int z, float w, float h, float leftTextureTx, float leftTextureTy, Level level){
        if(level.getMaterial(x, y+1, z).equals("air")) {
            return new float[]{
                    x, y, z, leftTextureTx, leftTextureTy + h, -1, 0, 0,
                    x, y, z + 1, leftTextureTx + w, leftTextureTy + h, -1, 0, 0,
                    x, y + 0.9F, z, leftTextureTx, leftTextureTy, -1, 0, 0,
                    x, y, z + 1, leftTextureTx + w, leftTextureTy + h, -1, 0, 0,
                    x, y + 0.9F, z + 1, leftTextureTx + w, leftTextureTy, -1, 0, 0,
                    x, y + 0.9F, z, leftTextureTx, leftTextureTy, -1, 0, 0
            };
        }
        return super.getLeft(x, y, z, w, h, leftTextureTx, leftTextureTy, level);
    }
    @Override
    public float[] getRight(int x, int y, int z, float w, float h, float rightTextureTx, float rightTextureTy, Level level){
        if(level.getMaterial(x, y+1, z).equals("air")) {
            return new float[]{
                    x + 1, y, z + 1, rightTextureTx, rightTextureTy + h, 1, 0, 0,
                    x + 1, y, z, rightTextureTx + w, rightTextureTy + h, 1, 0, 0,
                    x + 1, y + 0.9F, z + 1, rightTextureTx, rightTextureTy, 1, 0, 0,
                    x + 1, y, z, rightTextureTx + w, rightTextureTy + h, 1, 0, 0,
                    x + 1, y + 0.9F, z, rightTextureTx + w, rightTextureTy, 1, 0, 0,
                    x + 1, y + 0.9F, z + 1, rightTextureTx, rightTextureTy, 1, 0, 0
            };
        }
        return super.getRight(x, y, z, w, h, rightTextureTx, rightTextureTy, level);
    }
}
