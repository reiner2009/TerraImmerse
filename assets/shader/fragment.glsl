#version 330 core

in vec2 uv;
out vec4 FragColor;

uniform sampler2D tex;
uniform float daylight;

void main() {
    FragColor = texture(tex, uv)*vec4(daylight, daylight, daylight, 0);
}