package net.terraimmerse.client;

import net.terraimmerse.client.render.RenderThread;
import net.terraimmerse.client.render.world.WorldRenderer;
import net.terraimmerse.client.input.InputHandler;
import net.terraimmerse.client.manager.AtlasManager;
import net.terraimmerse.world.GameTickThread;
import net.terraimmerse.world.entity.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerraImmerse {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraImmerse.class);
    public static RenderThread renderThread;
    public static PlayerEntity playerEntity;
    public static AtlasManager atlasManager;
    public static WorldRenderer worldRenderer;
    public static void init() {
        renderThread = new RenderThread();
        atlasManager = new AtlasManager();
        playerEntity = new PlayerEntity();
        InputHandler.init();
        LOGGER.info("Initialized TerraImmerse");
    }
    public void run() {
        init();
        ClientInitializer.init();
        worldRenderer.init();
        GameTickThread.entityMovementThread.setDaemon(true);
        GameTickThread.entityMovementThread.start();
        GameTickThread.gameTickThread.setDaemon(true);
        GameTickThread.gameTickThread.start();
        RenderThread.render();
        RenderThread.shutdown();
        LOGGER.info("Stopped!");
    }
    public static void main(String[] args){
        new TerraImmerse().run();
    }

}
