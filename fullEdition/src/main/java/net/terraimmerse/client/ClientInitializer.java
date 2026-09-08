package net.terraimmerse.client;

import net.terraimmerse.client.manager.TextureManager;
import net.terraimmerse.client.render.MaterialRenderLayerMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInitializer.class);
    public static MaterialRenderLayerMap.Registry materialRenderLayerMapRegistry;
    private static String[] textures = {"/assets/textures/atlas.png", "/assets/textures/sky/sun.png"};
    public static void init(){
        materialRenderLayerMapRegistry = new MaterialRenderLayerMap.Registry();
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "grass_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "dirt_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "stone_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "wooden_log");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.CUTOUT, "leaves");
        LOGGER.info("Created materialRenderLayerMaps");
        TextureManager.build(textures);
        LOGGER.info("Loaded "+textures.length+" textures");
    }
}
