package net.terraimmerse.world.generator.feature;

import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.level.Level;

public interface Feature {
    void place(MaterialPos materialPos, Level level);
    void init();
}
