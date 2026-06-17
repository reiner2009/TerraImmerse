import numpy as np
from terraimmerse.client.blaze3d.Context import*
import terraimmerse.client.blaze3d.Material as Material
from terraimmerse.world.Chunk import Chunk
from opensimplex import noise2

vertices_list = []
voa=None

def getVertexList():
    return vertices_list

chunk=Chunk(0,0)

for x in range(16):
    for z in range(16):
        value = noise2(x * 0.02, z * 0.02) * 20 + 120
        ay = round(value)
        for y in range(ay):
            chunk.setMaterial(x, y, z, "bricks")

def initialize():
    global voa
    for pos in chunk.get().keys():
        for i in Material.getVertices(*pos, chunk):
            vertices_list.append(i)
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