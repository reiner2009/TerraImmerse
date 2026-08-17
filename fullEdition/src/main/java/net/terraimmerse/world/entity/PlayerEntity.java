package net.terraimmerse.world.entity;

import org.joml.Vector3f;

public class PlayerEntity {
    public Vector3f pos;
    public float yaw;
    public float pitch;
    public PlayerEntity(){
        this.pos = new Vector3f(0.0F,0.0F,0.0F);
        this.yaw=0.0F;
        this.pitch=0.0F;
    }
    public void move(float x, float y, float z){
        this.pos.x+=x;
        this.pos.y+=y;
        this.pos.z+=z;
    }
    public void setPos(float x, float y, float z){
        this.pos = new Vector3f(x, y, z);
    }
    public Vector3f getPos(){
        return this.pos;
    }
}
