package net.terraimmerse.client;

import org.lwjgl.glfw.GLFW;
import org.joml.Vector3f;

public class InputHandler {
    private static float move_x;
    private static float move_z;
    private static float move_y;
    private static float dx;
    private static float dz;
    private static float dx_side;
    private static float dz_side;
    private static boolean firstMouse = true;
    private static double lastX;
    private static double lastY;
    private static float sensity;
    private static float speed;
    private static float yaw;
    private static float pitch;
    private static boolean wPressed;
    private static boolean aPressed;
    private static boolean sPressed;
    private static boolean dPressed;
    private static boolean shiftPressed;
    private static boolean spacePressed;
    public static void init(){
        sensity=0.002F;
        speed=0.3F;
        wPressed=false;
        aPressed=false;
        sPressed=false;
        dPressed=false;
        shiftPressed=false;
        spacePressed=false;
    }
    public static void handleInput(){
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(TerraImmerse.window, xpos, ypos);
        if (firstMouse) {
            lastX = xpos[0];
            lastY = ypos[0];
            firstMouse = false;
        }
        double deltaX = xpos[0] - lastX;
        double deltaY = lastY - ypos[0];
        lastX = xpos[0];
        lastY = ypos[0];
        TerraImmerse.playerEntity.yaw -= deltaX * sensity;
        TerraImmerse.playerEntity.pitch += deltaY * sensity;
        TerraImmerse.playerEntity.pitch=Math.max(-1.5F, Math.min(1.5F,  TerraImmerse.playerEntity.pitch));
        yaw = TerraImmerse.playerEntity.yaw;
        pitch = TerraImmerse.playerEntity.pitch;
        Vector3f front = new Vector3f();
        front.x = (float)(Math.cos(pitch) * Math.sin(yaw));
        front.y = (float)Math.sin(pitch);
        front.z = (float)(Math.cos(pitch) * Math.cos(yaw));
        front.normalize();
        TerraImmerse.direction = new Vector3f(TerraImmerse.playerEntity.getPos()).add(front);
        TerraImmerse.view.identity().lookAt(
                TerraImmerse.playerEntity.getPos(),
                TerraImmerse.direction,
                new Vector3f(0, 1, 0)
        );
        move_x=0;
        move_y=0;
        move_z=0;
        dx = (float) Math.sin(TerraImmerse.playerEntity.yaw);
        dz = (float) Math.cos(TerraImmerse.playerEntity.yaw);
        dx_side = (float) Math.sin(TerraImmerse.playerEntity.yaw-Math.toRadians(90));
        dz_side = (float) Math.cos(TerraImmerse.playerEntity.yaw-Math.toRadians(90));
        wPressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS;
        sPressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS;
        aPressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS;
        dPressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS;
        spacePressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
        shiftPressed=GLFW.glfwGetKey(TerraImmerse.window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
    }
    public static void tickMovement(){
        if(wPressed){
            move_x += dx * speed;
            move_z += dz * speed;
        }
        if(sPressed){
            move_x -= dx * speed;
            move_z -= dz * speed;
        }
        if(aPressed){
            move_x -= dx_side * speed;
            move_z -= dz_side * speed;
        }
        if(dPressed){
            move_x += dx_side * speed;
            move_z += dz_side * speed;
        }
        if(spacePressed){
            move_y+=speed;
        }
        if(shiftPressed){
            move_y-=speed;
        }
        TerraImmerse.playerEntity.move(move_x, move_y, move_z);
    }
}