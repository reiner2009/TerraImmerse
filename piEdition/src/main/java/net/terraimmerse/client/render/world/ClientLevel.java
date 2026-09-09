package net.terraimmerse.client.render.world;

import net.terraimmerse.client.ClientInitializer;
import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.client.render.MaterialRenderLayerMap;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.level.Level;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.Map;

public class ClientLevel {
    private float[] vertices;
    private int vbo;
    private int vao;
    private Level level;
    private ArrayList<Float> verticesList;
    private MaterialRenderLayerMap materialRenderLayerMap;

    public ClientLevel(Level level_, MaterialRenderLayerMap materialRenderLayerMap_){
        this.materialRenderLayerMap=materialRenderLayerMap_;
        this.level = level_;
        this.verticesList = new ArrayList<>();

        for (Map.Entry<MaterialPos, String> entry : level.getLevel().entrySet()) {
            MaterialPos pos = entry.getKey();
            String material = entry.getValue();

            if( ClientInitializer.materialRenderLayerMapRegistry.get(material).equals(this.materialRenderLayerMap)) {
                float[] vert = MaterialRenderer.getVertices(pos.x, pos.y, pos.z, entry.getValue(), level, TerraImmerse.atlasManager.getAtlasData(), 3, 3, TerraImmerse.atlasManager.getTextureData());

                for (float v : vert) {
                    this.verticesList.add(v);
                }
            }
        }

        this.vertices = new float[this.verticesList.size()];

        for (int i = 0; i < this.verticesList.size(); i++) {
            this.vertices[i] = this.verticesList.get(i);
        }

        this.vbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8*Float.BYTES, 0L);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8*Float.BYTES, 12L);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8*Float.BYTES, 20L);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public int getVao(){
        return this.vbo;
    }

    public float[] getVertices(){
        return this.vertices;
    }
}