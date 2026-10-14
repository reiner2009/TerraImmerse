package net.terraimmerse.world.generator;

import com.raylabz.opensimplex.OpenSimplexNoise;
import net.terraimmerse.world.Biome;
import net.terraimmerse.world.material.MaterialPos;
import net.terraimmerse.world.level.Level;
import net.terraimmerse.world.generator.feature.TreeFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Random;

public class WorldGenerator {
    private static final Logger LOGGER= LoggerFactory.getLogger(WorldGenerator.class);
    private static int size;
    private static Random random;
    private static OpenSimplexNoise noise;
    private static TreeFeature treeFeature;
    public static double getHeight(int x, int z){
        double n1 = noise.getNoise2D(x * 0.1F, z * 0.1F).getValue() * 60 + 60;
        double n2 = noise.getNoise2D(x * 0.4F, z * 0.4F).getValue() * 16 + 8;
        double n3 = noise.getNoise2D(x * 1.6F, z * 1.6F).getValue() * 3;
        return n1 + n2 + n3;
    }
    private Biome getBiome(int x, int z){
        double n = noise.getNoise2D(x * 0.1, z * 0.1).getValue();
        if (n < -0.3){
            return Biome.DESERT;
        }
        if(n >= -0.3 && n < 0.3) {
            return Biome.PLAINS;
        }
        if(n >= 0.3 && n <= 1.0){
            return Biome.FOREST;
        }
        return Biome.PLAINS;
    }
    private String getWaterGround(int x, int z){
        double n = noise.getNoise2D(x * 1.0, z * 1.0).getValue();
        if(n < 0.0){
            return "dirt_cube";
        }
        return "desert_sand";
    }
    public void tryPlaceTree(MaterialPos materialPos, Level level, int treeDistribution){
        treeFeature = new TreeFeature();
        if (random.nextInt(treeDistribution) == 1 && treeFeature.checkSpawnRules(level, materialPos) && (!materialPos.equals(getSpawnPos()))) {
            treeFeature.init();
            treeFeature.place(new MaterialPos(materialPos.x, materialPos.y, materialPos.z), level);
        }
    }
    public WorldGenerator(Level level){
        LOGGER.info("Generating world...");
        size=128;
        noise=new OpenSimplexNoise();
        random=new Random();
        for (int x = (int)-size/2; x < (int)size/2; x++){
            for (int z = (int)-size/2; z < (int)size/2; z++){
                for (int y=0;y<getHeight(x,z);y++){
                    level.setMaterial(x,y,z,"stone_cube");
                }
                int y = (int)getHeight(x, z);
                if(getBiome(x, z).equals(Biome.PLAINS) || getBiome(x, z).equals(Biome.FOREST)) {
                    level.setMaterial(x, y, z, "grass_cube");
                    level.setMaterial(x, y - 1, z, "dirt_cube");
                }
                if(getBiome(x, z).equals(Biome.DESERT)) {
                    level.setMaterial(x, y, z, "desert_sand");
                    level.setMaterial(x, y - 1, z, "desert_sand");
                }
            }
        }
        for (var entry : new HashMap<>(level.getLevel()).entrySet()){
            MaterialPos materialPos = entry.getKey();
            if(materialPos.y < 50 && level.getMaterial(materialPos.x, materialPos.y + 1, materialPos.z).equals("air")){
                level.setMaterial(materialPos.x, materialPos.y, materialPos.z, getWaterGround(materialPos.x, materialPos.z));
                level.setMaterial(materialPos.x, materialPos.y + 1, materialPos.z, getWaterGround(materialPos.x, materialPos.z));
                for(int y = materialPos.y + 1; y <= 50; y++){
                    level.setMaterial(materialPos.x, y, materialPos.z, "water");
                }
            }
        }
        for (var entry : new HashMap<>(level.getLevel()).entrySet()){
            MaterialPos materialPos = entry.getKey();
            if(getBiome(materialPos.x, materialPos.z).equals(Biome.FOREST)) {
                tryPlaceTree(materialPos, level, 30);
            }
            if(getBiome(materialPos.x, materialPos.z).equals(Biome.PLAINS)) {
                tryPlaceTree(materialPos, level, 500);
            }
        }
    }
    public static MaterialPos getSpawnPos(){
        return new MaterialPos(0, (int)getHeight(0, 0) + 2, 0);
    }
}
