package net.terraimmerse.client;

import net.terraimmerse.client.render.RenderThread;
import net.terraimmerse.client.render.world.SceneRenderer;
import net.terraimmerse.client.input.InputHandler;
import net.terraimmerse.client.manager.AtlasManager;
import net.terraimmerse.world.GameTickThread;
import net.terraimmerse.world.entity.PlayerEntity;
import net.terraimmerse.world.generator.WorldGenerator;
import net.terraimmerse.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerraImmerse {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraImmerse.class);
    public static RenderThread renderThread;
    public static PlayerEntity playerEntity;
    public static AtlasManager atlasManager;
    public static SceneRenderer sceneRenderer;
    private static Level level;
    public static void init() {
        level=new Level();
        renderThread = new RenderThread();
        atlasManager = new AtlasManager();
        playerEntity = new PlayerEntity();
        InputHandler.init();
        Client.init();
        sceneRenderer.init(level);
        LOGGER.info("Initialized TerraImmerse");
    }
    public void run() {
        init();
        GameTickThread.entityMovementThread.setDaemon(true);
        GameTickThread.entityMovementThread.start();
        GameTickThread.gameTickThread.setDaemon(true);
        GameTickThread.gameTickThread.start();
        playerEntity.spawnAtMaterial(WorldGenerator.getSpawnPos());
        RenderThread.render();
        RenderThread.shutdown();
        LOGGER.info("Stopped!");
    }
    public static Level getLevel(){
        return level;
    }
    public static void main(String[] args){
		System.out.println("Starting TerraImmerse version 0.4.0-classic");
        new TerraImmerse().run();
    }

}
