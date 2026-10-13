package net.terraimmerse.world.generator;

import com.raylabz.opensimplex.OpenSimplexNoise;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.level.Level;
import net.terraimmerse.world.generator.feature.TreeFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenerator {
    private static final Logger LOGGER= LoggerFactory.getLogger(WorldGenerator.class);
    private static int size;
    private static Random random;
    private static OpenSimplexNoise noise;
    private static TreeFeature treeFeature;
    public double getHeight(int x, int z){
        double n1 = noise.getNoise2D(x * 0.1F, z * 0.1F).getValue() * 30 + 30;
        double n2 = noise.getNoise2D(x * 0.4F, z * 0.4F).getValue() * 8 + 4;
        double n3 = noise.getNoise2D(x * 1.6F, z * 1.6F).getValue() * 1.5;
        return n1 + n2 + n3;
    }
    public WorldGenerator(Level level){
        LOGGER.info("Generating world...");
        size=64;
        noise=new OpenSimplexNoise();
        random=new Random();
        for (int x = (int)-size/2; x < (int)size/2; x++){
            for (int z = (int)-size/2; z < (int)size/2; z++){
                for (int y=0;y<getHeight(x,z);y++){
                    level.setMaterial(x,y,z,"stone_cube");
                }
            }
        }
        for (Map.Entry<MaterialPos, String> entry : level.getLevel().entrySet()) {
            MaterialPos materialPos = entry.getKey();
            if (level.getMaterial(materialPos.x, materialPos.y + 1, materialPos.z).equals("air")) {
                level.setMaterial(materialPos.x, materialPos.y, materialPos.z, "grass_cube");
            }
        }
        for (Map.Entry<MaterialPos, String> entry : level.getLevel().entrySet()) {
            MaterialPos materialPos = entry.getKey();
            if (level.getMaterial(materialPos.x, materialPos.y+1, materialPos.z).equals("grass_cube")){
                level.setMaterial(materialPos.x, materialPos.y, materialPos.z, "dirt_cube");
            }
        }
        for (Map.Entry<MaterialPos, String> entry : new HashMap<>(level.getLevel()).entrySet()) {
            MaterialPos materialPos = entry.getKey();
            if (random.nextInt(150) == 1 && level.getMaterial(materialPos.x, materialPos.y, materialPos.z).equals("grass_cube") && level.getMaterial(materialPos.x+1, materialPos.y+1, materialPos.z).equals("air") && level.getMaterial(materialPos.x-1, materialPos.y+1, materialPos.z).equals("air") && level.getMaterial(materialPos.x, materialPos.y+1, materialPos.z+1).equals("air") && level.getMaterial(materialPos.x, materialPos.y+1, materialPos.z-1).equals("air")) {
                treeFeature = new TreeFeature();
                treeFeature.init();
                treeFeature.place(new MaterialPos(materialPos.x, materialPos.y, materialPos.z), level);
            }
        }
    }
}
