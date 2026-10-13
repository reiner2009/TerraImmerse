package net.terraimmerse.client.manager;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static final Logger LOGGER= LoggerFactory.getLogger(TextureManager.class);
    private static int width;
    private static int height;
    private static ByteBuffer image;
    public static Map<String, Integer> textures = new HashMap<>();
    private static int loadTexture(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            STBImage.stbi_set_flip_vertically_on_load(false);
            InputStream inputStream = ResourceStreamManager.getStreamResource(path);
            byte[] bytes = inputStream.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            image=STBImage.stbi_load_from_memory(buffer, w, h, comp, 4);
            width=w.get();
            height=h.get();
        } catch (IOException e){
            e.printStackTrace();
        }
        int textureID= GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        STBImage.stbi_image_free(image);
        LOGGER.info("Loaded texture "+textureID);
        return textureID;
    }
    public static void build(String[] texturePaths){
        for(String path : texturePaths){
            textures.put(path, loadTexture(path));
        }
    }
}
