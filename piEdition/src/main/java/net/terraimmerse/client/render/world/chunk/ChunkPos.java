package net.terraimmerse.client.render.world.chunk;

public class ChunkPos {
    public int x;
    public int z;
    public ChunkPos(int x_, int z_){
        this.x=x_;
        this.z=z_;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof ChunkPos)) return false;
        ChunkPos other = (ChunkPos) obj;
        return x == other.x && z == other.z;
    }
    @Override
    public int hashCode(){
        return x * 31 + z;
    }
}
