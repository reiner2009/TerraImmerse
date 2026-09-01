package net.terraimmerse.client.blaze3d;

import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.client.blaze3d.sky.SkyRenderer;
import net.terraimmerse.client.blaze3d.world.ClientChunk;
import net.terraimmerse.world.generator.WorldGenerator;
import net.terraimmerse.world.chunk.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class WorldRenderer {
    private static SkyRenderer skyRenderer;
    private static ShaderCompiler shaderCompiler;
    private static WorldGenerator worldGenerator;
    private static int locModel;
    private static int locView;
    private static int locProj;
    private static int locCutout;
    private static int atlasTexture;
    private static int textureLoc;
    private static Matrix4f model;
    public static Matrix4f view;
    public static Matrix4f projection;
    public static int shader;
    private static ClientChunk clientChunk;
    private static ClientChunk cutoutClientChunk;
    private static Chunk chunk;
    public static Vector3f direction;
    public static void init(){
        skyRenderer=new SkyRenderer();
        skyRenderer.init();
        shaderCompiler=new ShaderCompiler("/assets/shader/vertex.glsl", "/assets/shader/fragment.glsl");
        shader=shaderCompiler.createShaderProgram(shaderCompiler.vertexShaderSrc, shaderCompiler.fragmentShaderSrc);
        chunk=new Chunk(0,0);
        worldGenerator = new WorldGenerator(chunk);
        clientChunk = new ClientChunk(chunk, MaterialRenderLayerMap.SOLIDE);
        cutoutClientChunk = new ClientChunk(chunk, MaterialRenderLayerMap.CUTOUT);
        locModel= GL20.glGetUniformLocation(shader, "model");
        locView=GL20.glGetUniformLocation(shader, "view");
        locProj=GL20.glGetUniformLocation(shader, "projection");
        locCutout=GL20.glGetUniformLocation(shader, "cutout");
        atlasTexture= net.terraimmerse.client.blaze3d.TextureLoader.loadTexture("/assets/textures/atlas.png");
        textureLoc=GL20.glGetUniformLocation(shader, "tex");
        model = new Matrix4f();
        model.identity().translate(0.0F, 0.0F, 0.0F);
        view = new Matrix4f();
        view.identity().lookAt(
                new Vector3f(0.0F, 0.0F, 3.0F),
                new Vector3f(0.0F, 0.0F, 0.0F),
                new Vector3f(0.0F, 1.0F, 0.0F)
        );
        projection = new Matrix4f();
        projection.identity().perspective((float)Math.toRadians(45), (float) TerraImmerse.width /(float) TerraImmerse.height, 0.1F, 2000.0F);
    }
    public static void drawScene(){
        skyRenderer.render();
        GL20.glUseProgram(shader);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasTexture);
        GL20.glUniform1i(textureLoc, 0);
        GL20.glUniform1f(skyRenderer.locLightAngle, skyRenderer.lightAngle);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(locModel, false, model.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(locView, false, view.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(locProj, false, projection.get(stack.mallocFloat(16)));
        }
        GL20.glUniform1i(locCutout, 0);
        GL30.glBindVertexArray(clientChunk.getVao());
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, (int)clientChunk.getVertices().length / 8);
        GL30.glBindVertexArray(0);
        GL20.glUniform1i(locCutout, 1);
        GL30.glBindVertexArray(cutoutClientChunk.getVao());
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, (int)cutoutClientChunk.getVertices().length / 8);
        GL30.glBindVertexArray(0);
    }
}
