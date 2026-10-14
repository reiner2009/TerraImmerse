package net.terraimmerse.world.generator.feature;

import net.terraimmerse.world.material.MaterialPos;
import net.terraimmerse.world.level.Level;

public interface Feature {
    void place(MaterialPos materialPos, Level level);
    void init();
    boolean checkSpawnRules(Level level, MaterialPos materialPos);
}
