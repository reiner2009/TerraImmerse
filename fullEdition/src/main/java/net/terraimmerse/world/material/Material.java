package net.terraimmerse.world.material;

import net.terraimmerse.world.phys.AABB;

public class Material {
    public static AABB getMaterialBoxAt(int x, int y,  int z){
        return new AABB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F);
    }
}
