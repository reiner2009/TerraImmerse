package net.terraimmerse.client.blaze3d.sky;

import net.terraimmerse.client.blaze3d.TextureLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SunVBO {
    private static float[] sunVertices = {
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            -1.0F,-1.0F,0.0F,0.0F,0.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            1.0F,1.0F,0.0F,1.0F,1.0F
    };
    private static int vbo;
    private static int vao;
    private static int sunTexture;
    public SunVBO(){
        sunTexture = TextureLoader.loadTexture("/assets/textures/sky/sun.png");
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, sunVertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    public static int getSunTexture(){
        return sunTexture;
    }
    public static int getVao(){
        return vao;
    }
}
