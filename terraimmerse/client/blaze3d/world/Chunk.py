import numpy as np
from terraimmerse.client.blaze3d import*
import terraimmerse.client.blaze3d.world.Material as Material
from terraimmerse.world.Chunk import Chunk
from opensimplex import noise2, noise3
import json

chunk=Chunk(0,0)

size=200

for x in range(size):
    for z in range(size):
        value = noise2(x * 0.02, z * 0.02) * 10 + 50
        ay = round(value)
        for y in range(ay):
            chunk.setMaterial(x, y, z, "stone_cube")
for x in range(size):
    for z in range(size):
        value = noise2(x * 0.02, z * 0.02) * 10 + 50
        value1 = noise2(x * 0.1, z * 0.1) * 15 + 50 + value
        ay = round(value1)
        for y in range(ay):
            chunk.setMaterial(x, y, z, "stone_cube")
for pos, m in chunk.get().items():
    if (pos[0], pos[1]+2, pos[2]) not in chunk.get().keys() and (pos[0], pos[1]+1, pos[2]) in chunk.get().keys():
        chunk.setMaterial(*pos, "dirt_cube")
for pos, m in chunk.get().items():
    if (pos[0], pos[1]+1, pos[2]) not in chunk.get().keys():
        chunk.setMaterial(*pos, "grass_cube")
for x in range(size):
    for z in range(size):
        for y in range(100):
            cave = noise3(
                x * 0.1,
                y * 0.1,
                z * 0.1
            )
            if cave > 0.4:
                try:
                    chunk.setMaterial(x, y, z, "air")
                except KeyError as e:
                    pass

atlas_data=json.load(open(get_resource_path("assets/atlas.json")))
texture_data=json.load(open(get_resource_path("assets/textures.json")))
atlas_w=atlas_data["w"]
atlas_h=atlas_data["h"]

class ClientChunk:
    def __init__(self, chunk):
        self.chunk = chunk
        self.voa=None
        self.vertices=None
        self.vertices_list=[]
        self.vbo=None
    def build(self):
        for pos, m in chunk.get().items():
            for i in Material.getVertices(*pos, m, chunk, atlas_data, atlas_w, atlas_h, texture_data):
                self.vertices_list.append(i)
        vertices = np.array(self.vertices_list, dtype=np.float32)
        self.voa = glGenVertexArrays(1)
        self.vbo = glGenBuffers(1)
        glBindVertexArray(self.voa)
        glBindBuffer(GL_ARRAY_BUFFER, self.vbo)
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
    def rebuild(self):
        glBindBuffer(GL_ARRAY_BUFFER, self.vbo)
        glBufferData(GL_ARRAY_BUFFER, self.vertices.nbytes, self.vertices, GL_STATIC_DRAW)
    def getVao(self):
        return self.voa
    def getVertices(self):
        return self.vertices_list