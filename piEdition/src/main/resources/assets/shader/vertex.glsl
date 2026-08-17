#version 120

attribute vec3 aPos;
attribute vec2 aUV;
attribute vec3 aNormal;

varying vec2 uv;
varying vec3 fragPos;
varying vec3 normal;
varying vec3 lightPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float lightAngle;

void main() {
    float radius = 5000.0;
    lightPos = vec3(0.0,radius * sin(lightAngle),radius * cos(lightAngle + 3.14159265));
    fragPos = vec3(model * vec4(aPos, 1.0));
    normal = aNormal;
    uv = aUV;
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}
