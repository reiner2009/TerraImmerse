package net.terraimmerse.world.generator.feature;

import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TreeFeature implements Feature{
    private Map<MaterialPos, String> tree;
    public TreeFeature(){
        this.tree=new HashMap<>();
    }
    @Override
    public void init(){
        this.tree.clear();
        Random random = new Random();
        for(int i=0;i<5;i++){
            this.tree.put(new MaterialPos(0,i,0), "wooden_log");
        }
        for(int x=-2;x<=2;x++){
            for(int z=-2;z<=2;z++){
                if(random.nextInt(10) < 8){
                    this.tree.put(new MaterialPos(x,4,z), "leaves");
                }
            }
        }
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                if(random.nextInt(10) < 8){
                    this.tree.put(new MaterialPos(x,5,z), "leaves");
                }
            }
        }
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                if(random.nextInt(10) < 8){
                    this.tree.put(new MaterialPos(x,6,z), "leaves");
                }
            }
        }
        this.tree.put(new MaterialPos(0,7,0), "leaves");
    }
    @Override
    public void place(MaterialPos treePos, Chunk chunk){
        for(Map.Entry<MaterialPos, String> entry : new HashMap<>(this.tree).entrySet()){
            MaterialPos relativePos=entry.getKey();
            String material=entry.getValue();
            chunk.setMaterial(relativePos.x+ treePos.x, relativePos.y+ treePos.y, relativePos.z+ treePos.z, material);
        }
    }
}
