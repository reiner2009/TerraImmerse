import numpy as np
from terraimmerse.client.blaze3d import*
from terraimmerse.client.Textures import load_texture

sun_vertices = [
    -1,1,0,0,1,
    -1,-1,0,0,0,
     1,-1,0,1,0,
    -1,1,0,0,1,
     1,-1,0,1,0,
     1,1,0,1,1
]

class SunVBO:
    def __init__(self):
        self.sun_vao = glGenVertexArrays(1)
        self.sun_vbo = glGenBuffers(1)
        glBindVertexArray(self.sun_vao)
        glBindBuffer(GL_ARRAY_BUFFER, self.sun_vbo)
        glBufferData(
            GL_ARRAY_BUFFER,
            np.array(sun_vertices, dtype=np.float32),
            GL_STATIC_DRAW
        )
        glVertexAttribPointer(
            0,
            3,
            GL_FLOAT,
            GL_FALSE,
            5 * 4,
            ctypes.c_void_p(0)
        )
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(
            1,
            2,
            GL_FLOAT,
            GL_FALSE,
            5 * 4,
            ctypes.c_void_p(12)
        )
        glEnableVertexAttribArray(1)
        glBindVertexArray(0)
        self.sun_texture=load_texture("assets/textures/sky/sun.png")
    def getSunTexture(self):
        return self.sun_texture
    def getSunVAO(self):
        return self.sun_vao
