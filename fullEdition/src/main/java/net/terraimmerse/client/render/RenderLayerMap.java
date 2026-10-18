package net.terraimmerse.client.render;

import java.util.HashMap;
import java.util.Map;

public enum RenderLayerMap {
    SOLIDE(),
    CUTOUT(),
    TRANSLUCENT();
    public static class Registry {
        private final Map<String, RenderLayerMap> materialRenderLayerMap = new HashMap<>();
        public void register(RenderLayerMap map, String material) {
            materialRenderLayerMap.put(material, map);
        }
        public RenderLayerMap get(String material) {
            if(materialRenderLayerMap.containsKey(material)) {
                return materialRenderLayerMap.get(material);
            }
            else{
                return RenderLayerMap.CUTOUT;
            }
        }
    }
}