package net.terraimmerse.client.blaze3d.sky;

import net.terraimmerse.client.blaze3d.TextureLoader;
import org.lwjgl.opengl.GL15;

public class SunVBO {
    private static final float[] sunVertices = {
            -1.0F,  1.0F, 0.0F, 0.0F, 1.0F,
            -1.0F, -1.0F, 0.0F, 0.0F, 0.0F,
             1.0F, -1.0F, 0.0F, 1.0F, 0.0F,
            -1.0F,  1.0F, 0.0F, 0.0F, 1.0F,
             1.0F, -1.0F, 0.0F, 1.0F, 0.0F,
             1.0F,  1.0F, 0.0F, 1.0F, 1.0F
    };
    private static int vbo;
    private static int sunTexture;
    public SunVBO() {
        sunTexture = TextureLoader.loadTexture("/assets/textures/sky/sun.png");
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,sunVertices,GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    public static int getSunTexture() {
        return sunTexture;
    }
    public static int getVbo() {
        return vbo;
    }
}
