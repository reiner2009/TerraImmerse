package net.terraimmerse.client.blaze3d.sky;

import net.terraimmerse.client.blaze3d.TextureLoader;
import net.terraimmerse.client.blaze3d.ShaderCompiler;
import net.terraimmerse.client.TerraImmerse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class SkyRenderer {
    private static int sunLocModel;
    private static int sunLocView;
    private static int sunLocProj;
    private static int sunLocTexture;
    private static int sunLocColor;
    private static int sunLocBrightness;
    private static int sunShader;
    public static int locLightAngle;
    private static int radius;
    private static float sunAngle;
    private static float brightness;
    private static float bright;
    private static float dark;
    private static float t;
    private static float sunR;
    private static float sunG;
    private static float sunB;
    public static float lightAngle;
    private static ShaderCompiler sunShaderCompiler;
    private static Matrix4f sunModel;
    private static Matrix4f sunView;
    private static float[] sunVertices = {
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            -1.0F,-1.0F,0.0F,0.0F,0.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            1.0F,1.0F,0.0F,1.0F,1.0F
    };
    private static int vbo;
    private static int vao;
    private static int sunTexture;
    public SkyRenderer(){
        sunTexture = TextureLoader.loadTexture("/assets/textures/sky/sun.png");
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, sunVertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*4, 0L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false, 5*4, 12L);
        GL20.glEnableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    public static void init(){
        sunShaderCompiler=new ShaderCompiler("/assets/shader/sun.vert", "/assets/shader/sun.frag");
        sunShader=sunShaderCompiler.createShaderProgram(sunShaderCompiler.vertexShaderSrc, sunShaderCompiler.fragmentShaderSrc);
        sunLocModel= GL20.glGetUniformLocation(sunShader, "model");
        sunLocView = GL20.glGetUniformLocation(sunShader, "view");
        sunLocProj = GL20.glGetUniformLocation(sunShader, "projection");
        sunLocTexture = GL20.glGetUniformLocation(sunShader, "sunTexture");
        sunLocColor = GL20.glGetUniformLocation(sunShader, "sunColor");
        sunLocBrightness = GL20.glGetUniformLocation(sunShader, "brightness");
        locLightAngle=GL20.glGetUniformLocation(TerraImmerse.shader, "lightAngle");
        sunR=1.0F;
        sunG=1.0F;
        sunB=1.0F;
        brightness=0.0F;
        radius=10;
    }
    public static void render(){
        tickLight();
        brightness=calculateLight(sunAngle);
        GL11.glClearColor(0.2F*brightness, 0.4F*brightness, 0.8F*brightness, 1.0F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        sunView=new Matrix4f(new Matrix3f(TerraImmerse.view));
        calculateSunPos();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(sunShader);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(sunLocModel, false, sunModel.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(sunLocView, false, sunView.get(stack.mallocFloat(16)));
            GL20.glUniformMatrix4fv(sunLocProj, false, TerraImmerse.projection.get(stack.mallocFloat(16)));
        }
        GL20.glUniform1f(sunLocBrightness,2.0F);
        GL20.glActiveTexture(GL20.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sunTexture);
        GL20.glUniform1i(sunLocTexture,0);
        GL20.glUniform3f(sunLocColor,sunR,sunG,sunB);
        GL30.glBindVertexArray(vao);
        GL20.glDrawArrays(GL11.GL_TRIANGLES,0,6);
        GL30.glBindVertexArray(0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }
    private static void tickLight(){
        if (sunAngle>=360.0F){
            sunAngle=0.0F;
        }
        sunAngle+=0.1F;
        lightAngle=(float) Math.toRadians(sunAngle);
        calculateSunColor(sunAngle);
    }
    private static void calculateSunPos(){
        sunModel = new Matrix4f().identity();
        sunModel.rotate(lightAngle, new Vector3f(1, 0, 0));
        sunModel.translate(0, 0, -radius);
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
    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    private static float smoothstep(float t) {
        return t * t * (3.0F - 2.0F * t);
    }
    public static void calculateSunColor(float w) {
        float r;
        float g;
        float b;
        if (w < 20.0F) {
            float t = smoothstep(w / 20.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.25F, 0.70F, t);
            b = lerp(0.05F, 0.30F, t);
        }
        else if (w < 70.0F) {
            float t = smoothstep((w - 20.0F) / 50.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.70F, 0.88F, t);
            b = lerp(0.30F, 0.60F, t);
        }
        else if (w < 110.0F) {
            float t = smoothstep((w - 70.0F) / 40.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.88F, 0.98F, t);
            b = lerp(0.60F, 0.75F, t);
        }
        else if (w < 155.0F) {
            float t = smoothstep((w - 110.0F) / 45.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.98F, 0.78F, t);
            b = lerp(0.75F, 0.45F, t);
        }
        else if (w < 190.0F) {
            float t = smoothstep((w - 155.0F) / 25.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.78F, 0.32F, t);
            b = lerp(0.45F, 0.08F, t);
        }
        else {
            r = 1.0F;
            g = 1.0F;
            b = 1.0F;
        }
        sunR = r;
        sunG = g;
        sunB = b;
    }
}
