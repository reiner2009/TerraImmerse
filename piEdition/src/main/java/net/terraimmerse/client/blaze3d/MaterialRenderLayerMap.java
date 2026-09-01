package net.terraimmerse.client.blaze3d;

import java.util.HashMap;
import java.util.Map;

public enum MaterialRenderLayerMap {
    SOLIDE("solide"),
    CUTOUT("cutout");
    private final String id;
    MaterialRenderLayerMap(String id) {
        this.id = id;
    }
    public static class Registry {
        private final Map<String, MaterialRenderLayerMap> materialRenderLayerMap = new HashMap<>();
        public void putMaterial(MaterialRenderLayerMap map, String material) {
            materialRenderLayerMap.put(material, map);
        }
        public MaterialRenderLayerMap get(String material) {
            return materialRenderLayerMap.get(material);
        }
    }
}