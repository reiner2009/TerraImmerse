package net.terraimmerse.world.chunk;

import net.terraimmerse.core.MaterialPos;

import java.util.HashMap;
import java.util.Map;

public class Chunk {
    private final long x;
    private final long z;
    private Map<MaterialPos, String> material;
    public Chunk(long x_, long z_){
        this.x=x_;
        this.z=z_;
        this.material = new HashMap<>();
    }
    public void setMaterial(int x, int y, int z, String id){
        if ("air".equals(id)){
            this.material.remove(new MaterialPos(x,y,z));
        }
        else {
            this.material.put(new MaterialPos(x,y,z), id);
        }
    }
    public String getMaterial(int x, int y, int z){
        return this.material.getOrDefault(new MaterialPos(x, y, z), "air");
    }
    public Map<MaterialPos, String> getChunk(){
        return this.material;
    }
}
