package net.terraimmerse.client.render;

import net.terraimmerse.client.render.world.material.SimpleCubeMaterialRenderer;

import java.util.HashMap;

public class RendererRegistry {
    private HashMap<String, SimpleCubeMaterialRenderer> materialRenderRegistry;
    public RendererRegistry(){
        this.materialRenderRegistry = new HashMap<>();
    }
    public void register(String id, SimpleCubeMaterialRenderer materialRendererClass){
        this.materialRenderRegistry.put(id, materialRendererClass);
    }
    public SimpleCubeMaterialRenderer getMaterialRenderRegistry(String id){
        return this.materialRenderRegistry.get(id);
    }
}
