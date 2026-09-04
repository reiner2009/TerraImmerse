package net.terraimmerse.world.generator.feature;

import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TreeFeature implements Feature{
    private Map<MaterialPos, String> tree;
    private int treeH;
    public TreeFeature(){
        this.tree=new HashMap<>();
        this.treeH=0;
    }
    @Override
    public void init(){
        this.tree.clear();
        Random random = new Random();
		this.treeH=random.nextInt(3)+4;
		for(int x = -2; x < 3; x++){
			for(int y = this.treeH-3; y < this.treeH-1; y++){
				for(int z = -2; z < 3; z++){
					this.tree.put(new MaterialPos(x,y,z), "leaves");
				}
			}
		}
		for(int y = 0; y < this.treeH; y++){
			this.tree.put(new MaterialPos(0, y, 0), "wooden_log");
		}
		this.tree.put(new MaterialPos(0, this.treeH, 0), "leaves");
		this.tree.put(new MaterialPos(1, this.treeH, 0), "leaves");
		this.tree.put(new MaterialPos(-1, this.treeH, 0), "leaves");
		this.tree.put(new MaterialPos(0, this.treeH, 1), "leaves");
		this.tree.put(new MaterialPos(0, this.treeH, -1), "leaves");
		this.tree.put(new MaterialPos(1, this.treeH-1, 0), "leaves");
		this.tree.put(new MaterialPos(-1, this.treeH-1, 0), "leaves");
		this.tree.put(new MaterialPos(0, this.treeH-1, 1), "leaves");
		this.tree.put(new MaterialPos(0, this.treeH-1, -1), "leaves");
		if(random.nextInt(10) > 1){
			this.tree.put(new MaterialPos(-2, this.treeH-3, -2), "air");
			this.tree.put(new MaterialPos(-2, this.treeH-2, -2), "air");
		}
		if(random.nextInt(10) > 1){
			this.tree.put(new MaterialPos(-2, this.treeH-3, -2), "air");
			this.tree.put(new MaterialPos(2, this.treeH-2, -2), "air");
		}
		if(random.nextInt(10) > 1){
			this.tree.put(new MaterialPos(2, this.treeH-3, -2), "air");
			this.tree.put(new MaterialPos(-2, this.treeH-2, -2), "air");
		}
		if(random.nextInt(10) > 1){
			this.tree.put(new MaterialPos(2, this.treeH-3, -2), "air");
			this.tree.put(new MaterialPos(2, this.treeH-2, -2), "air");
		}
    }
    @Override
    public void place(MaterialPos treePos, Chunk chunk){
        for(Map.Entry<MaterialPos, String> entry : new HashMap<>(this.tree).entrySet()){
            MaterialPos relativePos=entry.getKey();
            String material=entry.getValue();
            chunk.setMaterial(relativePos.x + treePos.x, relativePos.y + treePos.y + 1, relativePos.z + treePos.z, material);
        }
    }
}
