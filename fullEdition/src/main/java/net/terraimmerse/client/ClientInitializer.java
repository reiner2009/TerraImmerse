package net.terraimmerse.client;

import net.terraimmerse.client.blaze3d.MaterialRenderLayerMap;

public class ClientInitializer {
    public static MaterialRenderLayerMap.Registry materialRenderLayerMapRegistry;
    public static void onInitializeClient(){
        materialRenderLayerMapRegistry = new MaterialRenderLayerMap.Registry();
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "grass_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "dirt_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "stone_cube");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.SOLIDE, "wooden_log");
        materialRenderLayerMapRegistry.putMaterial(MaterialRenderLayerMap.CUTOUT, "leaves");
    }
}
