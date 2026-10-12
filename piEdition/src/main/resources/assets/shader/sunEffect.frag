#version 120

varying vec2 vTexCoord;

uniform sampler2D sunEffectTexture;
uniform vec3 sunEffectColor;
uniform float brightness;
uniform float sunEffectAlpha;

void main() {
    vec4 texColor = texture2D(sunEffectTexture, vTexCoord);
    float alpha = texColor.a * sunEffectAlpha;
    gl_FragColor = vec4(sunEffectColor * texColor.rgb * brightness, alpha);
}
