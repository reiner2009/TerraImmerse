package net.terraimmerse.client.render.world.chunk;

public class ChunkOverflowException extends RuntimeException {
    public ChunkOverflowException(String message) {
        super(message);
    }
}
