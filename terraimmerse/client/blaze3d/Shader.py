from terraimmerse.util import get_resource_path

with open(get_resource_path("assets/shader/vertex.glsl"), "r") as f:
    src_vertex = f.read()

with open(get_resource_path("assets/shader/fragment.glsl"), "r") as f:
    src_fragment = f.read()

from OpenGL.GL import *

def compile_shader(source, shader_type):
    shader = glCreateShader(shader_type)
    glShaderSource(shader, source)
    glCompileShader(shader)
    if not glGetShaderiv(shader, GL_COMPILE_STATUS):
        error = glGetShaderInfoLog(shader).decode()
        raise Exception("Shader compile error:\n" + error)
    return shader

def create_shader_program(vertex_src, fragment_src):
    vertex_shader = compile_shader(vertex_src, GL_VERTEX_SHADER)
    fragment_shader = compile_shader(fragment_src, GL_FRAGMENT_SHADER)
    program = glCreateProgram()
    glAttachShader(program, vertex_shader)
    glAttachShader(program, fragment_shader)
    glLinkProgram(program)
    if not glGetProgramiv(program, GL_LINK_STATUS):
        error = glGetProgramInfoLog(program).decode()
        raise Exception("Linking error:\n" + error)
    glDeleteShader(vertex_shader)
    glDeleteShader(fragment_shader)
    return program