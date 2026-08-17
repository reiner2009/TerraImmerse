package net.terraimmerse.client.blaze3d;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureLoader {
    public static int loadTexture(String path) {
        ByteBuffer image;
        int width;
        int height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            STBImage.stbi_set_flip_vertically_on_load(false);
            try(InputStream stream=TextureLoader.class.getResourceAsStream(path)) {
                if (stream == null) {
                    throw new RuntimeException("Falied to load texture: " + STBImage.stbi_failure_reason());
                }
                byte[] bytes=stream.readAllBytes();
                ByteBuffer buffer= BufferUtils.createByteBuffer(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                image=STBImage.stbi_load_from_memory(buffer, w, h, comp, 4);
                if (image==null){
                    throw new RuntimeException("Falied to load texture");
                }
            } catch (IOException e){
                throw new RuntimeException("Couldn't load texture: "+e);
            }
            width=w.get();
            height=h.get();
        }
        int textureID= GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        STBImage.stbi_image_free(image);
        return textureID;
    }
}
