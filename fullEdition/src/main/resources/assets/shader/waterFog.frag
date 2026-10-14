#version 330 core

out vec4 FragColor;

uniform vec3 screenColor;
uniform float alpha;

void main() {
    FragColor = vec4(screenColor, alpha);
}