#version 330 core

in VS_OUT {
    vec2 texCoord;
} fs_in;

out vec4 FragColor;

uniform sampler2D sunTexture;
uniform vec3 sunColor;
uniform float brightness;

void main(){
    vec4 texColor = texture(sunTexture, fs_in.texCoord);
    FragColor = vec4(sunColor * texColor.rgb * brightness, texColor.a);
}