import numpy as np
from terraimmerse.client.blaze3d.Context import*
import terraimmerse.client.blaze3d.Material as Material
from terraimmerse.world.Chunk import Chunk
from opensimplex import noise2

vertices_list = []
voa=None
vbo=None
vertices=None

def getVertexList():
    return vertices_list

chunk=Chunk(0,0)

for x in range(50):
    for z in range(50):
        value = noise2(x * 0.02, z * 0.02) * 10 + 10
        ay = round(value)
        for y in range(ay):
            chunk.setMaterial(x, y, z, "dirt_cube")
for pos, m in chunk.get().items():
    if (pos[0], pos[1]+1, pos[2]) not in chunk.get().keys():
        chunk.setMaterial(*pos, "grass_cube")

atlas_data={
    "grass": [0,0],
    "grass_side": [0,1],
    "dirt": [1,0]
}

texture_data={
    "grass_cube": ["grass", "dirt", "grass_side", "grass_side", "grass_side", "grass_side"],
    "dirt_cube":["dirt", "dirt", "dirt", "dirt", "dirt", "dirt"]
}

atlas_w=2
atlas_h=2

def initialize():
    global voa, vertices, vbo
    for pos, m in chunk.get().items():
        for i in Material.getVertices(*pos, m,  chunk, atlas_data, atlas_w, atlas_h, texture_data):
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

def rebuild():
    global vbo, vertices
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices.nbytes, vertices, GL_STATIC_DRAW)

def getVao():
    global voa
    return voa