package net.terraimmerse.core;

public class MaterialPos {
    public int x;
    public int y;
    public int z;
    public MaterialPos(int x_, int y_, int z_){
        this.x=x_;
        this.y=y_;
        this.z=z_;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof MaterialPos)) return false;
        MaterialPos other = (MaterialPos) obj;
        return x == other.x &&
                y == other.y &&
                z == other.z;
    }
    @Override
    public int hashCode(){
        return x * 31 * 31 + y * 31 + z;
    }
}
