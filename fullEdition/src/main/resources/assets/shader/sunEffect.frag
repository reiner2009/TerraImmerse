#version 330 core

in VS_OUT {
    vec2 texCoord;
} fs_in;

out vec4 FragColor;

uniform sampler2D sunEffectTexture;
uniform vec3 sunEffectColor;
uniform float brightness;
uniform float sunEffectAlpha;

void main() {
    vec4 texColor = texture(sunEffectTexture, fs_in.texCoord);
    float alpha = texColor.a * sunEffectAlpha;
    FragColor = vec4(sunEffectColor * texColor.rgb * brightness, alpha);
}
