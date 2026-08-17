#version 330 core

in vec2 uv;
in vec3 fragPos;
in vec3 normal;
in vec3 lightPos;

out vec4 FragColor;

uniform sampler2D tex;

void main() {
    vec3 n = normalize(normal);
    vec3 lightDir = normalize(lightPos - fragPos);
    float diff = max(dot(n, lightDir), 0.0);
    float ambient = 0.25;
    vec3 color = texture(tex, uv).rgb * (ambient + diff);
    FragColor = vec4(color, 1.0);
}