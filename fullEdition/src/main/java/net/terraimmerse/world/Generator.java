package net.terraimmerse.world;

import com.raylabz.opensimplex.OpenSimplexNoise;
import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;

import java.util.Map;

public class Generator {
    private static int size;
    private static OpenSimplexNoise noise;
    public double getHeight(int x, int z){
        double n1 = noise.getNoise2D(x*0.1F, z*0.1F).getValue()*30+30;
        double n2 = noise.getNoise2D(x*0.7F, z*0.7F).getValue()*40+40;
        double n3 = noise.getNoise2D(x*1, z*1).getValue()*45+40;
        return n1+n2+n3;
    }
    public Generator(Chunk chunk){
        size=200;
        noise=new OpenSimplexNoise();
        for (int x = (int)-size/2; x < (int)size/2; x++){
            for (int z = (int)-size/2; z < (int)size/2; z++){
                for (int y=0;y<getHeight(x,z);y++){
                    chunk.setMaterial(x,y,z,"stone_cube");
                }
            }
        }
        for (Map.Entry<MaterialPos, String> entry : chunk.getChunk().entrySet()) {
            MaterialPos materialPos = entry.getKey();
            if (chunk.getMaterial(materialPos.x, materialPos.y+1, materialPos.z).equals("air")){
                chunk.setMaterial(materialPos.x, materialPos.y, materialPos.z, "grass_cube");
            }
            if (chunk.getMaterial(materialPos.x, materialPos.y+1, materialPos.z).equals("grass_cube")){
                chunk.setMaterial(materialPos.x, materialPos.y, materialPos.z, "dirt_cube");
            }
        }
    }
}
