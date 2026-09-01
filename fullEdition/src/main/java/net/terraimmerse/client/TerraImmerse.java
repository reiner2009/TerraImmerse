package net.terraimmerse.client;

import net.terraimmerse.client.blaze3d.ShaderCompiler;
import net.terraimmerse.client.blaze3d.TextureLoader;
import net.terraimmerse.client.blaze3d.sky.SkyRenderer;
import net.terraimmerse.client.blaze3d.world.ClientChunk;
import net.terraimmerse.world.Generator;
import net.terraimmerse.world.ServerTickThread;
import net.terraimmerse.world.chunk.Chunk;
import net.terraimmerse.world.entity.PlayerEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class TerraImmerse {
    private static Chunk chunk;
    private static ClientChunk clientChunk;
    public static PlayerEntity playerEntity;
    private static ShaderCompiler shaderCompiler;
    private static Generator worldGenerator;
    private static SkyRenderer skyRenderer;
    public static TextureManager textureManager;
    public static int shader;
    private static int locModel;
    private static int locView;
    private static int locProj;
    private static int atlasTexture;
    private static int textureLoc;
    private static Matrix4f model;
    public static Matrix4f view;
    public static Matrix4f projection;
    public static long window;
    private static int width;
    private static int height;
    public static Vector3f direction;
    public static void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Falied to load GLFW");
        }
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        width=videoMode.width();
        height=videoMode.height();
        window = GLFW.glfwCreateWindow(
                videoMode.width(),
                videoMode.height(),
                "TerraImmerse-1.0.SNAPSHOT",
                monitor,
                0
        );
        if (window == 0) {
            throw new RuntimeException("Falied to create window");
        }
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glFrontFace(GL11.GL_CCW);
        GL11.glViewport(0, 0, width, height);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);
        textureManager=new TextureManager();
        chunk=new Chunk(0,0);
        worldGenerator = new Generator(chunk);
        playerEntity = new PlayerEntity();
        clientChunk = new ClientChunk(chunk);
        shaderCompiler=new ShaderCompiler("/assets/shader/vertex.glsl", "/assets/shader/fragment.glsl");
        shader=shaderCompiler.createShaderProgram(shaderCompiler.vertexShaderSrc, shaderCompiler.fragmentShaderSrc);
        locModel= GL20.glGetUniformLocation(shader, "model");
        locView=GL20.glGetUniformLocation(shader, "view");
        locProj=GL20.glGetUniformLocation(shader, "projection");
        skyRenderer=new SkyRenderer();
        atlasTexture=TextureLoader.loadTexture("/assets/textures/atlas.png");
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
        projection.identity().perspective((float)Math.toRadians(45), (float) width/(float) height, 0.1F, 2000.0F);
        InputHandler.init();
        skyRenderer.init();
    }
    public void run() {
        init();
        ServerTickThread.movementThread.setDaemon(true);
        ServerTickThread.movementThread.start();
        loop();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
    public static void main(String[] args){
        new TerraImmerse().run();
    }
    public static void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();
            InputHandler.handleInput();
            drawScene();
            GLFW.glfwSwapBuffers(window);
        }
    }
    
    private static void drawScene(){
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
        GL30.glBindVertexArray(clientChunk.getVao());
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, (int)clientChunk.getVertices().length / 8);
        GL30.glBindVertexArray(0);
    }
}
