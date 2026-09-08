package net.terraimmerse.client.render.world;

import net.terraimmerse.client.render.MaterialRenderLayerMap;
import net.terraimmerse.client.render.RenderThread;
import net.terraimmerse.client.render.ShaderCompiler;
import net.terraimmerse.client.render.sky.SkyRenderer;
import net.terraimmerse.world.generator.WorldGenerator;
import net.terraimmerse.client.manager.TextureManager;
import net.terraimmerse.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldRenderer {
    private static final Logger LOGGER= LoggerFactory.getLogger(WorldGenerator.class);
    private static SkyRenderer skyRenderer;
    private static ShaderCompiler shaderCompiler;
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
    private static ClientLevel clientLevel;
    private static ClientLevel cutoutTextureClientLevel;
    private static Level level;
    public static Vector3f direction;
    public static void init(){
        shaderCompiler=new ShaderCompiler("/assets/shader/vertex.glsl", "/assets/shader/fragment.glsl", "WorldShader");
        shader=shaderCompiler.createShaderProgram(shaderCompiler.vertexShaderSrc, shaderCompiler.fragmentShaderSrc);
        skyRenderer=new SkyRenderer();
        skyRenderer.init();
        level =new Level();
        new WorldGenerator(level);
        LOGGER.info("Loading terrian...");
        clientLevel = new ClientLevel(level, MaterialRenderLayerMap.SOLIDE);
        cutoutTextureClientLevel = new ClientLevel(level, MaterialRenderLayerMap.CUTOUT);
        locModel=GL20.glGetUniformLocation(shader, "model");
        locView=GL20.glGetUniformLocation(shader, "view");
        locProj=GL20.glGetUniformLocation(shader, "projection");
        locCutout=GL20.glGetUniformLocation(shader, "cutout");
        atlasTexture=TextureManager.textures.get("/assets/textures/atlas.png");
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
        projection.identity().perspective(
                (float)Math.toRadians(45),
                (float) RenderThread.width / (float)RenderThread.height,
                0.1F,
                2000.0F
        );
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
        GL30.glBindVertexArray(clientLevel.getVao());
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, (int) clientLevel.getVertices().length / 8);
        GL30.glBindVertexArray(0);
        GL20.glUniform1i(locCutout, 1);
        GL30.glBindVertexArray(cutoutTextureClientLevel.getVao());
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, (int) cutoutTextureClientLevel.getVertices().length / 8);
        GL30.glBindVertexArray(0);
    }
}
