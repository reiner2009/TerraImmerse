import os
import contextlib

from terraimmerse.util import get_resource_path

with open(os.devnull, 'w') as f, contextlib.redirect_stdout(f):
    import pygame
    from pygame.locals import *
from OpenGL.GL import *

def load_texture(path):
    surface = pygame.image.load(get_resource_path(path)).convert_alpha()
    surface = pygame.transform.flip(surface, False, True)
    data = pygame.image.tostring(surface, "RGBA", 1)
    width, height = surface.get_size()
    tex_id = glGenTextures(1)
    glBindTexture(GL_TEXTURE_2D, tex_id)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGBA,
        width,
        height,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        data
    )
    return tex_id