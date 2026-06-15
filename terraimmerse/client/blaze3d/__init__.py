import numpy as np
import terraimmerse.client.blaze3d.Context as context

voa=None

def initialize():
    global voa
    vertices=np.array([
        -0.5, -0.5, 0.0,
        0.5, -0.5, 0.0,
        0.0, 0.5, 0.0,
    ], dtype=np.float32)
    voa=context.glGenVertexArrays(1)
    context.glBindVertexArray(voa)
    vbo=context.glGenBuffers(1)
    context.glBindBuffer(context.GL_ARRAY_BUFFER, vbo)
    context. glBufferData(
        context.GL_ARRAY_BUFFER,
        vertices.nbytes,
        vertices,
        context.GL_STATIC_DRAW
    )
    context.glVertexAttribPointer(
        0,
        3,
        context.GL_FLOAT,
        context.GLU_FALSE,
        3 * 4,
        None
    )
    context.glEnableVertexAttribArray(0)
    context.glBindBuffer(context.GL_ARRAY_BUFFER, 0)
    context.glBindVertexArray(0)

def getVao():
    global voa
    return voa