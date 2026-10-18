package net.terraimmerse.world.entity;

import net.terraimmerse.world.material.MaterialPos;
import net.terraimmerse.world.phys.AABB;
import net.terraimmerse.client.TerraImmerse;
import net.terraimmerse.world.level.Level;
import net.terraimmerse.world.material.MaterialPos;
import org.joml.Vector3f;

import java.util.List;

public class PlayerEntity {
    public Vector3f pos;
    public float yaw;
    public float pitch;
    private MaterialPos materialPos;
    private String[] noCollision={"flower", "high_grass", "water", "air"};
    public PlayerEntity(){
        this.pos = new Vector3f(0.0F,0.0F,0.0F);
        this.yaw=0.0F;
        this.pitch=0.0F;
    }
    public void move(float moveX, float moveY, float moveZ){
        Level level = TerraImmerse.getLevel();
        int mx = this.getMaterialPos().x;
        int my = this.getMaterialPos().y;
        int mz = this.getMaterialPos().z;
        boolean canMoveX = true;
        boolean canMoveY = true;
        boolean canMoveZ = true;
        for(int ix=mx-2; ix<=mx+2; ix++){
            for(int iy=my-3; iy<=my+2; iy++){
                for(int iz=mz-2; iz<=mz+2; iz++){
                    float x=(float)ix;
                    float y=(float)iy;
                    float z=(float)iz;
                    if(this.getBoundingBox(this.pos.x + moveX, this.pos.y, this.pos.z)
                    .intersects(new AABB(x, y, z, x + 1, y + 1, z + 1))
                     && !List.of(noCollision).contains(level.getMaterial((int)Math.floor(ix+moveX), (int)Math.floor(iy), (int)Math.floor(iz))))
                     {
                        canMoveX=false;
                    }
                    if(this.getBoundingBox(this.pos.x, this.pos.y + moveY, this.pos.z)
                    .intersects(new AABB(x, y - 1, z, x + 1, y, z + 1))
                    && !List.of(noCollision).contains(level.getMaterial((int)Math.floor(ix), (int)Math.floor(iy+moveY), (int)Math.floor(iz))))
                    {
                        canMoveY=false;
                    }
                    if(this.getBoundingBox(this.pos.x, this.pos.y, this.pos.z + moveZ)
                    .intersects(new AABB(x, y, z, x + 1, y + 1, z + 1))
                    && !List.of(noCollision).contains(level.getMaterial((int)Math.floor(ix), (int)Math.floor(iy), (int)Math.floor(iz+moveZ))))
                    {
                        canMoveZ=false;
                    }
                }
            }
        }
        if(canMoveX){
            this.pos.x+=moveX;
        }
        if(canMoveY){
            this.pos.y+=moveY;
        }
        if(canMoveZ){
            this.pos.z+=moveZ;
        }
    }
    public Vector3f getPos(){
        return this.pos;
    }
    public MaterialPos getMaterialPos(){
        return new MaterialPos((int)Math.floor(this.pos.x - 0.5F),(int)Math.floor(this.pos.y - 0.5F),(int)Math.floor(this.pos.z - 0.5F));
    }
    public AABB getBoundingBox(float xOffset, float yOffset, float zOffset){
        return new AABB(-0.3F + xOffset, -1.5F + yOffset, -0.3F + zOffset, 0.3F + xOffset, 0.3F + yOffset, 0.3F + zOffset);
    }
    public void spawnAtMaterial(MaterialPos materialPos){
        this.pos.x= materialPos.x + 0.5F;
        this.pos.y= materialPos.y + 0.5F;
        this.pos.z= materialPos.z + 0.5F;
    }
}
