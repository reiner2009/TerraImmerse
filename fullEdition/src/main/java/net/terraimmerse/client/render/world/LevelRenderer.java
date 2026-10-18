package net.terraimmerse.client.render.world;

import net.terraimmerse.client.Client;
import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.client.render.RenderLayerMap;
import net.terraimmerse.world.material.MaterialPos;
import net.terraimmerse.world.level.Level;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Map;

public class LevelRenderer {
    private float[] vertices;
    private int vbo;
    private int vao;
    private Level level;
    private ArrayList<Float> verticesList;
    private RenderLayerMap materialRenderLayerMap;
    public LevelRenderer(Level level_, RenderLayerMap materialRenderLayerMap_){
        this.materialRenderLayerMap=materialRenderLayerMap_;
        this.level = level_;
        this.verticesList = new ArrayList<>();
        this.vertices = new float[this.verticesList.size()];
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        this.updateVertices();
        this.rebuildVBO();
    }
    private void rebuildVBO() {
        if (this.vbo != 0) {
            GL15.glDeleteBuffers(this.vbo);
        }
        if (this.vao != 0) {
            GL30.glDeleteVertexArrays(this.vao);
        }
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0L);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 12L);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 20L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    public void updateVertices(){
        this.verticesList.clear();
        for (Map.Entry<MaterialPos, String> entry : level.getLevel().entrySet()) {
            MaterialPos pos = entry.getKey();
            String material = entry.getValue();
            if(Client.materialRenderLayerMapRegistry.get(material).equals(this.materialRenderLayerMap)) {
                float[] vert=Client.materialRendererRegistry.getMaterialRenderRegistry(material)
                        .getVertices(pos.x, pos.y, pos.z, entry.getValue(), level, TerraImmerse.atlasManager.getAtlasData(), 4, 4, TerraImmerse.atlasManager.getTextureData());
                for (float v : vert) {
                    this.verticesList.add(v);
                }
            }
        }
        this.vertices = new float[this.verticesList.size()];
        for (int i = 0; i < this.verticesList.size(); i++) {
            this.vertices[i] = this.verticesList.get(i);
        }
    }
    public void update(){
        this.updateVertices();
        this.rebuildVBO();
    }
    public int getVao(){
        return this.vao;
    }
    public float[] getVertices(){
        return this.vertices;
    }
}