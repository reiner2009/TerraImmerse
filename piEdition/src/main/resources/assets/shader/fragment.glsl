#version 120

varying vec2 uv;
varying vec3 fragPos;
varying vec3 normal;
varying vec3 lightPos;

uniform sampler2D tex;

void main() {
    vec3 n = normalize(normal);
    vec3 lightDir = normalize(lightPos - fragPos);

    float diff = max(dot(n, lightDir), 0.0);

    float ambient = 0.25;

    vec3 color = texture2D(tex, uv).rgb * (ambient + diff);

    gl_FragColor = vec4(color, 1.0);
}