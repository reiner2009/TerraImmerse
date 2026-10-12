#version 330 core

in VS_OUT {
    vec2 texCoord;
} fs_in;

out vec4 FragColor;

uniform sampler2D skyTexture;
uniform vec3 skyColor;

void main() {
    vec4 texColor = texture(skyTexture, fs_in.texCoord);
    FragColor = vec4(skyColor * texColor.rgb, texColor.a);
}
