package net.terraimmerse.client.blaze3d.world;

import net.terraimmerse.client.ClientInitializer;
import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.client.blaze3d.MaterialRenderLayerMap;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.Map;

public class ClientChunk {
    private float[] vertices;
    private int vbo;
    private Chunk chunk;
    private ArrayList<Float> verticesList;
    private MaterialRenderLayerMap materialRenderLayerMap;

    public ClientChunk(Chunk chunk_, MaterialRenderLayerMap materialRenderLayerMap_) {
        this.materialRenderLayerMap = materialRenderLayerMap_;
        this.chunk = chunk_;
        this.verticesList = new ArrayList<>();

        for (Map.Entry<MaterialPos, String> entry : chunk.getChunk().entrySet()) {
            MaterialPos pos = entry.getKey();
            String material = entry.getValue();

            if (ClientInitializer.materialRenderLayerMapRegistry.get(material).equals(this.materialRenderLayerMap)) {
                float[] vert = MaterialRenderer.getVertices(
                        pos.x,
                        pos.y,
                        pos.z,
                        entry.getValue(),
                        chunk,
                        TerraImmerse.textureManager.getAtlasData(),
                        3,
                        3,
                        TerraImmerse.textureManager.getTextureData()
                );

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
        GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                this.vertices,
                GL15.GL_STATIC_DRAW
        );
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);

        GL20.glVertexAttribPointer(
                0,
                3,
                GL11.GL_FLOAT,
                false,
                8 * Float.BYTES,
                0L
        );

        GL20.glVertexAttribPointer(
                1,
                2,
                GL11.GL_FLOAT,
                false,
                8 * Float.BYTES,
                3L * Float.BYTES
        );

        GL20.glVertexAttribPointer(
                2,
                3,
                GL11.GL_FLOAT,
                false,
                8 * Float.BYTES,
                5L * Float.BYTES
        );

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public int getVbo() {
        return this.vbo;
    }

    public float[] getVertices() {
        return this.vertices;
    }
}