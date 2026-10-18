#version 120

uniform vec3 screenColor;
uniform float alpha;

void main() {
    gl_FragColor = vec4(screenColor, alpha);
}
