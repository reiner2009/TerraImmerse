#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aUV;
layout(location = 2) in vec3 aNormal;

out vec2 uv;
out vec3 fragPos;
out vec3 normal;
out vec3 lightPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
float lightAngle = 0;

void main() {
    float radius = 5000.0;
    lightPos = vec3(0.0, radius * sin(lightAngle), radius * cos(lightAngle+180));
    fragPos = vec3(model * vec4(aPos, 1.0));
    normal = mat3(transpose(inverse(model))) * aNormal;
    uv = aUV;
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}