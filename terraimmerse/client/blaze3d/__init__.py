import numpy as np
import terraimmerse.client.blaze3d.Context as context

voa=None

def initialize():
    global voa
    vertices = np.array([
        -0.5, -0.5, 0.0, 0.0, 0.0,
        0.5, -0.5, 0.0, 1.0, 0.0,
        0.0, 0.5, 0.0, 0.5, 1.0,
    ], dtype=np.float32)
    voa = context.glGenVertexArrays(1)
    vbo = context.glGenBuffers(1)
    context.glBindVertexArray(voa)
    context.glBindBuffer(context.GL_ARRAY_BUFFER, vbo)
    context.glBufferData(
        context.GL_ARRAY_BUFFER,
        vertices.nbytes,
        vertices,
        context.GL_STATIC_DRAW
    )
    context.glVertexAttribPointer(
        0,
        3,
        context.GL_FLOAT,
        context.GL_FALSE,
        5 * 4,
        context.ctypes.c_void_p(0)
    )
    context.glEnableVertexAttribArray(0)
    context.glVertexAttribPointer(
        1,
        2,
        context.GL_FLOAT,
        context.GL_FALSE,
        5 * 4,
        context.ctypes.c_void_p(12)
    )
    context.glEnableVertexAttribArray(1)
    context.glBindBuffer(context.GL_ARRAY_BUFFER, 0)
    context.glBindVertexArray(0)

def getVao():
    global voa
    return voa