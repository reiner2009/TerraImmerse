package net.terraimmerse.world;

import net.terraimmerse.client.input.InputHandler;
import net.terraimmerse.client.render.sky.SkyRenderer;

public class GameTickThread {
    public static Thread entityMovementThread = new Thread(() -> {
        final long tickTime = 1_000_000_000L / 60;
        long nextTick = System.nanoTime();
        while (!Thread.currentThread().isInterrupted()) {
            InputHandler.tickMovement();
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
    public static Thread gameTickThread = new Thread(() -> {
        final long tickTime = 1_000_000_000L / 20;
        long nextTick = System.nanoTime();
        while (!Thread.currentThread().isInterrupted()) {
            serverTick();
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
    private static void serverTick(){
        SkyRenderer.tickLight();
    }
}