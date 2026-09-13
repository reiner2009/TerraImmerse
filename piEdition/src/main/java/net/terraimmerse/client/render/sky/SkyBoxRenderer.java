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

public class SkyBoxRenderer{
	private static int skyLocModel;
	private static int skyLocView;
	private static int skyLocProj;
	private static int skyLocTexture;
	private static int skyLocColor;
	private static int skyShader;
	private static ShaderCompiler skyShaderCompiler;
	public static float skyR;
	public static float skyG;
	public static float skyB;
	private static Matrix4f skyModel;
	private static Matrix4f skyView;
	private static int vbo;
	public static float[] skyVertices = {
		-1.0F,1.0F,-1.0F,0.0F,0.0F,
		-1.0F,-1.0F,-1.0F,0.0F,1.0F/3,
		1.0F,-1.0F,-1.0F,0.5F,1.0F/3,
		-1.0F,1.0F,-1.0F,0.0F,0.0F,
		1.0F,-1.0F,-1.0F,0.5F,1.0F/3,
		1.0F,1.0F,-1.0F,0.5F,0.0F,
		-1.0F,1.0F,1.0F,0.5F,1.0F/3,
		-1.0F,1.0F,-1.0F,0.5F,1.0F/3*2,
		1.0F,1.0F,-1.0F,1.0F,1.0F/3*2,
		-1.0F,1.0F,1.0F,0.5F,1.0F/3,
		1.0F,1.0F,-1.0F,1.0F,1.0F/3*2,
		1.0F,1.0F,1.0F,1.0F,1.0F/3,
		1.0F,-1.0F,1.0F,0.5F,0.0F,
		1.0F,-1.0F,-1.0F,0.5F,1.0F/3,
		-1.0F,-1.0F,-1.0F,1.0F,1.0F/3,
		1.0F,-1.0F,1.0F,0.5F,0.0F,
		-1.0F,-1.0F,-1.0F,1.0F,1.0F/3,
		-1.0F,-1.0F,1.0F,1.0F,0.0F,
		1.0F,1.0F,1.0F,0.0F,1.0F/3,
		1.0F,-1.0F,1.0F,0.0F,1.0F/3*2,
		-1.0F,-1.0F,1.0F,0.5F,1.0F/3*2,
		1.0F,1.0F,1.0F,0.0F,1.0F/3,
		-1.0F,-1.0F,1.0F,0.5F,1.0F/3*2,
		-1.0F,1.0F,1.0F,0.5F,1.0F/3,
		1.0F,1.0F,-1.0F,0.0F,1.0F/3*2,
		1.0F,-1.0F,-1.0F,0.0F,1.0F,
		1.0F,-1.0F,1.0F,0.5F,1.0F,
		1.0F,1.0F,-1.0F,0.0F,1.0F/3*2,
		1.0F,-1.0F,1.0F,0.5F,1.0F,
		1.0F,1.0F,1.0F,0.5F,1.0F/3*2,
		-1.0F,1.0F,1.0F,0.5F,1.0F/3*2,
		-1.0F,-1.0F,1.0F,0.5F,1.0F,
		-1.0F,-1.0F,-1.0F,1.0F,1.0F,
		-1.0F,1.0F,1.0F,0.5F,1.0F/3*2,
		-1.0F,-1.0F,-1.0F,1.0F,1.0F,
		-1.0F,1.0F,-1.0F,1.0F,1.0F/3*2,
	};
	public static int skyBoxVbo;
	public static int skyBoxTexture;
	public SkyBoxRenderer(){
		skyBoxTexture = TextureManager.textures.get("/assets/textures/sky/skybox.png");
		vbo=GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, skyVertices, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	public static void init(){
		skyShaderCompiler=new ShaderCompiler("/assets/shader/sky.vert", "/assets/shader/sky.frag", "SkyShader");
        skyShader=skyShaderCompiler.createShaderProgram(skyShaderCompiler.vertexShaderSrc, skyShaderCompiler.fragmentShaderSrc);
        skyLocModel= GL20.glGetUniformLocation(skyShader, "model");
        skyLocView = GL20.glGetUniformLocation(skyShader, "view");
        skyLocProj = GL20.glGetUniformLocation(skyShader, "projection");
        skyLocTexture = GL20.glGetUniformLocation(skyShader, "skyTexture");
        skyLocColor = GL20.glGetUniformLocation(skyShader, "skyColor");
        skyR=1.0F;
        skyG=1.0F;
        skyB=1.0F;
	}
	public static void render(){
		skyView=new Matrix4f(new Matrix3f(WorldRenderer.view));
        skyModel=new Matrix4f().identity();
        skyModel.translate(0,0,0);
        GL20.glUseProgram(skyShader);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(skyLocModel, false, skyModel.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(skyLocView, false, skyView.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(skyLocProj, false, WorldRenderer.projection.get(stack.mallocFloat(16)));
        }
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBoxTexture);
        GL20.glUniform1i(skyLocTexture,0);
        GL20.glUniform3f(skyLocColor,skyR,skyG,skyB);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glDrawArrays(GL11.GL_TRIANGLES,0,(int)skyVertices.length/6*2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}
