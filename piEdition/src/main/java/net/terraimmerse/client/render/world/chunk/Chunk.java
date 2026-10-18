package net.terraimmerse.client.render.world.chunk;

import net.terraimmerse.world.material.MaterialPos;

import java.util.HashMap;

public class Chunk {
    private HashMap<MaterialPos, String> chunk;
    public Chunk(){
        this.chunk = new HashMap<>();
    }
    public void setMaterial(int x, int y, int z, String id){
        if(x > 15 || x < 0 || z > 15 || z < 0){
            throw new ChunkOverflowException("x:"+x+",y:"+y+",z:"+z+": Material outside of chunkSize");
        }
        this.chunk.put(new MaterialPos(x, y, z), id);
    }
    public String getMaterial(int x, int y, int z){
        if(x > 15 || x < 0 || z > 15 || z < 0){
            throw new ChunkOverflowException("x:"+x+",y:"+y+",z:"+z+": Material outside of chunkSize");
        }
        return this.chunk.getOrDefault(new MaterialPos(x, y, z), "air");
    }
    public static int materialPosToChunkPos(int pos){
        return Math.floorDiv(pos, 16);
    }

    public HashMap<MaterialPos, String> getChunk(){
        return this.chunk;
    }
}
