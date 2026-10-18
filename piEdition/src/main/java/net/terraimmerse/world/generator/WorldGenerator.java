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
    private static OpenSimplexNoise landscapeNoise;
    private static OpenSimplexNoise biomeNoise;
    private static TreeFeature treeFeature;
    private static String[] plants;
    public static double getHeight(int x, int z){
        double n1 = landscapeNoise.getNoise2D(x * 0.1F, z * 0.1F).getValue() * 30 + 30;
        double n2 = landscapeNoise.getNoise2D(x * 0.4F, z * 0.4F).getValue() * 8 + 4;
        double n3 = landscapeNoise.getNoise2D(x * 1.6F, z * 1.6F).getValue() * 1.5;
        return n1 + n2 + n3;
    }
    private Biome getBiome(int x, int z){
        double n = biomeNoise.getNoise2D(x * 0.2, z * 0.2).getValue();
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
    private String getWaterGround(int x, int y, int z){
        double n = landscapeNoise.getNoise2D(x * 2.0, z * 2.0).getValue();
        if(!getBiome(x, z).equals(Biome.DESERT)) {
            if (n < -0.6) {
                if(y < 25) {
                    return "dirt_cube";
                }
                else {
                    return "grass_cube";
                }
            }
            if (n >= -0.6 && n < 0.0) {
                return "gravel_cube";
            }
            return "sand_cube";
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
    public void tryPlacePlant(MaterialPos materialPos, Level level, int plantDistribution){
        if (random.nextInt(plantDistribution) == 1) {
            if(level.getMaterial(materialPos.x, materialPos.y + 1, materialPos.z).equals("air") && level.getMaterial(materialPos).equals("grass_cube")) {
                level.setMaterial(new MaterialPos(materialPos.x, materialPos.y + 1, materialPos.z), plants[random.nextInt(plants.length)]);
            }
        }
    }
    public WorldGenerator(Level level){
        LOGGER.info("Generating world...");
        size=100;
        landscapeNoise=new OpenSimplexNoise(0L);
        biomeNoise=new OpenSimplexNoise(1L);
        random=new Random();
        plants=new String[]{"high_grass","high_grass","high_grass","high_grass", "flower"};
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
            if(materialPos.y <= 27 && level.getMaterial(materialPos.x, materialPos.y + 1, materialPos.z).equals("air")){
                level.setMaterial(materialPos.x, materialPos.y, materialPos.z, getWaterGround(materialPos.x, materialPos.y, materialPos.z));
                level.setMaterial(materialPos.x, materialPos.y - 1, materialPos.z, getWaterGround(materialPos.x, materialPos.y - 1, materialPos.z));
                for(int y = materialPos.y + 1; y <= 25; y++){
                    level.setMaterial(materialPos.x, y, materialPos.z, "water");
                }
            }
        }
        for (var entry : new HashMap<>(level.getLevel()).entrySet()){
            MaterialPos materialPos = entry.getKey();
            if(getBiome(materialPos.x, materialPos.z).equals(Biome.FOREST)) {
                tryPlacePlant(materialPos, level, 100);
                tryPlaceTree(materialPos, level, 30);
            }
            if(getBiome(materialPos.x, materialPos.z).equals(Biome.PLAINS)) {
                tryPlacePlant(materialPos, level, 10);
                tryPlaceTree(materialPos, level, 500);
            }
        }
    }
    public static MaterialPos getSpawnPos(){
        return new MaterialPos(0, (int)getHeight(0, 0) + 2, 0);
    }
}
