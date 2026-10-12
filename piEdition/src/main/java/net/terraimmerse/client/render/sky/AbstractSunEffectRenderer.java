package net.terraimmerse.client.render.sky;

import net.terraimmerse.client.manager.TextureManager;
import net.terraimmerse.client.render.ShaderCompiler;
import net.terraimmerse.client.render.world.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public abstract class AbstractSunEffectRenderer{
    private int sunEffectLocModel;
    private int sunEffectLocView;
    private int sunEffectLocProj;
    private int sunEffectLocTexture;
    private int sunEffectLocColor;
    private int sunEffectLocAlpha;
    private int sunEffectLocBrightness;
    private int sunEffectShader;
    private float sunEffectR;
    private float sunEffectG;
    private float sunEffectB;
    private float sunEffectAlpha;
    private ShaderCompiler sunEffectShaderCompiler;
    private Matrix4f sunEffectModel;
    private Matrix4f sunEffectView;
    private float[] sunEffectVertices;
    private int vbo;
    private int sunEffectTexture;
    public AbstractSunEffectRenderer(String texturePath, float[] vboArray){
        this.sunEffectVertices=vboArray;
        this.sunEffectTexture = TextureManager.textures.get(texturePath);
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.sunEffectVertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this.sunEffectShaderCompiler=new ShaderCompiler("/assets/shader/sunEffect.vert", "/assets/shader/sunEffect.frag", "SunEffectShader");
        this.sunEffectShader=sunEffectShaderCompiler.createShaderProgram(sunEffectShaderCompiler.vertexShaderSrc, sunEffectShaderCompiler.fragmentShaderSrc);
        this.sunEffectLocModel= GL20.glGetUniformLocation(this.sunEffectShader, "model");
        this.sunEffectLocView = GL20.glGetUniformLocation(this.sunEffectShader, "view");
        this.sunEffectLocProj = GL20.glGetUniformLocation(this.sunEffectShader, "projection");
        this.sunEffectLocTexture = GL20.glGetUniformLocation(this.sunEffectShader, "sunEffectTexture");
        this.sunEffectLocColor = GL20.glGetUniformLocation(this.sunEffectShader, "sunEffectColor");
        this.sunEffectLocAlpha = GL20.glGetUniformLocation(this.sunEffectShader, "sunEffectAlpha");
        this.sunEffectLocBrightness = GL20.glGetUniformLocation(this.sunEffectShader, "brightness");
        this.sunEffectR=1.0F;
        this.sunEffectG=1.0F;
        this.sunEffectB=1.0F;
        this.sunEffectAlpha=1.0F;
    }
    public void setColor(float r, float g, float b){
        this.sunEffectR=r;
        this.sunEffectG=g;
        this.sunEffectB=b;
    }
    public void setAlpha(float alpha){
        this.sunEffectAlpha=alpha;
    }
    public void render(){
        this.sunEffectView=new Matrix4f(new Matrix3f(WorldRenderer.view));
        this.sunEffectModel=new Matrix4f().identity();
        this.sunEffectModel.translate(0,0,-1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(this.sunEffectShader);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(this.sunEffectLocModel, false, this.sunEffectModel.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(this.sunEffectLocView, false, this.sunEffectView.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(this.sunEffectLocProj, false, WorldRenderer.projection.get(stack.mallocFloat(16)));
        }
        GL20.glUniform1f(this.sunEffectLocBrightness,2.0F);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.sunEffectTexture);
        GL20.glUniform1i(this.sunEffectLocTexture,0);
        GL20.glUniform3f(this.sunEffectLocColor,this.sunEffectR,this.sunEffectG,this.sunEffectB);
        GL20.glUniform1f(this.sunEffectLocAlpha, this.sunEffectAlpha);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glDrawArrays(GL11.GL_TRIANGLES,0,6);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
