package net.terraimmerse.world.level;

import net.terraimmerse.core.MaterialPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Level {
    private static final Logger LOGGER = LoggerFactory.getLogger(Level.class);
    private Map<MaterialPos, String> material;
    public Level(){
        this.material = new HashMap<>();
        LOGGER.info("Created new level");
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
    public Map<MaterialPos, String> getLevel(){
        return this.material;
    }
}
