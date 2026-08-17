package net.terraimmerse.client.blaze3d;

import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ShaderCompiler {
    public String vertexShaderSrc;
    public String fragmentShaderSrc;
    private int fragmentShader;
    private int vertexShader;
    private int program;
    public ShaderCompiler(String vertexPath, String fragmentPath){
        this.vertexShaderSrc=loadShader(vertexPath);
        this.fragmentShaderSrc=loadShader(fragmentPath);
    }
    private static String loadShader(String path){
        try (InputStream stream=ShaderCompiler.class.getResourceAsStream(path)){
            if (stream==null){
                throw new RuntimeException("Couldn't find shader: "+path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e){
            throw new RuntimeException("Couldn't load shader: "+e);
        }
    }
    public int compileShader(String source, int shaderType){
        int shader= GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        if ((GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS))==GL20.GL_FALSE){
            String error=GL20.glGetShaderInfoLog(shader);
            throw new RuntimeException("Falied to compile shader:\n"+error);
        }
        return shader;
    }
    public int createShaderProgram(String vertexSrc, String fragmentSrc){
        this.vertexShader=this.compileShader(vertexSrc, GL20.GL_VERTEX_SHADER);
        this.fragmentShader=this.compileShader(fragmentSrc, GL20.GL_FRAGMENT_SHADER);
        program=GL20.glCreateProgram();
        GL20.glAttachShader(program, this.vertexShader);
        GL20.glAttachShader(program, this.fragmentShader);
        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS)==GL20.GL_FALSE){
            String error=GL20.glGetProgramInfoLog(program);
            throw new RuntimeException("Linking error:\n"+error);
        }
        GL20.glDeleteShader(this.vertexShader);
        GL20.glDeleteShader(this.fragmentShader);
        return program;
    }
}
