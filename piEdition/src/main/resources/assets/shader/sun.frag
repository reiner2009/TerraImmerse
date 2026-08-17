#version 120

varying vec2 vTexCoord;

uniform sampler2D sunTexture;
uniform vec3 sunColor;
uniform float brightness;

void main() {
    vec4 texColor = texture2D(sunTexture, vTexCoord);
    gl_FragColor = vec4(sunColor * texColor.rgb * brightness,texColor.a);
}
