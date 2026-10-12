package net.terraimmerse.client.render.sky;

import net.terraimmerse.client.render.world.WorldRenderer;
import org.lwjgl.opengl.GL20;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SunRenderer extends AbstractSkyObject{
    public static float sunAngle;
    public static float lightAngle;
    public SunRenderer(){
        super("/assets/textures/sky/sun.png", sunVertices, 10);
        sunAngle=349;
    }
    private static float[] sunVertices = {
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            -1.0F,-1.0F,0.0F,0.0F,0.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            -1.0F,1.0F,0.0F,0.0F,1.0F,
            1.0F,-1.0F,0.0F,1.0F,0.0F,
            1.0F,1.0F,0.0F,1.0F,1.0F
    };
    private void calculateSunPos(){
        this.skyObjectModel = new Matrix4f().identity();
        this.skyObjectModel.rotate(lightAngle, new Vector3f(1, 0, 0));
        this.skyObjectModel.translate(0, 0, -this.skyObjectRadius);
    }
    public void render(){
        this.calculateSunPos();
        super.render();
    }
    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    private static float smoothstep(float t) {
        return t * t * (3.0F - 2.0F * t);
    }
    public void calculateSunColor(float w) {
        float r;
        float g;
        float b;
        if (w <= 20.0F) {
            float t = smoothstep(w / 20.0F);
            r = 1.0F;
            g = 0.70F;
            b = 0.30F;
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
        else if (w < 200.0f){
            float t = smoothstep((w -190.0F) / 10.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(0.32F, 1.0F, t);
            b = lerp(0.08F, 1.0F, t);
        }
        else if (w <= 360.0F){
            float t = smoothstep((w - 200.0F) / 160.0F);
            r = lerp(1.0F, 1.0F, t);
            g = lerp(1.0F, 0.70F, t);
            b = lerp(1.0F, 0.30F, t);
        }
        else{
            r = 1.0F;
            g = 1.0F;
            b = 1.0F;
        }
        this.setColor(r, g, b);
    }
}
