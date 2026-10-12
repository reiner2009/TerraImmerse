#version 120

varying vec2 vTexCoord;

uniform sampler2D skyObjectTexture;
uniform vec3 skyObjectColor;
uniform float brightness;
uniform float skyObjectAlpha;

void main() {
    vec4 texColor = texture2D(skyObjectTexture, vTexCoord);
    float alpha = texColor.a * skyObjectAlpha;
    gl_FragColor = vec4(skyObjectColor * texColor.rgb * brightness, alpha);
}
