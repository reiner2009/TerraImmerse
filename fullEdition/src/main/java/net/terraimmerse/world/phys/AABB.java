package net.terraimmerse.world.phys;

public class AABB {
    private float minX;
    private float minY;
    private float minZ;
    private float maxX;
    private float maxY;
    private float maxZ;
    public AABB(float minX_, float minY_, float minZ_, float maxX_, float maxY_, float maxZ_) {
        this.minX = minX_;
        this.minY = minY_;
        this.minZ = minZ_;
        this.maxX = maxX_;
        this.maxY = maxY_;
        this.maxZ = maxZ_;
    }
    public boolean intersects(AABB other) {
        return this.maxX > other.minX &&
                this.minX < other.maxX &&
                this.maxY > other.minY &&
                this.minY < other.maxY &&
                this.maxZ > other.minZ &&
                this.minZ < other.maxZ;
    }
}