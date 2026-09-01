package net.terraimmerse.world.generator.feature;

import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

public class TreeFeature implements Feature{
    Map<MaterialPos, String> tree;
    @Override
    public void init(){
        tree=new HashMap<>();
        for(int i=0;i<5;i++){
            tree.put(new MaterialPos(0,i,0), "wooden_log");
        }
        for(int x=-2;x<=2;x++){
            for(int z=-2;z<=2;z++){
                tree.put(new MaterialPos(x,4,z), "leaves");
            }
        }
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                tree.put(new MaterialPos(x,5,z), "leaves");
            }
        }
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                tree.put(new MaterialPos(x,6,z), "leaves");
            }
        }
        tree.put(new MaterialPos(0,7,0), "leaves");
    }

    @Override
    public void place(MaterialPos treePos, Chunk chunk){
        for(Map.Entry<MaterialPos, String> entry : new HashMap<>(tree).entrySet()){
            MaterialPos materialPos=entry.getKey();
            String material=entry.getValue();
            chunk.setMaterial(materialPos.x+ treePos.x, materialPos.y+ treePos.y, materialPos.z+ treePos.z, material);
        }
    }
}