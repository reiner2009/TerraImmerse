package net.terraimmerse.client;

import net.terraimmerse.client.blaze3d.WorldRenderer;
import net.terraimmerse.world.ServerTickThread;
import net.terraimmerse.world.entity.PlayerEntity;
import net.terraimmerse.world.generator.feature.Features;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class TerraImmerse {
    public static PlayerEntity playerEntity;
    public static TextureManager textureManager;
    public static WorldRenderer worldRenderer;
    public static long window;
    public static int width;
    public static int height;
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
                "TerraImmerse-classic-0.3.0",
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
        playerEntity = new PlayerEntity();
        InputHandler.init();
    }
    public void run() {
        init();
        ClientInitializer.onInitializeClient();
        Features.initFeatures();
        worldRenderer.init();
        ServerTickThread.movementThread.setDaemon(true);
        ServerTickThread.movementThread.start();
        ServerTickThread.serverTickThread.setDaemon(true);
        ServerTickThread.serverTickThread.start();
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
            WorldRenderer.drawScene();
            GLFW.glfwSwapBuffers(window);
        }
    }
}
