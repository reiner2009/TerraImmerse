package net.terraimmerse.client.render.world;

import net.terraimmerse.client.render.ShaderCompiler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class WaterFogRenderer {
    private int vbo;
    private int vao;
    private ShaderCompiler shaderCompiler;
    private int shader;
    public WaterFogRenderer(){
        shaderCompiler=new ShaderCompiler("/assets/shader/waterFog.vert", "/assets/shader/waterFog.frag", "waterFogShader");
        shader=shaderCompiler.createShaderProgram(shaderCompiler.vertexShaderSrc, shaderCompiler.fragmentShaderSrc);
        float[] vertices = {
                -1.0f, -1.0f,
                1.0f, -1.0f,
                1.0f,  1.0f,
                -1.0f, -1.0f,
                1.0f,  1.0f,
                -1.0f,  1.0f
        };
        vao = GL30.glGenVertexArrays();
        vbo = GL20.glGenBuffers();
        GL30.glBindVertexArray(vao);
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertices, GL20.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(
                0,
                2,
                GL11.GL_FLOAT,
                false,
                2 * Float.BYTES,
                0L
        );
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    public void render(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL20.glUseProgram(shader);
        GL20.glUniform3f(GL20.glGetUniformLocation(shader, "screenColor"), 0.157f, 0.471f, 0.710f);
        GL20.glUniform1f(GL20.glGetUniformLocation(shader, "alpha"), 0.9f);
        GL30.glBindVertexArray(vao);
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
        GL30.glBindVertexArray(0);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
