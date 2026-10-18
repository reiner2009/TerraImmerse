package net.terraimmerse.world.entity;

import net.terraimmerse.world.material.MaterialPos;
import net.terraimmerse.world.phys.AABB;
import org.joml.Vector3f;

public class PlayerEntity {
    public Vector3f pos;
    public float yaw;
    public float pitch;
    private AABB boundingBox;
    public PlayerEntity(){
        this.pos = new Vector3f(0.0F,0.0F,0.0F);
        this.yaw=0.0F;
        this.pitch=0.0F;
        this.boundingBox=new AABB(-0.3F, 0.0F, -0.3F, 0.3F, 1.8F, 0.3F);
    }
    public void move(float x, float y, float z){
        this.pos.x+=x;
        this.pos.y+=y;
        this.pos.z+=z;
    }
    public Vector3f getPos(){
        return this.pos;
    }
    public AABB getBoundingBox(){
        return this.boundingBox;
    }
    public void spawnAtMaterial(MaterialPos materialPos){
        this.pos.x= materialPos.x;
        this.pos.y= materialPos.y + 0.5F;
        this.pos.z= materialPos.z;
    }
}
