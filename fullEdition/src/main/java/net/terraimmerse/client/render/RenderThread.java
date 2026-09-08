package net.terraimmerse.client.render;

import net.terraimmerse.client.input.InputHandler;
import net.terraimmerse.client.render.world.WorldRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenderThread.class);
    public static long window;
    public static int width;
    public static int height;
    public RenderThread(){
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Falied to load GLFW");
        }
        long monitor = GLFW.glfwGetPrimaryMonitor();
        LOGGER.info("Created monitor");
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
        LOGGER.info("Initialized videoMode");
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        LOGGER.info("OpenGL version: 3.3");
        width=videoMode.width();
        height=videoMode.height();
        window = GLFW.glfwCreateWindow(
                videoMode.width(),
                videoMode.height(),
                "TerraImmerse-classic-0.4.0",
                monitor,
                0
        );
        if (window == 0) {
            throw new RuntimeException("Falied to create window");
        } else {
            LOGGER.info("Created window");
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
        LOGGER.info("Created OpenGL context");
    }
    public static void render() {
        while (!GLFW.glfwWindowShouldClose(RenderThread.window)) {
            GLFW.glfwPollEvents();
            InputHandler.handleInput();
            WorldRenderer.drawScene();
            GLFW.glfwSwapBuffers(RenderThread.window);
        }
    }
    public static void shutdown(){
        GLFW.glfwDestroyWindow(RenderThread.window);
        GLFW.glfwTerminate();
    }
}
