#version 110

varying vec2 uv;
varying vec3 fragPos;
varying vec3 normal;
varying vec3 lightPos;

uniform sampler2D tex;
uniform bool cutout;

void main() {
    vec4 texColor = texture2D(tex, uv);
    if (cutout && texColor.a < 0.5) {
        discard;
    }
    vec3 n = normalize(normal);
    vec3 lightDir = normalize(lightPos - fragPos);
    float diff = max(dot(n, lightDir), 0.0);
    float ambient = 0.25;
    vec3 color = texColor.rgb * (ambient + diff);
    gl_FragColor = vec4(color, texColor.a);
}