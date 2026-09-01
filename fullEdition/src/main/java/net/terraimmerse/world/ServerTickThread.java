package net.terraimmerse.world;

import net.terraimmerse.client.InputHandler;

public class ServerTickThread {
    public static Thread movementThread = new Thread(() -> {
        final long tickTime = 1_000_000_000L / 60;
        long nextTick = System.nanoTime();
        while (!Thread.currentThread().isInterrupted()) {
            tickWorld();
            nextTick += tickTime;
            long wait = nextTick - System.nanoTime();
            if (wait > 0) {
                try {
                    Thread.sleep(
                            wait / 1_000_000L,
                            (int)(wait % 1_000_000L)
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                nextTick = System.nanoTime();
            }
        }
    });
    private static void tickWorld() {
        InputHandler.tickMovement();
    }
}