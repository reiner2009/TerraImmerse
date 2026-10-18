package net.terraimmerse.client;

import net.terraimmerse.client.manager.TextureManager;
import net.terraimmerse.client.render.RenderLayerMap;
import net.terraimmerse.client.render.world.material.CrossMaterialRenderer;
import net.terraimmerse.client.render.world.material.SimpleCubeMaterialRenderer;
import net.terraimmerse.client.render.RendererRegistry;
import net.terraimmerse.client.render.world.material.WaterRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    public static RenderLayerMap.Registry materialRenderLayerMapRegistry;
    public static RendererRegistry materialRendererRegistry;
    private static String[] textures = {"/assets/textures/atlas.png", "/assets/textures/sky/sun.png","/assets/textures/sky/skybox.png"};
    public static void init(){
        materialRenderLayerMapRegistry = new RenderLayerMap.Registry();
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "grass_cube");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "dirt_cube");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "desert_sand");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "stone_cube");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "wooden_log");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "gravel_cube");
        materialRenderLayerMapRegistry.register(RenderLayerMap.SOLIDE, "sand_cube");
        materialRenderLayerMapRegistry.register(RenderLayerMap.CUTOUT, "leaves");
        materialRenderLayerMapRegistry.register(RenderLayerMap.CUTOUT, "flower");
        materialRenderLayerMapRegistry.register(RenderLayerMap.CUTOUT, "high_grass");
        materialRenderLayerMapRegistry.register(RenderLayerMap.TRANSLUCENT, "water");
        LOGGER.info("Created materialRenderLayerMaps");
        materialRendererRegistry = new RendererRegistry();
        materialRendererRegistry.register("grass_cube", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("dirt_cube", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("desert_sand", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("stone_cube", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("wooden_log", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("gravel_cube", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("sand_cube", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("leaves", new SimpleCubeMaterialRenderer());
        materialRendererRegistry.register("water", new WaterRenderer());
        materialRendererRegistry.register("flower", new CrossMaterialRenderer());
        materialRendererRegistry.register("high_grass", new CrossMaterialRenderer());
        LOGGER.info("Created materialRenderRegistries");
        TextureManager.build(textures);
        LOGGER.info("Loaded "+textures.length+" textures");
    }
}
