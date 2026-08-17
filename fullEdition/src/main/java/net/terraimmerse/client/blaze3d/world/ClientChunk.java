package net.terraimmerse.client.blaze3d.world;

import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Map;

public class ClientChunk {
    private static float[] vertices;
    private static int vbo;
    private static int vao;
    private Chunk chunk;
    private ArrayList<Float> verticesList;
    public ClientChunk(Chunk chunk_){
        this.chunk=chunk_;
        this.verticesList = new ArrayList<>();
        for (Map.Entry<MaterialPos, String> entry : chunk.getChunk().entrySet()) {
            MaterialPos pos = entry.getKey();
            float[] vert = MaterialRenderer.getVertices(pos.x, pos.y, pos.z,entry.getValue(),chunk, TerraImmerse.textureManager.getAtlasData(), 2, 2, TerraImmerse.textureManager.getTextureData());
            for (float v : vert) {
                this.verticesList.add(v);
            }
        }
        vertices = new float[verticesList.size()];
        for (int i = 0; i < verticesList.size(); i++) {
            vertices[i] = verticesList.get(i);
        }
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8*Float.BYTES, 0L);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8*Float.BYTES, 12L);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8*Float.BYTES, 20L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    public static int getVao(){
        return vao;
    }
    public static float[] getVertices(){
        return vertices;
    }
}
