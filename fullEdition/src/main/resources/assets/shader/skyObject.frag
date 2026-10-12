#version 330 core

in VS_OUT {
    vec2 texCoord;
} fs_in;

out vec4 FragColor;

uniform sampler2D skyObjectTexture;
uniform vec3 skyObjectColor;
uniform float brightness;
uniform float skyObjectAlpha;

void main(){
    vec4 texColor = texture(skyObjectTexture, fs_in.texCoord);
    float alpha = texColor.a * skyObjectAlpha;
    FragColor = vec4(skyObjectColor * texColor.rgb * brightness, alpha);
}