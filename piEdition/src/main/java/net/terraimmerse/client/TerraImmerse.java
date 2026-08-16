package net.terraimmerse.client;

import net.terraimmerse.client.blaze3d.ShaderCompiler;
import net.terraimmerse.client.blaze3d.TextureLoader;
import net.terraimmerse.client.blaze3d.sky.SunVBO;
import net.terraimmerse.client.blaze3d.world.ClientChunk;
import net.terraimmerse.world.Generator;
import net.terraimmerse.world.chunk.Chunk;
import net.terraimmerse.world.entity.PlayerEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

public class TerraImmerse {
    private static Chunk chunk;
    private static ClientChunk clientChunk;
    private static PlayerEntity playerEntity;
    private static ShaderCompiler shaderCompiler;
    private static ShaderCompiler sunShaderCompiler;
    private static Generator worldGenerator;
    public static TextureManager textureManager;
    private static int shader;
    private static int sunShader;
    private static int locModel;
    private static int locView;
    private static int locProj;
    private static int locLightAngle;
    private static int sunLocModel;
    private static int sunLocView;
    private static int sunLocProj;
    private static SunVBO sunVBO;
    private static int atlasTexture;
    private static int textureLoc;
    private static Matrix4f model;
    private static Matrix4f view;
    private static Matrix4f projection;
    private static Matrix4f sunModel;
    private static Matrix4f sunView;
    private static float sensity;
    private static float speed;
    private static float sunAngle;
    private static float brightness;
    private static float bright;
    private static float dark;
    private static float t;
    private static long window;
    private static int width;
    private static int height;
    private static double lastX;
    private static double lastY;
    private static boolean firstMouse = true;
    private static float yaw;
    private static float pitch;
    private static Vector3f direction;
    private static float move_x;
    private static float move_z;
    private static float move_y;
    private static float dx;
    private static float dz;
    private static float dx_side;
    private static float dz_side;
    private static float lightAngle;
    private static int radius;
    public static void init(){  GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err) ); 
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Falied to load GLFW");
        }
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
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
        sunShaderCompiler=new ShaderCompiler("/assets/shader/sun.vert", "/assets/shader/sun.frag");
        shader=shaderCompiler.createShaderProgram(shaderCompiler.vertexShaderSrc, shaderCompiler.fragmentShaderSrc);
        sunShader=sunShaderCompiler.createShaderProgram(sunShaderCompiler.vertexShaderSrc, sunShaderCompiler.fragmentShaderSrc);
        locModel= GL20.glGetUniformLocation(shader, "model");
        locView=GL20.glGetUniformLocation(shader, "view");
        locProj=GL20.glGetUniformLocation(shader, "projection");
        locLightAngle=GL20.glGetUniformLocation(shader, "lightAngle");
        sunLocModel= GL20.glGetUniformLocation(sunShader, "model");
        sunLocView = GL20.glGetUniformLocation(sunShader, "view");
        sunLocProj = GL20.glGetUniformLocation(sunShader, "projection");
        sunVBO=new SunVBO();
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
        sensity=0.002F;
        speed=0.1F;
        brightness=0.0F;
        radius=10;
    }
    public void run() {
        init();
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
            handleInput();
            drawScene();
            GLFW.glfwSwapBuffers(window);
        }
    }
    private static void tickLight(){
        if (sunAngle>=360.0F){
            sunAngle=0.0F;
        }
        sunAngle+=0.1F;
        lightAngle=(float) Math.toRadians(sunAngle);
    }
    private static void handleInput(){
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        if (firstMouse) {
            lastX = xpos[0];
            lastY = ypos[0];
            firstMouse = false;
        }
        double deltaX = xpos[0] - lastX;
        double deltaY = lastY - ypos[0];
        lastX = xpos[0];
        lastY = ypos[0];
        playerEntity.yaw -= deltaX * sensity;
        playerEntity.pitch += deltaY * sensity;
        playerEntity.pitch=Math.max(-1.5F, Math.min(1.5F, playerEntity.pitch));
        yaw = playerEntity.yaw;
        pitch = playerEntity.pitch;
        Vector3f front = new Vector3f();
        front.x = (float)(Math.cos(pitch) * Math.sin(yaw));
        front.y = (float)Math.sin(pitch);
        front.z = (float)(Math.cos(pitch) * Math.cos(yaw));
        front.normalize();
        direction = new Vector3f(playerEntity.getPos()).add(front);
        view.identity().lookAt(
                playerEntity.getPos(),
                direction,
                new Vector3f(0, 1, 0)
        );
        move_x=0;
        move_y=0;
        move_z=0;
        dx = (float) Math.sin(playerEntity.yaw);
        dz = (float) Math.cos(playerEntity.yaw);
        dx_side = (float) Math.sin(playerEntity.yaw-Math.toRadians(90));
        dz_side = (float) Math.cos(playerEntity.yaw-Math.toRadians(90));
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
            move_x += dx * speed;
            move_z += dz * speed;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS){
            move_x -= dx * speed;
            move_z -= dz * speed;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS){
            move_x -= dx_side * speed;
            move_z -= dz_side * speed;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS){
            move_x += dx_side * speed;
            move_z += dz_side * speed;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS){
            move_y+=speed;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS){
            move_y-=speed;
        }
        playerEntity.move(move_x, move_y, move_z);
    }
    private static void calculateSunPos(){
        sunModel = new Matrix4f().identity();
        sunModel.rotate(lightAngle, new Vector3f(1, 0, 0));
        sunModel.translate(0, 0, -radius);
    }
    private static void renderSun(){
        GL20.glUseProgram(sunShader);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(sunLocModel, false, sunModel.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(sunLocView, false, sunView.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(sunLocProj, false, projection.get(stack.mallocFloat(16)));
        }
        GL20.glUniform3f(GL20.glGetUniformLocation(sunShader,"sunColor"),1.0F,0.9F,0.5F);
        GL20.glUniform1f(GL20.glGetUniformLocation(sunShader,"brightness"),2.0F);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sunVBO.getSunTexture());
        GL20.glUniform1i(GL20.glGetUniformLocation(sunShader,"sunTexture"),0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, sunVBO.getVbo());
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0,3,GL11.GL_FLOAT,false,5 * Float.BYTES,0L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1,2,GL11.GL_FLOAT,false,5 * Float.BYTES,3L * Float.BYTES);
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    private static void drawScene(){
        tickLight();
        brightness=calculateLight(sunAngle);
        GL11.glClearColor(0.2F*brightness, 0.4F*brightness, 0.8F*brightness, 1.0F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        sunView=new Matrix4f(new Matrix3f(view));
        calculateSunPos();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        renderSun();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL20.glUseProgram(shader);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasTexture);
        GL20.glUniform1i(textureLoc, 0);
        GL20.glUniform1f(locLightAngle, lightAngle);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(locModel, false, model.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(locView, false, view.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(locProj, false, projection.get(stack.mallocFloat(16)));
        }
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, clientChunk.getVbo());
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0,3,GL11.GL_FLOAT,false,8 * Float.BYTES,0L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1,2,GL11.GL_FLOAT,false,8 * Float.BYTES,3L * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);
        GL20.glVertexAttribPointer(2,3,GL11.GL_FLOAT,false,8 * Float.BYTES,5L * Float.BYTES);
        GL20.glDrawArrays(GL11.GL_TRIANGLES,0,clientChunk.getVertices().length / 8);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    public static float calculateLight(float w) {
        dark = 0.1F;
        bright = 1.0F;
        if (w >= 370.0F){
            t = (w - 370.0F) / (390.0F - 370.0F);
            return dark + t * (bright - dark);
        }
        if (0.0F <= w && w <= 10.0F) {
            t = w / 10.0F;
            return dark + t * (bright - dark);
        }
        if (10.0F < w && w < 150.0F) {
            return bright;
        }
        if (150.0F <= w && w <= 170.0F) {
            t = (w - 150.0F) / (170.0F - 150.0F);
            return bright - t * (bright - dark);
        }
        return dark;
    }
}
