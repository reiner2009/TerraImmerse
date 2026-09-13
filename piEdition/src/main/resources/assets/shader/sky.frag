#version 120

varying vec2 vTexCoord;

uniform sampler2D skyTexture;
uniform vec3 skyColor;

void main() {
    vec4 texColor = texture2D(skyTexture, vTexCoord);
    gl_FragColor = vec4(skyColor * texColor.rgb,texColor.a);
}
