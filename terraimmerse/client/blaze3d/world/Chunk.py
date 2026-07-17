import numpy as np
from terraimmerse.client.blaze3d import*
import terraimmerse.client.blaze3d.world.Material as Material
import json

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
        for pos, m in self.chunk.get().items():
            for i in Material.getVertices(*pos, m, self.chunk, atlas_data, atlas_w, atlas_h, texture_data):
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
            8 * 4,
            ctypes.c_void_p(0)
        )
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(
            1,
            2,
            GL_FLOAT,
            GL_FALSE,
            8 * 4,
            ctypes.c_void_p(12)
        )
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(
            2,
            3,
            GL_FLOAT,
            GL_FALSE,
            8 * 4,
            ctypes.c_void_p(20)
        )
        glEnableVertexAttribArray(2)
        glEnableVertexAttribArray(2)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    def rebuild(self):
        glBindBuffer(GL_ARRAY_BUFFER, self.vbo)
        glBufferData(GL_ARRAY_BUFFER, self.vertices.nbytes, self.vertices, GL_STATIC_DRAW)
    def getVao(self):
        return self.voa
    def getVertices(self):
        return self.vertices_list