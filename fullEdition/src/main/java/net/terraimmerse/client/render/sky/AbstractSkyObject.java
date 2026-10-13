package net.terraimmerse.client.render.sky;

import net.terraimmerse.client.manager.TextureManager;
import net.terraimmerse.client.render.ShaderCompiler;
import net.terraimmerse.client.render.world.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public abstract class AbstractSkyObject{
	private int skyObjectLocModel;
    private int skyObjectLocView;
    private int skyObjectLocProj;
    private int skyObjectLocTexture;
    private int skyObjectLocColor;
    private int skyObjectLocAlpha;
    private int skyObjectLocBrightness;
    private int skyObjectShader;
    private float skyObjectR;
    private float skyObjectG;
    private float skyObjectB;
    private float skyObjectAlpha;
    private ShaderCompiler skyObjectShaderCompiler;
    public Matrix4f skyObjectModel;
    public Matrix4f skyObjectView;
    private int vbo;
    private int vao;
    private int skyObjectTexture;
    public int skyObjectRadius;
    public AbstractSkyObject(String texturePath, float[] vertices, int radius){
        this.skyObjectTexture = TextureManager.textures.get(texturePath);
        this.vbo = GL15.glGenBuffers();
        this.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this.skyObjectShaderCompiler=new ShaderCompiler("/assets/shader/skyObject.vert", "/assets/shader/skyObject.frag", "SkyObjectShader");
        this.skyObjectShader=skyObjectShaderCompiler.createShaderProgram(this.skyObjectShaderCompiler.vertexShaderSrc, this.skyObjectShaderCompiler.fragmentShaderSrc);
        this.skyObjectLocModel= GL20.glGetUniformLocation(this.skyObjectShader, "model");
        this.skyObjectLocView = GL20.glGetUniformLocation(this.skyObjectShader, "view");
        this.skyObjectLocProj = GL20.glGetUniformLocation(this.skyObjectShader, "projection");
        this.skyObjectLocTexture = GL20.glGetUniformLocation(this.skyObjectShader, "skyObjectTexture");
        this.skyObjectLocColor = GL20.glGetUniformLocation(this.skyObjectShader, "skyObjectColor");
        this.skyObjectLocAlpha = GL20.glGetUniformLocation(this.skyObjectShader, "skyObjectAlpha");
        this.skyObjectLocBrightness = GL20.glGetUniformLocation(this.skyObjectShader, "brightness");
        this.skyObjectR=1.0F;
        this.skyObjectG=1.0F;
        this.skyObjectB=1.0F;
        this.skyObjectAlpha=1.0F;
        this.skyObjectRadius=radius;
    }
    public void setColor(float r, float g, float b){
        this.skyObjectR=r;
        this.skyObjectG=g;
        this.skyObjectB=b;
    }
    public void render(){
        this.skyObjectView=new Matrix4f(new Matrix3f(WorldRenderer.view));
        GL20.glUseProgram(this.skyObjectShader);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(this.skyObjectLocModel, false, this.skyObjectModel.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(this.skyObjectLocView, false, this.skyObjectView.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(this.skyObjectLocProj, false, WorldRenderer.projection.get(stack.mallocFloat(16)));
        }
        GL20.glUniform1f(this.skyObjectLocBrightness,2.0F);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.skyObjectTexture);
        GL20.glUniform1i(this.skyObjectLocTexture,0);
        GL20.glUniform3f(this.skyObjectLocColor,this.skyObjectR,this.skyObjectG,this.skyObjectB);
        GL20.glUniform1f(this.skyObjectLocAlpha, this.skyObjectAlpha);
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glDrawArrays(GL11.GL_TRIANGLES,0,6);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
}
