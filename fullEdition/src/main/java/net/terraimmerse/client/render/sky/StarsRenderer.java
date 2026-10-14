package net.terraimmerse.client.render.sky;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Random;

public class StarsRenderer extends AbstractSkyObject{
    public StarsRenderer(){
        super("/assets/textures/sky/sun.png", generateStars() ,10);
        this.skyObjectModel = new Matrix4f().identity();
        this.skyObjectModel.rotate(90.0F, new Vector3f(1, 0, 0));
        this.skyObjectModel.translate(0, 0, -this.skyObjectRadius);
    }

    private static float[] generateStars() {
        float[] vertices = {};
        for(int i = 0; i < 500; i++){
            Random random=new Random();
            float rotx=random.nextFloat(360);
            float roty=random.nextFloat(360);
            float rotz=random.nextFloat(360);
            for(float vertex : getVertices(rotx, roty, rotz)){
                vertices = Arrays.copyOf(vertices, vertices.length + 3);
                vertices[vertices.length - 3] = vertex;
            }
        }
        return vertices;
    }

    private static float[] getVertices(float rotx, float roty, float rotz) {
        float[] vertices = {-1.0F, 1.0F, 0.0F, 0.0F, 1.0F,
                -1.0F, -1.0F, 0.0F, 0.0F, 0.0F,
                1.0F, -1.0F, 0.0F, 1.0F, 0.0F,
                -1.0F, 1.0F, 0.0F, 0.0F, 1.0F,
                1.0F, -1.0F, 0.0F, 1.0F, 0.0F,
                1.0F, 1.0F, 0.0F, 1.0F, 1.0F};
        return vertices;
    }
}
