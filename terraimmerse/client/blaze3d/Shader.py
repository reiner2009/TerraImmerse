from typing import override

from terraimmerse.util import get_resource_path
from terraimmerse.client.blaze3d import *
import numpy as np

class ShaderError(Exception):
    pass

class ShaderCompiler:
    def __init__(self):
        with open(get_resource_path("assets/shader/vertex.glsl"), "r") as f:
            self.src_vertex = f.read()
        with open(get_resource_path("assets/shader/fragment.glsl"), "r") as f:
            self.src_fragment = f.read()
    def compile_shader(self, source, shader_type):
        self.shader = glCreateShader(shader_type)
        glShaderSource(self.shader, source)
        glCompileShader(self.shader)
        if not glGetShaderiv(self.shader, GL_COMPILE_STATUS):
            error = glGetShaderInfoLog(self.shader).decode()
            raise Exception("Shader compile error:\n" + error)
        return self.shader
    def create_shader_program(self, vertex_src, fragment_src):
        vertex_shader = self.compile_shader(vertex_src, GL_VERTEX_SHADER)
        fragment_shader = self.compile_shader(fragment_src, GL_FRAGMENT_SHADER)
        program = glCreateProgram()
        glAttachShader(program, vertex_shader)
        glAttachShader(program, fragment_shader)
        glLinkProgram(program)
        if not glGetProgramiv(program, GL_LINK_STATUS):
            error = glGetProgramInfoLog(program).decode()
            raise ShaderError("Linking error:\n" + error)
        glDeleteShader(vertex_shader)
        glDeleteShader(fragment_shader)
        return program
