package net.terraimmerse.client.blaze3d.world;

import net.terraimmerse.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Map;

public class MaterialRenderer {
    public static float[] getVertices(int x, int y, int z, String material, Chunk chunk, Map<String, int[]> atlasData, int atlasW, int atlasH, Map<String, String[]> textureData) {
        String[] textures = textureData.get(material);
        float w = 1.0f / atlasW;
        float h = 1.0f / atlasH;
        int[] top = atlasData.get(textures[0]);
        int[] bottom = atlasData.get(textures[1]);
        int[] front = atlasData.get(textures[2]);
        int[] back = atlasData.get(textures[3]);
        int[] left = atlasData.get(textures[4]);
        int[] right = atlasData.get(textures[5]);
        float top_texture_tx = top[0] / (float) atlasW;
        float top_texture_ty = top[1] / (float) atlasH;
        float bottom_texture_tx = bottom[0] / (float) atlasW;
        float bottom_texture_ty = bottom[1] / (float) atlasH;
        float front_texture_tx = front[0] / (float) atlasW;
        float front_texture_ty = front[1] / (float) atlasH;
        float back_texture_tx = back[0] / (float) atlasW;
        float back_texture_ty = back[1] / (float) atlasH;
        float left_texture_tx = left[0] / (float) atlasW;
        float left_texture_ty = left[1] / (float) atlasH;
        float right_texture_tx = right[0] / (float) atlasW;
        float right_texture_ty = right[1] / (float) atlasH;
        float[] TOP = {
                x, y+1, z+1, top_texture_tx, top_texture_ty+h, 0, 1, 0,
                x+1, y+1, z, top_texture_tx+w, top_texture_ty, 0, 1, 0,
                x, y+1, z, top_texture_tx, top_texture_ty, 0, 1, 0,
                x+1, y+1, z+1, top_texture_tx+w, top_texture_ty+h, 0, 1, 0,
                x+1, y+1, z, top_texture_tx+w, top_texture_ty, 0, 1, 0,
                x, y+1, z+1, top_texture_tx, top_texture_ty+h, 0, 1, 0
        };
        float[] BOTTOM = {
                x, y, z, bottom_texture_tx, bottom_texture_ty, 0, -1, 0,
                x+1, y, z, bottom_texture_tx+w, bottom_texture_ty, 0, -1, 0,
                x, y, z+1, bottom_texture_tx, bottom_texture_ty+h, 0, -1, 0,
                x+1, y, z, bottom_texture_tx+w, bottom_texture_ty, 0, -1, 0,
                x+1, y, z+1, bottom_texture_tx+w, bottom_texture_ty+h, 0, -1, 0,
                x, y, z+1, bottom_texture_tx, bottom_texture_ty+h, 0, -1, 0
        };
        float[] FRONT = {
                x, y, z+1, front_texture_tx, front_texture_ty+h, 0, 0, 1,
                x+1, y, z+1, front_texture_tx+w, front_texture_ty+h, 0, 0, 1,
                x, y+1, z+1, front_texture_tx, front_texture_ty, 0, 0, 1,

                x+1, y, z+1, front_texture_tx+w, front_texture_ty+h, 0, 0, 1,
                x+1, y+1, z+1, front_texture_tx+w, front_texture_ty, 0, 0, 1,
                x, y+1, z+1, front_texture_tx, front_texture_ty, 0, 0, 1
        };

        float[] BACK = {
                x+1, y, z, back_texture_tx, back_texture_ty+h, 0, 0, -1,
                x, y, z, back_texture_tx+w, back_texture_ty+h, 0, 0, -1,
                x+1, y+1, z, back_texture_tx, back_texture_ty, 0, 0, -1,
                x, y, z, back_texture_tx+w, back_texture_ty+h, 0, 0, -1,
                x, y+1, z, back_texture_tx+w, back_texture_ty, 0, 0, -1,
                x+1, y+1, z, back_texture_tx, back_texture_ty, 0, 0, -1
        };
        float[] LEFT = {
                x, y, z, left_texture_tx, left_texture_ty+h, -1, 0, 0,
                x, y, z+1, left_texture_tx+w, left_texture_ty+h, -1, 0, 0,
                x, y+1, z, left_texture_tx, left_texture_ty, -1, 0, 0,
                x, y, z+1, left_texture_tx+w, left_texture_ty+h, -1, 0, 0,
                x, y+1, z+1, left_texture_tx+w, left_texture_ty, -1, 0, 0,
                x, y+1, z, left_texture_tx, left_texture_ty, -1, 0, 0
        };
        float[] RIGHT = {
                x+1, y, z+1, right_texture_tx, right_texture_ty+h, 1, 0, 0,
                x+1, y, z, right_texture_tx+w, right_texture_ty+h, 1, 0, 0,
                x+1, y+1, z+1, right_texture_tx, right_texture_ty, 1, 0, 0,
                x+1, y, z, right_texture_tx+w, right_texture_ty+h, 1, 0, 0,
                x+1, y+1, z, right_texture_tx+w, right_texture_ty, 1, 0, 0,
                x+1, y+1, z+1, right_texture_tx, right_texture_ty, 1, 0, 0
        };
        float[][] faces = {
                TOP,
                BOTTOM,
                FRONT,
                BACK,
                LEFT,
                RIGHT
        };
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
            if ("air".equals(chunk.getMaterial(
                    x + neighbors[i][0],
                    y + neighbors[i][1],
                    z + neighbors[i][2]
            ))) {
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