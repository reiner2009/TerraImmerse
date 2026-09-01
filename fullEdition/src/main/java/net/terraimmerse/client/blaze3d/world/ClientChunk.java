package net.terraimmerse.client.blaze3d.world;

import net.terraimmerse.client.ClientInitializer;
import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.client.blaze3d.MaterialRenderLayerMap;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Map;

public class ClientChunk {
    private float[] vertices;
    private int vbo;
    private int vao;
    private Chunk chunk;
    private ArrayList<Float> verticesList;
    private MaterialRenderLayerMap materialRenderLayerMap;
    public ClientChunk(Chunk chunk_, MaterialRenderLayerMap materialRenderLayerMap_){
        this.materialRenderLayerMap=materialRenderLayerMap_;
        this.chunk=chunk_;
        this.verticesList = new ArrayList<>();
        for (Map.Entry<MaterialPos, String> entry : chunk.getChunk().entrySet()) {
            MaterialPos pos = entry.getKey();
            String material = entry.getValue();
            if( ClientInitializer.materialRenderLayerMapRegistry.get(material).equals(this.materialRenderLayerMap)) {
                float[] vert = MaterialRenderer.getVertices(pos.x, pos.y, pos.z, entry.getValue(), chunk, TerraImmerse.textureManager.getAtlasData(), 3, 3, TerraImmerse.textureManager.getTextureData());
                for (float v : vert) {
                    this.verticesList.add(v);
                }
            }
        }
        this.vertices = new float[this.verticesList.size()];
        for (int i = 0; i < this.verticesList.size(); i++) {
            this.vertices[i] = this.verticesList.get(i);
        }
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(this.vao);
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
    public int getVao(){
        return this.vao;
    }
    public float[] getVertices(){
        return this.vertices;
    }
}
