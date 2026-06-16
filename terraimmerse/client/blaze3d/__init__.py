import numpy as np
from terraimmerse.client.blaze3d.Context import*

voa=None
vertices_list = [
    -1,0,1,0,0,
    1,0,1,1,0,
    -1,0,-1,0,1,
    1,0,1,1,0,
    1,0,-1,1,1,
    -1,0,-1,0,1
]

def getVertexList():
    return vertices_list

def initialize():
    global voa
    vertices = np.array(vertices_list, dtype=np.float32)
    voa = glGenVertexArrays(1)
    vbo = glGenBuffers(1)
    glBindVertexArray(voa)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(
        GL_ARRAY_BUFFER,
        vertices.nbytes,
        vertices,
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
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

def getVao():
    global voa
    return voa