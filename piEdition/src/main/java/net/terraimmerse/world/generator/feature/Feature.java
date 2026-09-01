package net.terraimmerse.world.generator.feature;

import net.terraimmerse.core.MaterialPos;
import net.terraimmerse.world.chunk.Chunk;

public interface Feature {
    void place(MaterialPos materialPos, Chunk chunk);
    void init();
}
