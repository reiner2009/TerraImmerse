package net.terraimmerse.world.level;

import net.terraimmerse.client.render.world.chunk.Chunk;
import net.terraimmerse.client.render.world.chunk.ChunkPos;
import net.terraimmerse.world.material.MaterialPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Level {
    private static final Logger LOGGER = LoggerFactory.getLogger(Level.class);
    private HashMap<ChunkPos, Chunk> material;
    public Level(){
        this.material = new HashMap<>();
        LOGGER.info("Created new level");
    }

    public void setMaterial(MaterialPos materialPos, String id){
        this.setMaterial(materialPos.x, materialPos.y, materialPos.z, id);
    }

    public void setMaterial(int x, int y, int z, String id) {
        int chunkX = Chunk.materialPosToChunkPos(x);
        int chunkZ = Chunk.materialPosToChunkPos(z);
        int localX = Math.floorMod(x, 16);
        int localZ = Math.floorMod(z, 16);
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        Chunk chunk = this.material.computeIfAbsent(chunkPos, pos -> new Chunk());
        chunk.setMaterial(localX, y, localZ, id);
    }

    public String getMaterial(MaterialPos materialPos){
        return this.getMaterial(materialPos.x, materialPos.y, materialPos.z);
    }

    public String getMaterial(int x, int y, int z) {
        int chunkX = Chunk.materialPosToChunkPos(x);
        int chunkZ = Chunk.materialPosToChunkPos(z);
        int localX = Math.floorMod(x, 16);
        int localZ = Math.floorMod(z, 16);
        Chunk chunk = this.material.get(new ChunkPos(chunkX, chunkZ));
        if (chunk == null) {
            return "air";
        }
        return chunk.getMaterial(localX, y, localZ);
    }


    public Map<MaterialPos, String> getLevel(){
        Map<MaterialPos, String> map = new HashMap<>();
        for(var entry : this.material.entrySet()) {
            Chunk chunk=entry.getValue();
            int chunkX=entry.getKey().x;
            int chunkZ=entry.getKey().z;
            for(var chunkEntry : chunk.getChunk().entrySet()){
                MaterialPos materialPos=chunkEntry.getKey();
                String id = chunkEntry.getValue();
                if(!id.equals("air")) {
                    map.put(new MaterialPos(chunkX * 16 + materialPos.x, materialPos.y, chunkZ * 16 + materialPos.z), id);
                }
            }
        }
        return map;
    }
}
